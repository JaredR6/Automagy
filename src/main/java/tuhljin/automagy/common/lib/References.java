package tuhljin.automagy.common.lib;

import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class References {
    public static final String MOD_ID = "automagy";
    public static final String MOD_NAME = "Automagy";
    public static final String VERSION = "3.0.0";
    public static final String MOD_DOMAIN;
    public static final String URL_VERSION_CHECK = "http://everburningtorch.com/minecraft/automagy/checkVersion2/";
    public static final String BLOCK_REDCRYSTAL = "redcrystal";
    public static final String BLOCK_REDCRYSTAL_AMP = "redcrystalAmp";
    public static final String BLOCK_REDCRYSTAL_DIM = "redcrystalDim";
    public static final String BLOCK_REDCRYSTAL_DENSE = "redcrystalDense";
    public static final String BLOCK_REDCRYSTAL_RES = "redcrystalRes";
    public static final String BLOCK_REDCRYSTAL_MERC = "redcrystalMerc";
    public static final String BLOCK_HOURGLASS = "hourglass";
    public static final String BLOCK_HOURGLASS_MAGIC = "hourglassMagic";
    public static final String BLOCK_BOOKSHELF_ENCHANTED = "bookshelf";
    public static final String BLOCK_TORCH_INVERSION_ON = "torchInversion";
    public static final String BLOCK_TORCH_INVERSION_OFF = "torchInversion_off";
    public static final String BLOCK_TALLY = "tally";
    public static final String BLOCK_TALLY_DROPS = "tallyYield";
    public static final String BLOCK_TALLY_WORLD = "tallyUnbound";
    public static final String BLOCK_REMOTECOMPARATOR = "remoteComparator";
    public static final String BLOCK_GOLEMWORKBENCH = "golemWorkbench";
    public static final String BLOCK_MAW_HUNGRY = "mawHungry";
    public static final String BLOCK_MAW_FINICAL = "mawFinical";
    public static final String BLOCK_MAW_SPITTING = "mawSpitting";
    public static final String BLOCK_PROCESS = "process";
    public static final String BLOCK_TANK_THIRSTY = "tankThirsty";
    public static final String BLOCK_MILK = "milk";
    public static final String BLOCK_MUSHROOMSOUP = "mushroomSoup";
    public static final String BLOCK_VISHROOMSOUP = "vishroomSoup";
    public static final String ITEM_SLIVER = "sliver";
    public static final String ITEM_CRYSTALEYE = "crystalEye";
    public static final String ITEM_AVARICEPEARL = "avaricePearl";
    public static final String ITEM_ENCHANTEDPAPER = "enchantedPaper";
    public static final String ITEM_ENCHANTEDPAPER_BOUND = "book";
    public static final String ITEM_FILTER = "filter";
    public static final String ITEM_RECIPE = "recipe";
    public static final String ITEM_FOODSTUFF = "foodstuff";
    public static final String ITEM_TALLYLENS = "tallyLens";
    public static final String ITEM_TANKGLYPH = "tankGlyph";
    public static final String ITEM_BUCKET_MUSHROOM = "bucketMushroom";
    public static final String ITEM_BUCKET_VISHROOM = "bucketVishroom";
    public static final String FLUID_MILK = "milk";
    public static final String FLUIDTEXTURE_MILK;
    public static final String FLUID_MUSHROOMSOUP = "mushroomsoup";
    public static final String FLUIDTEXTURE_MUSHROOMSOUP;
    public static final String FLUID_VISHROOMSOUP = "vishroomsoup";
    public static final String FLUIDTEXTURE_VISHROOMSOUP;
    public static final String ENTITY_AVARICEPEARL;
    public static final String SEAL_CRAFT = "craft";
    public static final String SEAL_CRAFT_PROVIDE = "craft_provide";
    public static final String SEAL_SHEAR = "shear";
    public static final String SEAL_SHEAR_ADV = "shear_advanced";
    public static final String SEAL_SUPPLY = "supply";
    public static final String PREFIX_GUI;
    public static final String GUI_CHECKBOXABLE;
    public static final String GUI_WIDGETS_RECOLOR;
    public static final String GUI_FILTERHOLDER;
    public static final String GUI_RUNICWHITELIST;
    public static final String GUI_RUNICBLACKLIST;
    public static final String GUI_RUNICRECIPE;
    public static final String GUI_GOLEMWORKBENCH;
    public static final int guiIDHourglassMagic = 1;
    public static final int guiIDFilterWhite = 2;
    public static final int guiIDFilterBlack = 3;
    public static final int guiIDRecipe = 4;
    public static final int guiIDTally = 5;
    public static final int guiIDGolemWorkbench = 6;
    public static final int guiIDMaw = 7;
    public static final String RESEARCH_CATEGORY = "AUTOMAGY";
    public static final String GUI_RESEARCH_ICON;
    public static final String GUI_RESEARCH_BACKGROUND = "thaumcraft:textures/gui/gui_research_back_5.jpg";
    public static final String GUI_RESEARCH_BACKGROUND_OVER;
    public static final String WAILA_REDCRYSTAL_POWER = "wailaTip.redcrystalPower";
    public static final String WAILA_REDCRYSTAL_POWER_AMP = "wailaTip.redcrystalPowerWithAmp";
    public static final String WAILA_REDCRYSTAL_POWER_DIM = "wailaTip.redcrystalPowerWithCap";
    public static final String WAILA_REDCRYSTAL_POWER_DENSE = "wailaTip.redcrystalPowerMin";
    public static final String WAILA_HOURGLASS_TIME_SEC = "wailaTip.hourglassTimerSec";
    public static final String WAILA_HOURGLASS_TIME_SEC_ONE = "wailaTip.hourglassTimerSecOne";
    public static final String WAILA_HOURGLASS_TIME_MINSEC = "wailaTip.hourglassTimerMinSec";
    public static final String WAILA_HOURGLASS_COUNTDOWN = "wailaTip.hourglassTimerCountdown";
    public static final String WAILA_FLUIDDATA = "wailaTip.tankFluidData";
    public static final String WAILA_FLUIDDATA_EMPTY = "wailaTip.tankFluidDataEmpty";
    public static final ResourceLocation ENTITY_AVARICEPEARL_REGISTRY;

    public References() {
    }

    static {
        MOD_DOMAIN = "Automagy".toLowerCase(Locale.ENGLISH);
        FLUIDTEXTURE_MILK = MOD_DOMAIN + ":blocks/milk";
        FLUIDTEXTURE_MUSHROOMSOUP = MOD_DOMAIN + ":blocks/mushroomSoup";
        FLUIDTEXTURE_VISHROOMSOUP = MOD_DOMAIN + ":blocks/vishroomSoup";
        PREFIX_GUI = MOD_DOMAIN + ":" + "textures/gui/";
        GUI_CHECKBOXABLE = PREFIX_GUI + "guiCheckboxable.png";
        GUI_WIDGETS_RECOLOR = PREFIX_GUI + "widgetsRecolor.png";
        GUI_FILTERHOLDER = PREFIX_GUI + "filterHolder.png";
        GUI_RUNICWHITELIST = PREFIX_GUI + "runicWhitelist.png";
        GUI_RUNICBLACKLIST = PREFIX_GUI + "runicBlacklist.png";
        GUI_RUNICRECIPE = PREFIX_GUI + "runicRecipe.png";
        GUI_GOLEMWORKBENCH = PREFIX_GUI + "golemWorkbench.png";
        GUI_RESEARCH_ICON = PREFIX_GUI + "researchIcon.png";
        GUI_RESEARCH_BACKGROUND_OVER = PREFIX_GUI + "gui_research_back_over_red.png";
        ENTITY_AVARICEPEARL = MOD_DOMAIN + ":avaricePearl";
        ENTITY_AVARICEPEARL_REGISTRY = new ResourceLocation(MOD_DOMAIN, "avarice_pearl");
    }
}
