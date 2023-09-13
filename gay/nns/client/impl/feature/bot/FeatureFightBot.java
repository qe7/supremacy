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
import gay.nns.client.impl.event.player.EventPreMotion;
import gay.nns.client.impl.event.render.EventRender2D;
import gay.nns.client.impl.feature.render.FeatureInterface;
import gay.nns.client.util.chat.UtilChat;
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
import net.minecraft.util.MathHelper;

import javax.vecmath.Vector2f;
import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SerializeFeature(name = "FightBot", description = "Plays the game for you.", category = FeatureCategory.BOT)
public class FeatureFightBot extends Feature {

	@SerializeSetting(name = "Debug")
	@SettingBoolean()
	public boolean debug = false;

	@SerializeSetting(name = "Mode")
	@SettingMode(modes = {"Classic"})
	public String mode = "Classic";

	@SerializeSetting(name = "Break Every (Minutes)")
	@SettingSlider(min = 0, max = 60, increment = 1)
	public double breakTime = 15;

	@SerializeSetting(name = "Break Length (Minutes)")
	@SettingSlider(min = 0, max = 60, increment = 1)
	public double breakTimeLength = 5;

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
			renderDebugLine(this.getFeatureInfo().name() + " is on break for " + (int) (breakTimeLength * 60) + " seconds.", (int) eventRender2D.scaledResolution().getScaledWidth() / 2 - ((int) fontRenderer.getStringWidth("§8(§r≧◡≦§8) §w" + this.getFeatureInfo().name() + " is on break for " + (int) (breakTimeLength * 60) + " seconds.") / 2), (int) eventRender2D.scaledResolution().getScaledHeight() / 2 + 60, 0);
			renderDebugLine("Remaining seconds: " + (int) ((breakTimeLength * 60) - Math.round(((float) breakTimer.getTime() / 1000))), (int) eventRender2D.scaledResolution().getScaledWidth() / 2 - ((int) fontRenderer.getStringWidth("§8(§r≧◡≦§8) §w" + "Remaining seconds: ") / 2), (int) eventRender2D.scaledResolution().getScaledHeight() / 2 + 70, 0);
		}

		if (!debug) return;

		count = 11;
		renderDebugLine("Current Game State: §7" + currentGameState, 2, count, 0);
			renderDebugLine("Has Joined: §7" + hasJoined, 2, count, 1);
			renderDebugLine("Join Timer: §7" + joinTimer.getTime(), 2, count, 1);
			renderDebugLine("Break Timer: §7" + breakTimer.getTime(), 2, count, 1);
			renderDebugLine("Swing Timer: §7" + swingTimer.getTime(), 2, count, 1);
			renderDebugLine("Previous Game State: §7" + previousGameState, 2, count, 1);
			renderDebugLine("Stats: §7",  2, count, 1);
				renderDebugLine("Wins: §7" + win, 2, count, 2);
				renderDebugLine("Losses: §7" + loss, 2, count, 2);
				renderDebugLine("W/L: §7" + ((float) win / (float) loss), 2, count, 2);
			renderDebugLine("Local Game Info: §7", 2, count, 1);
				renderDebugLine("Frames/s: §7" + Minecraft.getDebugFPS(), 2, count, 2);
				renderDebugLine("Ping: §7" + mc.getNetHandler().getPlayerInfo(mc.thePlayer.getUniqueID()).getResponseTime(), 2, count, 2);
			renderDebugLine("Local Player Info: §7", 2, count, 1);
				renderDebugLine("Name: §7" + mc.thePlayer.getName(), 2, count, 2);
				renderDebugLine("Health: §7" + mc.thePlayer.getHealth(), 2, count, 2);
				renderDebugLine("Armor: §7" + mc.thePlayer.getTotalArmorValue(), 2, count, 2);
				renderDebugLine("Rotation: §7" + MathHelper.wrapAngleTo180_float(mc.thePlayer.getRotationYawHead()) + ", " + mc.thePlayer.rotationPitch, 2, count, 2);
				renderDebugLine("On Ground: §7" + mc.thePlayer.onGround, 2, count, 2);
				renderDebugLine("Sprinting: §7" + mc.thePlayer.isSprinting(), 2, count, 2);
				renderDebugLine("Sneaking: §7" + mc.thePlayer.isSneaking(), 2, count, 2);
			if (mc.thePlayer.getHeldItem() != null)
				renderDebugLine("Held Item: §7" + mc.thePlayer.inventory.getCurrentItem().getDisplayName(), 2, count, 2);
		if (mcTarget != null && mcTarget != mc.thePlayer) {
			renderDebugLine("Target Player Info: §7", 2, count, 1);
				renderDebugLine("Name: §7" + mcTarget.getName(), 2, count, 2);
				renderDebugLine("Health: §7" + ((EntityPlayer) mcTarget).getHealth(), 2, count, 2);
				renderDebugLine("Armor: §7" + ((EntityPlayer) mcTarget).getTotalArmorValue(), 2, count, 2);
				renderDebugLine("Rotation: §7" + MathHelper.wrapAngleTo180_float(mcTarget.getRotationYawHead()) + ", " + mcTarget.rotationPitch, 2, count, 2);
				renderDebugLine("On Ground: §7" + mcTarget.onGround, 2, count, 2);
				renderDebugLine("Sprinting: §7" + mcTarget.isSprinting(), 2, count, 2);
				renderDebugLine("Sneaking: §7" + mcTarget.isSneaking(), 2, count, 2);
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
		entities.removeIf(e -> e == mc.thePlayer || !(e instanceof EntityPlayer) || e.getDistanceToEntity(mc.thePlayer) > 32.0f || e.isDead);

		if (!entities.isEmpty() && entities.get(0).getDistanceToEntity(mc.thePlayer) < 128.0f)
			mcTarget = entities.get(0);
		else mcTarget = mc.thePlayer;

		switch (currentGameState) {
			case BREAK -> {
				mc.gameSettings.keyBindForward.setKeyPressed(false);
				mc.gameSettings.keyBindBack.setKeyPressed(false);
				mc.gameSettings.keyBindLeft.setKeyPressed(false);
				mc.gameSettings.keyBindRight.setKeyPressed(false);

				if (breakTimer.hasTimeElapsed((long) breakTimeLength * 60 * 1000)) {
					currentGameState = gameState.LOBBY;
					breakTimer.reset();
				}
			}
			case LOBBY -> {
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
			}
			case STARTING -> {
				hasJoined = false;

				mc.gameSettings.keyBindForward.setKeyPressed(false);
				mc.gameSettings.keyBindBack.setKeyPressed(false);
				mc.gameSettings.keyBindLeft.setKeyPressed(false);
				mc.gameSettings.keyBindRight.setKeyPressed(false);
			}
			case IN_GAME -> {
				if (hasJoined)
					hasJoined = false;

				if (mcTarget != null && mcTarget != mc.thePlayer) {

					Vector2f rotation = UtilRotation.getRotations(mcTarget);
					rotation = UtilRotation.getSmoothRotations(mc.thePlayer.getPreviousRotation(), rotation, 20f);
					rotation = UtilRotation.applySanity(rotation);
					rotation = UtilRotation.applyGCD(rotation);

					mc.thePlayer.rotationYaw = rotation.x;
					mc.thePlayer.rotationPitch = rotation.y;

					mc.gameSettings.keyBindForward.setKeyPressed(mcTarget.getDistanceToEntity(mc.thePlayer) > 1f);
					mc.gameSettings.keyBindBack.setKeyPressed(mcTarget.getDistanceToEntity(mc.thePlayer) < 3f);

					mc.gameSettings.keyBindLeft.setKeyPressed(((EntityPlayer) mcTarget).getHeldItem().getItem() instanceof ItemBow);
					mc.gameSettings.keyBindRight.setKeyPressed(((EntityPlayer) mcTarget).getHeldItem().getItem() instanceof ItemFishingRod);

					mc.gameSettings.keyBindJump.setKeyPressed(mc.thePlayer.isCollidedHorizontally);

					if (mcTarget.getDistanceToEntity(mc.thePlayer) < 6.0f) {
						if (swingTimer.hasTimeElapsed((1000 / 12))) {
							UtilPlayer.sendClick(0, true);
						}
						if (swingTimer.hasTimeElapsed((1000 / 12) + 30)) {
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
			}
			case ENDING -> {
				mc.gameSettings.keyBindForward.setKeyPressed(false);
				mc.gameSettings.keyBindBack.setKeyPressed(false);
				mc.gameSettings.keyBindLeft.setKeyPressed(false);
				mc.gameSettings.keyBindRight.setKeyPressed(false);

				if (!hasJoined && joinTimer.hasTimeElapsed(500)) {
					mc.thePlayer.sendChatMessage("Good game!");
					sendToNewGame();
					hasJoined = true;
					joinTimer.reset();
				}
			}
			default -> {
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
		if (!(eventPacketReceive.getPacket() instanceof S02PacketChat s02)) return;

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

	private void sendToNewGame() {
		chatUtil.chatCommand("Sending command to join game.");
		mc.thePlayer.sendChatMessage("/play duels_classic_duel");
	}

	private void renderDebugLine(String text, int x, int y, int indented) {
		String indent = "    ";
		String sillyText = "≧◡≦";
		String prefix = "§8(§r" + sillyText + "§8) §w";
		fontRenderer.drawStringWithShadow(indent.repeat(indented) + prefix + text, x, y, UtilColor.getColor(FeatureInterface.colorMode, count * 20, Color.CYAN, (float) FeatureInterface.saturation, (float) FeatureInterface.brightness).getRGB());
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
