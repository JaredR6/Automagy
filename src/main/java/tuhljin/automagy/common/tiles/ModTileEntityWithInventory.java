package tuhljin.automagy.common.tiles;

import com.sun.istack.internal.NotNull;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class ModTileEntityWithInventory extends ModTileEntity implements ISidedInventory {
    protected NonNullList<ItemStack> inventorySlots;
    protected final int numSlots;
    private String inventoryName;
    protected int[] accessibleSlots;
    protected boolean notifyOnInventoryChanged;
    protected boolean sendInvToClient;
    protected ModTileEntityWithInventory.ItemHandler itemHandler;

    public ModTileEntityWithInventory(String inventoryName, int numSlots) {
        this(inventoryName, numSlots, false, true);
    }

    public ModTileEntityWithInventory(String inventoryName, int numSlots, boolean sendInvToClient, boolean createDefaultAccessibleSlots) {
        this.notifyOnInventoryChanged = false;
        this.sendInvToClient = false;
        this.inventoryName = inventoryName;
        this.numSlots = numSlots;
        this.sendInvToClient = sendInvToClient;
        this.inventorySlots = NonNullList.withSize(numSlots, ItemStack.EMPTY);
        if (createDefaultAccessibleSlots) {
            this.accessibleSlots = new int[numSlots];

            for(int i = 0; i < numSlots; this.accessibleSlots[i] = i++) {
            }
        }

        this.itemHandler = new ModTileEntityWithInventory.ItemHandler(EnumFacing.DOWN);
    }

    public void onInventoryChanged(int slot) {
    }

    @Override
    public int getSizeInventory() {
        return this.numSlots;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : this.inventorySlots) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return this.inventorySlots.get(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int num) {
        if (!this.inventorySlots.get(slot).isEmpty()) {
            ItemStack stack;
            if (this.inventorySlots.get(slot).getCount() <= num) {
                stack = removeStackFromSlot(slot);

            } else {
                if (this.notifyOnInventoryChanged) {
                    this.inventorySlots.get(slot).copy();
                } else {
                    Object var10000 = null;
                }

                stack = this.inventorySlots.get(slot).splitStack(num);

            }
            this.markInventoryDirty();
            if (this.notifyOnInventoryChanged) {
                this.onInventoryChanged(slot);
            }
            return stack;
        } else {
            return null;
        }
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = this.getStackInSlot(slot);
        this.setInventorySlotContents(slot, ItemStack.EMPTY);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (stack != this.inventorySlots.get(slot)) {
            ItemStack var10000;
            if (this.notifyOnInventoryChanged) {
                var10000 = this.inventorySlots.get(slot);
            } else {
                var10000 = null;
            }

            this.inventorySlots.set(slot, stack);
            if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
                stack.setCount(this.getInventoryStackLimit());
            }

            this.markInventoryDirty();
            if (this.notifyOnInventoryChanged) {
                this.onInventoryChanged(slot);
            }
        }

    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return true;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        return this.accessibleSlots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
        return this.isItemValidForSlot(slot, stack);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
        return true;
    }

    @Override
    public String getName() {
        return I18n.format("tile." + this.inventoryName + ".name");
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public void readServerNBT(NBTTagCompound nbttagcompound) {
        if (!this.sendInvToClient) {
            this.readInventoryFromNBT(nbttagcompound);
        }

    }

    @Override
    public void writeServerNBT(NBTTagCompound nbttagcompound) {
        if (!this.sendInvToClient) {
            this.writeInventoryToNBT(nbttagcompound);
        }

    }

    @Override
    public void readCommonNBT(NBTTagCompound nbttagcompound) {
        if (this.sendInvToClient) {
            this.readInventoryFromNBT(nbttagcompound);
        }

    }

    @Override
    public void writeCommonNBT(NBTTagCompound nbttagcompound) {
        if (this.sendInvToClient) {
            this.writeInventoryToNBT(nbttagcompound);
        }

    }

    protected void readInventoryFromNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = nbttagcompound.getTagList("Items", 10);
        this.inventorySlots = NonNullList.withSize(this.numSlots, ItemStack.EMPTY);
        if (nbttaglist.tagCount() > 0) {
            for(int i = 0; i < this.numSlots; ++i) {
                NBTTagCompound tagList = nbttaglist.getCompoundTagAt(i);
                byte slot = tagList.getByte("Slot");
                if (slot >= 0 && slot < this.numSlots && this.inventorySlots.get(slot) == null) {
                    this.inventorySlots.set(slot, new ItemStack(tagList));
                }
            }
        }

    }

    protected void writeInventoryToNBT(NBTTagCompound nbttagcompound) {
        NBTTagList nbttaglist = new NBTTagList();

        for(int i = 0; i < this.numSlots; ++i) {
            if (this.inventorySlots.get(i) != null) {
                NBTTagCompound tagList = new NBTTagCompound();
                tagList.setByte("Slot", (byte)i);
                this.inventorySlots.get(i).writeToNBT(tagList);
                nbttaglist.appendTag(tagList);
            }
        }

        nbttagcompound.setTag("Items", nbttaglist);
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
            ItemStack var10000 = this.removeStackFromSlot(i);
            if (this.notifyOnInventoryChanged) {
                this.onInventoryChanged(i);
            }
        }

        this.markInventoryDirty();
    }

    public void markInventoryDirty() {
        this.markDirty();
        if (this.sendInvToClient) {
            this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
        }

    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? true : super.hasCapability(capability, facing);
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)this.itemHandler : super.getCapability(capability, facing);
    }

    public class ItemHandler extends ItemStackHandler {
        public final EnumFacing dir;

        protected ItemHandler(EnumFacing dir) {
            super(ModTileEntityWithInventory.this.inventorySlots.size());
            this.stacks = ModTileEntityWithInventory.this.inventorySlots;
            this.dir = dir;
        }

        @Nonnull
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            return !ModTileEntityWithInventory.this.canInsertItem(slot, stack, this.dir) ? stack : super.insertItem(slot, stack, simulate);
        }

        @Nonnull
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            return !ModTileEntityWithInventory.this.canExtractItem(slot, this.getStackInSlot(slot), this.dir) ? ItemStack.EMPTY : super.extractItem(slot, amount, simulate);
        }

        protected void onContentsChanged(int slot) {
            ModTileEntityWithInventory.this.markInventoryDirty();
            if (ModTileEntityWithInventory.this.notifyOnInventoryChanged) {
                ModTileEntityWithInventory.this.onInventoryChanged(slot);
            }

        }
    }
}
