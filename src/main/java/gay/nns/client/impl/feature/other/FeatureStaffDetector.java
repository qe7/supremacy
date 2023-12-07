package gay.nns.client.impl.feature.other;


import gay.nns.client.api.event.interfaces.Subscribe;
import gay.nns.client.api.feature.Feature;
import gay.nns.client.api.feature.interfaces.SerializeFeature;
import gay.nns.client.impl.event.packet.EventPacketReceive;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S02PacketChat;
import net.minecraft.network.play.server.S3EPacketTeams;
import net.minecraft.util.ChatComponentText;

import java.util.ArrayList;
import java.util.List;

@SerializeFeature(name = "StaffDetector", description = "Detects Staff")
public class FeatureStaffDetector extends Feature {

    public FeatureStaffDetector() {
        super();
    }

    @Subscribe
    public void onPacket(EventPacketReceive event) {
        Packet<?> p = event.getPacket();


        if (p instanceof S3EPacketTeams) {
            if (mc.thePlayer.ticksExisted < 20) {
                return;
            }

            final S3EPacketTeams packet = (S3EPacketTeams) p;

            final List<String> fatPeople = new ArrayList<>();
            fatPeople.add("Ev2n");
            fatPeople.add("Eissaa");
            fatPeople.add("mohmad_q8");
            fatPeople.add("1Daykel");
            fatPeople.add("xImTaiG_");
            fatPeople.add("Nshme");
            fatPeople.add("comsterr");
            fatPeople.add("e9_");
            fatPeople.add("1MeKo");
            fatPeople.add("1LaB");
            fatPeople.add("MK_F16");
            fatPeople.add("loovq");
            fatPeople.add("sadeq");
            fatPeople.add("nv0ola");
            fatPeople.add("1F5aMH___3oo");
            fatPeople.add("xMz7");
            fatPeople.add("Harbi");
            fatPeople.add("xiDayzer");
            fatPeople.add("Firas");
            fatPeople.add("EyesO_Diamond");
            fatPeople.add("1Rana");
            fatPeople.add("DeFiCeNcY");
            fatPeople.add("DouglasF15");
            fatPeople.add("1HeyImHasson");
            fatPeople.add("devilsvul");
            fatPeople.add("Meedo_qb");
            fatPeople.add("Ahm2d");
            fatPeople.add("LuxRosea");
            fatPeople.add("Casteret");
            fatPeople.add("curiousmyths");
            fatPeople.add("420kinaka");
            fatPeople.add("1flyn");
            fatPeople.add("NonameIsHere");
            fatPeople.add("Bunkrat");
            fatPeople.add("n8al3lomblocks");
            fatPeople.add("Aymann_");
            fatPeople.add("unusunusunus");
            fatPeople.add("1ZEYAD");
            fatPeople.add("i7ilin");
            fatPeople.add("Zerokame44");
            fatPeople.add("ss3ed");
            fatPeople.add("akthem");
            fatPeople.add("Postme");
            fatPeople.add("3Mmr");
            fatPeople.add("Iv2a");
            fatPeople.add("Y2men");
            fatPeople.add("quinque0quinque");
            fatPeople.add("RamboKinq");
            fatPeople.add("1Ahmd");
            fatPeople.add("bogdanpvp1");
            fatPeople.add("1Elyy");
            fatPeople.add("rR3L");
            fatPeople.add("R1tanium");
            fatPeople.add("Sanfoor_J");
            fatPeople.add("A2boD");
            fatPeople.add("Jrx7");
            fatPeople.add("Hunter47");
            fatPeople.add("0hFault");
            fatPeople.add("xL2d");
            fatPeople.add("xfahadq");
            fatPeople.add("1Abdllah");
            fatPeople.add("7rBe_8aAad7");
            fatPeople.add("Mr_1990");
            fatPeople.add("KinderBueno__");
            fatPeople.add("lt1x");
            fatPeople.add("vxom");
            fatPeople.add("zChangerr");
            fatPeople.add("Mzad");
            fatPeople.add("Wyssap");
            fatPeople.add("GsOMAR");
            fatPeople.add("BeastToggled");
            fatPeople.add("CigsAfterBeer");
            fatPeople.add("Miyxmura");
            fatPeople.add("Endersent");
            fatPeople.add("Exhaustr");
            fatPeople.add("1S3oD");
            fatPeople.add("iKubca");
            fatPeople.add("Werthly");
            fatPeople.add("R3");
            fatPeople.add("StrongesT0ne");
            fatPeople.add("Ventz");
            fatPeople.add("938awy");
            fatPeople.add("Xd3M_");
            fatPeople.add("1SPEEDO_");
            fatPeople.add("7be");
            fatPeople.add("6nabh");
            fatPeople.add("N0uf");
            fatPeople.add("alk52");
            fatPeople.add("qLaith");
            fatPeople.add("zixgamer");
            fatPeople.add("xz7a");
            fatPeople.add("Bnmv123");
            fatPeople.add("zSwift");
            fatPeople.add("ITsMo7amed_");
            fatPeople.add("Watchdog");
            fatPeople.add("FexoraNEP");
            fatPeople.add("MVP11");
            fatPeople.add("akash1004");
            fatPeople.add("Neeres");
            fatPeople.add("TheOldDays_");
            fatPeople.add("Time98");
            fatPeople.add("Vhagardracarys");
            fatPeople.add("khaled12341234");
            fatPeople.add("0RyZe");
            fatPeople.add("1Reyleigh");
            fatPeople.add("ohhhhQls");
            fatPeople.add("Nshmee");
            fatPeople.add("Revoox");
            fatPeople.add("Rieus");
            fatPeople.add("ToxicLayer");
            fatPeople.add("Mr3nb_");
            fatPeople.add("1NotForMaster");
            fatPeople.add("1Lzye");
            fatPeople.add("Pynifical");
            fatPeople.add("Sexyz");
            fatPeople.add("M7mmqd");
            fatPeople.add("OnlySpam");
            fatPeople.add("UnderTest");
            fatPeople.add("1hry");
            fatPeople.add("M_7B");
            fatPeople.add("7rBe_WaAaFY");
            fatPeople.add("Blzxer");
            fatPeople.add("Escoco");
            fatPeople.add("smckingweed");
            fatPeople.add("SweetyAlice");
            fatPeople.add("1S4L");
            fatPeople.add("1Alii_");
            fatPeople.add("DarkVzd");
            fatPeople.add("uxvv");
            fatPeople.add("DrW2rden");
            fatPeople.add("ilerz_");
            fatPeople.add("Dr_Kratos1");
            fatPeople.add("Raqxklps");
            fatPeople.add("1HypersX");
            fatPeople.add("Vanitas_0");
            fatPeople.add("HDZT");
            fatPeople.add("Rma7o");
            fatPeople.add("3bdooooooo");
            fatPeople.add("420WaFFLe");
            fatPeople.add("Wacros");
            fatPeople.add("EmpatheticEyes");
            fatPeople.add("Yarin");
            fatPeople.add("Yawelkm");
            fatPeople.add("Lordui");
            fatPeople.add("rivsci");
            fatPeople.add("kingpvp90");
            fatPeople.add("izLORDeX");
            fatPeople.add("DreadPirateR0B");
            fatPeople.add("OpGaming2009");
            fatPeople.add("Pipars6");
            fatPeople.add("Mvjed");
            fatPeople.add("LovelyLayan");
            fatPeople.add("savobabo");
            fatPeople.add("GlowDown_");
            fatPeople.add("i_b");
            fatPeople.add("odex");
            fatPeople.add("sh59");
            fatPeople.add("Luffy404");
            fatPeople.add("Io2n");
            fatPeople.add("ixd7vl");
            fatPeople.add("Laarcii");
            fatPeople.add("0hQxmar");
            fatPeople.add("1Ashu");
            fatPeople.add("Rayqquaza");
            fatPeople.add("Zaytook");
            fatPeople.add("Krejums");
            fatPeople.add("Razen555");
            fatPeople.add("1Mostyy");
            fatPeople.add("Saallm");
            fatPeople.add("iMizuki");
            fatPeople.add("Mohvm3d");
            fatPeople.add("N13M");
            fatPeople.add("Refolt");
            fatPeople.add("MoldyToe5654");


            for (final String name : packet.getPlayers()) {
                if (fatPeople.contains(name)) {
                    for (int i = 0; i < 10; i++) {
                        mc.thePlayer.addChatComponentMessage(new ChatComponentText("Staff joined " + name));
                    }
                }
            }
        }
    }
}


