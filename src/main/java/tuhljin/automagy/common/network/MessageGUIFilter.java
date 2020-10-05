package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import javax.annotation.Nullable;
import tuhljin.automagy.common.gui.ContainerFilter;

import javax.annotation.Nonnull;

public class MessageGUIFilter implements IMessage, IMessageHandler<MessageGUIFilter, IMessage> {
    private int type;
    private String data;

    public MessageGUIFilter(int type, String data) {
        this.type = type;
        this.data = data;
    }

    public void fromBytes(@Nonnull ByteBuf buf) {
        this.type = buf.readByte();
        this.data = ByteBufUtils.readUTF8String(buf);
    }

    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeByte(this.type);
        ByteBufUtils.writeUTF8String(buf, this.data);
    }

    @Nullable
    public IMessage onMessage(@Nonnull MessageGUIFilter message, @Nonnull MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        if (player != null) {
            Container container = player.openContainer;
            if (container instanceof ContainerFilter) {
                ((ContainerFilter)container).receiveMessageFromClient(message.type, message.data);
            }
        }

        return null;
    }

    public static void sendToServer(int type, String data) {
        PacketHandler.INSTANCE.sendToServer(new MessageGUIFilter(type, data));
    }
}
