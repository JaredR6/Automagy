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
import javax.annotation.Nullable;
import tuhljin.automagy.common.gui.ContainerRecipe;

import javax.annotation.Nonnull;

public class MessageGUIRecipe implements IMessage, IMessageHandler<MessageGUIRecipe, IMessage> {
    protected NonNullList<ItemStack> stacks;

    public MessageGUIRecipe() {
    }

    public MessageGUIRecipe(@Nonnull ItemStack[] stacksRecipe) {
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

    @Nullable
    public IMessage onMessage(@Nonnull MessageGUIRecipe message, @Nonnull MessageContext ctx) {
        EntityPlayer player = ctx.getServerHandler().player;
        if (player != null) {
            Container container = player.openContainer;
            if (container instanceof ContainerRecipe) {
                ((ContainerRecipe)container).receiveMessageFromClient(message.stacks);
            }
        }

        return null;
    }

    public static void sendToServer(@Nonnull ItemStack[] stacks) {
        PacketHandler.INSTANCE.sendToServer(new MessageGUIRecipe(stacks));
    }
}
