package tuhljin.automagy.common.lib.compat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import tuhljin.automagy.common.blocks.BlockHourglass;
import tuhljin.automagy.common.blocks.BlockTankThirsty;
import tuhljin.automagy.common.lib.IOrientableRedstoneConductor;

public class WailaProviders {
    public WailaProviders() {
    }

    public static void wailaCallbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new ProviderForIOrientableRedstoneConductor(), IOrientableRedstoneConductor.class);
        registrar.registerBodyProvider(new ProviderForHourglass(), BlockHourglass.class);
        registrar.registerBodyProvider(new ProviderForThirstyTank(), BlockTankThirsty.class);
    }
}
