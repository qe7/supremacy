package gay.nns.client.impl.feature.combat;

import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.AbstractFeature;
import gay.nns.client.api.feature.enums.FeatureCategory;
import gay.nns.client.api.feature.interfaces.FeatureInfo;
import gay.nns.client.api.setting.annotations.CheckBox;
import gay.nns.client.api.setting.annotations.Serialize;
import gay.nns.client.api.setting.annotations.Slider;
import gay.nns.client.impl.event.packet.PacketReceiveEvent;
import gay.nns.client.impl.event.render.Render2DEvent;
import gay.nns.client.util.IMinecraft;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

@FeatureInfo(name = "Velocity", category = FeatureCategory.COMBAT, description = "Manipulates velocity")
public class FeatureVelocity extends AbstractFeature {

	@Serialize(name = "Horizontal")
	@Slider(min = -100, max = 100, increment = 1)
	public double horizontal = 0;

	@Serialize(name = "Vertical")
	@Slider(min = -100, max = 100, increment = 1)
	public double vertical = 0;

	@Serialize(name = "Water_Check")
	@CheckBox
	public boolean waterCheck = true;

	public FeatureVelocity() {
		super();
	}

	@Subscribe
	public void onRender(final Render2DEvent render2DEvent) {
		this.setSuffix((int) horizontal + "% " + (int) vertical + "%");
	}

	@Subscribe
	public void onPacketReceive(final PacketReceiveEvent event) {
		if (IMinecraft.mc.theWorld == null) return;
		if (IMinecraft.mc.thePlayer == null) return;
		if (IMinecraft.mc.thePlayer.isInWater() && waterCheck) return;

		if (event.getPacket() instanceof S12PacketEntityVelocity s12) {
			if (s12.getEntityID() == IMinecraft.mc.thePlayer.getEntityId()) {
				s12.motionX *= (int) (horizontal / 100);
				s12.motionY *= (int) (vertical / 100);
				s12.motionZ *= (int) (horizontal / 100);
			}
		}
		if (event.getPacket() instanceof S27PacketExplosion s27) {
			s27.posX *= horizontal / 100;
			s27.posY *= vertical / 100;
			s27.posZ *= horizontal / 100;
		}
	}

}
