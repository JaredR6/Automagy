package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.Material;
import tuhljin.automagy.common.blocks.ModBlockFluid.ModFluid;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;

public class BlockMushroomSoup extends ModBlockFluid {
    public BlockMushroomSoup() {
        super(new BlockMushroomSoup.FluidMushroomSoup(References.FLUID_MUSHROOMSOUP, References.FLUIDTEXTURE_MUSHROOMSOUP), Material.WATER);
        this.setLightOpacity(5);
    }

    public static class FluidMushroomSoup extends ModFluid {
        public FluidMushroomSoup(@Nonnull String fluidName, @Nonnull String texture) {
            super(fluidName, texture);
            this.setGaseous(false);
            this.setViscosity(1500);
        }
    }
}