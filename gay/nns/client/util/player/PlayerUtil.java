package gay.nns.client.util.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;

import java.util.ArrayList;

public class PlayerUtil {

	private static final ItemUtil itemUtil = new ItemUtil();

	public static void sendClick(int button, boolean state) {
		int keyBind = button == 0 ? Minecraft.getMinecraft().gameSettings.keyBindAttack.getKeyCode() : Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode();

		KeyBinding.setKeyBindState(keyBind, state);

		if (state) {
			KeyBinding.onTick(keyBind);
		}
	}

	public static boolean isOverVoid() {
		for (double posY = Minecraft.getMinecraft().thePlayer.posY; posY > 0.0; --posY) {
			Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, posY, Minecraft.getMinecraft().thePlayer.posZ)).getBlock();
			if (!(block instanceof BlockAir)) {
				return false;
			}
		}
		return true;
	}

	public static boolean onGround(double offset) {
		BlockPos nearestGround = null;

		for (int i = (int) Math.ceil(Minecraft.getMinecraft().thePlayer.posY); i > 0; --i) {
			Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, i, Minecraft.getMinecraft().thePlayer.posZ)).getBlock();
			if (block != Blocks.air && block != Blocks.water && block != Blocks.lava && block != Blocks.flowing_water && block != Blocks.flowing_lava) {
				nearestGround = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, i, Minecraft.getMinecraft().thePlayer.posZ);
				break;
			}
		}

		return nearestGround != null && Minecraft.getMinecraft().thePlayer.posY <= (double) (nearestGround.getY() + 1) + offset;
	}

	public static boolean isBadPotion(ItemStack stack) {
		if (stack != null && stack.getItem() instanceof ItemPotion potion) {
			if (ItemPotion.isSplash(stack.getItemDamage())) {
				for (PotionEffect o : potion.getEffects(stack)) {
					if (o.getPotionID() == Potion.poison.getId() || o.getPotionID() == Potion.harm.getId() || o.getPotionID() == Potion.moveSlowdown.getId() || o.getPotionID() == Potion.weakness.getId()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public static boolean isInventoryFull() {
		for (int i = 0; i < 36; ++i) {
			if (Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i] == null) {
				return false;
			}
		}
		return true;
	}

	private static final ArrayList<ItemStack> trash = new ArrayList<>();

	/* Thanks Co-Pilot */
	public static boolean isBadItem(Item item) {
		return
				item instanceof ItemBucket ||
				item instanceof ItemFishingRod ||
				item instanceof ItemFlintAndSteel ||
				item instanceof ItemPotion ||
				item instanceof ItemFood ||
				item instanceof ItemBoat ||
				item instanceof ItemMinecart ||
				item instanceof ItemSaddle ||
				item instanceof ItemFirework ||
				item instanceof ItemFireworkCharge ||
				item instanceof ItemMap ||
				item instanceof ItemHangingEntity ||
				item instanceof ItemRecord ||
				item instanceof ItemSign ||
				item instanceof ItemEnchantedBook ||
				item instanceof ItemNameTag ||
				item instanceof ItemArmorStand ||
				item instanceof ItemBanner;
	}

	/**
	 * @author Shae
	 *
	 * @param containerChest
	 *
	 * @description
	 * And so the cancer begins.
	 * This method is used to get the best items in a chest.
	 * It's used in the chest stealer feature. It's a mess.
	 * it uses the ItemUtil class (which is also a mess) to get the best items in a chest.
	 *
	 * @see ItemUtil
	 *
	 * @return ArrayList<Integer> of the best items in the chest
	 */
	public static ArrayList<Integer> getBestItems(ContainerChest containerChest) {
		ArrayList<Integer> useful = new ArrayList<>();

		for (int i = 0; i < containerChest.getLowerChestInventory().getSizeInventory(); ++i) {
			ItemStack itemStack = containerChest.getLowerChestInventory().getStackInSlot(i);

			if (itemStack != null /*&& !isBad(itemStack.getItem())*/) {
				if (!(itemStack.getItem() instanceof ItemSword) && !(itemStack.getItem() instanceof ItemTool) && !(itemStack.getItem() instanceof ItemArmor)) {
					useful.add(i);
				} else if (itemStack.getItem() instanceof ItemSword) {
					if (itemUtil.getBestWeaponInChestAsSlot(containerChest) != i) {
						trash.add(itemStack);
					} else if (itemUtil.getBestWeaponInInventory() != null) {
						if (itemUtil.getWeaponValue(itemStack) > itemUtil.getWeaponValue(itemUtil.getBestWeaponInInventory())) {
							useful.add(i);
						} else {
							trash.add(itemStack);
						}
					} else {
						useful.add(i);
					}
				} else if (itemStack.getItem() instanceof ItemTool) {
					if (itemStack.getItem() instanceof ItemPickaxe) {
						int bestPickaxeSlot = itemUtil.getBestToolsInChestAsSlots(containerChest)[0];
						ItemStack bestPickaxeInventory = itemUtil.getBestToolsInInventory()[0];

						if (bestPickaxeSlot != i) {
							trash.add(itemStack);
						} else if (bestPickaxeInventory != null) {
							if (itemUtil.getToolValue(itemStack) > itemUtil.getToolValue(bestPickaxeInventory)) {
								useful.add(i);
							} else {
								trash.add(itemStack);
							}
						} else {
							useful.add(i);
						}
					} else if (itemStack.getItem() instanceof ItemAxe) {
						int bestAxeSlot = itemUtil.getBestToolsInChestAsSlots(containerChest)[1];
						ItemStack bestAxeInventory = itemUtil.getBestToolsInInventory()[1];

						if (bestAxeSlot != i) {
							trash.add(itemStack);
						} else if (bestAxeInventory != null) {
							if (itemUtil.getToolValue(itemStack) > itemUtil.getToolValue(bestAxeInventory)) {
								useful.add(i);
							} else {
								trash.add(itemStack);
							}
						} else {
							useful.add(i);
						}
					} else if (itemStack.getItem() instanceof ItemSpade) {
						int bestSpadeSlot = itemUtil.getBestToolsInChestAsSlots(containerChest)[2];
						ItemStack bestSpadeInventory = itemUtil.getBestToolsInInventory()[2];

						if (bestSpadeSlot != i) {
							trash.add(itemStack);
						} else if (bestSpadeInventory != null) {
							if (itemUtil.getToolValue(itemStack) > itemUtil.getToolValue(bestSpadeInventory)) {
								useful.add(i);
							} else {
								trash.add(itemStack);
							}
						} else {
							useful.add(i);
						}
					}
				} else {
					int armorType = ((ItemArmor) itemStack.getItem()).armorType;
					int[] bestArmorSlots = itemUtil.getBestArmorInChestAsSlots(containerChest);
					ItemStack[] bestArmorInventory = itemUtil.getBestArmorInInventory();

					if (armorType >= 0 && armorType < 4) {
						if (bestArmorSlots[armorType] != i) {
							trash.add(itemStack);
						} else if (bestArmorInventory[armorType] != null) {
							if (itemUtil.getArmorValue(itemStack) > itemUtil.getArmorValue(bestArmorInventory[armorType])) {
								useful.add(i);
							} else {
								trash.add(itemStack);
							}
						} else {
							useful.add(i);
						}
					}
				}
			}
		}

		return useful;
	}

	public static ArrayList<ItemStack> getTrash() {
		return trash;
	}

}
