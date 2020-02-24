package tuhljin.automagy.common.lib.inventory;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class SizelessItem {
    public final int damage;
    public final NBTTagCompound tagCompound;
    private final int itemID;

    public SizelessItem(ItemStack stack) {
        this.itemID = Item.getIdFromItem(stack.getItem());
        this.damage = stack.getItemDamage();
        this.tagCompound = stack.getTagCompound();
    }

    public SizelessItem(Item item, int damage, NBTTagCompound tagCompound) {
        this.itemID = Item.getIdFromItem(item);
        this.damage = damage;
        this.tagCompound = tagCompound;
    }

    public SizelessItem(SizelessItem item, boolean ignoreMetadata, boolean ignoreNBT) {
        this.itemID = item.itemID;
        this.damage = ignoreMetadata ? 0 : item.damage;
        this.tagCompound = ignoreNBT ? null : item.tagCompound;
    }

    public ItemStack getItemStack(int size) {
        ItemStack stack = new ItemStack(Item.getItemById(this.itemID), size, this.damage);
        if (this.tagCompound != null) {
            stack.setTagCompound(this.tagCompound);
        }

        return stack;
    }

    public Item getItem() {
        return Item.getItemById(this.itemID);
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + this.damage;
        result = 31 * result + this.itemID;
        result = 31 * result + (this.tagCompound == null ? 0 : this.tagCompound.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj instanceof SizelessItem) {
            SizelessItem other = (SizelessItem)obj;
            if (this.damage != other.damage) {
                return false;
            } else if (this.itemID != other.itemID) {
                return false;
            } else {
                if (this.tagCompound == null) {
                    if (other.tagCompound != null) {
                        return false;
                    }
                } else if (!this.tagCompound.equals(other.tagCompound)) {
                    return false;
                }

                return true;
            }
        } else {
            return false;
        }
    }

    public boolean isSameItem(ItemStack stack) {
        return this.isSameItem(stack, false, false);
    }

    public boolean isSameItem(ItemStack stack, boolean ignoreMetadata, boolean ignoreNBT) {
        if (stack == null || this.itemID == Item.getIdFromItem(stack.getItem())) {
            return false;
        } else {
            if (!ignoreMetadata && this.damage != stack.getItemDamage()) {
                return false;
            } else {
                if (!ignoreNBT) {
                    NBTTagCompound checkTagCompound = stack.getTagCompound();
                    if (this.tagCompound == null) {
                        return checkTagCompound == null;
                    } else {
                        return this.tagCompound.equals(checkTagCompound);
                    }
                }

                return true;
            }
        }
    }

    public boolean isSameItem(SizelessItem item, boolean ignoreMetadata, boolean ignoreNBT) {
        if (item == null || this.itemID == item.itemID) {
            return false;
        } else {
            if (!ignoreMetadata && this.damage != item.damage) {
                return false;
            } else {
                if (!ignoreNBT) {
                    NBTTagCompound checkTagCompound = item.tagCompound;
                    if (this.tagCompound == null) {
                        return checkTagCompound == null;
                    } else {
                        return this.tagCompound.equals(checkTagCompound);
                    }
                }

                return true;
            }
        }
    }
}
