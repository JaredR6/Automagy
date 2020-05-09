package tuhljin.automagy.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystal;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalAmp;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalDense;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalDim;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalMerc;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalRes;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.tiles.TileGolemWorkbench;
import tuhljin.automagy.common.tiles.TileHourglass;
import tuhljin.automagy.common.tiles.TileHourglassMagic;
import tuhljin.automagy.common.tiles.TileMawFinical;
import tuhljin.automagy.common.tiles.TileMawHungry;
import tuhljin.automagy.common.tiles.TileMawSpitting;
import tuhljin.automagy.common.tiles.TileProcess;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.tiles.TileRedcrystal;
import tuhljin.automagy.common.tiles.TileRedcrystalMerc;
import tuhljin.automagy.common.tiles.TileRemoteComparator;
import tuhljin.automagy.common.tiles.TileTally;
import tuhljin.automagy.common.tiles.TileTallyDrops;
import tuhljin.automagy.common.tiles.TileTallyWorld;
import tuhljin.automagy.common.tiles.TileTankThirsty;
import tuhljin.automagy.common.tiles.TileTorchInversion;

public class ModBlocks {
    public static Material materialWaterproofCircuit = new MaterialWaterproofCircuit();
    public static Block redcrystal;
    public static Block redcrystalAmp;
    public static Block redcrystalDim;
    public static Block redcrystalDense;
    public static Block redcrystalRes;
    public static Block redcrystalMerc;
    public static Block enchantedBookshelf;
    public static Block torchInversion_on;
    public static Block torchInversion_off;
    public static Block hourglass;
    public static Block hourglassMagic;
    public static Block golemWorkbench;
    public static Block remoteComparator;
    public static BlockTallyBase tallyBox;
    public static BlockTallyBase tallyBlockWorld;
    public static BlockTallyBase tallyBlockDrops;
    public static Block hungryMaw;
    public static Block finicalMaw;
    public static Block spittingMaw;
    public static Block specialProcess;
    public static Block thirstyTank;
    public static ModBlockFluid milk;
    public static ModBlockFluid mushroomSoup;
    public static ModBlockFluid vishroomSoup;

    public ModBlocks() {
    }

    public static void initBlocks() {
        redcrystal = initializeBlock(new BlockRedcrystal(), "redcrystal");
        redcrystalAmp = initializeBlock(new BlockRedcrystalAmp(), "redcrystalAmp");
        redcrystalDim = initializeBlock(new BlockRedcrystalDim(), "redcrystalDim");
        redcrystalDense = initializeBlock(new BlockRedcrystalDense(), "redcrystalDense");
        redcrystalRes = initializeBlock(new BlockRedcrystalRes(), "redcrystalRes");
        redcrystalMerc = initializeBlock(new BlockRedcrystalMerc(), "redcrystalMerc");
        hourglass = initializeBlock(new BlockHourglass(), "hourglass");
        hourglassMagic = initializeBlock(new BlockHourglassMagic(), "hourglassMagic");
        enchantedBookshelf = initializeBlock(new BlockBookshelfEnchanted(), "bookshelf");
        torchInversion_on = initializeBlock(new BlockTorchInversion(true), "torchInversion");
        torchInversion_off = initializeBlock(new BlockTorchInversion(false), "torchInversion_off", false);
        remoteComparator = initializeBlock(new BlockRemoteComparator(), "remoteComparator");
        golemWorkbench = initializeBlock(new BlockGolemWorkbench(), "golemWorkbench");
        tallyBox = (BlockTallyBase)initializeBlock(new BlockTally(), "tally");
        tallyBlockWorld = (BlockTallyBase)initializeBlock(new BlockTallyWorld(), "tallyUnbound");
        tallyBlockDrops = (BlockTallyBase)initializeBlock(new BlockTallyDrops(), "tallyYield");
        hungryMaw = initializeBlock(new BlockMawHungry(), "mawHungry");
        finicalMaw = initializeBlock(new BlockMawFinical(), "mawFinical");
        spittingMaw = initializeBlock(new BlockMawSpitting(), "mawSpitting");
        specialProcess = initializeBlock(new BlockProcess(), "process", false);
        thirstyTank = initializeBlock(new BlockTankThirsty(), "tankThirsty");
        milk = (ModBlockFluid)initializeBlock(new BlockMilk("milk", References.FLUIDTEXTURE_MILK), "milk");
        mushroomSoup = (ModBlockFluid)initializeBlock(new BlockMushroomSoup("mushroomsoup", References.FLUIDTEXTURE_MUSHROOMSOUP), "mushroomSoup");
        vishroomSoup = (ModBlockFluid)initializeBlock(new BlockVishroomSoup("vishroomsoup", References.FLUIDTEXTURE_VISHROOMSOUP), "vishroomSoup");
    }

    private static Block initializeBlock(Block block, String name, boolean showInTab) {
        return Automagy.proxy.initializeBlock(block, name, showInTab);
    }

    private static Block initializeBlock(Block block, String name) {
        return initializeBlock(block, name, true);
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileRedcrystal.class, "TileRedcrystal");
        GameRegistry.registerTileEntity(TileRedcrystalMerc.class, "TileRedcrystalMerc");
        GameRegistry.registerTileEntity(TileHourglass.class, "TileHourglass");
        GameRegistry.registerTileEntity(TileHourglassMagic.class, "TileHourglassMagic");
        GameRegistry.registerTileEntity(TileTorchInversion.class, "TileTorchInversion");
        GameRegistry.registerTileEntity(TileRemoteComparator.class, "TileRemoteComparator");
        GameRegistry.registerTileEntity(TileTally.class, "TileTally");
        GameRegistry.registerTileEntity(TileTallyWorld.class, "TileTallyWorld");
        GameRegistry.registerTileEntity(TileTallyDrops.class, "TileTallyDrops");
        GameRegistry.registerTileEntity(TileGolemWorkbench.class, "TileGolemWorkbench");
        GameRegistry.registerTileEntity(TileMawHungry.class, "TileMawHungry");
        GameRegistry.registerTileEntity(TileMawFinical.class, "TileMawFinical");
        GameRegistry.registerTileEntity(TileMawSpitting.class, "TileMawSpitting");
        GameRegistry.registerTileEntity(TileProcess.class, "TileProcess");
        GameRegistry.registerTileEntity(TileTankThirsty.class, "TileTankThirsty");
    }
}
