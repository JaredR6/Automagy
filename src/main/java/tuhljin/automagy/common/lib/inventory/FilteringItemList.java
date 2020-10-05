package tuhljin.automagy.common.lib.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.IItemHandler;
import javax.annotation.Nullable;
import tuhljin.automagy.common.lib.TjUtil;

import javax.annotation.Nonnull;

public class FilteringItemList implements IItemMap {
    public static final int CHECK_EXTRACT_NONE = 0;
    public static final int CHECK_EXTRACT_FULL = 1;
    public static final int CHECK_EXTRACT_ANY = 2;
    protected Map<SizelessItem, Integer> map;
    @Nullable
    protected Map<SizelessItem, Integer> cacheNoMetadata;
    @Nullable
    protected Map<SizelessItem, Integer> cacheNoNBT;
    @Nullable
    protected Map<SizelessItem, Integer> cacheNoMetadataOrNBT;

    public FilteringItemList() {
        this.cacheNoMetadata = null;
        this.cacheNoNBT = null;
        this.cacheNoMetadataOrNBT = null;
        this.map = this.createNewMap();
    }

    public FilteringItemList(@Nonnull List<ItemStack> list, boolean allowBelowOne) {
        this();

        for (ItemStack stack : list) {
            if (allowBelowOne || stack.getCount() > 0) {
                this.map.put(new SizelessItem(stack), stack.getCount());
            }
        }
    }

    public FilteringItemList(@Nonnull FilteringItemList copySource) {
        this.cacheNoMetadata = null;
        this.cacheNoNBT = null;
        this.cacheNoMetadataOrNBT = null;
        this.map = new HashMap<>(copySource.map);
    }

    @Nonnull
    public FilteringItemList populateFromInventory(@Nonnull IInventory inv, boolean checkExtract) {
        return this.populateFromInventory(inv, null, checkExtract, 0, inv.getSizeInventory() - 1);
    }

    @Nonnull
    public FilteringItemList populateFromInventory(IInventory inv, boolean checkExtract, int firstSlot, int lastSlot) {
        return this.populateFromInventory(inv, null, checkExtract, firstSlot, lastSlot);
    }

    @Nonnull
    public FilteringItemList populateFromInventory(IInventory inv, EnumFacing side, boolean checkExtract, int firstSlot, int lastSlot) {
        this.cacheNoMetadata = null;
        this.cacheNoNBT = null;
        this.cacheNoMetadataOrNBT = null;
        this.map.clear();
        this.popFromInv(inv, side, checkExtract, firstSlot, lastSlot);
        return this;
    }

    @Nonnull
    public FilteringItemList populateFromInventories(@Nonnull List<IInventory> invs) {
        this.cacheNoMetadata = null;
        this.cacheNoNBT = null;
        this.cacheNoMetadataOrNBT = null;
        this.map.clear();

        for (IInventory inv : invs) {
            this.popFromInv(inv, null, false, 0, inv.getSizeInventory() - 1);
        }

        return this;
    }

    @Nonnull
    public FilteringItemList populateFromInventory(@Nonnull IItemHandler handler) {
        return this.populateFromInventory(handler, 0);
    }

    @Nonnull
    public FilteringItemList populateFromInventory(@Nonnull IItemHandler handler, int checkExtract) {
        this.cacheNoMetadata = null;
        this.cacheNoNBT = null;
        this.cacheNoMetadataOrNBT = null;
        this.map.clear();
        this.popFromInv(handler, checkExtract);
        return this;
    }

    private void popFromInv(IInventory inv, @Nullable EnumFacing side, boolean checkExtract, int firstSlot, int lastSlot) {
        ISidedInventory sidedInv = null;
        if (side != null && inv instanceof ISidedInventory) {
            sidedInv = (ISidedInventory)inv;
        }

        int[] slots;
        if (sidedInv == null) {
            slots = new int[inv.getSizeInventory()];
        } else {
            slots = sidedInv.getSlotsForFace(side);
        }

        for (int slotNum : slots) {
            if (slotNum >= firstSlot && slotNum <= lastSlot) {
                ItemStack stack = inv.getStackInSlot(slotNum);
                if (!stack.isEmpty() && (sidedInv == null || !checkExtract || sidedInv.canExtractItem(slotNum, stack, side))) {
                    SizelessItem item = new SizelessItem(stack);
                    if (this.map.containsKey(item)) {
                        this.map.put(item, TjUtil.overflowSafeAddInt(this.map.get(item), stack.getCount()));
                    } else {
                        this.map.put(item, stack.getCount());
                    }
                }
            }
        }

    }

    private void popFromInv(@Nonnull IItemHandler inv, int checkExtract) {
        int num = inv.getSlots();

        for(int i = 0; i < num; ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && this.checkExtract(inv, i, checkExtract)) {
                SizelessItem item = new SizelessItem(stack);
                if (this.map.containsKey(item)) {
                    this.map.put(item, TjUtil.overflowSafeAddInt(this.map.get(item), stack.getCount()));
                } else {
                    this.map.put(item, stack.getCount());
                }
            }
        }

    }

    private boolean checkExtract(@Nonnull IItemHandler inv, int slot, int mode) {
        if (mode == CHECK_EXTRACT_NONE) {
            return true;
        } else if (mode == CHECK_EXTRACT_FULL) {
            return TjUtil.canFullyExtract(inv, slot);
        } else {
            return !inv.extractItem(slot, 1, true).isEmpty();
        }
    }

    public int get(SizelessItem item) {
        Integer c = this.map.get(item);
        return c == null ? 0 : c;
    }

    public void set(@Nonnull SizelessItem item, int count) {
        this.set(item, count, false);
    }

    public void setZero(@Nonnull SizelessItem item) {
        this.set(item, 0, true);
    }

    public void add(@Nonnull SizelessItem item, int amount) {
        int count = this.get(item);
        this.set(item, TjUtil.overflowSafeAddInt(count, amount));
    }

    public void add(@Nonnull ItemStack stack) {
        this.add(new SizelessItem(stack), stack.getCount());
    }

    public void addAll(@Nonnull IItemMap items) {
        for (Entry<SizelessItem, Integer> entry : items) {
            this.add(entry.getKey(), entry.getValue());
        }

    }

    public void subtract(@Nonnull SizelessItem item, int amount) {
        int count = this.get(item);
        this.set(item, TjUtil.overflowSafeSubtractInt(count, amount));
    }

    public void subtract(@Nonnull ItemStack stack) {
        SizelessItem item = new SizelessItem(stack);
        this.subtract(item, stack.getCount());
    }

    public boolean containsKey(SizelessItem item) {
        return this.map.containsKey(item);
    }

    @Nonnull
    public Iterator<Entry<SizelessItem, Integer>> iterator() {
        return this.map.entrySet().iterator();
    }

    public void set(@Nonnull SizelessItem item, int count, boolean allowZero) {
        count = Math.max(count, 0);
        if (count == 0 && allowZero && !this.map.containsKey(item)) {
            this.map.put(item, 0);
        } else {
            int diff = count - this.get(item);
            if (diff != 0) {
                if (count > 0) {
                    this.map.put(item, count);
                } else if (this.map.containsKey(item)) {
                    if (allowZero) {
                        this.map.put(item, 0);
                    } else {
                        this.map.remove(item);
                    }
                }

                SizelessItem item2;
                Integer c;
                int val;
                if (this.cacheNoMetadata != null) {
                    item2 = new SizelessItem(item, true, false);
                    c = this.cacheNoMetadata.get(item2);
                    val = TjUtil.overflowSafeAddInt(c == null ? 0 : c, diff);
                    if (val > 0) {
                        this.cacheNoMetadata.put(item2, val);
                    } else if (this.cacheNoMetadata.containsKey(item2)) {
                        if (allowZero) {
                            this.cacheNoMetadata.put(item2, 0);
                        } else {
                            this.cacheNoMetadata.remove(item2);
                        }
                    }
                }

                if (this.cacheNoNBT != null) {
                    item2 = new SizelessItem(item, false, true);
                    c = this.cacheNoNBT.get(item2);
                    val = TjUtil.overflowSafeAddInt(c == null ? 0 : c, diff);
                    if (val > 0) {
                        this.cacheNoNBT.put(item2, val);
                    } else if (this.cacheNoNBT.containsKey(item2)) {
                        if (allowZero) {
                            this.cacheNoNBT.put(item2, 0);
                        } else {
                            this.cacheNoNBT.remove(item2);
                        }
                    }
                }

                if (this.cacheNoMetadataOrNBT != null) {
                    item2 = new SizelessItem(item, true, true);
                    c = this.cacheNoMetadataOrNBT.get(item2);
                    val = TjUtil.overflowSafeAddInt(c == null ? 0 : c, diff);
                    if (val > 0) {
                        this.cacheNoMetadataOrNBT.put(item2, val);
                    } else if (this.cacheNoMetadataOrNBT.containsKey(item2)) {
                        if (allowZero) {
                            this.cacheNoMetadataOrNBT.put(item2, 0);
                        } else {
                            this.cacheNoMetadataOrNBT.remove(item2);
                        }
                    }
                }

            }
        }
    }

    public int get(@Nonnull SizelessItem item, boolean ignoreMetadata, boolean ignoreNBT) {
        Map<SizelessItem, Integer> ref;
        if (ignoreMetadata) {
            if (ignoreNBT) {
                if (this.cacheNoMetadataOrNBT == null) {
                    this.generateCache(true, true);
                }

                ref = this.cacheNoMetadataOrNBT;
            } else {
                if (this.cacheNoMetadata == null) {
                    this.generateCache(true, false);
                }

                ref = this.cacheNoMetadata;
            }
        } else if (ignoreNBT) {
            if (this.cacheNoNBT == null) {
                this.generateCache(false, true);
            }

            ref = this.cacheNoNBT;
        } else {
            ref = this.map;
        }

        Integer c = ref.get(new SizelessItem(item, ignoreMetadata, ignoreNBT));
        return c == null ? 0 : c;
    }

    public int size() {
        return this.map.size();
    }

    public void clearCache() {
        this.cacheNoMetadata = null;
        this.cacheNoNBT = null;
        this.cacheNoMetadataOrNBT = null;
    }

    @Nonnull
    public FilteringItemList getDifferences(@Nonnull FilteringItemList previousList) {
        FilteringItemList diff = new FilteringItemList();

        for (Entry<SizelessItem, Integer> entry : this.map.entrySet()) {
            SizelessItem key = entry.getKey();
            if (previousList.map.containsKey(key)) {
                int oldValue = previousList.map.get(key);
                int newValue = entry.getValue();
                if (newValue > oldValue) {
                    diff.map.put(key, newValue - oldValue);
                }
            } else {
                diff.map.put(key, entry.getValue());
            }
        }

        return diff;
    }

    protected boolean containsAllFrom(@Nonnull IItemMap other, int additionalRequired, @Nullable FilteringItemList missing) {

        for (Entry<SizelessItem, Integer> otherEntry : other) {
            SizelessItem item = otherEntry.getKey();
            int numHere = this.get(item);
            int numThere = otherEntry.getValue();
            if (numHere < numThere + additionalRequired) {
                if (missing == null) {
                    return false;
                }

                missing.add(item, numThere + additionalRequired - numHere);
            }
        }

        return missing == null || missing.isEmpty();
    }

    public boolean containsAllFrom(@Nonnull IItemMap other, int additionalRequired) {
        return this.containsAllFrom(other, additionalRequired, null);
    }

    @Nonnull
    public FilteringItemList getMissingFrom(@Nonnull IItemMap other, int additionalRequired) {
        FilteringItemList missing = new FilteringItemList();
        this.containsAllFrom(other, additionalRequired, missing);
        return missing;
    }

    protected void generateCache(boolean ignoreMetadata, boolean ignoreNBT) {
        Map<SizelessItem, Integer> cache = this.createNewMap();

        for (Entry<SizelessItem, Integer> entry : this.map.entrySet()) {
            SizelessItem item = new SizelessItem(entry.getKey(), ignoreMetadata, ignoreNBT);
            Integer c = cache.get(item);
            cache.put(item, TjUtil.overflowSafeAddInt(c == null ? 0 : c, entry.getValue()));
        }

        if (ignoreMetadata) {
            if (ignoreNBT) {
                this.cacheNoMetadataOrNBT = cache;
            } else {
                this.cacheNoMetadata = cache;
            }
        } else if (ignoreNBT) {
            this.cacheNoNBT = cache;
        }

    }

    @Nonnull
    protected Map<SizelessItem, Integer> createNewMap() {
        return new HashMap<>();
    }

    @Nonnull
    public List<ItemStack> getItemStacks() {
        List<ItemStack> stacks = new ArrayList<>();

        for (Entry<SizelessItem, Integer> entry : this.map.entrySet()) {
            stacks.add(entry.getKey().getItemStack(entry.getValue()));
        }

        return stacks;
    }

    @Nonnull
    public FilteringItemList copy() {
        return new FilteringItemList(this);
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }
}
