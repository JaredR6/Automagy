package tuhljin.automagy.common.entities.golems;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.golems.GolemHelper;
import thaumcraft.api.golems.seals.ISeal;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.api.golems.seals.ISealGui;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.common.golems.client.gui.SealBaseContainer;
import thaumcraft.common.golems.client.gui.SealBaseGUI;

import javax.annotation.Nonnull;

public abstract class ModSeal implements ISeal, ISealGui {
    public ResourceLocation icon;
    public String key;

    public ModSeal(String key, ResourceLocation icon) {
        this.key = "automagy:" + key;
        this.icon = icon;
    }

    public ModSeal(String key) {
        this(key, new ResourceLocation("automagy", "items/seals/seal_" + key));
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public boolean canPlaceAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {
        return !world.isAirBlock(pos);
    }

    @Override
    public ResourceLocation getSealIcon() {
        return this.icon;
    }

    @Override
    public void readCustomNBT(NBTTagCompound nbt) {
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt) {
    }

    @Override
    public void onRemoval(World world, BlockPos pos, EnumFacing side) {
    }

    @Nonnull
    @Override
    public Object returnContainer(World world, @Nonnull EntityPlayer player, BlockPos pos, EnumFacing side, @Nonnull ISealEntity seal) {
        return new SealBaseContainer(player.inventory, world, seal);
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public Object returnGui(World world, @Nonnull EntityPlayer player, BlockPos pos, EnumFacing side, @Nonnull ISealEntity seal) {
        return new SealBaseGUI(player.inventory, world, seal);
    }

    @Override
    public int[] getGuiCategories() {
        return new int[]{0};
    }

    public static int createTaskAtSealPos(@Nonnull World world, @Nonnull ISealEntity seal) {
        Task task = new Task(seal.getSealPos(), seal.getSealPos().pos);
        task.setPriority(seal.getPriority());
        GolemHelper.addGolemTask(world.provider.getDimension(), task);
        return task.getId();
    }

    public static ISealEntity getSealEntity(@Nonnull World world, @Nonnull Task task) {
        return GolemHelper.getSealEntity(world.provider.getDimension(), task.getSealPos());
    }
}
