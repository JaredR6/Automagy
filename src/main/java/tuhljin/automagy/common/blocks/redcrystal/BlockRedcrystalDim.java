package tuhljin.automagy.common.blocks.redcrystal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;

import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.RedstoneCalc.PowerResult;
import tuhljin.automagy.common.tiles.TileRedcrystal;

import javax.annotation.Nonnull;

public class BlockRedcrystalDim extends BlockRedcrystalLarge {
    public static final int MAX_CAP = 14;
    public static final int MIN_CAP = 1;

    public BlockRedcrystalDim() {
        super(References.BLOCK_REDCRYSTAL_DIM);
    }

    @Nullable
    @Override
    protected PowerResult calculateRedstonePowerAt(@Nonnull World world, @Nonnull BlockPos pos, EnumFacing orientation) {
        PowerResult result = super.calculateRedstonePowerAt(world, pos, orientation);
        return result != null && result.strength > 0 ? new PowerResult(Math.min(result.strength, this.getRedstoneSignalCap(world, pos)), result.sourceDirection) : null;
    }

    @Override
    public boolean onBlockActivatedCenter(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player) {
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
            TjUtil.sendFormattedChatToPlayer(player, "automagy.chat.redcrystalDim.setCap", cap);
            return true;
        } else {
            return false;
        }
    }

    public int getRedstoneSignalCap(@Nonnull IBlockAccess blockaccess, @Nonnull BlockPos pos) {
        int cap = 1;

        try {
            TileRedcrystal te = (TileRedcrystal)blockaccess.getTileEntity(pos);
            cap = te.extraData;
        } catch (Exception ignored) {
        }

        return Math.min(MAX_CAP, Math.max(MIN_CAP, cap));
    }
}
