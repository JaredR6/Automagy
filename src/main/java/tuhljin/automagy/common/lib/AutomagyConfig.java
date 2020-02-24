package tuhljin.automagy.common.lib;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.io.File;

public class AutomagyConfig {
    protected static Configuration config;

    public static boolean versionChecking = true;
    public static boolean redcrystalEmitsLight = true;
    public static int redstoneBurnoutPoint = 28;
    public static boolean thirstyTankDrinksRain = true;
    public static boolean thirstyTankPreserveInfiniteWater = true;
    public static int waterBottleAmount = 0;
    public static int milkingCooldownPerCow = 1200;
    private static int realWaterBottleAmount;

    public static void load(File file) {
        config = new Configuration(file);
        config.load();
        versionChecking = loadBoolean("version_checking", "On startup, check whether a newer version of the mod exists", versionChecking);
        redcrystalEmitsLight = loadBoolean("redcrystal_emits_light", "Should redcrystal emit light when it has a redstone signal", redcrystalEmitsLight);
        redstoneBurnoutPoint = loadInteger("redstone_burnout_point", "The limiter used to determine when redstone inversion torches temporarily turn off in response to too many changes within a short period of time. Lower means less tolerance (torch burns out more easily). You generally shouldn't have to touch this but it is provided in case there is a problem.", redstoneBurnoutPoint);
        thirstyTankDrinksRain = loadBoolean("thirstytank_drink_rain", "Should thirsty tanks slowly fill with water if exposed to the sky while it is raining", true);
        thirstyTankPreserveInfiniteWater = loadBoolean("thirstytank_preserve_water_source", "Should thirsty tanks preserve water source blocks next to them", thirstyTankPreserveInfiniteWater);
        waterBottleAmount = loadInteger("waterbottle_amount", "How much water (in mB) a water bottle is considered to contain. 0 (default) means bottles can be filled from and emptied into Automagy tanks without changing the amount in a tank. -1 means empty and water bottles won't interact with Automagy tanks. -2 means bottles should use the fluid amount registered with Forge (normally 1000).", waterBottleAmount);
        realWaterBottleAmount = waterBottleAmount;
        milkingCooldownPerCow = loadInteger("cow_milking_cooldown", "Number of ticks that must pass after a thirsty tank milks a cow before it can be milked again.", milkingCooldownPerCow);
        if (config.hasChanged())
            config.save();
    }

    public static boolean loadBoolean(String category, String name, String desc, boolean def) {
        Property p = config.get(category, name, def);
        if (desc != null)
            p.setComment(desc);
        return p.getBoolean(def);
    }

    public static boolean loadBoolean(String name, String desc, boolean def) {
        return loadBoolean("general", name, desc, def);
    }

    public static int loadInteger(String category, String name, String desc, int def) {
        Property p = config.get(category, name, def);
        if (desc != null)
            p.setComment(desc);
        return p.getInt(def);
    }

    public static int loadInteger(String name, String desc, int def) {
        return loadInteger("general", name, desc, def);
    }

    /**
     * Forge no longer has a fluid container registry. Water is usually hard-coded in as 1000mb.
     * See: https://github.com/BluSunrize/ImmersiveEngineering/blob/master/src/main/java/blusunrize/immersiveengineering/common/crafting/MixerPotionHelper.java#L52
     */
    public static int getRealWaterBottleAmount() {
        /*
        if (realWaterBottleAmount < 0) {
            ItemStack waterBottle = new ItemStack(Items.POTIONITEM);
            FluidStack waterBottleLiquid = FluidRegistry.getFluidForFilledItem(waterBottle);
            realWaterBottleAmount = (waterBottleLiquid == null) ? 0 : ((waterBottleLiquid.amount >= 0) ? waterBottleLiquid.amount : 0);
        }
        */
        return realWaterBottleAmount;
    }
}
