package tuhljin.automagy.common.lib.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nonnull;

public class InventorySimple implements IInventory {
    protected NonNullList<ItemStack> inventorySlots;
    protected String inventoryName;
    protected int numSlots;
    protected final String nbtKey;
    protected boolean notifyOnInventoryChanged;

    public InventorySimple(String inventoryName, int numSlots, String nbtKey) {
        this.notifyOnInventoryChanged = false;
        this.inventoryName = inventoryName;
        this.numSlots = numSlots;
        this.nbtKey = nbtKey;
        this.inventorySlots = NonNullList.withSize(numSlots, ItemStack.EMPTY);
    }

    public InventorySimple(String inventoryName, int numSlots) {
        this(inventoryName, numSlots, "ContainedItems");
    }

    public void onInventoryChanged(int slot, ItemStack prevStack) {
    }

    @Override
    public int getSizeInventory() {
        return this.numSlots;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.inventorySlots) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return this.inventorySlots.get(slot);
    }

    @Override
    @Nonnull
    public ItemStack decrStackSize(int slot, int num) {
        if (!this.inventorySlots.get(slot).isEmpty()) {
            ItemStack stack;
            if (this.inventorySlots.get(slot).getCount() <= num) {
                stack = this.inventorySlots.get(slot);
                this.inventorySlots.set(slot, ItemStack.EMPTY);
                this.markDirty();
                if (this.notifyOnInventoryChanged) {
                    this.onInventoryChanged(slot, stack.copy());
                }

            } else {
                ItemStack prev = this.inventorySlots.get(slot).copy();
                stack = this.inventorySlots.get(slot).splitStack(num);
                if (this.inventorySlots.get(slot).getCount() == 0) {
                    this.inventorySlots.set(slot, ItemStack.EMPTY);
                }

                this.markDirty();
                if (this.notifyOnInventoryChanged) {
                    this.onInventoryChanged(slot, prev);
                }

            }
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @Nonnull
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = this.getStackInSlot(slot);
        this.setInventorySlotContents(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, @Nonnull ItemStack stack) {
        if (stack != this.inventorySlots.get(slot)) {
            ItemStack prev = this.notifyOnInventoryChanged ? this.inventorySlots.get(slot) : null;
            this.inventorySlots.set(slot, stack);
            if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
                stack.setCount(this.getInventoryStackLimit());
            }

            this.markDirty();
            if (this.notifyOnInventoryChanged) {
                this.onInventoryChanged(slot, prev);
            }
        }
    }

    public void setInventorySlotContentsSoftly(int slot, ItemStack stack) {
        this.inventorySlots.set(slot, stack);
    }

    @Override
    @Nonnull
    public String getName() {
        return this.inventoryName;
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TextComponentTranslation(this.getName());
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(@Nonnull EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public void closeInventory(@Nonnull EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for(int i = 0; i < this.inventorySlots.size(); ++i) {
            ItemStack prev = this.inventorySlots.get(i);
            this.inventorySlots.set(i, ItemStack.EMPTY);
            if (this.notifyOnInventoryChanged) {
                this.onInventoryChanged(i, prev);
            }
        }

        this.markDirty();
    }

    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getTagList(this.nbtKey, 10);
        this.inventorySlots = NonNullList.withSize(this.numSlots, ItemStack.EMPTY);
        if (nbttaglist.tagCount() > 0) {
            for(int i = 0; i < this.numSlots; ++i) {
                NBTTagCompound tagList = nbttaglist.getCompoundTagAt(i);
                byte slot = tagList.getByte("Slot");
                if (slot >= 0 && slot < this.numSlots && !this.inventorySlots.get(slot).isEmpty()) {
                    this.inventorySlots.set(slot, this.loadItemStack(tagList));
                }
            }
        }

    }

    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.numSlots; ++i) {
            if (!this.inventorySlots.get(i).isEmpty()) {
                NBTTagCompound tagList = new NBTTagCompound();
                tagList.setByte("Slot", (byte)i);
                this.writeItemStack(this.inventorySlots.get(i), tagList);
                nbttaglist.appendTag(tagList);
            }
        }

        nbttagcompound.setTag(this.nbtKey, nbttaglist);
    }

    protected ItemStack loadItemStack(NBTTagCompound nbt) {
        return new ItemStack(nbt);
    }

    protected void writeItemStack(ItemStack stack, NBTTagCompound nbt) {
        stack.writeToNBT(nbt);
    }
}
