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

public class BlockRedcrystalDim extends BlockRedcrystalLarge {
    public static final int MAX_CAP = 14;
    public static final int MIN_CAP = 1;

    @Override
    protected PowerResult calculateRedstonePowerAt(World world, BlockPos pos, EnumFacing orientation) {
        PowerResult result = super.calculateRedstonePowerAt(world, pos, orientation);
        return result != null && result.strength > 0 ? new PowerResult(Math.min(result.strength, this.getRedstoneSignalCap(world, pos)), result.sourceDirection) : null;
    }

    @Override
    public boolean onBlockActivatedCenter(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        if (!world.isRemote) {
            int cap = this.getRedstoneSignalCap(world, pos);

            try {
                TileRedcrystal te = (TileRedcrystal)world.getTileEntity(pos);
                ++cap;
                if (cap > 14) {
                    cap = 1;
                }

                te.extraData = cap;
                te.markForUpdate();
            } catch (Exception ignored) {
            }

            this.updateAndPropagateChanges(world, pos, true, false, true, false);
            TjUtil.sendFormattedChatToPlayer(player, "Automagy.chat.redcrystalDim.setCap", cap);
            return true;
        } else {
            return false;
        }
    }

    public int getRedstoneSignalCap(IBlockAccess blockaccess, BlockPos pos) {
        int cap = 1;

        try {
            TileRedcrystal te = (TileRedcrystal)blockaccess.getTileEntity(pos);
            cap = te.extraData;
        } catch (Exception ignored) {
        }

        return Math.min(MAX_CAP, Math.max(MIN_CAP, cap));
    }
}
