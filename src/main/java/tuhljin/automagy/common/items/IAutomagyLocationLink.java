package tuhljin.automagy.common.items;

import net.minecraft.item.ItemStack;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;

public interface IAutomagyLocationLink {
    WorldSpecificCoordinates getLinkLocation(ItemStack stack);
}