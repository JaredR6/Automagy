package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.Material;
import tuhljin.automagy.common.blocks.ModBlockFluid.ModFluid;

public class BlockMushroomSoup extends ModBlockFluid {
    public BlockMushroomSoup(String fluidName, String fluidTexture) {
        super(new BlockMushroomSoup.FluidMushroomSoup(fluidName, fluidTexture), Material.WATER);
        this.setLightOpacity(5);
    }

    public static class FluidMushroomSoup extends ModFluid {
        public FluidMushroomSoup(String fluidName, String texture) {
            super(fluidName, texture);
            this.setGaseous(false);
            this.setViscosity(1500);
        }
    }
}