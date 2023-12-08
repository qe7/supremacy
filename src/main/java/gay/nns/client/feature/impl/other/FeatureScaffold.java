package gay.nns.client.feature.impl.other;

import gay.nns.client.SupremacyCore;
import gay.nns.client.event.api.interfaces.Subscribe;
import gay.nns.client.feature.api.types.Feature;
import gay.nns.client.feature.api.interfaces.SerializeFeature;
import gay.nns.client.setting.api.annotations.SerializeSetting;
import gay.nns.client.setting.api.annotations.SettingBoolean;
import gay.nns.client.setting.api.annotations.SettingMode;
import gay.nns.client.setting.api.annotations.SettingSlider;
import gay.nns.client.event.impl.player.EventPreMotion;
import gay.nns.client.event.impl.player.EventUpdate;
import gay.nns.client.event.impl.render.EventRender2D;
import gay.nns.client.util.chat.UtilChat;
import gay.nns.client.util.math.UtilMath;
import gay.nns.client.util.math.UtilTimer;
import gay.nns.client.util.player.UtilRotation;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2f;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@SerializeFeature(name = "Scaffold", description = "Automatically places blocks under you")
public class FeatureScaffold extends Feature {

	@SerializeSetting(name = "Safe_Walk")
	@SettingBoolean
	public static boolean safeWalk = true;

	@SerializeSetting(name = "Ray_Trace")
	@SettingBoolean
	public boolean rayTrace = true;

	@SerializeSetting(name = "Limit_Speed")
	@SettingBoolean
	public boolean limitSpeed = true;

	@SerializeSetting(name = "Mode")
	@SettingMode(modes = {"Vanilla", "Hypixel", "Cancer"})
	public String mode = "Vanilla";

	@SerializeSetting(name = "Place_Delay")
	@SettingSlider(min = 0, max = 1000, increment = 1)
	public double placeDelay = 0;

	@SerializeSetting(name = "Rotation_Speed")
	@SettingSlider(min = 0, max = 20, increment = 1)
	public double rotationSpeed = 17;

	private final UtilChat chatUtil = new UtilChat();
	private final UtilTimer timerUtil = new UtilTimer();
	private final UtilTimer spinTimerUtil = new UtilTimer();
	private final List<Block> badBlocks = Arrays.asList(
			Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
			Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane,
			Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore,
			Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch,
			Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore,
			Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore,
			Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate,
			Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever,
			Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily,
			Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor,
			Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser,
			Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling,
			Blocks.cobblestone_wall, Blocks.oak_fence, Blocks.beacon);
	private final List<Block> invalidBlocks = Arrays.asList(
			Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava,
			Blocks.tallgrass);

	private BlockData blockData, prevBlockData;
	private float yaw, pitch;
	private int oldSlot = -1;

	public FeatureScaffold() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();
		oldSlot = mc.thePlayer.inventory.currentItem;
	}

	@Override
	protected void onDisable() {
		super.onDisable();
		mc.thePlayer.inventory.currentItem = oldSlot;
	}

	@Subscribe
	public void onRender2D(final EventRender2D render2DEvent) {
		FontRenderer fr = mc.fontRendererObj;
		String blocks = "blocks: " + getBlockCount();
		fr.drawStringWithShadow(blocks, (float) mc.displayWidth / 4 - (fr.getStringWidth(blocks) / 2f), (float) mc.displayHeight / 4 + 10, -1);
	}

	@Subscribe
	public void onUpdate(final EventUpdate updateEvent) {
		if (getBlockCount() <= 0) {
			chatUtil.chatCommand("Toggling '" + getFeatureInfo().name() + "' as you are out of blocks!");
			toggle();
		}
	}

	@Subscribe
	public void onPreMotion(final EventPreMotion preMotionEvent) {
		BlockPos blockUnder = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
		blockData = getBlockData(blockUnder);
		if (blockData == null) {
			return;
		}
		if (prevBlockData == null) {
			prevBlockData = blockData;
		}
		if (!Objects.equals(prevBlockData, blockData)) {
			prevBlockData = blockData;
		}
		pitch = UtilMath.getRandom(80.0F, 82.0F);
		switch (mode.toLowerCase()) {
			case "vanilla": {
				switch (blockData.getFacing()) {
					case NORTH: yaw = UtilMath.getRandom(-1.0F, 1.0F);
					case EAST: yaw = UtilMath.getRandom(89.0F, 91.0F);
					case SOUTH: yaw = UtilMath.getRandom(179.0F, 181.0F);
					case WEST: yaw = UtilMath.getRandom(-89.0F, -91.0F);
					case UP: {
						yaw = mc.thePlayer.rotationYaw;
						pitch = UtilMath.getRandom(88.0F, 90.0F);
					}
				}
				break;
			}
			case "hypixel": {
				yaw = mc.thePlayer.rotationYaw - UtilMath.getRandom(178.0F, 182.0F);
				if (Objects.requireNonNull(blockData.getFacing()) == EnumFacing.UP) {
					yaw = mc.thePlayer.rotationYaw;
					pitch = UtilMath.getRandom(88.0F, 90.0F);
				}
				break;
			}
			case "cancer": {
				switch (blockData.getFacing()) {
					case NORTH: yaw = 180.0F;
					case EAST: yaw = 270.0F;
					case SOUTH: yaw = 0.0F;
					case WEST: yaw = 90.0F;
					case UP: {
						yaw = UtilMath.getRandom(-180.0F, 180.0F);
						pitch = 90.0f;
					}
				}
				break;
			}
		}
		Vector2f rotations = new Vector2f(yaw, pitch);
		Vector2f smoothRotations = UtilRotation.getSmoothRotations(mc.thePlayer.getPreviousRotation(), rotations, rotationSpeed);
		smoothRotations = UtilRotation.applySanity(smoothRotations);
		smoothRotations = UtilRotation.applyGCD(smoothRotations);
		SupremacyCore.getSingleton().getRotationManager().setRotation(smoothRotations);
		if (limitSpeed && mc.thePlayer.onGround && !Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode()) &&
				Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode())) {
			double currentSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
			double speed = mc.thePlayer.isPotionActive(Potion.moveSpeed) ? currentSpeed * 0.9D : currentSpeed * 0.825D;
			float direction = mc.thePlayer.rotationYaw;
			double motionX = -Math.sin(direction / 180.0F * Math.PI) * speed;
			double motionZ = Math.cos(direction / 180.0F * Math.PI) * speed;
			mc.thePlayer.motionX = motionX;
			mc.thePlayer.motionZ = motionZ;
		}
		if (rayTrace && !rayCast(yaw, pitch)) {
			timerUtil.reset();
			return;
		}
		if (blockData.getFacing().equals(EnumFacing.UP)) {
			if (timerUtil.hasTimeElapsed(UtilMath.getRandom(100, 100 + 10))) {
				place();
			}
		} else if (timerUtil.hasTimeElapsed(UtilMath.getRandom((int) placeDelay, (int) placeDelay + 10))) {
			place();
		}
	}

	private void place() {
		if (getBlockSlot() != -1) {
			if (mc.thePlayer.inventory.currentItem != getBlockSlot()) {
				mc.thePlayer.inventory.currentItem = getBlockSlot();
			}
			if (mc.playerController.onPlayerRightClick(
					mc.thePlayer,
					mc.theWorld,
					mc.thePlayer.getHeldItem(),
					blockData.getPosition(),
					blockData.enumFacing,
					getHitVec()
			)) {
				mc.thePlayer.swingItem();
				timerUtil.reset();
			}
		}
	}

	private Vec3 getVectorForRotation(float yaw, float pitch) {
		double cosYaw = Math.cos(Math.toRadians(-yaw) - Math.PI);
		double sinYaw = Math.sin(Math.toRadians(-yaw) - Math.PI);
		double cosPitch = -Math.cos(Math.toRadians(-pitch));
		double sinPitch = Math.sin(Math.toRadians(-pitch));
		return new Vec3(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch);
	}

	private boolean rayCast(float yaw, float pitch) {
		Vec3 eyePosition = mc.thePlayer.getPositionEyes(1.0F);
		Vec3 directionVector = getVectorForRotation(yaw, pitch);
		Vec3 rayEndPoint = eyePosition.addVector(directionVector.xCoord * 5.0, directionVector.yCoord * 5.0, directionVector.zCoord * 5.0);
		MovingObjectPosition result = mc.theWorld.rayTraceBlocks(eyePosition, rayEndPoint, false);
		return result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && blockData.getPosition().equals(result.getBlockPos());
	}

	private boolean isPosSolid(BlockPos pos) {
		Block block = mc.theWorld.getBlockState(pos).getBlock();
		return !invalidBlocks.contains(block);
	}

	private int getBlockSlot() {
		for (int k = 0; k < 9; ++k) {
			ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
			if (itemStack != null && isValid(itemStack) && itemStack.stackSize >= 1) {
				return k;
			}
		}
		return -1;
	}

	private boolean isValid(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemBlock) {
			return !badBlocks.contains(((ItemBlock) itemStack.getItem()).getBlock());
		} else {
			return false;
		}
	}

	private int getBlockCount() {
		int count = 0;
		for (ItemStack itemStack : mc.thePlayer.inventory.mainInventory) {
			if (itemStack != null && isValid(itemStack) && itemStack.stackSize >= 1) {
				count += itemStack.stackSize;
			}
		}
		return count;
	}

	private BlockData getBlockData(BlockPos pos) {
		BlockPos[] offsets = {
				new BlockPos(0, -1, 0),
				new BlockPos(-1, 0, 0),
				new BlockPos(1, 0, 0),
				new BlockPos(0, 0, 1),
				new BlockPos(0, 0, -1),
				new BlockPos(-1, 0, -1),
				new BlockPos(1, 0, -1),
				new BlockPos(-1, 0, 1),
				new BlockPos(1, 0, 1)
		};
		for (BlockPos offset : offsets) {
			BlockPos adjacentPos = pos.add(offset);
			if (isPosSolid(adjacentPos)) {
				if (offset.equals(new BlockPos(0, -1, 0))) {
					return new BlockData(adjacentPos, EnumFacing.UP);
				} else if (offset.equals(new BlockPos(-1, 0, 0))) {
					return new BlockData(adjacentPos, EnumFacing.EAST);
				} else if (offset.equals(new BlockPos(1, 0, 0))) {
					return new BlockData(adjacentPos, EnumFacing.WEST);
				} else if (offset.equals(new BlockPos(0, 0, 1))) {
					return new BlockData(adjacentPos, EnumFacing.NORTH);
				} else if (offset.equals(new BlockPos(0, 0, -1))) {
					return new BlockData(adjacentPos, EnumFacing.SOUTH);
				} else if (offset.equals(new BlockPos(-1, 0, -1))) {
					return new BlockData(adjacentPos, EnumFacing.SOUTH);
				} else if (offset.equals(new BlockPos(1, 0, -1))) {
					return new BlockData(adjacentPos, EnumFacing.SOUTH);
				} else if (offset.equals(new BlockPos(-1, 0, 1))) {
					return new BlockData(adjacentPos, EnumFacing.NORTH);
				} else if (offset.equals(new BlockPos(1, 0, 1))) {
					return new BlockData(adjacentPos, EnumFacing.NORTH);
				}
			}
		}
		return null;
	}

	public Vec3 getHitVec() {
		Vec3 hitVec = new Vec3(blockData.getPosition().getX() + Math.random(),
				blockData.getPosition().getY() + Math.random(), blockData.getPosition().getZ() + Math.random());
		final MovingObjectPosition movingObjectPosition = rayCast(yaw, pitch) ?
				mc.theWorld.rayTraceBlocks(mc.thePlayer.getPositionEyes(1.0F), hitVec, false, false, true) : null;
		switch (blockData.enumFacing) {
			case DOWN: hitVec.yCoord = blockData.getPosition().getY();
				break;
			case UP: hitVec.yCoord = blockData.getPosition().getY() + 1;
				break;
			case NORTH: hitVec.zCoord = blockData.getPosition().getZ();
				break;
			case EAST: hitVec.xCoord = blockData.getPosition().getX() + 1;
				break;
			case SOUTH: hitVec.zCoord = blockData.getPosition().getZ() + 1;
				break;
			case WEST: hitVec.xCoord = blockData.getPosition().getX();
				break;
		}
		if (movingObjectPosition != null && movingObjectPosition.getBlockPos().equals(blockData.getPosition()) &&
				movingObjectPosition.sideHit == blockData.enumFacing) {
			hitVec = movingObjectPosition.hitVec;
		}
		return hitVec;
	}

	public static class BlockData {
		private final BlockPos blockPos;
		private final EnumFacing enumFacing;

		public BlockData(BlockPos blockPos, EnumFacing enumFacing) {
			this.blockPos = blockPos;
			this.enumFacing = enumFacing;
		}

		public EnumFacing getFacing() {
			return enumFacing;
		}

		public BlockPos getPosition() {
			return blockPos;
		}
	}
}
