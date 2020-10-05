package tuhljin.automagy.common.tiles;

import tuhljin.automagy.common.lib.inventory.FilteringItemList;

import javax.annotation.Nonnull;

public interface IHasItemList {
    @Nonnull
    FilteringItemList getItemList();
}