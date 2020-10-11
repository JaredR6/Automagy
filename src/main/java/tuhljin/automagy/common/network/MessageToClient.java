package tuhljin.automagy.common.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;

public abstract class MessageToClient<T> implements IMessage {

    public abstract static class Handler<T extends MessageToClient> implements IMessageHandler<T, IMessage> {
        @Nullable
        public IMessage onMessage(@Nonnull final T message, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(message::processMessage);
            return null;
        }
    }

    public abstract void processMessage();
}
