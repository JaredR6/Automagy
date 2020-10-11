package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class ModTileRenderedBlockWithFacing extends ModTileBlockWithFacing {
    public ModTileRenderedBlockWithFacing(@Nonnull Material material, @Nonnull MapColor mapColor, String name) {
        super(material, mapColor, name);
    }

    public ModTileRenderedBlockWithFacing(@Nonnull Material material, String name) {
        super(material, name);
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }

    @Nonnull
    @Override
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    /*
    @Override
    public boolean isVisuallyOpaque() {
        return false;
    }
     */
}

