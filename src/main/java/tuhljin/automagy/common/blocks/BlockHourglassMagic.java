package tuhljin.automagy.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.gui.AutomagyGUIHandler;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.tiles.TileHourglassMagic;

import javax.annotation.Nonnull;

public class BlockHourglassMagic extends BlockHourglass {
    public static final int TIMER_MAGIC_MINIMUM = 1;
    public static final int TIMER_MAGIC_MAXIMUM = 3600;

    public BlockHourglassMagic() {
        super(Material.IRON, MapColor.IRON, References.BLOCK_HOURGLASS_MAGIC);
        this.setSoundType(SoundType.METAL);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileHourglassMagic();
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            if (ThaumcraftExtension.playerHasCasterEquipped(player)) {
                AutomagyGUIHandler.openGUI(1, player, world, pos);
            } else {
                TileHourglassMagic te = null;

                try {
                    te = (TileHourglassMagic)world.getTileEntity(pos);
                } catch (Exception ignored) {
                }

                if (te == null) {
                    return true;
                }

                if (te.modeSignalReact == 2 && te.receivingSignal || te.modeSignalReact == 3 && !te.receivingSignal) {
                    world.playSound(null, pos, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.6F, 0.8F);
                } else if (te.startFlipOnServer(true)) {
                    world.playSound(null, pos, SoundsTC.clack, SoundCategory.BLOCKS, 1.0F, 1.0F);
                }
            }
        }

        return true;
    }
}
