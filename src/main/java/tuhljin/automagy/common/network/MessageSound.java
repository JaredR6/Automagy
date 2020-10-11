package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.Automagy;

import javax.annotation.Nonnull;

public class MessageSound extends MessageToClient<MessageSound> {
    public static final short WHISPERS = 0;
    protected short id;

    public MessageSound(short id) {
        this.id = id;
    }

    @Override
    public void fromBytes(@Nonnull ByteBuf buf) {
        this.id = buf.readShort();
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeShort(this.id);
    }

    @SideOnly(Side.CLIENT)
    public void processMessage() {
        EntityPlayerSP player = FMLClientHandler.instance().getClientPlayerEntity();
        if (this.id == 0) {
            player.playSound(SoundsTC.whispers, 0.5F, 1.0F);
        }
    }

    public static void sendToClient(short id, EntityPlayerMP player) {
        PacketHandler.INSTANCE.sendTo(new MessageSound(id), player);
    }

    public static void sendToClient(short id, EntityPlayer player) {
        if (player instanceof EntityPlayerMP) {
            sendToClient(id, (EntityPlayerMP)player);
        } else {
            Automagy.logError("Could not send sound packet to client. Player object was the wrong type!");
        }
    }

    public static class Handler extends MessageToClient.Handler<MessageSound> {}
}
