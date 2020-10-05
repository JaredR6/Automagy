package tuhljin.automagy.common.blocks;

import java.util.Map;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.EnumFacing.Plane;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;

public abstract class ModBlockWithFacing<T extends Enum<T> & IStringSerializable & IEnumWithMetadata> extends ModBlock {
    public PropertyDirection FACING;

    public ModBlockWithFacing(@Nonnull Material material, @Nonnull MapColor mapColor) {
        super(material, mapColor);
        this.setDefaultState(this.getDefaultState().withProperty(this.FACING, EnumFacing.NORTH));
    }

    public ModBlockWithFacing(@Nonnull Material material) {
        this(material, material.getMaterialMapColor());
    }

    // may not be necessary
    @Nullable
    @Override
    public Map<String, Integer> getVariantNamesAndMetadata() {
        return null;
    }

    @Nonnull
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing dir = EnumFacing.VALUES[meta];
        return this.getDefaultState().withProperty(this.FACING, dir);
    }

    @Override
    public int getMetaFromState(@Nonnull IBlockState state) {
        EnumFacing dir = state.getValue(this.FACING);
        return dir.getIndex();
    }

    @Nonnull
    @Override
    public IBlockState getStateForPlacement(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull EnumFacing facing, float hitX, float hitY, float hitZ, int meta, @Nonnull EntityLivingBase placer, EnumHand hand) {
        return this instanceof IBlockFacesHorizontal ? this.getDefaultState().withProperty(this.FACING, placer.getHorizontalFacing().getOpposite()) : this.getDefaultState().withProperty(this.FACING, EnumFacing.getDirectionFromEntityLiving(pos, placer));
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        if (this.FACING == null) {
            if (this instanceof IBlockFacesHorizontal) {
                this.FACING = PropertyDirection.create("facing", Plane.HORIZONTAL);
            } else {
                this.FACING = PropertyDirection.create("facing");
            }
        }

        return new BlockStateContainer(this, this.FACING);
    }

    @Nonnull
    public EnumFacing getFacing(@Nonnull IBlockAccess world, @Nonnull BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        return state.getValue(this.FACING);
    }
}
