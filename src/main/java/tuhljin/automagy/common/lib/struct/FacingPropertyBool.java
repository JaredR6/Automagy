package tuhljin.automagy.common.lib.struct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import javax.annotation.Nonnull;

public class FacingPropertyBool {
    public final String name;
    private Map<EnumFacing, PropertyBool> map;

    public FacingPropertyBool(String name) {
        this.name = name;
        this.map = new EnumMap<>(EnumFacing.class);
        this.map.put(EnumFacing.DOWN, PropertyBool.create(name + "d"));
        this.map.put(EnumFacing.UP, PropertyBool.create(name + "u"));
        this.map.put(EnumFacing.NORTH, PropertyBool.create(name + "n"));
        this.map.put(EnumFacing.SOUTH, PropertyBool.create(name + "s"));
        this.map.put(EnumFacing.WEST, PropertyBool.create(name + "w"));
        this.map.put(EnumFacing.EAST, PropertyBool.create(name + "e"));
    }

    public PropertyBool get(EnumFacing dir) {
        return this.map.get(dir);
    }

    public IBlockState addToState(IBlockState state, EnumFacing... isTrue) {
        List<EnumFacing> list = Arrays.asList(isTrue);

        for (EnumFacing dir : EnumFacing.VALUES) {
            state = state.withProperty(this.get(dir), list.contains(dir));
        }

        return state;
    }

    public IBlockState addToState(IBlockState state, @Nonnull boolean[] dirs) {
        assert dirs.length == 6;

        for(int i = 0; i < 6; ++i) {
            EnumFacing dir = EnumFacing.VALUES[i];
            state = state.withProperty(this.get(dir), dirs[i]);
        }

        return state;
    }

    @Nonnull
    public List<IProperty> getProperties() {
        return new ArrayList<>(this.map.values());
    }
}
