package tuhljin.automagy.common.lib.inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import tuhljin.automagy.common.lib.TjUtil;

public class InventoryWithFilterOptions extends InventoryForLargeItemStacks {
    public boolean useItemCount;
    public boolean ignoreNBT;
    public boolean ignoreMetadata;
    protected String nameFilter;
    protected Pattern pattern;
    protected boolean patternCompiled = false;
    public static final InventoryWithFilterOptions NULL_FILTER = new InventoryWithFilterOptions(ItemStack.EMPTY, (String)null, 0, 1);


    public InventoryWithFilterOptions(ItemStack containerStack, String inventoryName, int numSlots, int limit) {
        super(containerStack, inventoryName, numSlots, limit);
        if (this.nameFilter == null) {
            this.nameFilter = "";
        }

    }

    public void readCustomNBT(NBTTagCompound nbttagcompound) {
        super.readCustomNBT(nbttagcompound);
        this.useItemCount = false;
        this.ignoreNBT = false;
        this.ignoreMetadata = false;
        this.nameFilter = "";
        this.pattern = null;
        this.patternCompiled = false;
        NBTBase base = nbttagcompound.getTag("FilterOptions");
        if (base instanceof NBTTagCompound) {
            NBTTagCompound nbtsub = (NBTTagCompound)base;
            if (!nbtsub.hasNoTags()) {
                this.useItemCount = nbtsub.getBoolean("useItemCount");
                this.ignoreNBT = nbtsub.getBoolean("ignoreNBT");
                this.ignoreMetadata = nbtsub.getBoolean("ignoreMetadata");
                this.nameFilter = nbtsub.getString("nameFilter");
            }
        }

    }

    public void writeCustomNBT(NBTTagCompound nbttagcompound) {
        super.writeCustomNBT(nbttagcompound);
        NBTTagCompound nbtsub = new NBTTagCompound();
        if (this.useItemCount) {
            nbtsub.setBoolean("useItemCount", true);
        }

        if (this.ignoreNBT) {
            nbtsub.setBoolean("ignoreNBT", true);
        }

        if (this.ignoreMetadata) {
            nbtsub.setBoolean("ignoreMetadata", true);
        }

        this.nameFilter = this.nameFilter.trim();
        if (!this.nameFilter.isEmpty()) {
            nbtsub.setString("nameFilter", this.nameFilter);
        }

        if (!nbtsub.hasNoTags()) {
            nbttagcompound.setTag("FilterOptions", nbtsub);
        } else if (nbttagcompound.hasKey("FilterOptions")) {
            nbttagcompound.removeTag("FilterOptions");
        }

    }

    public void removeDuplicates() {
        HashMap<SizelessItem, Integer> mapAmt = new HashMap<SizelessItem, Integer>();
        HashMap<SizelessItem, Integer> mapSlot = new HashMap<SizelessItem, Integer>();

        for(int i = 0; i < this.numSlots; ++i) {
            ItemStack stack = this.inventorySlots.get(i);
            if (!stack.isEmpty()) {
                SizelessItem item = new SizelessItem(stack);
                int amt = stack.getCount();
                if (mapAmt.containsKey(item)) {
                    if (amt <= (Integer)mapAmt.get(item)) {
                        this.inventorySlots.set(i, ItemStack.EMPTY);
                    } else {
                        this.inventorySlots.set((Integer)mapSlot.get(item), ItemStack.EMPTY);
                        mapAmt.put(item, amt);
                        mapSlot.put(item, i);
                    }
                } else {
                    mapAmt.put(item, amt);
                    mapSlot.put(item, i);
                }
            }
        }

    }

    public String getNameFilter() {
        return this.nameFilter;
    }

    public void setNameFilter(String s) {
        this.nameFilter = s;
        this.pattern = null;
        this.patternCompiled = false;
    }

    public Pattern getPattern() {
        if (this.patternCompiled) {
            return this.pattern;
        } else {
            if (this.nameFilter.isEmpty()) {
                this.pattern = null;
            } else {
                this.pattern = TjUtil.getSafePatternUsingAsteriskForWildcard(this.nameFilter.toLowerCase());
            }

            this.patternCompiled = true;
            return this.pattern;
        }
    }

    public boolean nameFilterMatches(ItemStack stack) {
        Pattern p = this.getPattern();
        if (p != null) {
            String name = stack.getDisplayName().toLowerCase();
            Matcher m = p.matcher(name);
            return m.matches();
        } else {
            return false;
        }
    }

    public boolean isNull() {
        return this == NULL_FILTER;
    }
}
