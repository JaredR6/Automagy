package tuhljin.automagy.common.tiles;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.inventory.FilteringItemList;

public class TileTallyDrops extends TileTallyBase {
    public TileTallyDrops() {
        super("tallyYield");
    }

    public FilteringItemList getDetectedItems() {
        BlockPos bc = this.getUltimateTarget();
        if (bc == null) {
            return null;
        } else {
            NonNullList<ItemStack> drops = TjUtil.getDropsFromBlock(this.world, bc, true, 0);
            return drops.size() > 0 ? new FilteringItemList(drops, false) : new FilteringItemList();
        }
    }

    public boolean hasValidTarget() {
        return this.targetY != -1 && !this.world.isAirBlock(new BlockPos(this.targetX, this.targetY, this.targetZ));
    }
}
