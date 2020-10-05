package tuhljin.automagy.common.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import thaumcraft.api.golems.GolemHelper;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.ModBlocks;

import javax.annotation.Nonnull;
import tuhljin.automagy.common.entities.EntityAvaricePearl;
import tuhljin.automagy.common.lib.References;

@Mod.EventBusSubscriber
public class ModItems {
    //public static ItemSliver sliver;
    public static Item enchantedPaper = new ModItem(References.ITEM_ENCHANTEDPAPER);
    public static Item enchantedPaperBound = new ModItem(References.ITEM_ENCHANTEDPAPER_BOUND);
    public static Item filterWhitelist = new ItemFilterWhite();
    public static Item filterBlacklist = new ItemFilterBlack();
    public static Item recipe = new ItemRecipe();
    public static Item food = new ItemFoodstuff();
    public static Item crystalEye = new ItemCrystalEye();
    public static Item avaricePearl = new ItemAvaricePearl().setRegistryName(References.ITEM_AVARICEPEARL);
    public static Item tallyLens = new ItemTallyLens().setRegistryName(References.ITEM_TALLYLENS);
    public static ItemGlyph tankGlyph = (ItemGlyph) new ItemGlyph().setRegistryName(References.ITEM_TANKGLYPH);

    public ModItems() {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> evt) {
        //sliver = (ItemSliver)Automagy.proxy.initializeItem(new ItemSliver(), "sliver");
        IForgeRegistry<Item> r = evt.getRegistry();
        r.register(enchantedPaper);
        r.register(enchantedPaperBound);
        r.register(filterWhitelist);
        r.register(filterBlacklist);
        r.register(recipe);
        r.register(food);
        r.register(crystalEye);
        r.register(avaricePearl);
        r.register(tallyLens);
        r.register(tankGlyph);
    }

    public static ItemStack getSealStack(String partialKey) {
        return GolemHelper.getSealStack("automagy:" + partialKey);
    }

    public static void initFluidContainers() {
        FluidRegistry.enableUniversalBucket();
        FluidRegistry.addBucketForFluid(ModBlocks.mushroomSoup.getFluid());
        FluidRegistry.addBucketForFluid(ModBlocks.vishroomSoup.getFluid());
    }

    public static void registerDispenserBehaviors() {
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(avaricePearl, new BehaviorProjectileDispense() {
            @Nonnull
            protected IProjectile getProjectileEntity(@Nonnull World worldIn, @Nonnull IPosition position, @Nonnull ItemStack stack) {
                return new EntityAvaricePearl(worldIn, position.getX(), position.getY(), position.getZ(), stack.getItemDamage());
            }
        });
    }
}
