package tuhljin.automagy.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static int DEFAULT_PACKET_RANGE = 20;
    public static final SimpleNetworkWrapper INSTANCE;

    public PacketHandler() {
    }

    public static void registerMessages() {
        int id = 0;
        INSTANCE.registerMessage(MessageGUIFilter.class, MessageGUIFilter.class, id++, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIRecipe.class, MessageGUIRecipe.class, id++, Side.SERVER);
        INSTANCE.registerMessage(MessageParticles.class, MessageParticles.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageParticlesFloat.class, MessageParticlesFloat.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageParticlesTargeted.class, MessageParticlesTargeted.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageSound.class, MessageSound.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageHourglassFlipped.class, MessageHourglassFlipped.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageZap.class, MessageZap.class, id++, Side.CLIENT);
    }

    static {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("Automagy".toLowerCase());
    }
}
