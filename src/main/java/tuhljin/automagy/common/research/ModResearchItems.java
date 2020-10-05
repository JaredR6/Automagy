package tuhljin.automagy.common.research;
import java.util.ArrayList;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.items.ItemsTC;
import thaumcraft.api.research.ResearchCategories;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.ThaumcraftExtension.ShardAspect;

public class ModResearchItems {

/*
    public static void registerResearch() {
        boolean mirrorsEnabled = ThaumcraftExtension.isMirrorResearchEnabled();
        ResearchCategories.registerCategory("AUTOMAGY", null, new AspectList(){}, new ResourceLocation(References.GUI_RESEARCH_ICON), new ResourceLocation(References.GUI_RESEARCH_BACKGROUND_OVER));
        ArrayList<IArcaneRecipe> sliverecipes = new ArrayList<>();
        ArrayList<IArcaneRecipe> sliverecipesUndo = new ArrayList<>();
        ShardAspect[] shards = ShardAspect.values();

        for (ShardAspect sa : shards) {
            if (sa.getAspect() != Aspect.FLUX) {
                int i = sa.getMetadata();
                ItemStack shard = new ItemStack(ItemsTC.shard, 1, i);
                ItemStack sliver = new ItemStack(ModItems.sliver, 1, i);
                sliverecipes.add((IArcaneRecipe) ModRecipes.getRecipe(sliver));
                sliverecipesUndo.add((IArcaneRecipe) ModRecipes.getRecipe(shard));
            }
        }

        (new ModResearchItem("SLIVERS", new AspectList(), -1, -2, 0, new Object[]{new ItemStack(ModItems.sliver, 1, 32767)})).createPages(new Object[]{"1", new ResearchPage((IArcaneRecipe[])((IArcaneRecipe[])sliverecipes.toArray(new IArcaneRecipe[0]))), new ResearchPage((IArcaneRecipe[])((IArcaneRecipe[])sliverecipesUndo.toArray(new IArcaneRecipe[0]))), BlocksTC.stone}).setRound().setAutoUnlock().setStub().registerResearchItem();
        (new ModResearchItem("ENCHANTEDPAPER", (new AspectList()).add(Aspect.ORDER, 2).add(Aspect.MIND, 2), 1, -2, 1, new Object[]{new ItemStack(ModItems.enchantedPaper)})).createPages(new Object[]{"1", new ItemStack(ModItems.enchantedPaper, 1, 0), "2", new ItemStack(ModItems.filter, 1, 0), new ItemStack(ModItems.filter, 1, 1), "3", ModItems.enchantedPaperBound, ModRecipes.getAltRecipe(new ItemStack(ModItems.enchantedPaperBound)), new ItemStack(ModBlocks.enchantedBookshelf)}).setRound().setAutoUnlock().setStub().registerResearchItem();
        (new ModResearchItem("REDSTONETHEORY", new AspectList(), 0, 0, 0, new Object[]{new ItemStack(Items.REDSTONE, 1)})).createPages(new Object[]{"1", "2", "3", ModBlocks.torchInversion_on, "4", ModBlocks.hourglass, "5"}).setRound().setAutoUnlock().setStub().registerResearchItem();
        (new ModResearchItem("REDCRYSTAL", (new AspectList()).add(Aspect.ENERGY, 2).add(Aspect.CRYSTAL, 2), 0, 2, 1, new Object[]{new ItemStack(ModBlocks.redcrystal)})).createPages(new Object[]{"1", ModBlocks.redcrystal, "2"}).setParents(new String[]{"REDSTONETHEORY"}).setSiblings(new String[]{"REDCRYSTAL_AMP", "REDCRYSTAL_DIM", "REDCRYSTAL_DENSE"}).registerResearchItem();
        (new ModResearchItem("REDCRYSTAL_DIM", (new AspectList()).add(Aspect.ENERGY, 1).add(Aspect.ENTROPY, 2), -1, 3, 1, new Object[]{new ItemStack(ModBlocks.redcrystalDim)})).createPages(new Object[]{"1", ModBlocks.redcrystalDim}).setParents(new String[]{"REDCRYSTAL"}).setHidden().setStub().setRound().registerResearchItem();
        (new ModResearchItem("REDCRYSTAL_DENSE", (new AspectList()).add(Aspect.ENERGY, 1).add(Aspect.EARTH, 2), 0, 4, 1, new Object[]{new ItemStack(ModBlocks.redcrystalDense)})).createPages(new Object[]{"1", ModBlocks.redcrystalDense}).setParents(new String[]{"REDCRYSTAL"}).setHidden().setStub().setRound().registerResearchItem();
        (new ModResearchItem("REDCRYSTAL_AMP", (new AspectList()).add(Aspect.ENERGY, 1).add(Aspect.FIRE, 2), 1, 3, 1, new Object[]{new ItemStack(ModBlocks.redcrystalAmp)})).createPages(new Object[]{"1", ModBlocks.redcrystalAmp}).setParents(new String[]{"REDCRYSTAL"}).setFlipped().setHidden().setStub().setRound().registerResearchItem();
        (new ModResearchItem("REDCRYSTAL_RES", (new AspectList()).add(Aspect.ENERGY, 3).add(Aspect.AIR, 4), -3, 3, 2, new Object[]{new ItemStack(ModBlocks.redcrystalRes)})).createPages(new Object[]{"1", ModBlocks.redcrystalRes, "2"}).setParents(new String[]{"REDCRYSTAL"}).setHidden().registerResearchItem();
        if (mirrorsEnabled) {
            (new ModResearchItem("REDCRYSTAL_MIRRORBOUND", (new AspectList()).add(Aspect.EXCHANGE, 6).add(Aspect.ENERGY, 3).add(Aspect.DARKNESS, 3), -2, 5, 2, new Object[]{new ItemStack(ModBlocks.redcrystalMerc)})).createPages(new Object[]{"1", ModBlocks.redcrystalMerc, "2"}).addHiddenPages(new String[]{"MIRRORESSENTIA", "MIRRORESSENTIA"}).setParents(new String[]{"REDCRYSTAL_RES"}).setParentsHidden(new String[]{"MIRROR"}).setHiddenUntil(new String[]{"REDCRYSTAL"}).registerResearchItem();
        }

        (new ModResearchItem("MAGICHOURGLASS", (new AspectList()).add(Aspect.MECHANISM, 2).add(Aspect.ENERGY, 2).add(Aspect.MOTION, 3), -2, 1, 1, new Object[]{new ItemStack(ModBlocks.hourglassMagic)})).createPages(new Object[]{"1", ModBlocks.hourglassMagic, "2", "3"}).setParents(new String[]{"REDCRYSTAL"}).setSecondary().registerResearchItem();
        (new ModResearchItem("REMOTECOMPARATOR", (new AspectList()).add(Aspect.ENERGY, 2).add(Aspect.SENSES, 2).add(Aspect.AIR, 2), 3, 2, 1, new Object[]{new ItemStack(ModBlocks.remoteComparator)})).createPages(new Object[]{"1", ModBlocks.remoteComparator, "2", ModItems.crystalEye}).setParents(new String[]{"REDCRYSTAL"}).registerResearchItem();
        (new ModResearchItem("TALLYBOX", (new AspectList()).add(Aspect.SENSES, 3).add(Aspect.ENERGY, 3).add(Aspect.ORDER, 3), 5, 1, 1, new Object[]{new ItemStack(ModBlocks.tallyBox)})).createPages(new Object[]{"1", ModBlocks.tallyBox, "2"}).setParents(new String[]{"REMOTECOMPARATOR"}).setParentsHidden(new String[]{"ENCHANTEDPAPER"}).setHiddenUntil(new String[]{"REMOTECOMPARATOR"}).setSecondary().registerResearchItem();
        (new ModResearchItem("TALLYBOX_LENS", (new AspectList()).add(Aspect.SENSES, 2).add(Aspect.VOID, 2).add(Aspect.ENTROPY, 2), 4, 0, 1, new Object[]{new ItemStack(ModItems.tallyLens, 1, 32767)})).createPages(new Object[]{"1", new ItemStack(ModItems.tallyLens, 1, 0), new ItemStack(ModItems.tallyLens, 1, 1), new ItemStack(ModItems.tallyLens, 1, 2)}).setParents(new String[]{"TALLYBOX"}).setHidden().setSecondary().registerResearchItem();
        (new ModResearchItem("SEALSHEAR", (new AspectList()).add(Aspect.MAN, 3).add(Aspect.MIND, 3).add(Aspect.TOOL, 5).add(Aspect.BEAST, 5), -5, 3, 2, new Object[]{ModItems.getSealStack("shear")})).createPages(new Object[]{"1", ModItems.getSealStack("shear")}).setParentsHidden(new String[]{"SEALUSE"}).registerResearchItem();
        (new ModResearchItem("SEALSHEARADV", (new AspectList()).add(Aspect.MAN, 2).add(Aspect.MIND, 2).add(Aspect.TOOL, 2).add(Aspect.ENTROPY, 3), -7, 3, 2, new Object[]{ModItems.getSealStack("shear_advanced")})).createPages(new Object[]{"1", ModItems.getSealStack("shear_advanced")}).setParents(new String[]{"SEALSHEAR"}).setParentsHidden(new String[]{"GOLEMCOMBATADV"}).setSecondary().registerResearchItem();
        (new ModResearchItem("SEALCRAFT", (new AspectList()).add(Aspect.MAN, 3).add(Aspect.MIND, 3).add(Aspect.CRAFT, 5), -5, 1, 2, new Object[]{new ItemStack(ModBlocks.golemWorkbench)})).createPages(new Object[]{"1", ModBlocks.golemWorkbench, "2", ModItems.getSealStack("craft"), "3", ModItems.recipe}).setParentsHidden(new String[]{"ARCANEPATTERNCRAFTER", "MINDCLOCKWORK"}).registerResearchItem();
        (new ModResearchItem("SEALCRAFT_PROVIDE", (new AspectList()).add(Aspect.CRAFT, 3).add(Aspect.MIND, 5).add(Aspect.EXCHANGE, 4), -7, 1, 1, new Object[]{ModItems.getSealStack("craft_provide")})).createPages(new Object[]{"1", ModItems.getSealStack("craft_provide")}).setParents(new String[]{"SEALCRAFT"}).setParentsHidden(new String[]{"SEALPROVIDE"}).setSiblings(new String[]{"SEALSUPPLY"}).setFlipped().registerResearchItem();
        (new ModResearchItem("SEALSUPPLY", (new AspectList()).add(Aspect.CRAFT, 3).add(Aspect.MIND, 5).add(Aspect.EXCHANGE, 4), -6, -1, 1, new Object[]{ModItems.getSealStack("supply")})).createPages(new Object[]{"1", ModItems.getSealStack("supply")}).setParents(new String[]{"SEALCRAFT_PROVIDE"}).setParentsHidden(new String[]{"SEALPROVIDE"}).setFlipped().setStub().setHidden().setRound().registerResearchItem();
        (new ModResearchItem("HUNGRYMAW", (new AspectList()).add(Aspect.DESIRE, 2).add(Aspect.VOID, 2).add(Aspect.DARKNESS, 1).add(Aspect.EXCHANGE, 1), 3, 4, 1, new Object[]{new ItemStack(ModBlocks.hungryMaw)})).createPages(new Object[]{"1", ModBlocks.hungryMaw, "2", ModBlocks.finicalMaw}).setParentsHidden(new String[]{"HUNGRYCHEST", "FOCUSPORTABLEHOLE"}).registerResearchItem();
        (new ModResearchItem("SPITTINGMAW", (new AspectList()).add(Aspect.AVERSION, 2).add(Aspect.EXCHANGE, 2), 5, 4, 1, new Object[]{new ItemStack(ModBlocks.spittingMaw)})).createPages(new Object[]{"1", ModBlocks.spittingMaw, "2"}).setParents(new String[]{"HUNGRYMAW"}).setParentsHidden(new String[]{"METALLURGY"}).setHiddenUntil(new String[]{"HUNGRYMAW"}).setSecondary().registerResearchItem();
        (new ModResearchItem("AVARICEPEARL", (new AspectList()).add(Aspect.DESIRE, 2).add(Aspect.ELDRITCH, 2).add(Aspect.MOTION, 1), 3, 6, 1, new Object[]{new ItemStack(ModItems.avaricePearl)})).createPages(new Object[]{"1", ModItems.avaricePearl}).setParentsHidden(new String[]{"!TELEPORT"}).registerResearchItem();
        (new ModResearchItem("THIRSTYTANK", (new AspectList()).add(Aspect.DESIRE, 8).add(Aspect.WATER, 5).add(Aspect.EXCHANGE, 3), 5, 6, 1, new Object[]{new ItemStack(ModBlocks.thirstyTank)})).createPages(new Object[]{"1", ModBlocks.thirstyTank, "2"}).setParents(new String[]{"AVARICEPEARL"}).setParentsHidden(new String[]{"HUNGRYCHEST"}).registerResearchItem();
        (new ModResearchItem("THIRSTYTANK_GLYPH", (new AspectList()).add(Aspect.DESIRE, 3).add(Aspect.WATER, 3).add(Aspect.MECHANISM, 2), 6, 7, 1, new Object[]{new ItemStack(ModItems.tankGlyph, 1, 0)})).createPages(new Object[]{"1", new ItemStack(ModItems.tankGlyph), "2", "3", new ItemStack(ModItems.tankGlyph, 1, 1), new ItemStack(ModItems.tankGlyph, 1, 2), new ItemStack(ModItems.tankGlyph, 1, 3), new ItemStack(ModItems.tankGlyph, 1, 4), new ItemStack(ModItems.tankGlyph, 1, 5), new ItemStack(ModItems.tankGlyph, 1, 6), new ItemStack(ModItems.tankGlyph, 1, 7), new ItemStack(ModItems.tankGlyph, 1, 8)}).setParents(new String[]{"THIRSTYTANK"}).setHidden().setSecondary().registerResearchItem();
        (new ModResearchItem("THIRSTYTANK_GLYPH_BOVINE", (new AspectList()).add(Aspect.DESIRE, 3).add(Aspect.WATER, 3).add(Aspect.BEAST, 4), 5, 8, 2, new Object[]{new ItemStack(ModItems.tankGlyph, 1, 9)})).createPages(new Object[]{"1", new ItemStack(ModItems.tankGlyph, 1, 9)}).setParents(new String[]{"THIRSTYTANK_GLYPH"}).setHiddenUntil(new String[]{"THIRSTYTANK"}).setFlipped().setSecondary().registerResearchItem();
    }
*/
}