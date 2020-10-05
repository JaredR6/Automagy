package tuhljin.automagy.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class ModItemBlock extends ItemBlock {
    public ModItemBlock(@Nonnull Block block) {
        super(block);
        this.setMaxDamage(0);
        this.setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(@Nonnull ItemStack stack) {
        return super.getUnlocalizedName() + "." + stack.getUnlocalizedName();
    }
}
