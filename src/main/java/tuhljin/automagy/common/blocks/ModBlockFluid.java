package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ModBlockFluid extends BlockFluidClassic {
    @Nonnull
    private final Fluid fluid;

    public ModBlockFluid(@Nonnull Fluid fluid, @Nonnull Material material) {
        super(fluid, material);
        this.fluid = fluid;
        fluid.setBlock(this);
    }

    public ModBlockFluid(@Nonnull Fluid fluid) {
        this(fluid, Material.WATER);
    }

    public static class ModFluid extends Fluid {
        public ModFluid(@Nonnull String fluidName, ResourceLocation still, ResourceLocation flowing) {
            super(fluidName, still, flowing);
            FluidRegistry.registerFluid(this);
        }

        public ModFluid(@Nonnull String fluidName, ResourceLocation still) {
            this(fluidName, still, still);
        }

        public ModFluid(@Nonnull String fluidName, @Nonnull String still, @Nonnull String flowing) {
            this(fluidName, new ResourceLocation(still), new ResourceLocation(flowing));
        }

        public ModFluid(@Nonnull String fluidName, @Nonnull String texture) {
            this(fluidName, new ResourceLocation(texture));
        }
    }

    @SideOnly(Side.CLIENT)
    void render() {
        ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(LEVEL).build());
    }

    public static void renderFluids() {
        ModBlocks.milk.render();
        ModBlocks.mushroomSoup.render();
        ModBlocks.vishroomSoup.render();
    }
}
