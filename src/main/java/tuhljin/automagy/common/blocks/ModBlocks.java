package tuhljin.automagy.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystal;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalAmp;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalDense;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalDim;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalMerc;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalRes;
import tuhljin.automagy.common.items.ModItems;
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

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = References.MOD_ID)
public class ModBlocks {
    public static final Material materialWaterproofCircuit = new MaterialWaterproofCircuit();
    public static final Block redcrystal = new BlockRedcrystal();
    public static final Block redcrystalAmp = new BlockRedcrystalAmp();
    public static final Block redcrystalDim = new BlockRedcrystalDim();
    public static final Block redcrystalDense = new BlockRedcrystalDense();
    public static final Block redcrystalRes = new BlockRedcrystalAmp();
    public static final Block redcrystalMerc = new BlockRedcrystalMerc();
    public static final Block enchantedBookshelf = new BlockBookshelfEnchanted();
    public static final Block torchInversion_on = new BlockTorchInversion(true, References.BLOCK_TORCH_INVERSION_ON);
    public static final Block torchInversion_off = new BlockTorchInversion(false, References.BLOCK_TORCH_INVERSION_OFF);
    public static final Block hourglass = new BlockHourglass();
    public static final Block hourglassMagic = new BlockHourglassMagic();
    public static final Block golemWorkbench = new BlockGolemWorkbench();
    public static final Block remoteComparator = new BlockRemoteComparator();
    public static final BlockTallyBase tallyBox = new BlockTally();
    public static final BlockTallyBase tallyBlockWorld = new BlockTallyWorld();
    public static final BlockTallyBase tallyBlockDrops = new BlockTallyWorld();
    public static final Block hungryMaw = new BlockMawHungry();
    public static final Block finicalMaw = new BlockMawFinical();
    public static final Block spittingMaw = new BlockMawSpitting();
    public static final Block specialProcess = new BlockProcess();
    public static final Block thirstyTank = new BlockTankThirsty();
    public static final ModBlockFluid milk = new BlockMilk();
    public static final ModBlockFluid mushroomSoup = new BlockMushroomSoup();
    public static final ModBlockFluid vishroomSoup = new BlockVishroomSoup();


    @SubscribeEvent
    public static void registerBlocks(@Nonnull RegistryEvent.Register<Block> evt) {
        IForgeRegistry<Block> r = evt.getRegistry();
        r.register(redcrystal);
        r.register(redcrystalAmp);
        r.register(redcrystalDim);
        r.register(redcrystalDense);
        r.register(redcrystalRes);
        r.register(redcrystalMerc);
        r.register(enchantedBookshelf);
        r.register(torchInversion_on);
        r.register(torchInversion_off);
        r.register(hourglass);
        r.register(hourglassMagic);
        r.register(golemWorkbench);
        r.register(remoteComparator);
        r.register(tallyBox);
        r.register(tallyBlockWorld);
        r.register(tallyBlockDrops);
        r.register(hungryMaw);
        r.register(finicalMaw);
        r.register(spittingMaw);
        r.register(specialProcess);
        r.register(thirstyTank);
        //r.register(milk);
        //r.register(mushroomSoup);
        //r.register(vishroomSoup);
    }

    @SubscribeEvent
    public static void registerItemBlocks(@Nonnull RegistryEvent.Register<Item> evt) {
        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(new ItemBlockRedcrystal(redcrystal).setRegistryName(References.BLOCK_REDCRYSTAL));
        r.register(new ItemBlockRedcrystal(redcrystalAmp).setRegistryName(References.BLOCK_REDCRYSTAL_AMP));
        r.register(new ItemBlockRedcrystal(redcrystalDim).setRegistryName(References.BLOCK_REDCRYSTAL_DIM));
        r.register(new ItemBlockRedcrystal(redcrystalDense).setRegistryName(References.BLOCK_REDCRYSTAL_DENSE));
        r.register(new ItemBlockRedcrystal(redcrystalRes).setRegistryName(References.BLOCK_REDCRYSTAL_RES));
        r.register(new ItemBlockRedcrystal(redcrystalMerc).setRegistryName(References.BLOCK_REDCRYSTAL_MERC));
        r.register(new ModItemBlock(enchantedBookshelf).setRegistryName(References.BLOCK_BOOKSHELF_ENCHANTED));
        r.register(new ItemBlock(torchInversion_on).setRegistryName(References.BLOCK_TORCH_INVERSION_ON));
        r.register(new ItemBlock(torchInversion_off).setRegistryName(References.BLOCK_TORCH_INVERSION_OFF));
        r.register(new ItemBlock(hourglass).setRegistryName(References.BLOCK_HOURGLASS));
        r.register(new ItemBlock(hourglassMagic).setRegistryName(References.BLOCK_HOURGLASS_MAGIC));
        r.register(new ItemBlock(golemWorkbench).setRegistryName(References.BLOCK_GOLEMWORKBENCH));
        r.register(new ItemBlock(remoteComparator).setRegistryName(References.BLOCK_REMOTECOMPARATOR));
        r.register(new ItemBlock(tallyBox).setRegistryName(References.BLOCK_TALLY));
        r.register(new ItemBlock(tallyBlockWorld).setRegistryName(References.BLOCK_TALLY_WORLD));
        r.register(new ItemBlock(tallyBlockDrops).setRegistryName(References.BLOCK_TALLY_DROPS));
        r.register(new ItemBlock(hungryMaw).setRegistryName(References.BLOCK_MAW_HUNGRY));
        r.register(new ItemBlock(finicalMaw).setRegistryName(References.BLOCK_MAW_FINICAL));
        r.register(new ItemBlock(spittingMaw).setRegistryName(References.BLOCK_MAW_SPITTING));
        r.register(new ItemBlock(specialProcess).setRegistryName(References.BLOCK_PROCESS));
        r.register(new ItemBlockTankThirsty(thirstyTank).setRegistryName(References.BLOCK_TANK_THIRSTY));
        //r.register(new ItemBlock(milk).setRegistryName(References.BLOCK_MILK));
        //r.register(new ItemBlock(mushroomSoup).setRegistryName(References.BLOCK_MUSHROOMSOUP));
        //r.register(new ItemBlock(vishroomSoup).setRegistryName(References.BLOCK_VISHROOMSOUP));

        registerTileEntities();
    }

    public static void registerTileEntities() {
        GameRegistry.registerTileEntity(TileRedcrystal.class, new ResourceLocation(References.MOD_ID, "tileredcrystal"));
        GameRegistry.registerTileEntity(TileRedcrystalMerc.class, new ResourceLocation(References.MOD_ID, "tileredcrystalmerc"));
        GameRegistry.registerTileEntity(TileHourglass.class, new ResourceLocation(References.MOD_ID, "tilehourglass"));
        GameRegistry.registerTileEntity(TileHourglassMagic.class, new ResourceLocation(References.MOD_ID, "tilehourglassmagic"));
        GameRegistry.registerTileEntity(TileTorchInversion.class, new ResourceLocation(References.MOD_ID, "tiletorchinversion"));
        GameRegistry.registerTileEntity(TileRemoteComparator.class, new ResourceLocation(References.MOD_ID, "tileremotecomparator"));
        GameRegistry.registerTileEntity(TileTally.class, new ResourceLocation(References.MOD_ID, "tiletally"));
        GameRegistry.registerTileEntity(TileTallyWorld.class, new ResourceLocation(References.MOD_ID, "tiletallyworld"));
        GameRegistry.registerTileEntity(TileTallyDrops.class, new ResourceLocation(References.MOD_ID, "tiletallydrops"));
        GameRegistry.registerTileEntity(TileGolemWorkbench.class, new ResourceLocation(References.MOD_ID, "tilegolemworkbench"));
        GameRegistry.registerTileEntity(TileMawHungry.class, new ResourceLocation(References.MOD_ID, "tilemawhungry"));
        GameRegistry.registerTileEntity(TileMawFinical.class, new ResourceLocation(References.MOD_ID, "tilemawfinical"));
        GameRegistry.registerTileEntity(TileMawSpitting.class, new ResourceLocation(References.MOD_ID, "tilemawspitting"));
        GameRegistry.registerTileEntity(TileProcess.class, new ResourceLocation(References.MOD_ID, "tileprocess"));
        GameRegistry.registerTileEntity(TileTankThirsty.class, new ResourceLocation(References.MOD_ID, "tiletankthirsty"));
    }
}
