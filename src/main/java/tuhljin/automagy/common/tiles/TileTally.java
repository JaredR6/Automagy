package tuhljin.automagy.common.tiles;

import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import javax.annotation.Nullable;
import tuhljin.automagy.common.lib.inventory.FilteringItemList;

public class TileTally extends TileTallyBase {
    public TileTally() {
        super("tally");
    }

    @Nullable
    public FilteringItemList getDetectedItems() {
        BlockPos bc = this.getUltimateTarget();
        if (bc == null) {
            return null;
        } else {
            TileEntity target = this.world.getTileEntity(bc);
            if (target == null) {
                return null;
            } else if (target instanceof IHasItemList) {
                return ((IHasItemList)target).getItemList();
            } else if (target.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
                return (new FilteringItemList()).populateFromInventory(target.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
            } else {
                return target instanceof IInventory ? (new FilteringItemList()).populateFromInventory((IInventory)target, false) : null;
            }
        }
    }

    public boolean hasValidTarget() {
        if (this.targetY != -1) {
            TileEntity target = this.world.getTileEntity(new BlockPos(this.targetX, this.targetY, this.targetZ));
            return target instanceof IInventory || target instanceof IHasItemList;
        }

        return false;
    }
}
