package tuhljin.automagy.common.lib.compat;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import tuhljin.automagy.common.blocks.ModBlocks;

public class CompatibilityManager {
    public static boolean modLoadedBotania = false;
    private static BlockFluidBase milkBlock;
    private static BlockFluidBase mushroomSoupBlock;

    public CompatibilityManager() {
    }

    public static void init() {
        FMLInterModComms.sendMessage("Waila", "register", "tuhljin.automagy.common.lib.compat.waila.WailaProviders.wailaCallbackRegister");
    }

    public static void postInit() {
        if (Loader.isModLoaded("Botania")) {
            modLoadedBotania = true;
        }

    }

    public static boolean fillBotaniaPetalApothecary(TileEntity te, boolean doFill) {
        return modLoadedBotania && BotaniaHandler.fillBotaniaPetalApothecary(te, doFill);
    }

    public static BlockFluidBase getMilkBlock() {
        if (milkBlock == null) {
            Block block = FluidRegistry.getFluid("milk").getBlock();
            if (block instanceof BlockFluidBase) {
                milkBlock = (BlockFluidBase)block;
            } else {
                milkBlock = ModBlocks.milk;
            }
        }

        return milkBlock;
    }
}
