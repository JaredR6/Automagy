package tuhljin.automagy.common.lib.events;

import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.items.ItemFilter;
import tuhljin.automagy.common.items.ItemRecipe;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.compat.CompatibilityManager;

import javax.annotation.Nonnull;

public class AutomagyEventHandler {
    public AutomagyEventHandler() {
    }

    @SubscribeEvent
    public void onCrafting(@Nonnull ItemCraftedEvent event) {
        ItemStack sourceRecipe;
        int sourceRecipeSlot;
        int recipesFound;
        int size;
        int i;
        ItemStack stack;
        if (event.crafting.getItem() instanceof ItemFilter) {
            sourceRecipe = null;
            sourceRecipeSlot = 0;
            recipesFound = 0;
            size = event.craftMatrix.getSizeInventory();

            for(i = 0; i < size; ++i) {
                stack = event.craftMatrix.getStackInSlot(i);
                if (!stack.isEmpty() && stack.getItem() instanceof ItemFilter) {
                    if (sourceRecipe == null) {
                        sourceRecipe = stack;
                        sourceRecipeSlot = i;
                    }

                    ++recipesFound;
                    if (recipesFound > 2) {
                        break;
                    }
                }
            }

            if (sourceRecipe != null && recipesFound == 2) {
                sourceRecipe.grow(1);
                event.craftMatrix.setInventorySlotContents(sourceRecipeSlot, sourceRecipe);
            }
        } else if (ItemRecipe.stackIsRecipe(event.crafting)) {
            sourceRecipe = null;
            sourceRecipeSlot = 0;
            recipesFound = 0;
            size = event.craftMatrix.getSizeInventory();

            for(i = 0; i < size; ++i) {
                stack = event.craftMatrix.getStackInSlot(i);
                if (!stack.isEmpty() && ItemRecipe.stackIsRecipe(stack)) {
                    if (sourceRecipe == null) {
                        sourceRecipe = stack;
                        sourceRecipeSlot = i;
                    }

                    ++recipesFound;
                    if (recipesFound > 2) {
                        break;
                    }
                }
            }

            if (sourceRecipe != null && recipesFound == 2) {
                sourceRecipe.grow(1);
                event.craftMatrix.setInventorySlotContents(sourceRecipeSlot, sourceRecipe);
            }
        }

    }

    @SubscribeEvent
    public void fillBucket(@Nonnull FillBucketEvent event) {
        if (event.getTarget().typeOfHit == RayTraceResult.Type.BLOCK) {
            World world = event.getWorld();
            BlockPos pos = event.getTarget().getBlockPos();
            Block block = TjUtil.getBlock(world, pos);
            if ((block == ModBlocks.milk || block == CompatibilityManager.getMilkBlock()) && TjUtil.isSourceBlock(world, pos)) {
                world.setBlockToAir(pos);
                event.setFilledBucket(new ItemStack(Items.MILK_BUCKET));
                event.setResult(Result.ALLOW);
            }
            /*
            if (block == ModBlocks.mushroomSoup && TjUtil.isSourceBlock(world, pos)) {
                world.setBlockToAir(pos);
                event.setFilledBucket(event.getFilledBucket());
                event.result = new ItemStack(ModItems.bucketMushroom);
                event.setResult(Result.ALLOW);
                return;
            }

            if (block == ModBlocks.vishroomSoup && TjUtil.isSourceBlock(world, pos)) {
                world.setBlockToAir(pos);
                event.result = new ItemStack(ModItems.bucketVishroom);
                event.setResult(Result.ALLOW);
                return;
            }

             */
        }

    }
}
