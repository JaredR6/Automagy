package tuhljin.automagy.common.lib.inventory;

import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.item.ItemStack;

public interface IItemMap extends Iterable<Entry<SizelessItem, Integer>> {
    Iterator<Entry<SizelessItem, Integer>> iterator();

    boolean containsKey(SizelessItem item);

    int get(SizelessItem item);

    void set(SizelessItem item, int count);

    List<ItemStack> getItemStacks();

    IItemMap copy();

    boolean isEmpty();
}