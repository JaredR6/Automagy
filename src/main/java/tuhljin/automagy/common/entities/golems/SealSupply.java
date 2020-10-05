package tuhljin.automagy.common.entities.golems;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;

import javax.annotation.Nonnull;

public class SealSupply extends ModSeal {
    protected int delay = (new Random(System.nanoTime())).nextInt(50);
    protected int watchedTask = Integer.MIN_VALUE;
    @Nonnull
    protected Map<Integer, ItemStack> lookupDelivering = new HashMap<>();

    public SealSupply() {
        super("supply");
    }

    @Override
    public boolean canPlaceAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {
        return world.getTileEntity(pos) instanceof ISuppliedBySeal;
    }

    @Override
    public void tickSeal(@Nonnull World world, @Nonnull ISealEntity seal) {
        if (this.delay % 100 == 0) {
            this.cleanLookup(world, seal.getSealPos().pos, false);
        }

        if (this.delay++ % 10 == 0) {
            ISuppliedBySeal tile = this.getTile(world, seal.getSealPos().pos);
            if (tile != null && tile.getSupplyTracker().needsSupply()) {
                Task oldTask = ThaumcraftExtension.getGolemTask(world, this.watchedTask);
                if (oldTask == null || oldTask.isSuspended() || oldTask.isCompleted() || oldTask.isReserved()) {
                    this.watchedTask = createTaskAtSealPos(world, seal);
                }
            }

        }
    }

    @Override
    public void onTaskStarted(@Nonnull World world, @Nonnull IGolemAPI golem, @Nonnull Task task) {
        ItemStack carrying = golem.getCarrying().get(0);
        if (!carrying.isEmpty()) {
            ISuppliedBySeal tile = this.getTile(world, task.getSealPos().pos);
            if (tile != null && tile.getSupplyTracker().needsSupply() && tile.getSupplyTracker().getAdditionalNeeded(carrying) > 0) {
                tile.getSupplyTracker().markSuppliesIncoming(carrying);
                this.lookupDelivering.put(task.getId(), carrying.copy());
                return;
            }
        }

        task.setSuspended(true);
    }

    @Override
    public boolean onTaskCompletion(@Nonnull World world, @Nonnull IGolemAPI golem, @Nonnull Task task) {
        ISuppliedBySeal tile = this.getTile(world, task.getSealPos().pos);
        ItemStack carrying = golem.getCarrying().get(0);
        if (tile != null && !carrying.isEmpty()) {
            ItemStack leftover = tile.getSupplyTracker().supplyArrived(carrying.copy());
            golem.dropItem(carrying);
            if (leftover != null) {
                leftover = golem.holdItem(leftover);
                if (leftover != null) {
                    EntityLivingBase eGolem = golem.getGolemEntity();
                    TjUtil.dropItemIntoWorld(leftover, world, eGolem.posX, eGolem.posY, eGolem.posZ);
                }
            }
        }

        return true;
    }

    @Override
    public void onTaskSuspension(@Nonnull World world, @Nonnull Task task) {
        Integer i = task.getId();
        if (this.lookupDelivering.containsKey(i)) {
            ISuppliedBySeal tile = this.getTile(world, task.getSealPos().pos);
            if (tile != null) {
                tile.getSupplyTracker().unmarkSuppliesIncoming(this.lookupDelivering.get(i));
            }

            this.lookupDelivering.remove(i);
        }

    }

    @Override
    public void onRemoval(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {
        this.cleanLookup(world, pos, true);
    }

    protected void cleanLookup(@Nonnull World world, @Nonnull BlockPos pos, boolean all) {
        ISuppliedBySeal tile = this.getTile(world, pos);
        if (tile != null) {

            Iterator<Integer> it = this.lookupDelivering.keySet().iterator();

            while(it.hasNext()) {
                Integer key = it.next();
                if (all) {
                    tile.getSupplyTracker().unmarkSuppliesIncoming(this.lookupDelivering.get(key));
                    it.remove();
                } else {
                    Task t = ThaumcraftExtension.getGolemTask(world, key);
                    if (t == null) {
                        tile.getSupplyTracker().unmarkSuppliesIncoming(this.lookupDelivering.get(key));
                        it.remove();
                    }
                }
            }

        }
    }

    @Override
    public boolean canGolemPerformTask(@Nonnull IGolemAPI golem, @Nonnull Task task) {
        ItemStack carrying = golem.getCarrying().get(0);
        if (!carrying.isEmpty()) {
            World world = golem.getGolemWorld();
            ISuppliedBySeal tile = this.getTile(world, task.getSealPos().pos);
            if (tile != null && tile.getSupplyTracker().needsSupply()) {
                return tile.getSupplyTracker().getAdditionalNeeded(carrying) > 0;
            } else {
                task.setSuspended(true);
            }
        }

        return false;
    }

    @Nullable
    @Override
    public EnumGolemTrait[] getRequiredTags() {
        return null;
    }

    @Nonnull
    @Override
    public EnumGolemTrait[] getForbiddenTags() {
        return new EnumGolemTrait[]{EnumGolemTrait.CLUMSY};
    }

    @Nonnull
    @Override
    public int[] getGuiCategories() {
        return new int[]{0, 4};
    }

    @Nullable
    public ISuppliedBySeal getTile(@Nonnull World world, @Nonnull BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof ISuppliedBySeal ? (ISuppliedBySeal)te : null;
    }
}
