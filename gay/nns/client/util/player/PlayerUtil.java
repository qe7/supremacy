package gay.nns.client.util.player;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;

public class PlayerUtil {

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
			Block block = Minecraft.getMinecraft().theWorld.getBlockState(new BlockPos(Minecraft.getMinecraft().thePlayer.posX, (double) i, Minecraft.getMinecraft().thePlayer.posZ)).getBlock();
			if (block != Blocks.air && block != Blocks.water && block != Blocks.lava && block != Blocks.flowing_water && block != Blocks.flowing_lava) {
				nearestGround = new BlockPos(Minecraft.getMinecraft().thePlayer.posX, (double) i, Minecraft.getMinecraft().thePlayer.posZ);
				break;
			}
		}

		return nearestGround != null && Minecraft.getMinecraft().thePlayer.posY <= (double) (nearestGround.getY() + 1) + offset;
	}
}
