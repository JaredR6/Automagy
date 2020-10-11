package tuhljin.automagy.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;

public class PacketHandler {
    public static int DEFAULT_PACKET_RANGE = 20;
    @Nonnull
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(References.MOD_ID);

    public static void registerMessages() {
        int id = 0;
        INSTANCE.registerMessage(MessageGUIFilter.Handler.class, MessageGUIFilter.class, id++, Side.SERVER);
        INSTANCE.registerMessage(MessageGUIRecipe.Handler.class, MessageGUIRecipe.class, id++, Side.SERVER);
        INSTANCE.registerMessage(MessageParticles.Handler.class, MessageParticles.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageParticlesFloat.Handler.class, MessageParticlesFloat.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageParticlesTargeted.Handler.class, MessageParticlesTargeted.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageSound.Handler.class, MessageSound.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageHourglassFlipped.Handler.class, MessageHourglassFlipped.class, id++, Side.CLIENT);
        INSTANCE.registerMessage(MessageZap.Handler.class, MessageZap.class, id++, Side.CLIENT);
    }
}
