package tuhljin.automagy.common.blocks;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class BlockBookshelfEnchanted extends ModBlockMetaStates<BlockBookshelfEnchanted.BookshelfVariants> {
    public static final int ENCHANTPOWER_GREATWOOD = 4;
    public static final int ENCHANTPOWER_SILVERWOOD = 8;

    public BlockBookshelfEnchanted() {
        super(Material.WOOD);
        this.setHardness(1.5F);
        this.setSoundType(SoundType.WOOD);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, @Nonnull NonNullList<ItemStack> list) {
        list.add(new ItemStack(this, 1, BookshelfVariants.GREATWOOD.meta));
        list.add(new ItemStack(this, 1, BookshelfVariants.SILVERWOOD.meta));
    }

    @Nonnull
    @Override
    protected PropertyEnum<BlockBookshelfEnchanted.BookshelfVariants> createVariantProperty() {
        return PropertyEnum.create("type", BlockBookshelfEnchanted.BookshelfVariants.class);
    }

    @Override
    public float getEnchantPowerBonus(@Nonnull World world, @Nonnull BlockPos pos) {
        BlockBookshelfEnchanted.BookshelfVariants v = (BlockBookshelfEnchanted.BookshelfVariants)world.getBlockState(pos).getValue(this.VARIANT);
        return v == BlockBookshelfEnchanted.BookshelfVariants.SILVERWOOD ? ENCHANTPOWER_SILVERWOOD : ENCHANTPOWER_GREATWOOD;
    }

    public enum BookshelfVariants implements IStringSerializable, IEnumWithMetadata {
        GREATWOOD(0),
        SILVERWOOD(1);

        private final int meta;

        BookshelfVariants(int meta) {
            this.meta = meta;
        }

        @Nonnull
        @Override
        public String getName() {
            return this.name().toLowerCase();
        }

        @Nonnull
        @Override
        public String toString() {
            return this.getName();
        }

        @Override
        public int getMetadata() {
            return this.meta;
        }
    }
}
