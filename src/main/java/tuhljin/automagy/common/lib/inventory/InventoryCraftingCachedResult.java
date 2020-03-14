package tuhljin.automagy.common.lib.inventory;

import java.util.HashMap;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import tuhljin.automagy.common.gui.ContainerEmpty;

import javax.annotation.Nonnull;

public class InventoryCraftingCachedResult extends InventoryCrafting {
    private static int WIDTH = 3;
    private static int HEIGHT = 3;
    protected final String nbtKey;
    protected boolean hasResult;
    protected ItemStack result;
    protected World world;
    private HashMap<SizelessItem, Integer> map;

    public InventoryCraftingCachedResult(Container container, String nbtKey) {
        super(container, WIDTH, HEIGHT);
        this.hasResult = false;
        this.result = ItemStack.EMPTY;
        this.map = null;
        this.nbtKey = nbtKey;
    }

    public InventoryCraftingCachedResult(String nbtKey) {
        this(new ContainerEmpty(), nbtKey);
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int index, int count) {
        this.hasResult = false;
        this.result = null;
        this.map = null;
        return super.decrStackSize(index, count);
    }

    @Override
    public void setInventorySlotContents(int index, @Nonnull ItemStack stack) {
        this.hasResult = false;
        this.result = null;
        this.map = null;
        super.setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public ItemStack getCraftingResult() {
        if (!this.hasResult) {
            this.result = CraftingManager.findMatchingResult(this, this.world);
            this.hasResult = true;
        }

        return this.result;
    }

    public boolean containsItem(ItemStack stack) {
        if (this.map == null) {
            this.getItemMap();
        }

        return this.map.containsKey(new SizelessItem(stack));
    }

    public HashMap<SizelessItem, Integer> getItemMap() {
        if (this.map == null) {
            this.map = new HashMap<>();
            int size = this.getSizeInventory();

            for(int slot = 0; slot < size; ++slot) {
                ItemStack stack = this.getStackInSlot(slot);
                if (!stack.isEmpty()) {
                    SizelessItem item = new SizelessItem(stack);
                    if (this.map.containsKey(item)) {
                        this.map.put(item, this.map.get(item) + 1);
                    } else {
                        this.map.put(item, 1);
                    }
                }
            }
        }

        return this.map;
    }

    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getTagList(this.nbtKey, 10);
        if (nbttaglist.tagCount() > 0) {
            int numSlots = this.getSizeInventory();

            for(int i = 0; i < numSlots; ++i) {
                NBTTagCompound tagList = nbttaglist.getCompoundTagAt(i);
                byte slot = tagList.getByte("Slot");
                if (slot >= 0 && slot < numSlots && this.getStackInSlot(slot).isEmpty()) {
                    this.setInventorySlotContents(slot, new ItemStack(tagList));
                }
            }
        }

    }

    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = new NBTTagList();
        int numSlots = this.getSizeInventory();

        for(int i = 0; i < numSlots; ++i) {
            ItemStack stack = this.getStackInSlot(i);
            if (!stack.isEmpty()) {
                NBTTagCompound tagList = new NBTTagCompound();
                tagList.setByte("Slot", (byte)i);
                stack.writeToNBT(tagList);
                nbttaglist.appendTag(tagList);
            }
        }

        nbttagcompound.setTag(this.nbtKey, nbttaglist);
    }

    @Override
    public void clear() {
        super.clear();
        this.hasResult = false;
        this.result = null;
        this.map = null;
    }
}
