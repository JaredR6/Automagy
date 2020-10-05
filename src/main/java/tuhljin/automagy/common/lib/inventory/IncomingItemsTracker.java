package tuhljin.automagy.common.lib.inventory;

import java.util.Map.Entry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.SealPos;
import tuhljin.automagy.common.entities.golems.ISuppliedBySeal;
import tuhljin.automagy.common.entities.golems.SealSupply;

import javax.annotation.Nonnull;

public class IncomingItemsTracker {
    @Nonnull
    protected FilteringItemList neededSuppliesUnmarked = new FilteringItemList();
    @Nonnull
    protected FilteringItemList incomingSupplies = new FilteringItemList();
    @Nonnull
    protected FilteringItemList incomingSuppliesExtra = new FilteringItemList();
    protected ISuppliedBySeal parent;
    protected World worldObj;
    protected BlockPos pos;
    protected int cacheTasksNeeded = -1;

    public IncomingItemsTracker(ISuppliedBySeal parent, World worldObj, BlockPos pos) {
        this.parent = parent;
        this.worldObj = worldObj;
        this.pos = pos;
    }

    public boolean needsSupply() {
        return !this.neededSuppliesUnmarked.isEmpty();
    }

    public int numSupplyTasksNeeded() {
        if (this.cacheTasksNeeded != -1) {
            return this.cacheTasksNeeded;
        } else {
            int needed = 0;
            if (this.needsSupply()) {
                for (Entry<SizelessItem, Integer> entry : this.neededSuppliesUnmarked) {
                    int count = entry.getValue();
                    ItemStack stack = entry.getKey().getItemStack(count);
                    int stacksTo = stack.isStackable() ? stack.getMaxStackSize() : 1;
                    if (stacksTo > 1) {
                        needed += MathHelper.roundUp(count, stacksTo);
                    } else {
                        ++needed;
                    }
                }
            }

            this.cacheTasksNeeded = needed;
            return needed;
        }
    }

    public int getAdditionalNeeded(@Nonnull ItemStack stack) {
        return this.neededSuppliesUnmarked.get(new SizelessItem(stack));
    }

    public void markSuppliesIncoming(@Nonnull ItemStack stack) {
        this.cacheTasksNeeded = -1;
        SizelessItem item = new SizelessItem(stack);
        int wanted = this.neededSuppliesUnmarked.get(item);
        int inc = Math.min(wanted, stack.getCount());
        if (wanted > 0) {
            this.neededSuppliesUnmarked.subtract(item, inc);
        }

        this.incomingSupplies.add(item, inc);
        if (stack.getCount() > inc) {
            this.incomingSuppliesExtra.add(item, stack.getCount() - inc);
        }

    }

    public void unmarkSuppliesIncoming(@Nonnull ItemStack stack) {
        this.cacheTasksNeeded = -1;
        SizelessItem item = new SizelessItem(stack);
        int toSubtract = stack.getCount();
        int extra = this.incomingSuppliesExtra.get(item);
        int i;
        if (extra > 0) {
            i = Math.min(extra, stack.getCount());
            this.incomingSuppliesExtra.subtract(item, i);
            toSubtract -= i;
        }

        if (toSubtract > 0) {
            i = Math.min(this.incomingSupplies.get(item), toSubtract);
            this.incomingSupplies.subtract(item, i);
            this.neededSuppliesUnmarked.add(item, i);
        }

    }

    public ItemStack supplyArrived(@Nonnull ItemStack stack) {
        this.cacheTasksNeeded = -1;
        ItemStack leftover = this.parent.receiveSupplyFromTracker(stack);
        int toSubtract = leftover.isEmpty() ? stack.getCount() : stack.getCount() - leftover.getCount();
        if (toSubtract > 0) {
            SizelessItem item = new SizelessItem(stack);
            int inc = this.incomingSupplies.get(item);
            if (inc > 0) {
                int i = Math.min(inc, toSubtract);
                this.incomingSupplies.subtract(item, i);
                toSubtract -= i;
            }

            if (toSubtract > 0) {
                this.incomingSuppliesExtra.subtract(item, toSubtract);
                if (this.neededSuppliesUnmarked.get(item) > 0) {
                    this.neededSuppliesUnmarked.subtract(item, toSubtract);
                }
            }
        }

        return leftover;
    }

    public void requestSupplies(@Nonnull IItemMap ingredients) {
        ISealEntity seal = this.getSupplySeal();
        if (seal != null) {
            for (Entry<SizelessItem, Integer> entry : ingredients) {
                int count = entry.getValue();
                if (count > 0) {
                    ItemStack stack = entry.getKey().getItemStack(count);
                    if (this.parent.hasInventorySpaceFor(stack)) {
                        this.cacheTasksNeeded = -1;
                        this.neededSuppliesUnmarked.add(entry.getKey(), count);
                        GolemHelper.requestProvisioning(this.worldObj, seal, stack);
                    }
                }
            }

        }
    }

    @Nullable
    public ISealEntity getSupplySeal() {
        int dim = this.worldObj.provider.getDimension();

        for (EnumFacing face : EnumFacing.values()) {
            SealPos sp = new SealPos(this.pos, face);
            ISealEntity se = GolemHelper.getSealEntity(dim, sp);
            if (se != null && se.getSeal() instanceof SealSupply && !se.isStoppedByRedstone(this.worldObj)) {
                return se;
            }
        }

        return null;
    }
}
