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

import javax.annotation.Nonnull;

public abstract class ModBlockMetaStates<T extends Enum<T> & IStringSerializable & IEnumWithMetadata> extends ModBlock {
    public final PropertyEnum<T> VARIANT;
    private Map<Integer, T> statesFromMetaEnumLookup;
    private static PropertyEnum lastPropertyFromConstructor;

    public ModBlockMetaStates(Material material, MapColor mapColor) {
        super(material, mapColor);
        this.statesFromMetaEnumLookup = null;
        this.VARIANT = lastPropertyFromConstructor;
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.VARIANT, Iterables.get(this.VARIANT.getAllowedValues(), 0)));
    }

    public ModBlockMetaStates(Material material) {
        this(material, material.getMaterialMapColor());
    }

    public Map<String, Integer> getVariantNamesAndMetadata() {
        Collection<? extends T> values = this.VARIANT.getAllowedValues();
        Map<String, Integer> map = new HashMap<>();

        for (T value : values) {
            map.put(value.getName(), value.getMetadata());
        }

        return map;
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

    public Class<? extends ItemBlock> getItemBlockClass() {
        return ModItemBlock.class;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        if (this.statesFromMetaEnumLookup == null) {
            Collection<? extends T> values = this.VARIANT.getAllowedValues();
            this.statesFromMetaEnumLookup = new HashMap<>();

            for (T value : values) {
                this.statesFromMetaEnumLookup.put(((T) value).getMetadata(), (T) value);
            }
        }

        T theVariant = (T) this.statesFromMetaEnumLookup.get(meta);
        return theVariant != null ? this.getDefaultState().withProperty(this.VARIANT, theVariant) : this.getDefaultState();
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(this.VARIANT).getMetadata();
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (this.VARIANT == null) {
            lastPropertyFromConstructor = this.createVariantProperty();
            return new BlockStateContainer(this, lastPropertyFromConstructor);
        } else {
            return new BlockStateContainer(this, this.VARIANT);
        }
    }

    protected abstract PropertyEnum<T> createVariantProperty();
}
