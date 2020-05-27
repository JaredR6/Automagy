package tuhljin.automagy.common;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tuhljin.automagy.common.blocks.ModBlock;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.entities.ModEntities;
import tuhljin.automagy.common.gui.AutomagyGUIHandler;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.AutomagyConfig;
import tuhljin.automagy.common.lib.compat.CompatibilityManager;
import tuhljin.automagy.common.lib.events.AutomagyCasterTriggerManager;
import tuhljin.automagy.common.lib.events.AutomagyEventHandler;
import tuhljin.automagy.common.network.PacketHandler;

public class ProxyCommon {
    public AutomagyCasterTriggerManager casterTriggerManager;

    public void preInit(FMLPreInitializationEvent event) {
        AutomagyConfig.load(event.getSuggestedConfigurationFile());
        ModBlocks.initBlocks();
        ModItems.initItems();
        ModItems.initFluidContainers();
        ModItems.registerDispenserBehaviors();
        ModBlocks.registerTileEntities();
        ModEntities.registerSeals();
        PacketHandler.registerMessages();
        MinecraftForge.EVENT_BUS.register(new AutomagyEventHandler());
        this.casterTriggerManager = new AutomagyCasterTriggerManager();
    }

    public void init(FMLInitializationEvent event) {
        ModEntities.register();
        CompatibilityManager.init();
        NetworkRegistry.INSTANCE.registerGuiHandler(Automagy.instance, (IGuiHandler)new AutomagyGUIHandler());
    }

    public void postInit(FMLPostInitializationEvent event) {
        ModRecipes.registerRecipes();
        ModRecipes.setAspects();
        ModResearchItems.registerResearch();
    }

    public Item initializeItem(Item item, String name) {
        item.setUnlocalizedName("Automagy".toLowerCase() + "." + name);
        item.setCreativeTab(Automagy.creativeTab);
        GameRegistry.registerItem(item, name);
        return item;
    }

    public Block initializeBlock(Block block, String name, boolean showInTab) {
        block.setUnlocalizedName("Automagy".toLowerCase() + "." + name);
        if (showInTab)
            block.setCreativeTab(Automagy.creativeTab);
        Class clazz = (block instanceof ModBlock) ? ((ModBlock)block).getItemBlockClass() : null;
        if (clazz == null) {
            GameRegistry.registerBlock(block, name);
        } else {
            GameRegistry.registerBlock(block, clazz, name);
        }
        return block;
    }

    public EntityPlayer getCurrentPlayer() {
        return null;
    }

    public void fxGhostlyBallStream(World worldObj, double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {}

    public void fxWisp3(double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {}

    public void fxWisp3NoClip(double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {}

    public void fxBurst(double x, double y, double z, float size) {}

    public void fxOldBlockSparkle(World world, int x, int y, int z, int color, int count) {}
}