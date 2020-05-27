package tuhljin.automagy.common.entities.golems;

import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemStack;
import net.minecraft.network.EnumPacketDirection;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import thaumcraft.api.golems.EnumGolemTrait;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.seals.ISealConfigArea;
import thaumcraft.api.golems.seals.ISealConfigToggles;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.lib.network.FakeNetHandlerPlayServer;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;

public class SealShear extends ModSealFiltered implements ISealConfigToggles, ISealConfigArea {
    protected SealToggle[] props;
    protected int delay = (new Random(System.nanoTime())).nextInt(50);
    protected FakePlayer fp;
    protected HashMap<Integer, Long> cacheBlocks = new HashMap<>();

    public SealShear() {
        super("shear");
        this.props = new SealToggle[]{new SealToggle(true, "saveShears", "Automagy.golem.saveShears"), new SealToggle(false, "ppro", "golem.prop.provision.wl")};
    }

    public SealShear(String key, SealToggle[] toggles) {
        super(key);
        this.props = toggles;
    }

    public void tickSeal(World world, ISealEntity seal) {
        if (this.delay % 100 == 0) {
            Iterator<Integer> it = this.cacheBlocks.keySet().iterator();

            while(it.hasNext()) {
                Task t = ThaumcraftExtension.getGolemTask(world, it.next());
                if (t == null) {
                    it.remove();
                }
            }
        }

        this.delay++;
        if (this.shouldShearBlocks()) {
            BlockPos p = GolemHelper.getPosInArea(seal, this.delay);
            ItemStack shearsDefault = new ItemStack(Items.SHEARS);
            if (!this.cacheBlocks.containsValue(p.toLong()) && this.isValidBlock(world, p, shearsDefault)) {
                Task task = new Task(seal.getSealPos(), p);
                task.setPriority(seal.getPriority());
                GolemHelper.addGolemTask(world.provider.getDimension(), task);
                this.cacheBlocks.put(task.getId(), p.toLong());
                task.setLifespan((short)19);
            }
        }

        if (this.shouldShearCreatures() && this.delay % 200 == 0) {
            AxisAlignedBB area = GolemHelper.getBoundsForArea(seal);
            List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, area);
            if (list.size() > 0) {
                ItemStack shearsDefault = new ItemStack(Items.SHEARS);

                for (EntityLivingBase target : list) {
                    if (this.isValidTarget(target, shearsDefault)) {
                        Task task = new Task(seal.getSealPos(), target);
                        task.setPriority(seal.getPriority());
                        task.setLifespan((short) 10);
                        GolemHelper.addGolemTask(world.provider.getDimension(), task);
                    }
                }
            }
        }

    }

    private boolean isValidTarget(Entity target, ItemStack shears) {
        return target instanceof IShearable && ((IShearable) target).isShearable(shears, target.getEntityWorld(), new BlockPos(target.posX, target.posY, target.posZ));
    }

    private boolean isValidBlock(World world, BlockPos pos, ItemStack shears) {
        IBlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        return block instanceof IShearable && ((IShearable) block).isShearable(shears, world, pos);
    }

    public void onTaskStarted(World world, IGolemAPI golem, Task task) {
        switch(task.getType()) {
            case 0:
                if (!this.isValidBlock(world, task.getPos(), this.canGolemShearWithoutItem(golem) ? new ItemStack(Items.SHEARS) : golem.getCarrying().get(0))) {
                    task.setSuspended(true);
                }
                break;
            case 1:
                Entity target = task.getEntity();
                if (!this.isValidTarget(target, this.canGolemShearWithoutItem(golem) ? new ItemStack(Items.SHEARS) : golem.getCarrying().get(0))) {
                    task.setSuspended(true);
                } else {
                    ISealEntity se = GolemHelper.getSealEntity(golem.getGolemWorld().provider.getDimension(), task.getSealPos());
                    if (se != null) {
                        AxisAlignedBB area = GolemHelper.getBoundsForArea(se);
                        if (!area.intersects(target.getEntityBoundingBox())) {
                            task.setSuspended(true);
                        }
                    }
                }
        }

    }

    public boolean onTaskCompletion(World world, IGolemAPI golem, Task task) {
        byte type = task.getType();
        if (type == 0) {
            this.cacheBlocks.remove(task.getId());
        }

        boolean builtInShears = this.canGolemShearWithoutItem(golem);
        ItemStack carrying = builtInShears ? new ItemStack(Items.SHEARS) : golem.getCarrying().get(0);
        if (builtInShears || !carrying.isEmpty() && carrying.getItem() instanceof ItemShears) {
            if (this.fp == null) {
                this.fp = FakePlayerFactory.get((WorldServer)world, new GameProfile(null, "FakeAutomagyGolem"));
                this.fp.connection = new FakeNetHandlerPlayServer(this.fp.mcServer, new NetworkManager(EnumPacketDirection.CLIENTBOUND), this.fp);
            }

            this.fp.setPositionAndRotation(golem.getGolemEntity().posX, golem.getGolemEntity().posY, golem.getGolemEntity().posZ, golem.getGolemEntity().rotationYaw, golem.getGolemEntity().rotationPitch);
            switch(type) {
                case 0:
                    if (this.isValidBlock(world, task.getPos(), carrying.copy())) {
                        BlockPos targetPos = task.getPos();
                        this.fp.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, carrying.copy());
                        this.fp.interactionManager.tryHarvestBlock(targetPos);
                        if (!builtInShears) {
                            golem.dropItem(carrying);
                        }

                        golem.addRankXp(1);
                        if (!this.fp.getActiveItemStack().isEmpty() && this.fp.getActiveItemStack().getCount() <= 0) {
                            this.fp.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        }

                        if (!builtInShears) {
                            ThaumcraftExtension.golemMatchFakePlayerInventory(this.fp, golem);
                        }

                        golem.swingArm();
                    }
                    break;
                case 1:
                    if (this.isValidTarget(task.getEntity(), carrying.copy())) {
                        EntityLivingBase target = (EntityLivingBase)task.getEntity();
                        this.fp.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, carrying.copy());
                        if (carrying.getItem().itemInteractionForEntity(this.fp.getActiveItemStack(), this.fp, target, EnumHand.MAIN_HAND)) {
                            if (!builtInShears) {
                                golem.dropItem(carrying);
                            }

                            golem.addRankXp(1);
                            if (!this.fp.getActiveItemStack().isEmpty() && this.fp.getActiveItemStack().getCount() <= 0) {
                                this.fp.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
                            }

                            if (!builtInShears) {
                                ThaumcraftExtension.golemMatchFakePlayerInventory(this.fp, golem);
                            }

                            golem.swingArm();
                        }
                    }
            }
        }

        task.setSuspended(true);
        return true;
    }

    public void onTaskSuspension(World world, Task task) {
        if (task.getType() == 0) {
            this.cacheBlocks.remove(task.getId());
        }

    }

    public boolean canGolemPerformTask(IGolemAPI golem, Task task) {
        ItemStack carrying = golem.getCarrying().get(0);
        if (carrying.isEmpty() && this.canGolemShearWithoutItem(golem)) {
            return true;
        } else if (carrying.isEmpty() || !this.canShearWith(carrying) || this.shouldPreserveShears() && carrying.getItemDamage() + 1 >= carrying.getMaxDamage()) {
            if (this.shouldRequestProvisioning() && !this.isBlacklist() && !this.getInv().get(0).isEmpty() && this.getInv().get(0).getItem() instanceof ItemShears) {
                ItemStack shears = this.getInv().get(0).copy();
                if (shears.isItemStackDamageable()) {
                    shears.setItemDamage(32767);
                }

                ISealEntity se = GolemHelper.getSealEntity(golem.getGolemWorld().provider.getDimension(), task.getSealPos());
                if (se != null) {
                    GolemHelper.requestProvisioning(golem.getGolemWorld(), se, shears);
                }
            }

            return false;
        } else {
            return true;
        }
    }

    protected boolean canShearWith(ItemStack stack) {
        if (stack.getItem() instanceof ItemShears) {
            if (this.getInv().get(0).isEmpty()) {
                return this.isBlacklist();
            } else if (TjUtil.areItemsEqualIgnoringSize(stack, this.getInv().get(0))) {
                return !this.isBlacklist();
            } else {
                return this.isBlacklist();
            }
        } else {
            return false;
        }
    }

    protected boolean canGolemShearWithoutItem(IGolemAPI golem) {
        return false;
    }

    public void onRemoval(World world, BlockPos pos, EnumFacing side) {
    }

    public int getFilterSize() {
        return 1;
    }

    public EnumGolemTrait[] getRequiredTags() {
        return null;
    }

    public EnumGolemTrait[] getForbiddenTags() {
        return new EnumGolemTrait[]{EnumGolemTrait.CLUMSY};
    }

    public int[] getGuiCategories() {
        return new int[]{2, 1, 3, 0, 4};
    }

    public SealToggle[] getToggles() {
        return this.props;
    }

    public void setToggle(int index, boolean value) {
        this.props[index].setValue(value);
    }

    public boolean shouldShearCreatures() {
        return true;
    }

    public boolean shouldShearBlocks() {
        return false;
    }

    public boolean shouldPreserveShears() {
        return this.getToggles()[0].getValue();
    }

    public boolean shouldRequestProvisioning() {
        return this.getToggles()[1].getValue();
    }

    public void setFilterSlot(int i, ItemStack stack) {
        if (stack.isEmpty() || stack.getItem() instanceof ItemShears) {
            super.setFilterSlot(i, stack);
        }
    }
}
