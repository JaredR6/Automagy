package tuhljin.automagy.common.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class MessageToClient<T extends MessageToClient> implements IMessage, IMessageHandler<T, IMessage> {
    public MessageToClient() {
    }

    @SideOnly(Side.CLIENT)
    public IMessage onMessage(final T message, MessageContext ctx) {
        Minecraft.getMinecraft().addScheduledTask(message::processMessage);
        return null;
    }

    public abstract void processMessage();
}
