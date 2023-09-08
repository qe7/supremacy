package gay.nns.client.impl.feature.other;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.Mode;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.impl.event.player.PostMotionEvent;
import gay.nns.client.impl.event.player.PreMotionEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import net.minecraft.block.Block;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;

import java.util.Arrays;
import java.util.List;

@FeatureInfo(name = "Scaffold", description = "Automatically places blocks under you")
public class FeatureScaffold extends AbstractFeature {

	/* thank co-pilot for this, :) */
	private final List<Block> badBlocks = Arrays.asList(Blocks.air, Blocks.water, Blocks.flowing_water, Blocks.lava, Blocks.flowing_lava, Blocks.enchanting_table, Blocks.carpet, Blocks.glass_pane, Blocks.stained_glass_pane, Blocks.iron_bars, Blocks.snow_layer, Blocks.ice, Blocks.packed_ice, Blocks.coal_ore, Blocks.diamond_ore, Blocks.emerald_ore, Blocks.chest, Blocks.trapped_chest, Blocks.torch, Blocks.anvil, Blocks.trapped_chest, Blocks.noteblock, Blocks.jukebox, Blocks.tnt, Blocks.gold_ore, Blocks.iron_ore, Blocks.lapis_ore, Blocks.lit_redstone_ore, Blocks.quartz_ore, Blocks.redstone_ore, Blocks.wooden_pressure_plate, Blocks.stone_pressure_plate, Blocks.light_weighted_pressure_plate, Blocks.heavy_weighted_pressure_plate, Blocks.stone_button, Blocks.wooden_button, Blocks.lever, Blocks.tallgrass, Blocks.tripwire, Blocks.tripwire_hook, Blocks.rail, Blocks.waterlily, Blocks.red_flower, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.vine, Blocks.trapdoor, Blocks.yellow_flower, Blocks.ladder, Blocks.furnace, Blocks.sand, Blocks.cactus, Blocks.dispenser, Blocks.noteblock, Blocks.dropper, Blocks.crafting_table, Blocks.web, Blocks.pumpkin, Blocks.sapling, Blocks.cobblestone_wall, Blocks.oak_fence, Blocks.beacon);

	@Serialize(name = "Mode")
	@Mode(modes = {"Vanilla", "Hypixel"})
	public String mode = "Vanilla";

	public FeatureScaffold() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();
	}

	@Override
	protected void onDisable() {
		super.onDisable();
	}

	@Subscribe
	public void onRender2D(final Render2DEvent render2DEvent) {
		FontRenderer fr = mc.fontRendererObj;

		String blocks = "blocks:";



		fr.drawStringWithShadow(blocks, (float) mc.displayWidth / 4 - ((float) fr.getStringWidth(blocks) / 2), (float) mc.displayHeight / 4 + 10, 0xffffff);
	}

	@Subscribe
	public void onPreMotion(final PreMotionEvent preMotionEvent) {

	}

	@Subscribe
	public void onPostMotion(final PostMotionEvent postMotionEvent) {

	}

}
