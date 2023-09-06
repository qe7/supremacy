package gay.nns.client.util.player;

import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.item.*;

public class ItemUtil {

	public static ItemStack[] getBestArmorInChest(ContainerChest chest) {
		ItemStack[] itemStacks = new ItemStack[4];
		float helmetValue = 0.0F;
		float leggingsValue = 0.0F;
		float chestplateValue = 0.0F;
		float bootsValue = 0.0F;

		for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
			ItemStack itemStack = chest.getLowerChestInventory().getStackInSlot(i);
			if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
				if (((ItemArmor)itemStack.getItem()).armorType == 0 && getArmorValue(itemStack) > helmetValue) {
					helmetValue = getArmorValue(itemStack);
					itemStacks[0] = itemStack;
				}

				if (((ItemArmor)itemStack.getItem()).armorType == 1 && getArmorValue(itemStack) > chestplateValue) {
					chestplateValue = getArmorValue(itemStack);
					itemStacks[1] = itemStack;
				}

				if (((ItemArmor)itemStack.getItem()).armorType == 2 && getArmorValue(itemStack) > leggingsValue) {
					leggingsValue = getArmorValue(itemStack);
					itemStacks[2] = itemStack;
				}

				if (((ItemArmor)itemStack.getItem()).armorType == 3 && getArmorValue(itemStack) > bootsValue) {
					bootsValue = getArmorValue(itemStack);
					itemStacks[3] = itemStack;
				}
			}
		}

		return itemStacks;
	}

	public static ItemStack[] getBestToolsInChest(ContainerChest chest) {
		ItemStack[] itemStacks = new ItemStack[3];
		float pickaxeValue = 0.0F;
		float axeValue = 0.0F;
		float shovelValue = 0.0F;

		for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
			ItemStack itemStack = chest.getLowerChestInventory().getStackInSlot(i);
			if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemTool) {
				if (itemStack.getItem() instanceof ItemPickaxe && getToolValue(itemStack) > pickaxeValue) {
					pickaxeValue = getToolValue(itemStack);
					itemStacks[0] = itemStack;
				}

				if (itemStack.getItem() instanceof ItemAxe && getToolValue(itemStack) > axeValue) {
					axeValue = getToolValue(itemStack);
					itemStacks[1] = itemStack;
				}

				if (itemStack.getItem() instanceof ItemSpade && getToolValue(itemStack) > shovelValue) {
					shovelValue = getToolValue(itemStack);
					itemStacks[2] = itemStack;
				}
			}
		}

		return itemStacks;
	}

	public static int[] getBestArmorInChestAsSlots(ContainerChest chest) {
		int[] slots = new int[]{-1, -1, -1, -1};
		float helmetValue = 0.0F;
		float leggingsValue = 0.0F;
		float chestplateValue = 0.0F;
		float bootsValue = 0.0F;

		for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
			ItemStack itemStack = chest.getLowerChestInventory().getStackInSlot(i);
			if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
				if (((ItemArmor)itemStack.getItem()).armorType == 0 && getArmorValue(itemStack) > helmetValue) {
					helmetValue = getArmorValue(itemStack);
					slots[0] = i;
				}

				if (((ItemArmor)itemStack.getItem()).armorType == 1 && getArmorValue(itemStack) > chestplateValue) {
					chestplateValue = getArmorValue(itemStack);
					slots[1] = i;
				}

				if (((ItemArmor)itemStack.getItem()).armorType == 2 && getArmorValue(itemStack) > leggingsValue) {
					leggingsValue = getArmorValue(itemStack);
					slots[2] = i;
				}

				if (((ItemArmor)itemStack.getItem()).armorType == 3 && getArmorValue(itemStack) > bootsValue) {
					bootsValue = getArmorValue(itemStack);
					slots[3] = i;
				}
			}
		}

		return slots;
	}

	public static ItemStack[] getBestArmorInInventory() {
		ItemStack[] itemStacks = new ItemStack[4];
		float helmetValue = 0.0F;
		float leggingsValue = 0.0F;
		float chestplateValue = 0.0F;
		float bootsValue = 0.0F;

		for(int i = 0; i < 45; ++i) {
			if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack() != null) {
				ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
				if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
					if (((ItemArmor)itemStack.getItem()).armorType == 0 && getArmorValue(itemStack) > helmetValue) {
						helmetValue = getArmorValue(itemStack);
						itemStacks[0] = itemStack;
					}

					if (((ItemArmor)itemStack.getItem()).armorType == 1 && getArmorValue(itemStack) > chestplateValue) {
						chestplateValue = getArmorValue(itemStack);
						itemStacks[1] = itemStack;
					}

					if (((ItemArmor)itemStack.getItem()).armorType == 2 && getArmorValue(itemStack) > leggingsValue) {
						leggingsValue = getArmorValue(itemStack);
						itemStacks[2] = itemStack;
					}

					if (((ItemArmor)itemStack.getItem()).armorType == 3 && getArmorValue(itemStack) > bootsValue) {
						bootsValue = getArmorValue(itemStack);
						itemStacks[3] = itemStack;
					}
				}
			}
		}

		return itemStacks;
	}

	public static int[] getBestArmorInInventoryAsSlots() {
		int[] slots = new int[]{-1, -1, -1, -1};
		float helmetValue = 0.0F;
		float leggingsValue = 0.0F;
		float chestplateValue = 0.0F;
		float bootsValue = 0.0F;

		for(int i = 0; i < 45; ++i) {
			if (Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack() != null) {
				ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
				if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemArmor) {
					if (((ItemArmor)itemStack.getItem()).armorType == 0 && getArmorValue(itemStack) > helmetValue) {
						helmetValue = getArmorValue(itemStack);
						slots[0] = i;
					}

					if (((ItemArmor)itemStack.getItem()).armorType == 1 && getArmorValue(itemStack) > chestplateValue) {
						chestplateValue = getArmorValue(itemStack);
						slots[1] = i;
					}

					if (((ItemArmor)itemStack.getItem()).armorType == 2 && getArmorValue(itemStack) > leggingsValue) {
						leggingsValue = getArmorValue(itemStack);
						slots[2] = i;
					}

					if (((ItemArmor)itemStack.getItem()).armorType == 3 && getArmorValue(itemStack) > bootsValue) {
						bootsValue = getArmorValue(itemStack);
						slots[3] = i;
					}
				}
			}
		}

		return slots;
	}

	public static ItemStack[] getBestToolsInInventory() {
		ItemStack[] itemStacks = new ItemStack[3];
		float pickaxeValue = 0.0F;
		float axeValue = 0.0F;
		float shovelValue = 0.0F;

		for(int i = 0; i < 45; ++i) {
			ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemTool) {
				if (itemStack.getItem() instanceof ItemPickaxe && getToolValue(itemStack) > pickaxeValue) {
					pickaxeValue = getToolValue(itemStack);
					itemStacks[0] = itemStack;
				}

				if (itemStack.getItem() instanceof ItemAxe && getToolValue(itemStack) > axeValue) {
					axeValue = getToolValue(itemStack);
					itemStacks[1] = itemStack;
				}

				if (itemStack.getItem() instanceof ItemSpade && getToolValue(itemStack) > shovelValue) {
					shovelValue = getToolValue(itemStack);
					itemStacks[2] = itemStack;
				}
			}
		}

		return itemStacks;
	}

	public static int[] getBestToolsInInventoryAsSlots() {
		int[] slots = new int[]{-1, -1, -1};
		float pickaxeValue = 0.0F;
		float axeValue = 0.0F;
		float shovelValue = 0.0F;

		for(int i = 0; i < 45; ++i) {
			ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemTool) {
				if (itemStack.getItem() instanceof ItemPickaxe && getToolValue(itemStack) > pickaxeValue) {
					pickaxeValue = getToolValue(itemStack);
					slots[0] = i;
				}

				if (itemStack.getItem() instanceof ItemAxe && getToolValue(itemStack) > axeValue) {
					axeValue = getToolValue(itemStack);
					slots[1] = i;
				}

				if (itemStack.getItem() instanceof ItemSpade && getToolValue(itemStack) > shovelValue) {
					shovelValue = getToolValue(itemStack);
					slots[2] = i;
				}
			}
		}

		return slots;
	}

	public static int getBestWeaponInInventoryAsSlot() {
		int slot = -1;
		float value = 0.0F;

		for(int i = 0; i < 45; ++i) {
			ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() != null && (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemSword) && getWeaponValue(itemStack) > value) {
				value = getWeaponValue(itemStack);
				slot = i;
			}
		}

		return slot;
	}

	public static ItemStack getBestWeaponInInventory() {
		float value = 0.0F;
		ItemStack returnStack = null;

		for(int i = 0; i < 45; ++i) {
			ItemStack itemStack = Minecraft.getMinecraft().thePlayer.inventoryContainer.getSlot(i).getStack();
			if (itemStack != null && itemStack.getItem() != null && (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemSword) && getWeaponValue(itemStack) > value) {
				value = getWeaponValue(itemStack);
				returnStack = itemStack;
			}
		}

		return returnStack;
	}

	public static int getBestWeaponInChestAsSlot(ContainerChest chest) {
		int slot = -1;
		float value = 0.0F;

		for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
			ItemStack itemStack = chest.getLowerChestInventory().getStackInSlot(i);
			if (itemStack != null && itemStack.getItem() != null && (itemStack.getItem() instanceof ItemTool || itemStack.getItem() instanceof ItemSword) && getWeaponValue(itemStack) > value) {
				value = getWeaponValue(itemStack);
				slot = i;
			}
		}

		return slot;
	}

	public static int[] getBestToolsInChestAsSlots(ContainerChest chest) {
		int[] slots = new int[]{-1, -1, -1};
		float pickAxeValue = 0.0F;
		float axeValue = 0.0F;
		float shovelValue = 0.0F;

		for(int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); ++i) {
			ItemStack itemStack = chest.getLowerChestInventory().getStackInSlot(i);
			if (itemStack != null && itemStack.getItem() != null && itemStack.getItem() instanceof ItemTool) {
				if (itemStack.getItem() instanceof ItemPickaxe && getToolValue(itemStack) > pickAxeValue) {
					pickAxeValue = getToolValue(itemStack);
					slots[0] = i;
				}

				if (itemStack.getItem() instanceof ItemAxe && getToolValue(itemStack) > axeValue) {
					axeValue = getToolValue(itemStack);
					slots[1] = i;
				}

				if (itemStack.getItem() instanceof ItemSpade && getToolValue(itemStack) > shovelValue) {
					shovelValue = getToolValue(itemStack);
					slots[2] = i;
				}
			}
		}

		return slots;
	}


	public static float getArmorValue(ItemStack itemStack) {
		float value = (float)(((ItemArmor)itemStack.getItem()).damageReduceAmount + EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack));
		value += (float)(itemStack.getMaxDamage() - itemStack.getItemDamage()) / 2.0F;
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack) * 10);
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.projectileProtection.effectId, itemStack) * 5);
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.fireProtection.effectId, itemStack) * 5);
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 5);
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.blastProtection.effectId, itemStack) * 2);
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, itemStack) * 2);
		return value;
	}

	public static float getToolValue(ItemStack itemStack) {
		float value = ((ItemTool) itemStack.getItem()).getEfficiencyOnProperMaterial() + (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, itemStack);
		value += (float)(itemStack.getMaxDamage() - itemStack.getItemDamage());
		value += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.efficiency.effectId, itemStack);
		value += (float)EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) / 2.0F;
		return value;
	}

	public static float getWeaponValue(ItemStack itemStack) {
		float value = (float)(itemStack.getMaxDamage() - itemStack.getItemDamage()) / 300.0F;
		
		if (itemStack.getItem() instanceof ItemSword) {
			value += ((ItemSword) itemStack.getItem()).getAttackDamage() * ((ItemSword) itemStack.getItem()).getAttackDamage();
			value *= 1.33F;
		}

		if (itemStack.getItem() instanceof ItemTool) {
			value += ((ItemTool) itemStack.getItem()).getDamageVsEntity() * ((ItemTool) itemStack.getItem()).getDamageVsEntity();
		}

		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, itemStack) * 20);
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, itemStack) * 15);
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, itemStack) * 10);
		value += (float)(EnchantmentHelper.getEnchantmentLevel(Enchantment.smite.effectId, itemStack) * 5);
		if (itemStack.getMaxDamage() - itemStack.getItemDamage() < 13) {
			value = 0.0F;
		}

		return value;
	}

}
