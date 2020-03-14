package tuhljin.automagy.common.blocks;

import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.inventory.ItemHandlerUtil;
import tuhljin.automagy.common.tiles.TileMawHungry;

import javax.annotation.Nonnull;

public class BlockMawHungry extends ModTileRenderedBlockWithFacing {
    private static AxisAlignedBB AABB_DOWN = new AxisAlignedBB(0.25F, 0.0F, 0.25F, 0.75F, 0.0625F, 0.75F);
    private static AxisAlignedBB AABB_UP = new AxisAlignedBB(0.25F, 0.9375F, 0.25F, 0.75F, 1.0F, 0.75F);
    private static AxisAlignedBB AABB_SOUTH = new AxisAlignedBB(0.25F, 0.25F, 0.9375F, 0.75F, 0.75F, 1.0F);
    private static AxisAlignedBB AABB_NORTH = new AxisAlignedBB(0.25F, 0.25F, 0.0625F, 0.75F, 0.75F, 0.0F);
    private static AxisAlignedBB AABB_EAST = new AxisAlignedBB(0.9375F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
    private static AxisAlignedBB AABB_WEST = new AxisAlignedBB(0.0F, 0.25F, 0.25F, 0.0625F, 0.75F, 0.75F);

    public BlockMawHungry() {
        super(Material.CORAL);
        this.setHardness(1.5F);
        this.setResistance(2000.0F);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileMawHungry();
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        return this.getDefaultState().withProperty(this.FACING, facing.getOpposite());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (rand.nextInt(4) <= 0) {
            EnumFacing facing = state.getValue(this.FACING);
            int r = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            double angle = rand.nextDouble() * 3.141592653589793D * 2.0D;
            double xa = Math.cos(angle);
            double za = Math.sin(angle);
            float dist = rand.nextFloat() * 0.5F;
            double x1;
            double y1;
            double z1;
            double x2;
            double y2;
            double z2;
            switch(facing) {
                case DOWN:
                default:
                    x1 = r + 0.5D + xa * dist;
                    z1 = z + 0.5D + za * dist;
                    x2 = r + 0.5D + xa * 0.02D;
                    z2 = z + 0.5D + za * 0.02D;
                    y1 = y + 0.3D;
                    y2 = y - 0.1D;
                    break;
                case UP:
                    x1 = r + 0.5D + xa * dist;
                    z1 = z + 0.5D + za * dist;
                    x2 = r + 0.5D + xa * 0.02D;
                    z2 = z + 0.5D + za * 0.02D;
                    y1 = y + 0.7D;
                    y2 = y + 1.1D;
                    break;
                case SOUTH:
                    x1 = r + 0.5D + xa * dist;
                    y1 = y + 0.5D + za * dist;
                    x2 = r + 0.5D + xa * 0.02D;
                    y2 = y + 0.5D + za * 0.02D;
                    z1 = z + 0.7D;
                    z2 = z + 1.1D;
                    break;
                case NORTH:
                    x1 = r + 0.5D + xa * dist;
                    y1 = y + 0.5D + za * dist;
                    x2 = r + 0.5D + xa * 0.02D;
                    y2 = y + 0.5D + za * 0.02D;
                    z1 = z + 0.3D;
                    z2 = z - 0.1D;
                    break;
                case EAST:
                    y1 = y + 0.5D + xa * dist;
                    z1 = z + 0.5D + za * dist;
                    y2 = y + 0.5D + xa * 0.02D;
                    z2 = z + 0.5D + za * 0.02D;
                    x1 = r + 0.7D;
                    x2 = r + 1.1D;
                    break;
                case WEST:
                    y1 = y + 0.5D + xa * dist;
                    z1 = z + 0.5D + za * dist;
                    y2 = y + 0.5D + xa * 0.02D;
                    z2 = z + 0.5D + za * 0.02D;
                    x1 = r + 0.3D;
                    x2 = r - 0.1D;
            }

            r = rand.nextInt(120);
            if (r < 3) {
                float gravity = rand.nextFloat() * 0.01F;
                if (facing != EnumFacing.DOWN) {
                    gravity *= -1.0F;
                }

                Automagy.proxy.fxWisp3NoClip(x1, y1, z1, x2, y2, z2, 0.05F, 2, r == 0, gravity);
            } else {
                Automagy.proxy.fxWisp3NoClip(x1, y1, z1, x2, y2, z2, 0.02F, 5, false, 0.0F);
            }

        }
    }

    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        EnumFacing facing = this.getFacing(source, pos);
        switch(facing) {
            default:
            case DOWN:
                return AABB_DOWN;
            case UP:
                return AABB_UP;
            case SOUTH:
                return AABB_SOUTH;
            case NORTH:
                return AABB_NORTH;
            case EAST:
                return AABB_EAST;
            case WEST:
                return AABB_WEST;
        }

    }

    public boolean locationIsValid(IBlockAccess world, BlockPos pos, EnumFacing side) {
        BlockPos pos2 = pos.offset(side);
        if (!world.isAirBlock(pos2)) {
            TileEntity te = world.getTileEntity(pos2);
            return ItemHandlerUtil.isValid(te, side.getOpposite());
        }

        return false;
    }

    @Override
    public boolean canPlaceBlockOnSide(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing side) {
        return this.locationIsValid(world, pos, side.getOpposite());
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        IBlockState state = world.getBlockState(pos);
        if (!this.locationIsValid(world, pos, state.getValue(this.FACING))) {
            ((World) world).destroyBlock(pos, true);
        }

    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote && entity instanceof EntityItem && !entity.isDead) {
            EntityItem item = (EntityItem)entity;
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileMawHungry) {
                ((TileMawHungry)te).moveItem(item);
            }
        }

    }
}
