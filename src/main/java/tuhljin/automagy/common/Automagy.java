package tuhljin.automagy.common;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tuhljin.automagy.common.lib.CreativeTabAutomagy;

import javax.annotation.Nonnull;

@Mod(modid = "automagy",
        name = "automagy",
        version = "3.0.0",
        dependencies = "required-after:thaumcraft@[6.1.BETA26,);after:waila",
        acceptedMinecraftVersions = "[1.12.2]")
public class Automagy {

    @SidedProxy(clientSide = "tuhljin.automagy.client.ProxyClient", serverSide = "tuhljin.automagy.common.ProxyCommon")
    public static ProxyCommon proxy;

    @Instance("automagy")
    public static Automagy instance;

    public static final Logger log = LogManager.getLogger("FML");

    @EventHandler
    public void preInit(@Nonnull FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        logInfo("Warning: Humorous one-liner not found.");
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Nonnull
    public static CreativeTabs creativeTab = (CreativeTabs)new CreativeTabAutomagy(CreativeTabs.getNextID(), "automagy");

    public static void logInfo(String s) {
        log.info("[Automagy] " + s);
    }

    public static void logDebug(String s) {
        log.debug("[Automagy DEBUG] " + s);
    }

    public static void logDebug(@Nonnull BlockPos pos, String s) {
        log.debug("[Automagy DEBUG] [" + pos.getX() + "," + pos.getY() + "," + pos.getZ() + "] " + s);
    }

    public static void logWarning(String s, Object... o) {
        log.warn("[Automagy] " + s, o);
    }

    public static void logError(String s) {
        log.error("[Automagy] " + s);
    }

    public static void logSevereError(String s) {
        log.fatal("[Automagy] " + s);
    }
    
    static {
        FluidRegistry.enableUniversalBucket();
    }
}
