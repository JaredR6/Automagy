package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import tuhljin.automagy.common.gui.ContainerRecipe;

public class MessageGUIRecipe implements IMessage, IMessageHandler<MessageGUIRecipe, IMessage> {
    protected NonNullList<ItemStack> stacks;

    public MessageGUIRecipe() {
    }

    public MessageGUIRecipe(ItemStack[] stacksRecipe) {
        this.stacks = NonNullList.create();

        for (ItemStack itemStack : stacksRecipe) {
            this.stacks.add(itemStack.copy());
        }

    }

    public void fromBytes(ByteBuf buf) {
        this.stacks = NonNullList.create();

        for(int i = 0; i < 9; ++i) {
            this.stacks.add(ByteBufUtils.readItemStack(buf));
        }

    }

    public void toBytes(ByteBuf buf) {
        for(int i = 0; i < 9; ++i) {
            ByteBufUtils.writeItemStack(buf, this.stacks.get(i));
        }

    }

    public IMessage onMessage(MessageGUIRecipe message, MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        if (player != null) {
            Container container = player.openContainer;
            if (container instanceof ContainerRecipe) {
                ((ContainerRecipe)container).receiveMessageFromClient(message.stacks);
            }
        }

        return null;
    }

    public static void sendToServer(ItemStack[] stacks) {
        PacketHandler.INSTANCE.sendToServer(new MessageGUIRecipe(stacks));
    }
}
