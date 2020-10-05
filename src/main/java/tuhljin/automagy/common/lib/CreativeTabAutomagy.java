package tuhljin.automagy.common.lib;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.ModBlocks;

import javax.annotation.Nonnull;

public class CreativeTabAutomagy extends CreativeTabs {
    public CreativeTabAutomagy(int par1, @Nonnull String par2Str) {
        super(par1, par2Str);
    }

    @Nonnull
    @Override
    public ItemStack getTabIconItem() {
        return new ItemStack(ModBlocks.redcrystal);
    }
}