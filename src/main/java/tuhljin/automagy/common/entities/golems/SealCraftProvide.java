package tuhljin.automagy.common.entities.golems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.ProvisionRequest;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.entities.construct.EntityOwnedConstruct;
import thaumcraft.common.golems.EntityThaumcraftGolem;
import thaumcraft.common.golems.tasks.TaskHandler;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.inventory.IItemMap;
import tuhljin.automagy.common.tiles.TileGolemWorkbench;

import javax.annotation.Nonnull;

public class SealCraftProvide extends SealCraft {
    @Nullable ProvisionRequest request = null;

    public SealCraftProvide() {
        super("craft_provide", new SealToggle[]{new SealToggle(false, "pleave", "golem.prop.leave"), new SealToggle(false, "ppro", "automagy.golem.craftSupply")});
    }

    @Nonnull
    public EnumGolemTrait[] getRequiredTags() {
        return new EnumGolemTrait[]{EnumGolemTrait.DEFT, EnumGolemTrait.SMART};
    }

    public boolean shouldCycleWhitelist() {
        return false;
    }

    public boolean shouldRequestMissingIngredients() {
        return this.getToggles()[1].getValue();
    }

    public void tickSeal(@Nonnull World world, @Nonnull ISealEntity seal) {
        if (this.delay++ % 20 == 0) {
            Task oldTask = ThaumcraftExtension.getGolemTask(world, this.watchedTask);
            if (oldTask == null || oldTask.isSuspended() || oldTask.isCompleted()) {
                this.stackGoal = null;
                TileGolemWorkbench te = this.getWorkbench(world, seal);
                if (te != null) {
                    int dim = world.provider.getDimension();
                    if (GolemHelper.provisionRequests.containsKey(dim)) {
                        ArrayList<ItemStack> goalsMissingIngredients = new ArrayList<>();
                        ListIterator<ProvisionRequest> it = GolemHelper.provisionRequests.get(dim).listIterator();

                        while(it.hasNext()) {
                            ProvisionRequest pr = it.next();
                            if (pr.equals(this.request)) {
                                it.remove();
                                this.request = null;
                            } else {
                                if (pr.getSeal().getSealPos().pos.distanceSq(seal.getSealPos().pos) < 4096.0D) {
                                    ItemStack goal = pr.getStack();
                                    if (this.isRequestAllowedByFilter(goal)) {
                                        if (this.canFulfillRequest(te, seal, goal)) {
                                            Task task = new Task(seal.getSealPos(), seal.getSealPos().pos);
                                            task.setPriority(pr.getSeal().getPriority());
                                            task.setLifespan((short)5);
                                            TaskHandler.addTask(dim, task);
                                            this.watchedTask = task.getId();
                                            this.stackGoal = goal.copy();
                                            this.request = pr;
                                            it.remove();
                                            return;
                                        }

                                        for(int i = 0; i < 4; ++i) {
                                            ItemStack recGoal = te.getRecipeGoal(i);
                                            if (TjUtil.areItemsEqualIgnoringSize(goal, recGoal)) {
                                                goalsMissingIngredients.add(goal);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!goalsMissingIngredients.isEmpty() && this.shouldRequestMissingIngredients()) {
                            int i = Math.abs(this.filterCycle % goalsMissingIngredients.size());
                            ++this.filterCycle;
                            IItemMap ingredients = te.getMissingIngredients(goalsMissingIngredients.get(i), this.shouldPreserveLastItem());
                            if (ingredients != null) {
                                te.getSupplyTracker().requestSupplies(ingredients);
                            }
                        }
                    }
                }
            }

        }
    }

    protected boolean isRequestAllowedByFilter(@Nonnull ItemStack goal) {
        if (this.isBlacklist()) {
            return !this.isInFilter(goal);
        } else {
            return this.isInFilter(goal);
        }

    }

    protected boolean canFulfillRequest(@Nonnull TileGolemWorkbench te, ISealEntity seal, @Nonnull ItemStack goal) {
        return te.hasCraftingComponents(goal, this.shouldPreserveLastItem(), Integer.MIN_VALUE);
    }

    public boolean canGolemPerformTask(@Nonnull IGolemAPI golem, Task task) {
        if (this.request != null && super.canGolemPerformTask(golem, task)) {
            return ((EntityThaumcraftGolem)golem).isWithinHomeDistanceFromPosition(this.request.getSeal().getSealPos().pos) && this.areGolemTagsValidForTask(this.request.getSeal(), golem) && !this.request.getStack().isEmpty() && golem.canCarry(this.request.getStack(), true);
        } else {
            return false;
        }
    }

    protected boolean areGolemTagsValidForTask(@Nullable ISealEntity se, @Nonnull IGolemAPI golem) {
        if (se == null) {
            return true;
        } else if (se.isLocked() && ((EntityOwnedConstruct)golem).isOwned() && !se.getOwner().equals(((IEntityOwnable) (golem)).getOwnerId().toString())) {
            return false;
        } else if (se.getSeal().getRequiredTags() != null && !golem.getProperties().getTraits().containsAll(Arrays.asList(se.getSeal().getRequiredTags()))) {
            return false;
        } else {
            if (se.getSeal().getForbiddenTags() != null) {
                for (EnumGolemTrait tag : se.getSeal().getForbiddenTags()) {
                    if (golem.getProperties().getTraits().contains(tag)) {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    public void onTaskSuspension(@Nonnull World world, @Nonnull Task task) {
        super.onTaskSuspension(world, task);
    }

    public void craftingSuccess(@Nonnull World world, ISealEntity seal, IGolemAPI golem, Task task) {
        if (this.request != null) {
            Integer dim = world.provider.getDimension();
            if (GolemHelper.provisionRequests.containsKey(dim)) {
                ListIterator<ProvisionRequest> it = GolemHelper.provisionRequests.get(dim).listIterator();

                while(it.hasNext()) {
                    if (it.next().equals(this.request)) {
                        it.remove();
                        break;
                    }
                }
            }
        }

    }
}
