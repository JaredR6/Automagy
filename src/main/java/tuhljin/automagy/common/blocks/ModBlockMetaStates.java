package tuhljin.automagy.common.blocks;

import com.google.common.collect.Iterables;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;

public abstract class ModBlockMetaStates<T extends Enum<T> & IStringSerializable & IEnumWithMetadata> extends ModBlock {
    private Map<Integer, T> statesFromMetaEnumLookup;

    public ModBlockMetaStates(@Nonnull Material material, @Nonnull MapColor mapColor, String name) {
        super(material, mapColor, name);
        this.statesFromMetaEnumLookup = null;
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getVariant(), Iterables.get(this.getVariant().getAllowedValues(), 0)));
    }

    public ModBlockMetaStates(@Nonnull Material material, String name) {
        this(material, material.getMaterialMapColor(), name);
    }

    @Nonnull
    public Map<String, Integer> getVariantNamesAndMetadata() {
        Collection<? extends T> values = this.getVariant().getAllowedValues();
        Map<String, Integer> map = new HashMap<>();

        for (T value : values) {
            map.put(value.getName(), value.getMetadata());
        }

        return map;
    }
    
    @Override
    public int damageDropped(@Nonnull IBlockState state) {
        return this.getMetaFromState(state);
    }

    @Nonnull
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ModItemBlock.class;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (this.statesFromMetaEnumLookup == null) {
            Collection<? extends T> values = this.getVariant().getAllowedValues();
            this.statesFromMetaEnumLookup = new HashMap<>();

            for (T value : values) {
                this.statesFromMetaEnumLookup.put(((T) value).getMetadata(), (T) value);
            }
        }

        T theVariant = (T) this.statesFromMetaEnumLookup.get(meta);
        return theVariant != null ? this.getDefaultState().withProperty(this.getVariant(), theVariant) : this.getDefaultState();
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        return state.getValue(this.getVariant()).getMetadata();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, this.getVariant());
    }

    protected abstract PropertyEnum<T> getVariant();
}
