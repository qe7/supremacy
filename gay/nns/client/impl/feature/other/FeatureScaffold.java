package gay.nns.client.impl.feature.other;

import gay.nns.client.api.core.Core;
import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.CheckBox;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.player.PostMotionEvent;
import gay.nns.client.impl.event.player.PreMotionEvent;
import gay.nns.client.impl.event.player.UpdateEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.util.chat.ChatUtil;
import gay.nns.client.util.math.MathUtil;
import gay.nns.client.util.math.TimerUtil;
import gay.nns.client.util.player.MovementUtil;
import gay.nns.client.util.player.RotationUtil;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Vector2f;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@FeatureInfo(name = "Scaffold", description = "Automatically places blocks under you")
public class FeatureScaffold extends AbstractFeature {

	@Serialize(name = "Safe_Walk")
	@CheckBox
	public static boolean safeWalk = true;

	@Serialize(name = "Mode")
	@Mode(modes = {"Vanilla", "Hypixel"})
	public String mode = "Vanilla";

	@Serialize(name = "Place_Delay")
	@Slider(min = 0, max = 1000, increment = 1)
	public double placeDelay = 0;

	@Serialize(name = "Rotation_Speed")
	@Slider(min = 0, max = 20, increment = 1)
	public double rotationSpeed = 17;

	private final ChatUtil chatUtil = new ChatUtil();
	private final TimerUtil timerUtil = new TimerUtil();

	/* thank co-pilot for this, :) */
	private final List<Block> badBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence, Blocks.beacon), invalidBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.fire, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava);

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
	public void onRender2D(final Render2DEvent render2DEvent) {
		FontRenderer fr = mc.fontRendererObj;

		String blocks = "blocks: " + getBlockCount() + " | " + getBlockSlot();

		fr.drawStringWithShadow(blocks, (float) mc.displayWidth / 4 - ((float) fr.getStringWidth(blocks) / 2), (float) mc.displayHeight / 4 + 10, -1);
//
//		fr.drawStringWithShadow("yaw: " + mc.thePlayer.rotationYaw + " pitch: " + mc.thePlayer.rotationPitch, (float) mc.displayWidth / 4 - ((float) fr.getStringWidth("yaw: " + mc.thePlayer.rotationYaw + " pitch: " + mc.thePlayer.rotationPitch) / 2), (float) mc.displayHeight / 4 + 20, -1);
//
//		fr.drawStringWithShadow("yaw: " + yaw + " pitch: " + pitch, (float) mc.displayWidth / 4 - ((float) fr.getStringWidth("yaw: " + yaw + " pitch: " + pitch) / 2), (float) mc.displayHeight / 4 + 30, -1);
//
//		if (blockData != null) {
//			fr.drawStringWithShadow("facing: " + blockData.getFacing().getName(), (float) mc.displayWidth / 4 - ((float) fr.getStringWidth("facing: " + blockData.getFacing().getName()) / 2), (float) mc.displayHeight / 4 + 40, -1);
//		}
//
//		if (prevBlockData != null) {
//			fr.drawStringWithShadow("prev facing: " + prevBlockData.getFacing().getName(), (float) mc.displayWidth / 4 - ((float) fr.getStringWidth("prev facing: " + prevBlockData.getFacing().getName()) / 2), (float) mc.displayHeight / 4 + 50, -1);
//		}
	}

	@Subscribe
	public void onUpdate(final UpdateEvent updateEvent) {
		if (getBlockCount() <= 0) {
			chatUtil.chatCommand("Toggling '" + getFeatureInfo().name() + "' as you are out of blocks!");
			toggle();
		}
	}

	@Subscribe
	public void onPreMotion(final PreMotionEvent preMotionEvent) {

		BlockPos blockUnder = new BlockPos(mc.thePlayer.posX, mc.thePlayer.posY - 1.0, mc.thePlayer.posZ);
		blockData = getBlockData(blockUnder);

		if (blockData == null) {
			return;
		}

		if (prevBlockData == null) {
			prevBlockData = blockData;
		}

		if (prevBlockData != blockData) {
			prevBlockData = blockData;
		}

		pitch = MathUtil.getRandom(80.0F, 82.0F);
		switch (mode.toLowerCase()) {
			case "vanilla" -> {
				switch (blockData.getFacing()) {
					case NORTH -> yaw = 0.0F;
					case EAST -> yaw = 90.0F;
					case SOUTH -> yaw = 180.0F;
					case WEST -> yaw = 270.0F;
					case UP -> {
						yaw = mc.thePlayer.rotationYaw;
						pitch = 90.0F;
					}
				}
			}
			case "hypixel" -> {
				// Calculate the yaw and pitch needed to look at the block, these should use as legit values as possible

				yaw = mc.thePlayer.rotationYaw - MathUtil.getRandom(178.0F, 182.0F);;
				if (Objects.requireNonNull(blockData.getFacing()) == EnumFacing.UP) {
					yaw = mc.thePlayer.rotationYaw;
					pitch = 90.0F;
				}
			}
		}

		Vector2f rotations = new Vector2f(yaw, pitch);
		Vector2f smoothRotations = RotationUtil.getSmoothRotations(mc.thePlayer.getPreviousRotation(), rotations, rotationSpeed);

		smoothRotations = RotationUtil.applySanity(smoothRotations);

		smoothRotations = RotationUtil.applyGCD(smoothRotations);

		Core.getSingleton().getRotationManager().setRotation(smoothRotations);

		// get current speed and get around 10% less
		if (mc.thePlayer.onGround && !Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			double currentSpeed = Math.sqrt(mc.thePlayer.motionX * mc.thePlayer.motionX + mc.thePlayer.motionZ * mc.thePlayer.motionZ);
			double speed = currentSpeed * 0.95D;

			// get direction
			float direction = mc.thePlayer.rotationYaw;

			// get motion
			double motionX = -Math.sin(direction / 180.0F * Math.PI) * speed;
			double motionZ = Math.cos(direction / 180.0F * Math.PI) * speed;

			// set motion
			mc.thePlayer.motionX = motionX;
			mc.thePlayer.motionZ = motionZ;
		}

		if (!rayCast(yaw, pitch)) {
			timerUtil.reset();
			return;
		}

		if (timerUtil.hasTimeElapsed(MathUtil.getRandom((int) placeDelay, (int) placeDelay + 10))) {
			if (getBlockSlot() != -1 && rayCast(yaw, pitch)) {
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
	}

	@Subscribe
	public void onPostMotion(final PostMotionEvent postMotionEvent) {

	}

	private Vec3 getVectorForRotation(float yaw, float pitch) {
		// Calculate trigonometric values for yaw and pitch
		double cosYaw = Math.cos(Math.toRadians(-yaw) - Math.PI);
		double sinYaw = Math.sin(Math.toRadians(-yaw) - Math.PI);
		double cosPitch = -Math.cos(Math.toRadians(-pitch));
		double sinPitch = Math.sin(Math.toRadians(-pitch));

		// Calculate and return the resulting vector
		return new Vec3(sinYaw * cosPitch, sinPitch, cosYaw * cosPitch);
	}

	private boolean rayCast(float yaw, float pitch) {
		// Get the player's eye position
		Vec3 eyePosition = mc.thePlayer.getPositionEyes(1.0F);

		// Calculate the direction vector based on yaw and pitch
		Vec3 directionVector = getVectorForRotation(yaw, pitch);

		// Calculate the endpoint of the ray-cast
		Vec3 rayEndPoint = eyePosition.addVector(directionVector.xCoord * 5.0, directionVector.yCoord * 5.0, directionVector.zCoord * 5.0);

		// Perform the ray-cast
		MovingObjectPosition result = mc.theWorld.rayTraceBlocks(eyePosition, rayEndPoint, false);

		// Check if the ray-cast hit a block and if it's the expected block position
		return result != null && result.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && blockData.getPosition().equals(result.getBlockPos());
	}


	private boolean isPosSolid(BlockPos pos) {
		Block block = mc.theWorld.getBlockState(pos).getBlock();
		return !invalidBlocks.contains(block);
	}

	private int getBlockSlot() {
		for(int k = 0; k < 9; ++k) {
			ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
			if (itemStack != null && isValid(itemStack) && itemStack.stackSize >= 1) {
				return k;
			}
		}

		return -1;
	}

	private boolean isValid(ItemStack itemStack) {
		if (itemStack.getItem() instanceof ItemBlock) {
			return !badBlocks.contains(((ItemBlock)itemStack.getItem()).getBlock());
		} else {
			return false;
		}
	}

	private int getBlockCount() {
		int count = 0;

		for(int k = 0; k < mc.thePlayer.inventory.mainInventory.length; ++k) {
			ItemStack itemStack = mc.thePlayer.inventory.mainInventory[k];
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
				new BlockPos(0, 0, -1)
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
				}
			}
		}

		return null; // Return null if no solid block is found in adjacent positions.
	}

	public Vec3 getHitVec() {
		/* Correct HitVec */
		Vec3 hitVec = new Vec3(blockData.getPosition().getX() + Math.random(), blockData.getPosition().getY() + Math.random(), blockData.getPosition().getZ() + Math.random());

		final MovingObjectPosition movingObjectPosition = rayCast(yaw, pitch) ? mc.theWorld.rayTraceBlocks(mc.thePlayer.getPositionEyes(1.0F), hitVec, false, false, true) : null;

		switch (blockData.enumFacing) {
			case DOWN -> hitVec.yCoord = blockData.getPosition().getY();
			case UP -> hitVec.yCoord = blockData.getPosition().getY() + 1;
			case NORTH -> hitVec.zCoord = blockData.getPosition().getZ();
			case EAST -> hitVec.xCoord = blockData.getPosition().getX() + 1;
			case SOUTH -> hitVec.zCoord = blockData.getPosition().getZ() + 1;
			case WEST -> hitVec.xCoord = blockData.getPosition().getX();
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
