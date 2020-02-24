package tuhljin.automagy.common.items;

import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import thaumcraft.api.golems.GolemHelper;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.ModBlocks;
//import tuhljin.automagy.common.entities.EntityAvaricePearl;

public class ModItems {
    //public static ItemSliver sliver;
    public static Item enchantedPaper;
    public static Item enchantedPaperBound;
    public static Item filter;
    public static Item recipe;
    public static Item food;
    public static Item crystalEye;
    public static Item avaricePearl;
    public static Item tallyLens;
    //public static ItemGlyph tankGlyph;
    //public static ItemBucketCustom bucketMushroom;
    //public static ItemBucketCustom bucketVishroom;

    public ModItems() {
    }

    public static void initItems() {
        //sliver = (ItemSliver)Automagy.proxy.initializeItem(new ItemSliver(), "sliver");
        food = Automagy.proxy.initializeItem(new ItemFoodstuff(), "foodstuff");
        enchantedPaper = Automagy.proxy.initializeItem(new Item(), "enchantedPaper");
        enchantedPaperBound = Automagy.proxy.initializeItem(new ItemEnchantedPaperBound(), "book");
        filter = Automagy.proxy.initializeItem(new ItemFilter(), "filter");
        recipe = Automagy.proxy.initializeItem(new ItemRecipe(), "recipe");
        crystalEye = Automagy.proxy.initializeItem(new ItemCrystalEye(), "crystalEye");
        avaricePearl = Automagy.proxy.initializeItem(new ItemAvaricePearl(), "avaricePearl");
        tallyLens = Automagy.proxy.initializeItem(new ItemTallyLens(), "tallyLens");
        tankGlyph = (ItemGlyph)Automagy.proxy.initializeItem(new ItemGlyph(), "tankGlyph");
    }

    public static ItemStack getSealStack(String partialKey) {
        return GolemHelper.getSealStack("Automagy:" + partialKey);
    }

    public static void initFluidContainers() {
        bucketMushroom = (ItemBucketCustom)Automagy.proxy.initializeItem(new ItemBucketCustom(ModBlocks.mushroomSoup), "bucketMushroom");
        bucketVishroom = (ItemBucketCustom)Automagy.proxy.initializeItem(new ItemBucketCustom(ModBlocks.vishroomSoup), "bucketVishroom");
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("mushroomsoup"), 1000), new ItemStack(bucketMushroom), new ItemStack(Items.field_151133_ar));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("vishroomsoup"), 1000), new ItemStack(bucketVishroom), new ItemStack(Items.field_151133_ar));
        FluidContainerRegistry.registerFluidContainer(new FluidStack(FluidRegistry.getFluid("milk"), 1000), new ItemStack(Items.field_151117_aB), new ItemStack(Items.field_151133_ar));
    }

    public static void registerDispenserBehaviors() {
        BlockDispenser.field_149943_a.func_82595_a(avaricePearl, new BehaviorProjectileDispenseWithItemStack() {
            protected IProjectile func_82499_a(World worldIn, IPosition position) {
                return new EntityAvaricePearl(worldIn, position.func_82615_a(), position.func_82617_b(), position.func_82616_c(), this.theItemStack.func_77952_i());
            }
        });
    }
}
