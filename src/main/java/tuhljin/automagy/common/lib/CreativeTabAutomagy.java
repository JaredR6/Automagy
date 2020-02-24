package tuhljin.automagy.common.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.ModBlocks;

public class CreativeTabAutomagy extends CreativeTabs {
    public CreativeTabAutomagy(int par1, String par2Str) {
        super(par1, par2Str);
    }

    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.redcrystal);
    }
}