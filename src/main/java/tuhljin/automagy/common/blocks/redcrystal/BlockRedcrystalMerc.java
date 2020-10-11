package tuhljin.automagy.common.blocks.redcrystal;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.RedstoneCalc.PowerResult;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;
import tuhljin.automagy.common.tiles.TileRedcrystalMerc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BlockRedcrystalMerc extends BlockRedcrystalLarge {
    @Nullable
    private WorldSpecificCoordinates lastCoord = null;

    public BlockRedcrystalMerc() {
        super(References.BLOCK_REDCRYSTAL_MERC);
        //this.func_149676_a(0.0F, 0.0F, 0.0F, 1.0F, 0.32F, 1.0F);
        this.setSoundType(SoundType.STONE);
        this.setHardness(2.5F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        TileRedcrystalMerc te = new TileRedcrystalMerc(this.nextTEOrientation, this.nextTENoConnections);
        this.nextTEOrientation = EnumFacing.UP;
        this.nextTENoConnections = false;
        return te;
    }

    @Override
    public boolean canPlaceBlockAt(@Nonnull World world, @Nonnull BlockPos pos) {
        return TjUtil.isAcceptableSurfaceBelowPos(world, pos, true, true, false) && TjUtil.getBlock(world, pos).isReplaceable(world, pos);
    }

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {
        return TjUtil.isAcceptableSurfaceBelowPos(world, pos, true, true, false);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(@Nonnull IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        TileRedcrystalMerc te = null;

        try {
            te = (TileRedcrystalMerc)world.getTileEntity(pos);
        } catch (Exception ignored) {
        }

        if (te != null && te.hasValidMirrorConnection()) {
            int strength = this.getRedstoneSignalStrength(world, pos, true);
            boolean power = strength > 0;
            int color = power ? 8 : 6;
            if (power) {
                boolean mirrorIsSource = te.strengthWithoutMirror != strength;
                int sx;
                int sy;
                int sz;
                if (rand.nextInt(4) == 0) {
                    int dx;
                    int dy;
                    int dz;
                    if (mirrorIsSource) {
                        sx = te.mirrorX;
                        sy = te.mirrorY;
                        sz = te.mirrorZ;
                        dx = pos.getX();
                        dy = pos.getY();
                        dz = pos.getZ();
                    } else {
                        sx = pos.getX();
                        sy = pos.getY();
                        sz = pos.getZ();
                        dx = te.mirrorX;
                        dy = te.mirrorY;
                        dz = te.mirrorZ;
                    }

                    Automagy.proxy.fxGhostlyBallStream(world, sx + 0.5F, sy + 0.5F, sz + 0.5F, dx + 0.5F, dy + 0.5F, dz + 0.5F, 0.05F, 8, false, 0.0F);
                }

                sx = pos.getX();
                sy = pos.getY();
                sz = pos.getZ();
                ThaumcraftExtension.sparkle(sx + 0.2F + rand.nextFloat() * 0.6F, sy + (mirrorIsSource ? 0.8F : 0.5F), sz + 0.2F + rand.nextFloat() * 0.6F, 1.0F, color, mirrorIsSource ? 0.1F : -0.1F);
            } else {
                ThaumcraftExtension.sparkle(pos.getX() + 0.2F + rand.nextFloat() * 0.6F, pos.getY() + 0.2F, pos.getZ() + 0.2F + rand.nextFloat() * 0.6F, 1.0F, color, -0.1F);
            }
        }

        super.randomDisplayTick(state, world, pos, rand);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileRedcrystalMerc te = null;

        try {
            te = (TileRedcrystalMerc)world.getTileEntity(pos);
        } catch (Exception ignored) {
        }

        this.lastCoord = te.mirrorY == -1 ? null : new WorldSpecificCoordinates(te.mirrorDim, te.mirrorX, te.mirrorY, te.mirrorZ);
        if (!world.isRemote) {
            te.removeFromMirrorNetwork();
        }

        super.breakBlock(world, pos, state);
    }

    @Nonnull
    public List<ItemStack> getDrops(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
        Item item = this.getItemDropped(state, rand, fortune);
        ItemStack stack = new ItemStack(item, 1, this.damageDropped(state));
        TileRedcrystalMerc te = null;

        try {
            te = (TileRedcrystalMerc)world.getTileEntity(pos);
        } catch (Exception ignored) {
        }

        if (te != null) {
            if (te.mirrorY != -1) {
                stack.setTagInfo("mirrorX", new NBTTagInt(te.mirrorX));
                stack.setTagInfo("mirrorY", new NBTTagInt(te.mirrorY));
                stack.setTagInfo("mirrorZ", new NBTTagInt(te.mirrorZ));
                stack.setTagInfo("mirrorDim", new NBTTagInt(te.mirrorDim));
                stack.setTagInfo("mirrorDimName", new NBTTagString(DimensionManager.getProvider(te.mirrorDim).getDimensionType().getName()));
            }
        } else if (this.lastCoord != null) {
            stack.setTagInfo("mirrorX", new NBTTagInt(this.lastCoord.x));
            stack.setTagInfo("mirrorY", new NBTTagInt(this.lastCoord.y));
            stack.setTagInfo("mirrorZ", new NBTTagInt(this.lastCoord.z));
            stack.setTagInfo("mirrorDim", new NBTTagInt(this.lastCoord.dim));
            stack.setTagInfo("mirrorDimName", new NBTTagString(DimensionManager.getProvider(this.lastCoord.dim).getDimensionType().getName()));
        }

        this.lastCoord = null;
        drops.add(stack);
        return drops;
    }

    @Nullable
    protected PowerResult calculateRedstonePowerAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing orientation) {
        int receivingStrength = 0;

        TileRedcrystalMerc te;
        try {
            te = (TileRedcrystalMerc)world.getTileEntity(pos);
        } catch (Exception var7) {
            return null;
        }

        if (te != null) {
            receivingStrength = Math.max(te.extraData - 1, 0);
            if (receivingStrength >= 99) {
                return new PowerResult(receivingStrength, null);
            }
        }

        PowerResult result = super.calculateRedstonePowerAt(world, pos, orientation);
        if (result != null && result.strength > 0) {
            te.strengthWithoutMirror = result.strength;
            te.markForUpdate();
            return receivingStrength > result.strength ? new PowerResult(receivingStrength, null) : result;
        } else {
            te.strengthWithoutMirror = 0;
            te.markForUpdate();
            return receivingStrength > 0 ? new PowerResult(receivingStrength, null) : null;
        }
    }

    public void updateAndPropagateChanges(@Nonnull World world, @Nonnull BlockPos pos, boolean checkStrength, boolean calledByNeighborWire, boolean forcePropagate, boolean immediateNeighborUpdates) {
        super.updateAndPropagateChanges(world, pos, checkStrength, calledByNeighborWire, forcePropagate, immediateNeighborUpdates);
        if (!world.isRemote) {
            TileRedcrystalMerc te = null;
            boolean var8 = false;

            try {
                te = (TileRedcrystalMerc)world.getTileEntity(pos);
            } catch (Exception ignored) {
            }

            if (te != null) {
                te.updateMirrorNetwork();
            }
        }

    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess world, @Nonnull BlockPos pos) {
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        return new AxisAlignedBB(x, y, z, x + 1.0D, y + 0.125D, z + 1.0D);
    }

    @Override
    public void addCollisionBoxToList(@Nonnull IBlockState state, World world, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entity, boolean isActualState) {
        super.addCollisionBoxToList(state, world, pos, entityBox, collidingBoxes, entity, isActualState);
        if (entity instanceof EntityPlayer) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            AxisAlignedBB aabb = new AxisAlignedBB((double)x + 0.36D, (double)y, (double)z + 0.36D, (double)x + 0.64D, (double)y + 0.8D, (double)z + 0.64D);
            if (aabb != NULL_AABB && entityBox.intersects(aabb)) {
                collidingBoxes.add(aabb);
            }
        }

    }
}
