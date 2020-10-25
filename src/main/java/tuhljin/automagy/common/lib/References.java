package tuhljin.automagy.common.lib;

import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class References {
    public static final String MOD_ID = "automagy";
    public static final String MOD_DOMAIN = "automagy";
    public static final String MOD_PREFIX = MOD_DOMAIN + ":";
    public static final String MOD_NAME = "automagy";
    public static final String VERSION = "3.0.0";
    public static final String URL_VERSION_CHECK = "http://everburningtorch.com/minecraft/automagy/checkVersion2/";
    public static final String BLOCK_REDCRYSTAL = "redcrystal";
    public static final String BLOCK_REDCRYSTAL_AMP = "redcrystalamp";
    public static final String BLOCK_REDCRYSTAL_DIM = "redcrystaldim";
    public static final String BLOCK_REDCRYSTAL_DENSE = "redcrystaldense";
    public static final String BLOCK_REDCRYSTAL_RES = "redcrystalres";
    public static final String BLOCK_REDCRYSTAL_MERC = "redcrystalmerc";
    public static final String BLOCK_HOURGLASS = "hourglass";
    public static final String BLOCK_HOURGLASS_MAGIC = "hourglassmagic";
    public static final String BLOCK_BOOKSHELF_ENCHANTED = "bookshelf";
    public static final String BLOCK_TORCH_INVERSION_ON = "torchinversion";
    public static final String BLOCK_TORCH_INVERSION_OFF = "torchinversion_off";
    public static final String BLOCK_TALLY = "tally";
    public static final String BLOCK_TALLY_DROPS = "tallyyield";
    public static final String BLOCK_TALLY_WORLD = "tallyunbound";
    public static final String BLOCK_REMOTECOMPARATOR = "remotecomparator";
    public static final String BLOCK_GOLEMWORKBENCH = "golemworkbench";
    public static final String BLOCK_MAW_HUNGRY = "mawhungry";
    public static final String BLOCK_MAW_FINICAL = "mawfinical";
    public static final String BLOCK_MAW_SPITTING = "mawspitting";
    public static final String BLOCK_PROCESS = "process";
    public static final String BLOCK_TANK_THIRSTY = "tankthirsty";
    public static final String BLOCK_MILK = "milk";
    public static final String BLOCK_MUSHROOMSOUP = "mushroomsoup";
    public static final String BLOCK_VISHROOMSOUP = "vishroomstew";
    public static final String ITEM_SLIVER = "sliver";
    public static final String ITEM_CRYSTALEYE = "crystaleye";
    public static final String ITEM_AVARICEPEARL = "avaricepearl";
    public static final String ITEM_ENCHANTEDPAPER = "enchantedpaper";
    public static final String ITEM_ENCHANTEDPAPER_BOUND = "book";
    public static final String ITEM_FILTER_WHITE = "filterwhitelist";
    public static final String ITEM_FILTER_BLACK = "filterblacklist";
    public static final String ITEM_RECIPE = "recipe";
    public static final String ITEM_FOODSTUFF = "foodstuff";
    public static final String ITEM_TALLYLENS = "tallylens";
    public static final String ITEM_TANKGLYPH = "tankglyph";
    public static final String ITEM_BUCKET_MUSHROOM = "bucketmushroom";
    public static final String ITEM_BUCKET_VISHROOM = "bucketvishroom";
    public static final String FLUID_MILK = "milk";
    public static final String FLUIDTEXTURE_MILK = MOD_PREFIX + "blocks/milk";
    public static final String FLUID_MUSHROOMSOUP = "mushroomsoup";
    public static final String FLUIDTEXTURE_MUSHROOMSOUP = MOD_PREFIX + "blocks/mushroomsoup";
    public static final String FLUID_VISHROOMSOUP = "vishroomsoup";
    public static final String FLUIDTEXTURE_VISHROOMSOUP = MOD_PREFIX + "blocks/vishroomsoup";
    public static final String ENTITY_AVARICEPEARL = MOD_PREFIX + "avaricepearl";
    public static final String SEAL_CRAFT = "craft";
    public static final String SEAL_CRAFT_PROVIDE = "craft_provide";
    public static final String SEAL_SHEAR = "shear";
    public static final String SEAL_SHEAR_ADV = "shear_advanced";
    public static final String SEAL_SUPPLY = "supply";
    public static final String PREFIX_GUI = MOD_PREFIX + "textures/gui/";
    public static final String GUI_CHECKBOXABLE = PREFIX_GUI + "guicheckboxable.png";
    public static final String GUI_WIDGETS_RECOLOR = PREFIX_GUI + "widgetsrecolor.png";
    public static final String GUI_FILTERHOLDER = PREFIX_GUI + "filterholder.png";
    public static final String GUI_RUNICWHITELIST = PREFIX_GUI + "runicwhitelist.png";
    public static final String GUI_RUNICBLACKLIST = PREFIX_GUI + "runicblacklist.png";
    public static final String GUI_RUNICRECIPE = PREFIX_GUI + "runicrecipe.png";
    public static final String GUI_GOLEMWORKBENCH = PREFIX_GUI + "golemworkbench.png";
    public static final int guiIDHourglassMagic = 1;
    public static final int guiIDFilterWhite = 2;
    public static final int guiIDFilterBlack = 3;
    public static final int guiIDRecipe = 4;
    public static final int guiIDTally = 5;
    public static final int guiIDGolemWorkbench = 6;
    public static final int guiIDMaw = 7;
    public static final String RESEARCH_CATEGORY = "AUTOMAGY";
    public static final String GUI_RESEARCH_ICON = PREFIX_GUI + "researchicon.png";
    public static final String GUI_RESEARCH_BACKGROUND = "thaumcraft:textures/gui/gui_research_back_5.jpg";
    public static final String GUI_RESEARCH_BACKGROUND_OVER = PREFIX_GUI + "gui_research_back_over_red.png";
    public static final String WAILA_REDCRYSTAL_POWER = "wailaTip.redcrystalpower";
    public static final String WAILA_REDCRYSTAL_POWER_AMP = "wailaTip.redcrystalpowerwithamp";
    public static final String WAILA_REDCRYSTAL_POWER_DIM = "wailaTip.redcrystalpowerwithcap";
    public static final String WAILA_REDCRYSTAL_POWER_DENSE = "wailaTip.redcrystalpowermin";
    public static final String WAILA_HOURGLASS_TIME_SEC = "wailaTip.hourglasstimersec";
    public static final String WAILA_HOURGLASS_TIME_SEC_ONE = "wailaTip.hourglasstimersecone";
    public static final String WAILA_HOURGLASS_TIME_MINSEC = "wailaTip.hourglasstimerminsec";
    public static final String WAILA_HOURGLASS_COUNTDOWN = "wailaTip.hourglasstimercountdown";
    public static final String WAILA_FLUIDDATA = "wailaTip.tankfluiddata";
    public static final String WAILA_FLUIDDATA_EMPTY = "wailaTip.tankfluiddataempty";
    public static final ResourceLocation ENTITY_AVARICEPEARL_REGISTRY = new ResourceLocation(MOD_DOMAIN, "avarice_pearl");
}
