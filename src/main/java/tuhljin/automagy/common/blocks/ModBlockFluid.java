package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class ModBlockFluid extends BlockFluidClassic {
    private final Fluid fluid;

    public ModBlockFluid(Fluid fluid, Material material) {
        super(registerFluid(fluid), material);
        this.fluid = fluid;
        fluid.setBlock(this);
    }

    public ModBlockFluid(Fluid fluid) {
        this(fluid, Material.WATER);
    }

    private static Fluid registerFluid(Fluid fluid) {
        FluidRegistry.registerFluid(fluid);
        return fluid;
    }

    public static class ModFluid extends Fluid {
        public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
            super(fluidName, still, flowing);
        }

        public ModFluid(String fluidName, ResourceLocation still) {
            this(fluidName, still, still);
        }

        public ModFluid(String fluidName, String still, String flowing) {
            this(fluidName, new ResourceLocation(still), new ResourceLocation(flowing));
        }

        public ModFluid(String fluidName, String texture) {
            this(fluidName, new ResourceLocation(texture));
        }
    }
}
