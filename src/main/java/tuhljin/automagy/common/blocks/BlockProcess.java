package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.tiles.TileProcess;

import javax.annotation.Nonnull;

public class BlockProcess extends ModTileRenderedBlock {
    public BlockProcess() {
        super(Material.CIRCUITS, References.BLOCK_PROCESS);
        this.setBlockUnbreakable();
        this.setResistance(6000000.0F);
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileProcess();
    }
}
