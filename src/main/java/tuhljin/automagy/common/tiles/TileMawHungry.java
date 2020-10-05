package tuhljin.automagy.common.tiles;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.items.IItemHandler;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.inventory.ItemHandlerUtil;

import javax.annotation.Nonnull;

public class TileMawHungry extends TileMawBase {
    public static int MIN_SOUND_INTERVAL = 250;
    public long nextNoise = 0L;

    public TileMawHungry() {
    }

    public void moveItem(@Nonnull EntityItem item) {
        if (!this.blockedBySignal()) {
            EnumFacing facing = this.getFacing();
            TileEntity te = this.world.getTileEntity(this.pos.offset(facing));
            IItemHandler itemHandler = ItemHandlerUtil.getHandler(te, facing.getOpposite());
            if (itemHandler != null) {
                ItemStack leftovers = this.depositItem(item.getItem(), itemHandler);
                if (!leftovers.isEmpty() || leftovers.getCount() != item.getItem().getCount()) {
                    if (leftovers.isEmpty()) {
                        item.setDead();
                    } else {
                        item.setItem(leftovers.copy());
                    }

                    long currentTime = System.currentTimeMillis();
                    if (currentTime >= this.nextNoise) {
                        this.nextNoise = currentTime + (long)MIN_SOUND_INTERVAL;
                        this.world.playSound(null, item.getPosition(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 0.3F, (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F + 1.5F);
                        this.world.playSound(null, item.getPosition(), SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 0.35F, (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F - 1.0F);
                    }
                }
            } else {
                this.getBlockType().onNeighborChange(this.world, this.pos, this.pos.offset(facing));
            }

        }
    }

    protected ItemStack depositItem(@Nonnull ItemStack stack, IItemHandler itemHandler) {
        return TjUtil.addToInventory(stack, itemHandler, false);
    }
}
