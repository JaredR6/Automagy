package tuhljin.automagy.common.blocks.redcrystal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.RedstoneCalc.PowerResult;
import tuhljin.automagy.common.tiles.TileRedcrystal;

public class BlockRedcrystalDense extends BlockRedcrystalLarge {
    public static final int MAX_MINIMUM = 15;
    public static final int MIN_MINIMUM = 2;

    @Override
    protected PowerResult calculateRedstonePowerAt(World world, BlockPos pos, EnumFacing orientation) {
        PowerResult result = super.calculateRedstonePowerAt(world, pos, orientation);
        return result != null && result.strength >= this.getRedstoneSignalMin(world, pos) ? result : null;
    }

    @Override
    public boolean onBlockActivatedCenter(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote) {
            int min = this.getRedstoneSignalMin(world, pos);

            try {
                TileRedcrystal te = (TileRedcrystal)world.getTileEntity(pos);
                ++min;
                if (min > MAX_MINIMUM) {
                    min = MIN_MINIMUM;
                }

                te.extraData = min;
                te.markForUpdate();
            } catch (Exception ignored) {
            }

            this.updateAndPropagateChanges(world, pos, true, false, true, false);
            TjUtil.sendFormattedChatToPlayer(player, "Automagy.chat.redcrystalDense.setMin", min);
            return true;
        } else {
            return false;
        }
    }

    public int getRedstoneSignalMin(IBlockAccess blockaccess, BlockPos pos) {
        int min = MIN_MINIMUM;

        try {
            TileRedcrystal te = (TileRedcrystal)blockaccess.getTileEntity(pos);
            min = te.extraData;
        } catch (Exception ignored) {
        }

        return Math.min(MAX_MINIMUM, Math.max(MIN_MINIMUM, min));
    }
}
