package tuhljin.automagy.common.blocks;

import java.util.Random;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.tiles.TileMawSpitting;

import javax.annotation.Nonnull;

public class BlockMawSpitting extends BlockMawFinical {
    public BlockMawSpitting() {
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileMawSpitting();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(@Nonnull IBlockState state, World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
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
                    x1 = (double)r + 0.5D + xa * (double)dist;
                    z1 = (double)z + 0.5D + za * (double)dist;
                    x2 = (double)r + 0.5D + xa * 0.02D;
                    z2 = (double)z + 0.5D + za * 0.02D;
                    y1 = (double)y + 0.3D;
                    y2 = (double)y - 0.1D;
                    break;
                case UP:
                    x1 = (double)r + 0.5D + xa * (double)dist;
                    z1 = (double)z + 0.5D + za * (double)dist;
                    x2 = (double)r + 0.5D + xa * 0.02D;
                    z2 = (double)z + 0.5D + za * 0.02D;
                    y1 = (double)y + 0.7D;
                    y2 = (double)y + 1.1D;
                    break;
                case SOUTH:
                    x1 = (double)r + 0.5D + xa * (double)dist;
                    y1 = (double)y + 0.5D + za * (double)dist;
                    x2 = (double)r + 0.5D + xa * 0.02D;
                    y2 = (double)y + 0.5D + za * 0.02D;
                    z1 = (double)z + 0.7D;
                    z2 = (double)z + 1.1D;
                    break;
                case NORTH:
                    x1 = (double)r + 0.5D + xa * (double)dist;
                    y1 = (double)y + 0.5D + za * (double)dist;
                    x2 = (double)r + 0.5D + xa * 0.02D;
                    y2 = (double)y + 0.5D + za * 0.02D;
                    z1 = (double)z + 0.3D;
                    z2 = (double)z - 0.1D;
                    break;
                case EAST:
                    y1 = (double)y + 0.5D + xa * (double)dist;
                    z1 = (double)z + 0.5D + za * (double)dist;
                    y2 = (double)y + 0.5D + xa * 0.02D;
                    z2 = (double)z + 0.5D + za * 0.02D;
                    x1 = (double)r + 0.7D;
                    x2 = (double)r + 1.1D;
                    break;
                case WEST:
                    y1 = (double)y + 0.5D + xa * (double)dist;
                    z1 = (double)z + 0.5D + za * (double)dist;
                    y2 = (double)y + 0.5D + xa * 0.02D;
                    z2 = (double)z + 0.5D + za * 0.02D;
                    x1 = (double)r + 0.3D;
                    x2 = (double)r - 0.1D;
            }

            r = rand.nextInt(120);
            if (r < 3) {
                float gravity = rand.nextFloat() * 0.01F;
                if (facing != EnumFacing.DOWN) {
                    gravity *= -1.0F;
                }

                Automagy.proxy.fxWisp3NoClip(x2, y2, z2, x1, y1, z1, 0.05F, 2, r == 0, gravity);
            } else {
                Automagy.proxy.fxWisp3NoClip(x2, y2, z2, x1, y1, z1, 0.02F, 5, false, 0.0F);
            }

        }
    }

    @Override
    public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity entity) {
    }
}
