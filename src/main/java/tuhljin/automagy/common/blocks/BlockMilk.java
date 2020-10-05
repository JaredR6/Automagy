package tuhljin.automagy.common.blocks;

import java.awt.Color;
import net.minecraft.block.material.Material;
import tuhljin.automagy.common.blocks.ModBlockFluid.ModFluid;
import tuhljin.automagy.common.lib.References;

public class BlockMilk extends ModBlockFluid {
    public BlockMilk() {
        super(new BlockMilk.FluidMilk(References.FLUID_MILK, References.FLUIDTEXTURE_MILK), Material.WATER);
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