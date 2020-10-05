package tuhljin.automagy.common.blocks;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.tiles.TileRemoteComparator;
import tuhljin.automagy.common.tiles.TileTallyWorld;

import javax.annotation.Nonnull;

public class BlockTallyWorld extends BlockTallyBase {
    public BlockTallyWorld() {
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileTallyWorld();
    }

    @Nonnull
    @Override
    public List<ItemStack> getDrops(@Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();
        drops.add(new ItemStack(ModBlocks.tallyBox));
        drops.add(new ItemStack(ModItems.tallyLens, 1, 1));
        return drops;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public int getRemoteComparatorParticleColor(@Nonnull World world, @Nonnull BlockPos pos, TileRemoteComparator teRC) {
        int ret = super.getRemoteComparatorParticleColor(world, pos, teRC);
        if (ret != -1 && world.rand.nextInt(3) == 0) {
            ret = 1;
        }

        return ret;
    }
}
