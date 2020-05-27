package tuhljin.automagy.common.tiles;

import com.mojang.authlib.GameProfile;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.entities.golems.ISuppliedBySeal;
import tuhljin.automagy.common.items.ItemRecipe;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.inventory.FakeInventoryCrafting;
import tuhljin.automagy.common.lib.inventory.FilteringItemList;
import tuhljin.automagy.common.lib.inventory.IItemMap;
import tuhljin.automagy.common.lib.inventory.IncomingItemsTracker;
import tuhljin.automagy.common.lib.inventory.InventoryForRecipe;
import tuhljin.automagy.common.lib.inventory.SizelessItem;

import javax.annotation.Nonnull;

public class TileGolemWorkbench extends ModTileEntityWithInventory implements ISuppliedBySeal {
    public static final int RESERVATIONS_IGNORE = Integer.MIN_VALUE+1;
    public static final int RESERVATIONS_NEWTASK = Integer.MIN_VALUE;
    public static final int NUM_RECIPES = 4;
    protected InventoryForRecipe[] cacheRecipeInventories = new InventoryForRecipe[4];
    protected FilteringItemList cacheContained = null;
    protected Map<Integer, FilteringItemList> reservedItems = new HashMap<>();
    protected IncomingItemsTracker supplyTracker;
    protected FakePlayer fakePlayer;
    protected static final InventoryForRecipe NullRecipeInventory = new InventoryForRecipe(ItemStack.EMPTY, "", 0, null);

    public TileGolemWorkbench() {
        super("golemWorkbench", 13);
        this.notifyOnInventoryChanged = true;
    }

    public void onInventoryChanged(int slot) {
        if (slot >= 9) {
            this.cacheRecipeInventories[slot - 9] = null;
        } else {
            this.cacheContained = null;
        }

    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        return slot < 9 || ItemRecipe.isItemPopulatedRecipe(stack, this.world);
    }

    @Override
    public boolean canInsertItem(int slot, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
        return slot < 9;
    }

    @Override
    public boolean canExtractItem(int slot, @Nonnull ItemStack stack, @Nonnull EnumFacing direction) {
        return slot < 9;
    }

    public boolean hasCraftingComponents(ItemStack stack, boolean requireExtra, int availableToTask) {
        return this.whichRecipeIndexHasCraftingComponents(stack, requireExtra, availableToTask) != null;
    }

    public Integer whichRecipeIndexHasCraftingComponents(ItemStack stack, boolean requireExtra, int availableToTask) {
        FilteringItemList contained = this.getContainedItems(availableToTask);
        if (contained.isEmpty()) {
            return null;
        } else {
            Map<Integer, IItemMap> ingredients = this.getPossibleIngredients(stack);
            if (ingredients != null) {

                for (Integer index : ingredients.keySet()) {
                    if (contained.containsAllFrom(ingredients.get(index), requireExtra ? 1 : 0)) {
                        return index;
                    }
                }
            }

            return null;
        }
    }

    public Map<Integer, IItemMap> getPossibleIngredients(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        } else {
            Map<Integer, IItemMap> maps = null;

            for(int i = 0; i < NUM_RECIPES; ++i) {
                InventoryForRecipe rec = this.getRecipeInventory(i);
                if (rec != null && TjUtil.areItemsEqualIgnoringSize(stack, rec.getCraftingResult())) {
                    if (maps == null) {
                        maps = new HashMap<>();
                    }

                    maps.put(i, rec.getIngredientItemMap());
                }
            }

            return maps;
        }
    }

    public IItemMap getMissingIngredients(ItemStack stack, boolean requireExtra) {
        FilteringItemList contained = this.getContainedItems(RESERVATIONS_NEWTASK);
        IItemMap ingredients = this.getFirstIngredients(stack);
        return ingredients == null ? null : contained.getMissingFrom(ingredients, requireExtra ? 1 : 0);
    }

    public IItemMap getFirstIngredients(ItemStack stack) {
        if (stack.isEmpty()) {
            return null;
        } else {
            for(int i = 0; i < NUM_RECIPES; ++i) {
                InventoryForRecipe rec = this.getRecipeInventory(i);
                if (rec != null && TjUtil.areItemsEqualIgnoringSize(stack, rec.getCraftingResult())) {
                    return rec.getIngredientItemMap();
                }
            }

            return null;
        }
    }

    public FilteringItemList getContainedItems(int availableToTask) {
        if (this.cacheContained == null) {
            this.cacheContained = (new FilteringItemList()).populateFromInventory(this, false, 0, 8);
        }

        if (availableToTask != RESERVATIONS_IGNORE && !this.cacheContained.isEmpty() && !this.reservedItems.isEmpty()) {
            FilteringItemList list = this.cacheContained.copy();

            for (Integer item : this.reservedItems.keySet()) {
                if (availableToTask != RESERVATIONS_NEWTASK && item == availableToTask) {
                    for (Entry<SizelessItem, Integer> subentry : this.reservedItems.get(item)) {
                        list.subtract(subentry.getKey(), subentry.getValue());
                    }
                }
            }

            return list;
        } else {
            return this.cacheContained;
        }
    }

    public void reserveItems(int taskID, int recipeIndex) {
        InventoryForRecipe rec = this.getRecipeInventory(recipeIndex);
        if (rec != null) {
            FilteringItemList list = (new FilteringItemList()).populateFromInventory(rec, false, 0, 8);
            this.reservedItems.put(taskID, list);
        }

    }

    public void releaseItemReservation(int taskID) {
        this.reservedItems.remove(taskID);

    }

    public InventoryForRecipe getRecipeInventory(int i) {
        if (this.cacheRecipeInventories[i] == null) {
            ItemStack recipeStack = this.getRecipeStack(i);
            if (recipeStack.isEmpty()) {
                return null;
            }

            InventoryForRecipe rec = ItemRecipe.getRecipeInventory(recipeStack, this.world);
            if (rec == null) {
                this.cacheRecipeInventories[i] = NullRecipeInventory;
                return null;
            }

            this.cacheRecipeInventories[i] = rec;
        } else if (this.cacheRecipeInventories[i] == NullRecipeInventory) {
            return null;
        }

        return this.cacheRecipeInventories[i];
    }

    public ItemStack consumeComponentsAndCraft(ItemStack goal, boolean requireExtra) {
        Integer recipeIndex = this.whichRecipeIndexHasCraftingComponents(goal, requireExtra, RESERVATIONS_IGNORE);
        if (recipeIndex == null) {
            return null;
        } else {
            InventoryForRecipe rec = this.getRecipeInventory(recipeIndex);
            ItemStack product = rec.getCraftingResult();
            InventoryCrafting craftMatrix = new FakeInventoryCrafting(rec);
            if (this.fakePlayer == null) {
                this.fakePlayer = FakePlayerFactory.get((WorldServer)this.world, new GameProfile(null, "FakeAutomagyCrafter"));
                this.fakePlayer.setPositionAndRotation(this.pos.getX(), this.pos.getY(), this.pos.getZ(), 0.0F, 0.0F);
            }

            NonNullList<ItemStack> aitemstack = NonNullList.create();
            try {
                FMLCommonHandler.instance().firePlayerCraftingEvent(this.fakePlayer, product, craftMatrix);
                ForgeHooks.setCraftingPlayer(this.fakePlayer);
                // TODO:
                //  aitemstack = CraftingManager.getInstance().addRecipe(craftMatrix, this.world);
                ForgeHooks.setCraftingPlayer(null);
            } catch (Exception ex) {
                Automagy.logError("Exception upon firing crafting event.");
                ex.printStackTrace();
                return null;
            }

            int slot;
            ItemStack stack;
            for(int craftSlot = 0; craftSlot < 9; ++craftSlot) {
                ItemStack ingredient = rec.getStackInSlot(craftSlot);
                if (!ingredient.isEmpty()) {
                    for(slot = 0; slot < 9; ++slot) {
                        stack = this.getStackInSlot(slot);
                        if (TjUtil.areItemsEqualIgnoringSize(ingredient, stack)) {
                            this.decrStackSize(slot, 1);
                            break;
                        }
                    }
                }
            }

            for (ItemStack astack : aitemstack) {
                if (!astack.isEmpty()) {
                    ItemStack leftover = this.insertItemIntoLastAvailableSlot(astack);
                    if (!leftover.isEmpty()) {
                        TjUtil.dropItemIntoWorld(leftover, this.world, this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D);
                    }
                }
            }

            return product.copy();
        }
    }

    @Nonnull
    public ItemStack getRecipeStack(int i) {
        return this.getStackInSlot(i + 9);
    }

    @Nonnull
    public ItemStack getRecipeGoal(int i) {
        InventoryForRecipe rec = this.getRecipeInventory(i);
        return rec == null ? ItemStack.EMPTY : rec.getCraftingResult();
    }

    @Nonnull
    public ItemStack insertItemIntoLastAvailableSlot(ItemStack stack) {
        return TjUtil.addToInventory(stack, this, this.getSizeInventory() - NUM_RECIPES - 1, 0);
    }

    public IncomingItemsTracker getSupplyTracker() {
        if (this.supplyTracker == null) {
            this.supplyTracker = new IncomingItemsTracker(this, this.getWorld(), this.getPos());
        }

        return this.supplyTracker;
    }

    public ItemStack receiveSupplyFromTracker(ItemStack stack) {
        return TjUtil.addToInventory(stack, this, 0, this.getSizeInventory() - NUM_RECIPES - 1);
    }

    public boolean hasInventorySpaceFor(ItemStack stack) {
        return TjUtil.canFitInInventory(stack, this, 0, this.getSizeInventory() - NUM_RECIPES - 1);
    }
}
