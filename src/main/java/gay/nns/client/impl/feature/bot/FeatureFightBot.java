package gay.nns.client.impl.feature.bot;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.api.setting.annotations.SerializeSetting;
import gay.nns.client.api.setting.annotations.SettingBoolean;
import gay.nns.client.api.setting.annotations.SettingMode;
import gay.nns.client.api.setting.annotations.SettingSlider;
import gay.nns.client.impl.event.packet.EventPacketReceive;
import gay.nns.client.impl.event.player.EventMessageSent;
import gay.nns.client.impl.event.player.EventPreMotion;
import gay.nns.client.impl.event.render.EventRender2D;
import gay.nns.client.impl.feature.render.FeatureInterface;
import gay.nns.client.util.chat.UtilChat;
import gay.nns.client.util.math.UtilMath;
import gay.nns.client.util.math.UtilTimer;
import gay.nns.client.util.player.UtilPlayer;
import gay.nns.client.util.player.UtilRotation;
import gay.nns.client.util.render.UtilColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SerializeFeature(name = "FightBot", description = "Plays the game for you.", category = FeatureCategory.BOT)
public class FeatureFightBot extends Feature {

	@SerializeSetting(name = "AutoJoin")
	@SettingBoolean()
	public boolean autoJoin = true;

	@SerializeSetting(name = "Mode")
	@SettingMode(modes = {"Classic"})
	public String mode = "Classic";

	@SerializeSetting(name = "Break Every (Minutes)")
	@SettingSlider(min = 0, max = 60, increment = 1)
	public double breakTime = 15;

	@SerializeSetting(name = "Break Length (Minutes)")
	@SettingSlider(min = 0, max = 60, increment = 1)
	public double breakTimeLength = 5;

	@SerializeSetting(name = "TargetRange")
	@SettingSlider(min = 1.0, max = 128.0, increment = 1.0)
	public double targetRange = 32.0;

	@SerializeSetting(name = "SwingInterval (ms)")
	@SettingSlider(min = 1.0, max = 20.0, increment = 1.0)
	public double swingInterval = 10.0;

	private final FontRenderer fontRenderer = mc.fontRendererObj;
	private final UtilChat chatUtil = new UtilChat();

	private final UtilTimer joinTimer = new UtilTimer();
	private final UtilTimer breakTimer = new UtilTimer();
	private final UtilTimer swingTimer = new UtilTimer();

	private Entity mcTarget;

	private boolean hasJoined = false;

	private int count = 11;
	private int win = 0, loss = 0;

	public gameState currentGameState = gameState.LOBBY;
	public gameState previousGameState = null;

	public FeatureFightBot() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();

		joinTimer.reset();
		swingTimer.reset();
		hasJoined = false;
		currentGameState = gameState.LOBBY;
	}

	@Override
	protected void onDisable() {
		super.onDisable();
	}

	@Subscribe
	public void onRender2D(final EventRender2D eventRender2D) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;

		this.setSuffix(mode);

		if (currentGameState == gameState.BREAK) {
			renderDebugLine(this.getFeatureInfo().name() + " is on break for " + (int) (breakTimeLength * 60) + " seconds.", eventRender2D.getScaledResolution().getScaledWidth() / 2 - ((int) fontRenderer.getStringWidth("§8(§r≧◡≦§8) §w" + this.getFeatureInfo().name() + " is on break for " + (int) (breakTimeLength * 60) + " seconds.") / 2), (int) eventRender2D.getScaledResolution().getScaledHeight() / 2 + 60);
			renderDebugLine("Remaining seconds: " + (int) ((breakTimeLength * 60) - Math.round(((float) breakTimer.getTime() / 1000))), eventRender2D.getScaledResolution().getScaledWidth() / 2 - ((int) fontRenderer.getStringWidth("§8(§r≧◡≦§8) §w" + "Remaining seconds: ") / 2), (int) eventRender2D.getScaledResolution().getScaledHeight() / 2 + 70);
		}
	}

	@Subscribe
	public void onMotion(final EventPreMotion eventPreMotion) {
		if (mc.theWorld == null) return;
		if (mc.thePlayer == null) return;

		/* check to see if the currentState is different that send a message of what it was and what it changed too */
		if (previousGameState != currentGameState) {
			chatUtil.chatCommand("Game State Changed: " + previousGameState + " -> " + currentGameState);
			previousGameState = currentGameState;
		}

		List<Entity> entities = new ArrayList<>(mc.theWorld.getLoadedEntityList());
		entities.sort(Comparator.comparingDouble(e -> e.getDistanceToEntity(mc.thePlayer)));
		entities.removeIf(e -> e == mc.thePlayer || !(e instanceof EntityPlayer) || e.getDistanceToEntity(mc.thePlayer) > 128.0f || e.isDead);

		if (!entities.isEmpty() && entities.get(0).getDistanceToEntity(mc.thePlayer) < targetRange)
			mcTarget = entities.get(0);
		else mcTarget = mc.thePlayer;

		switch (currentGameState) {
			case BREAK: {
				mc.gameSettings.keyBindForward.setKeyPressed(false);
				mc.gameSettings.keyBindBack.setKeyPressed(false);
				mc.gameSettings.keyBindLeft.setKeyPressed(false);
				mc.gameSettings.keyBindRight.setKeyPressed(false);

				if (breakTimer.hasTimeElapsed((long) breakTimeLength * 60 * 1000)) {
					currentGameState = gameState.LOBBY;
					breakTimer.reset();
				}
				break;
			}
			case LOBBY: {
				mc.gameSettings.keyBindForward.setKeyPressed(false);
				mc.gameSettings.keyBindBack.setKeyPressed(false);
				mc.gameSettings.keyBindLeft.setKeyPressed(false);
				mc.gameSettings.keyBindRight.setKeyPressed(false);

				if (!hasJoined) {
					hasJoined = true;
					joinTimer.reset();
				}
				if (joinTimer.hasTimeElapsed(5000, true)) {
					sendToNewGame();
				}
				break;
			}
			case STARTING: {
				hasJoined = false;

				mc.gameSettings.keyBindForward.setKeyPressed(false);
				mc.gameSettings.keyBindBack.setKeyPressed(false);
				mc.gameSettings.keyBindLeft.setKeyPressed(false);
				mc.gameSettings.keyBindRight.setKeyPressed(false);

				mc.thePlayer.addChatComponentMessage(new ChatComponentText("§8(§r≧◡≦§8) §w" + "You have " + win + " wins and " + loss + " losses."));
				break;
			}
			case IN_GAME: {
				if (hasJoined)
					hasJoined = false;

				if (mcTarget != null && mcTarget != mc.thePlayer) {

					Vector2f rotation = UtilRotation.getRotations(mcTarget);
					float rotationSpeed = 40f;
					rotation = UtilRotation.getSmoothRotations(mc.thePlayer.getPreviousRotation(), rotation, rotationSpeed);
					rotation = UtilRotation.applySanity(rotation);
					rotation = UtilRotation.applyGCD(rotation);

					mc.thePlayer.rotationYaw = rotation.x;
					mc.thePlayer.rotationPitch = rotation.y;

					mc.gameSettings.keyBindForward.setKeyPressed(mcTarget.getDistanceToEntity(mc.thePlayer) > 1f);
					mc.gameSettings.keyBindBack.setKeyPressed(mcTarget.getDistanceToEntity(mc.thePlayer) < 3f);

					mc.gameSettings.keyBindLeft.setKeyPressed(((EntityPlayer) mcTarget).getHeldItem().getItem() instanceof ItemBow);
					mc.gameSettings.keyBindRight.setKeyPressed(((EntityPlayer) mcTarget).getHeldItem().getItem() instanceof ItemFishingRod);

					mc.gameSettings.keyBindJump.setKeyPressed(mc.thePlayer.isCollidedHorizontally);

					if (mcTarget.getDistanceToEntity(mc.thePlayer) < 4.2f) {
						long delay = (long) UtilMath.getRandom(swingInterval, swingInterval + 1);
						if (swingTimer.hasTimeElapsed(1000 / delay)) {
							UtilPlayer.sendClick(0, true);
						}
						if (swingTimer.hasTimeElapsed((1000 / delay) + UtilMath.getRandom(30, 50))) {
							UtilPlayer.sendClick(0, false);
							swingTimer.reset();
						}

					} else {
						swingTimer.reset();
					}
				} else {
					mc.gameSettings.keyBindJump.setKeyPressed(mc.thePlayer.isCollidedHorizontally);
					mc.gameSettings.keyBindForward.setKeyPressed(true);
					swingTimer.reset();
				}
				break;
			}
			case ENDING: {
				mc.gameSettings.keyBindForward.setKeyPressed(false);
				mc.gameSettings.keyBindBack.setKeyPressed(false);
				mc.gameSettings.keyBindLeft.setKeyPressed(false);
				mc.gameSettings.keyBindRight.setKeyPressed(false);

				if (!hasJoined && joinTimer.hasTimeElapsed(500)) {
					mc.thePlayer.sendChatMessage("GG");
					sendToNewGame();
					hasJoined = true;
					joinTimer.reset();
				}
				break;
			}
			default: {
				chatUtil.chatCommand("Unexpected value: " + currentGameState);
				throw new IllegalStateException("Unexpected value: " + currentGameState);
			}
		}

		if (currentGameState != gameState.BREAK && currentGameState != gameState.IN_GAME && breakTimer.hasTimeElapsed((long) breakTime * 60 * 1000)) {
			mc.thePlayer.sendChatMessage("/lobby");
			currentGameState = gameState.BREAK;
			breakTimer.reset();
		}
	}

	@Subscribe
	public void onPacketReceived(final EventPacketReceive eventPacketReceive) {
		if (!(eventPacketReceive.getPacket() instanceof S02PacketChat)) return;
		S02PacketChat s02 = (S02PacketChat) eventPacketReceive.getPacket();

		if (s02.getChatComponent().getUnformattedText().contains("has joined")) {
			currentGameState = gameState.STARTING;
		}

		if (s02.getChatComponent().getUnformattedText().contains("The game starts in 1 second")) {
			currentGameState = gameState.IN_GAME;
		}

		if (s02.getChatComponent().getUnformattedText().contains("Classic Duel -")) {
			currentGameState = gameState.ENDING;
			joinTimer.reset();
		}

		if (s02.getChatComponent().getUnformattedText().contains(mc.session.getUsername() + " WINNER!")) {
			win++;
		}

		if (s02.getChatComponent().getUnformattedText().contains(mcTarget.getName() + " WINNER!")) {
			loss++;
		}
	}

	@Subscribe
	public void onMessageSent(final EventMessageSent eventMessageSent) {
		if (eventMessageSent.getMessage().contains("/leave") || eventMessageSent.getMessage().contains("/lobby")) {
			currentGameState = gameState.LOBBY;
		}
	}

	private void sendToNewGame() {
		if (!autoJoin) {
			chatUtil.chatCommand("AutoJoin is disabled.");
			return;
		}
		chatUtil.chatCommand("Sending command to join game.");
		mc.thePlayer.sendChatMessage("/play duels_classic_duel");
	}

	private void renderDebugLine(String text, int x, int y) {
		String sillyText = "≧◡≦";
		String prefix = "§8(§r" + sillyText + "§8) §w";
		fontRenderer.drawStringWithShadow(prefix + text, x, y, UtilColor.getColor(FeatureInterface.colorMode, count * 20, Color.CYAN, (float) FeatureInterface.saturation, (float) FeatureInterface.brightness).getRGB());
		count += 10;
	}

	public enum gameState {
		LOBBY,
		STARTING,
		IN_GAME,
		ENDING,
		BREAK
	}

}
