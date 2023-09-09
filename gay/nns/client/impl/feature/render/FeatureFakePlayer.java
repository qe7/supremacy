package gay.nns.client.impl.feature.render;

import com.mojang.authlib.GameProfile;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

@FeatureInfo(name = "FakePlayer", description = "Spawns a fake player", category = FeatureCategory.RENDER)
public class FeatureFakePlayer extends Feature {

	public FeatureFakePlayer() {
		super();
	}

	@Override
	protected void onEnable() {
		super.onEnable();

		EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP(mc.theWorld, new GameProfile(UUID.fromString("62007255-b0c2-4a8b-b4c0-1a1f76ff4d14"), "CyberTF2"));
		fakePlayer.copyLocationAndAnglesFrom(mc.thePlayer);
		fakePlayer.copyDataFromOld(mc.thePlayer);
		mc.theWorld.addEntityToWorld(62007255, fakePlayer);
	}

	@Override
	protected void onDisable() {
		super.onDisable();

		mc.theWorld.removeEntityFromWorld(62007255);
	}

}
