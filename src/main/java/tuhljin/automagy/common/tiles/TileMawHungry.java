package tuhljin.automagy.common.tiles;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.inventory.ItemHandlerUtil;

public class TileMawHungry extends TileMawBase {
    public static int MIN_SOUND_INTERVAL = 250;
    public long nextNoise = 0L;

    public TileMawHungry() {
    }

    public void moveItem(EntityItem item) {
        if (!this.blockedBySignal()) {
            EnumFacing facing = this.getFacing();
            TileEntity te = this.field_145850_b.func_175625_s(this.field_174879_c.func_177972_a(facing));
            IItemHandler itemHandler = ItemHandlerUtil.getHandler(te, facing.func_176734_d());
            if (itemHandler != null) {
                ItemStack leftovers = this.depositItem(item.func_92059_d(), itemHandler);
                if (leftovers == null || leftovers.field_77994_a != item.func_92059_d().field_77994_a) {
                    if (leftovers == null) {
                        item.func_70106_y();
                    } else {
                        item.func_92058_a(leftovers.func_77946_l());
                    }

                    long currentTime = System.currentTimeMillis();
                    if (currentTime >= this.nextNoise) {
                        this.nextNoise = currentTime + (long)MIN_SOUND_INTERVAL;
                        this.field_145850_b.func_72956_a(item, "random.eat", 0.3F, (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.2F + 1.5F);
                        this.field_145850_b.func_72956_a(item, "random.eat", 0.35F, (this.field_145850_b.field_73012_v.nextFloat() - this.field_145850_b.field_73012_v.nextFloat()) * 0.2F - 1.0F);
                    }
                }
            } else {
                this.func_145838_q().func_176204_a(this.field_145850_b, this.field_174879_c, this.field_145850_b.func_180495_p(this.field_174879_c), this.func_145838_q());
            }

        }
    }

    protected ItemStack depositItem(ItemStack stack, IItemHandler itemHandler) {
        return TjUtil.addToInventory(stack, itemHandler, false);
    }
}
