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

public class UtilPlayer {

	private static final ArrayList<ItemStack> trash = new ArrayList<>();

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

	/* Thanks Co-Pilot */
	public static boolean isBadItem(Item item) {
		return item instanceof ItemBucket || item instanceof ItemFishingRod || item instanceof ItemFlintAndSteel || item instanceof ItemPotion || item instanceof ItemFood || item instanceof ItemBoat || item instanceof ItemMinecart || item instanceof ItemSaddle || item instanceof ItemFirework || item instanceof ItemFireworkCharge || item instanceof ItemMap || item instanceof ItemHangingEntity || item instanceof ItemRecord || item instanceof ItemSign || item instanceof ItemEnchantedBook || item instanceof ItemNameTag || item instanceof ItemArmorStand || item instanceof ItemBanner;
	}

	/**
	 * @param containerChest
	 * @return ArrayList<Integer> of the best items in the chest
	 * @author Shae
	 * @description And so the cancer begins.
	 * This method is used to get the best items in a chest.
	 * It's used in the chest stealer feature. It's a mess.
	 * it uses the ItemUtil class (which is also a mess) to get the best items in a chest.
	 * @see UtilItem
	 */
	public static ArrayList<Integer> getBestItems(ContainerChest containerChest) {
		ArrayList<Integer> useful = new ArrayList<>();

		for (int i = 0; i < containerChest.getLowerChestInventory().getSizeInventory(); ++i) {
			ItemStack itemStack = containerChest.getLowerChestInventory().getStackInSlot(i);

			if (itemStack != null /*&& !isBad(itemStack.getItem())*/) {
				if (!(itemStack.getItem() instanceof ItemSword) && !(itemStack.getItem() instanceof ItemTool) && !(itemStack.getItem() instanceof ItemArmor)) {
					useful.add(i);
				} else if (itemStack.getItem() instanceof ItemSword) {
					if (UtilItem.getBestWeaponInChestAsSlot(containerChest) != i) {
						trash.add(itemStack);
					} else if (UtilItem.getBestWeaponInInventory() != null) {
						if (UtilItem.getWeaponValue(itemStack) > UtilItem.getWeaponValue(UtilItem.getBestWeaponInInventory())) {
							useful.add(i);
						} else {
							trash.add(itemStack);
						}
					} else {
						useful.add(i);
					}
				} else if (itemStack.getItem() instanceof ItemTool) {
					if (itemStack.getItem() instanceof ItemPickaxe) {
						int bestPickaxeSlot = UtilItem.getBestToolsInChestAsSlots(containerChest)[0];
						ItemStack bestPickaxeInventory = UtilItem.getBestToolsInInventory()[0];

						bestSlot(useful, i, itemStack, bestPickaxeSlot, bestPickaxeInventory);
					} else if (itemStack.getItem() instanceof ItemAxe) {
						int bestAxeSlot = UtilItem.getBestToolsInChestAsSlots(containerChest)[1];
						ItemStack bestAxeInventory = UtilItem.getBestToolsInInventory()[1];

						bestSlot(useful, i, itemStack, bestAxeSlot, bestAxeInventory);
					} else if (itemStack.getItem() instanceof ItemSpade) {
						int bestSpadeSlot = UtilItem.getBestToolsInChestAsSlots(containerChest)[2];
						ItemStack bestSpadeInventory = UtilItem.getBestToolsInInventory()[2];

						bestSlot(useful, i, itemStack, bestSpadeSlot, bestSpadeInventory);
					}
				} else {
					int armorType = ((ItemArmor) itemStack.getItem()).armorType;
					int[] bestArmorSlots = UtilItem.getBestArmorInChestAsSlots(containerChest);
					ItemStack[] bestArmorInventory = UtilItem.getBestArmorInInventory();

					if (armorType >= 0 && armorType < 4) {
						if (bestArmorSlots[armorType] != i) {
							trash.add(itemStack);
						} else if (bestArmorInventory[armorType] != null) {
							if (UtilItem.getArmorValue(itemStack) > UtilItem.getArmorValue(bestArmorInventory[armorType])) {
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

	private static void bestSlot(ArrayList<Integer> useful, int i, ItemStack itemStack, int bestSpadeSlot, ItemStack bestSpadeInventory) {
		if (bestSpadeSlot != i) {
			trash.add(itemStack);
		} else if (bestSpadeInventory != null) {
			if (UtilItem.getToolValue(itemStack) > UtilItem.getToolValue(bestSpadeInventory)) {
				useful.add(i);
			} else {
				trash.add(itemStack);
			}
		} else {
			useful.add(i);
		}
	}

	public static ArrayList<ItemStack> getTrash() {
		return trash;
	}

}
