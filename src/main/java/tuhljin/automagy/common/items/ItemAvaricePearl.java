package tuhljin.automagy.common.items;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import tuhljin.automagy.common.entities.EntityAvaricePearl;
import tuhljin.automagy.common.lib.References;

public class ItemAvaricePearl extends ModItem {
    public static final int DUMMY = 15;

    public ItemAvaricePearl() {
        super(References.ITEM_AVARICEPEARL);
    }

    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        int metadata = stack.getItemDamage();
        if (metadata == DUMMY) {
            stack.setCount(0);
            return stack;
        } else {
            boolean creative = player.capabilities.isCreativeMode;
            boolean canThrow = creative || metadata == 0;
            if (canThrow) {
                if (!creative) {
                    stack.shrink(1);
                }

                world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.BLOCKS, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
                if (!world.isRemote) {
                    int currentItemSlot = player.inventory.currentItem;
                    world.spawnEntity(new EntityAvaricePearl(world, metadata, player, currentItemSlot, creative, player.posX, player.posY, player.posZ));
                }
            }

            return stack;
        }
    }
}
