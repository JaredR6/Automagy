package tuhljin.automagy.common.tiles;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;

public interface ITileWithTank extends IFluidHandler {
    FluidTank getTank();

    boolean drainExactAmount(EnumFacing facing, int var2, boolean var3);

    boolean fillExactAmount(EnumFacing var1, FluidStack var2);
}