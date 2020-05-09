package tuhljin.automagy.common.blocks;

import java.awt.Color;
import net.minecraft.block.material.Material;
import tuhljin.automagy.common.blocks.ModBlockFluid.ModFluid;

public class BlockMilk extends ModBlockFluid {
    public BlockMilk(String fluidName, String fluidTexture) {
        super(new BlockMilk.FluidMilk(fluidName, fluidTexture), Material.WATER);
        this.setLightOpacity(3);
    }

    public static class FluidMilk extends ModFluid {
        public FluidMilk(String fluidName, String texture) {
            super(fluidName, texture);
            this.setGaseous(false);
            this.setViscosity(1050);
        }

        public int getColor() {
            return Color.WHITE.getRGB();
        }
    }
}