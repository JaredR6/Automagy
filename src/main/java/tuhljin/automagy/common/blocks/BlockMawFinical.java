package tuhljin.automagy.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import tuhljin.automagy.common.gui.AutomagyGUIHandler;
import tuhljin.automagy.common.items.ItemFilter;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.tiles.TileMawFinical;

import javax.annotation.Nonnull;

public class BlockMawFinical extends BlockMawHungry {

    public BlockMawFinical() {
        this(References.BLOCK_MAW_FINICAL);
    }

    public BlockMawFinical(String name) {
        super(name);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileMawFinical();
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!ThaumcraftExtension.playerHasCasterEquipped(player) && !ItemFilter.isItemPopulatedFilter(player.getActiveItemStack())) {
            return false;
        } else {
            if (!world.isRemote) {
                AutomagyGUIHandler.openGUI(7, player, world, pos);
            }

            return true;
        }
    }
}
