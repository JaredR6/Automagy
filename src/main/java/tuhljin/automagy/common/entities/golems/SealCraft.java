package tuhljin.automagy.common.entities.golems;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.gui.ContainerSealCraft;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileGolemWorkbench;

import javax.annotation.Nonnull;

public class SealCraft extends ModSealFiltered implements ISealConfigToggles {
    protected SealToggle[] props;
    protected int delay = (new Random(System.nanoTime())).nextInt(50);
    protected int watchedTask = -2147483648;
    @Nullable
    protected ItemStack stackGoal = null;
    protected int filterCycle = 0;

    public SealCraft() {
        super("craft");
        this.props = new SealToggle[]{new SealToggle(false, "pleave", "golem.prop.leave"), new SealToggle(false, "pcycle", "golem.prop.cycle")};
    }

    public SealCraft(String key, SealToggle[] toggles) {
        super(key);
        this.props = toggles;
    }

    public boolean canPlaceAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {
        return TjUtil.getBlock(world, pos) == ModBlocks.golemWorkbench;
    }

    public void tickSeal(@Nonnull World world, @Nonnull ISealEntity seal) {
        if (this.delay++ % 20 == 0) {
            Task oldTask = ThaumcraftExtension.getGolemTask(world, this.watchedTask);
            if (oldTask == null || oldTask.isSuspended() || oldTask.isCompleted()) {
                this.stackGoal = null;
                TileGolemWorkbench te = this.getWorkbench(world, seal);
                if (te != null) {
                    boolean leaveOne = this.shouldPreserveLastItem();
                    if (!this.isBlacklist()) {
                        if (this.shouldCycleWhitelist()) {
                            ArrayList<ItemStack> list = new ArrayList<>();
                            for (ItemStack stack : this.getInv()) {
                                if (!stack.isEmpty()) {
                                    list.add(stack);
                                }
                            }

                            if (!list.isEmpty()) {
                                int count = list.size();
                                int iFinal = Math.abs(this.filterCycle % count);
                                int index = iFinal;
                                while (true) {
                                    ItemStack goal = list.get(index);
                                    if (this.tryCreateTask(world, seal, goal, te, leaveOne))
                                        break;
                                    this.filterCycle++;
                                    index = Math.abs(this.filterCycle % count);
                                    if (index == iFinal)
                                        break;
                                }
                            }
                        } else {
                            for (ItemStack goal : this.getInv()) {
                                if (!goal.isEmpty()) {
                                    if (this.tryCreateTask(world, seal, goal, te, leaveOne))
                                        break;
                                }
                            }
                        }
                    } else {
                        boolean filterEmpty = this.isFilterEmpty();

                        for(int i = 0; i < 4; ++i) {
                            ItemStack goal = te.getRecipeGoal(i);
                            if (!goal.isEmpty() && (filterEmpty || !this.isInFilter(goal)) && this.tryCreateTask(world, seal, goal, te, leaveOne)) {
                                break;
                            }
                        }
                    }
                }
            }

        }
    }

    protected boolean tryCreateTask(@Nonnull World world, @Nonnull ISealEntity seal, @Nonnull ItemStack goal, @Nonnull TileGolemWorkbench te, boolean leaveOne) {
        Integer i = te.whichRecipeIndexHasCraftingComponents(goal, leaveOne, -2147483648);
        if (i == null) {
            return false;
        } else {
            this.watchedTask = ModSeal.createTaskAtSealPos(world, seal);
            this.stackGoal = goal.copy();
            return true;
        }
    }

    public void onTaskStarted(@Nonnull World world, @Nonnull IGolemAPI golem, @Nonnull Task task) {
        ISealEntity seal = GolemHelper.getSealEntity(golem.getGolemWorld().provider.getDimension(), task.getSealPos());
        TileGolemWorkbench te = this.getWorkbench(world, seal);
        if (te != null) {
            Integer i = te.whichRecipeIndexHasCraftingComponents(this.stackGoal, this.shouldPreserveLastItem(), -2147483648);
            if (i != null) {
                te.reserveItems(this.watchedTask, i);
                return;
            }
        }

        task.setSuspended(true);
    }

    public boolean onTaskCompletion(@Nonnull World world, @Nonnull IGolemAPI golem, @Nonnull Task task) {
        ISealEntity seal = GolemHelper.getSealEntity(golem.getGolemWorld().provider.getDimension(), task.getSealPos());
        TileGolemWorkbench te = this.getWorkbench(world, seal);
        boolean leaveOne = this.shouldPreserveLastItem();
        if (te != null && te.hasCraftingComponents(this.stackGoal, leaveOne, this.watchedTask)) {
            EntityLivingBase eGolem = golem.getGolemEntity();
            if (eGolem.motionX != 0.0D || Math.abs(eGolem.motionY) > 0.1D && eGolem.motionZ != 0.0D) {
                task.setLifespan((short)((int)Math.max(task.getLifespan(), 10L)));
                return false;
            }

            golem.swingArm();
            ItemStack result = te.consumeComponentsAndCraft(this.stackGoal, leaveOne);
            ItemStack leftover;
            if (this.shouldHoldResult()) {
                leftover = golem.holdItem(result);
                eGolem.playSound(SoundEvents.BLOCK_LAVA_POP, 0.1F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 1.5F);
            } else {
                leftover = te.insertItemIntoLastAvailableSlot(result);
                if (!leftover.isEmpty()) {
                    leftover = golem.holdItem(result);
                    eGolem.playSound(SoundEvents.BLOCK_LAVA_POP, 0.1F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 1.5F);
                }
            }

            if (!leftover.isEmpty()) {
                TjUtil.dropItemIntoWorld(leftover, world, eGolem.posX, eGolem.posY, eGolem.posZ);
            }

            this.stackGoal = null;
            this.craftingSuccess(world, seal, golem, task);
        }

        task.setSuspended(true);
        return true;
    }

    public void craftingSuccess(World world, ISealEntity seal, IGolemAPI golem, Task task) {
    }

    public void onTaskSuspension(@Nonnull World world, @Nonnull Task task) {
        TileEntity te = world.getTileEntity(task.getSealPos().pos);
        if (te instanceof TileGolemWorkbench) {
            ((TileGolemWorkbench)te).releaseItemReservation(task.getId());
        }

    }

    public boolean canGolemPerformTask(@Nonnull IGolemAPI golem, Task task) {
        return ThaumcraftExtension.isGolemCarryingNothing(golem);
    }

    public void onRemoval(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileGolemWorkbench) {
            ((TileGolemWorkbench)te).releaseItemReservation(this.watchedTask);
        }

    }

    public int getFilterSize() {
        return 9;
    }

    public boolean hasStacksizeLimiters() {
        return false;
    }

    public EnumGolemTrait[] getRequiredTags() {
        return new EnumGolemTrait[]{EnumGolemTrait.DEFT};
    }

    @Nullable
    public EnumGolemTrait[] getForbiddenTags() {
        return null;
    }

    @Nonnull
    public int[] getGuiCategories() {
        return new int[]{1, 3, 0, 4};
    }

    public SealToggle[] getToggles() {
        return this.props;
    }

    public void setToggle(int indx, boolean value) {
        this.props[indx].setValue(value);
    }

    public boolean shouldPreserveLastItem() {
        return this.getToggles()[0].getValue();
    }

    public boolean shouldHoldResult() {
        return true;
    }

    public boolean shouldCycleWhitelist() {
        return this.getToggles()[1].getValue();
    }

    @Nonnull
    public Object returnContainer(World world, @Nonnull EntityPlayer player, BlockPos pos, EnumFacing side, @Nonnull ISealEntity seal) {
        return new ContainerSealCraft(player.inventory, world, seal);
    }

    @Nullable
    public TileGolemWorkbench getWorkbench(@Nonnull World world, @Nonnull ISealEntity seal) {
        TileEntity te = world.getTileEntity(seal.getSealPos().pos);
        return te instanceof TileGolemWorkbench ? (TileGolemWorkbench)te : null;
    }
}
