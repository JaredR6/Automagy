package tuhljin.automagy.common.tiles;

import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import javax.annotation.Nullable;
import tuhljin.automagy.common.lib.inventory.FilteringItemList;

public class TileTallyWorld extends TileTallyBase {
    public static double EXTRARANGE = 0.1D;

    public TileTallyWorld() {
        super("tallyUnbound");
    }

    @Nullable
    public FilteringItemList getDetectedItems() {
        BlockPos bc = this.getUltimateTarget();
        if (bc == null) {
            return null;
        } else {
            FilteringItemList list = new FilteringItemList();
            AxisAlignedBB box = new AxisAlignedBB((double)this.targetX - EXTRARANGE, (double)this.targetY - EXTRARANGE, (double)this.targetZ - EXTRARANGE, (double)this.targetX + EXTRARANGE + 1.0D, (double)this.targetY + EXTRARANGE + 1.0D, (double)this.targetZ + EXTRARANGE + 1.0D);
            List<EntityItem> entities = this.world.getEntitiesWithinAABB(EntityItem.class, box);

            for (EntityItem item : entities) {
                list.add(item.getItem());
            }

            return list;
        }
    }

    public boolean hasValidTarget() {
        return true;
    }
}