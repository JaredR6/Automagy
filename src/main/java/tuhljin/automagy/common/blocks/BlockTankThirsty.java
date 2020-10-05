package tuhljin.automagy.common.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import javax.annotation.Nullable;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileTankThirsty;

import javax.annotation.Nonnull;

public class BlockTankThirsty extends BlockFillableByBucket {
    @Nonnull
    private static AxisAlignedBB AABB_BOUNDS = new AxisAlignedBB(0.064F, 0.0F, 0.064F, 0.936F, 0.875F, 0.936F);

    @Nullable
    private FluidStack lastFluidStack = null;
    @Nullable
    private int[] lastGlyphs = null;

    public BlockTankThirsty() {
        super(Material.ROCK, true);
        this.setHardness(3.0F);
        this.setResistance(10.0F);
        this.setHarvestLevel("pickaxe", 0);
    }

    @Nonnull
    @Override
    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockTankThirsty.class;
    }

    @Override
    public TileEntity createNewTileEntity(@Nonnull World world, int meta) {
        return new TileTankThirsty();
    }

    @Nonnull
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    @Nonnull
    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public boolean hasComparatorInputOverride(IBlockState blockState) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState blockState, @Nonnull World worldIn, @Nonnull BlockPos pos) {
        return ((TileTankThirsty)worldIn.getTileEntity(pos)).getComparatorStrength();
    }

    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        try {
            TileTankThirsty te = (TileTankThirsty)worldIn.getTileEntity(pos);
            this.lastFluidStack = te.tank.getFluid();
            this.lastGlyphs = null;

            for(int i = 0; i < te.glyphs.length; ++i) {
                if (te.glyphs[i] != 0) {
                    this.lastGlyphs = te.glyphs;
                    break;
                }
            }
        } catch (Exception ex) {
            this.lastFluidStack = null;
            this.lastGlyphs = null;
        }

        super.breakBlock(worldIn, pos, state);
    }

    @Nonnull
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList();
        Random rand = world instanceof World ? ((World)world).rand : RANDOM;
        Item item = this.getItemDropped(state, rand, fortune);
        ItemStack stack = new ItemStack(item, 1, this.damageDropped(state));
        TileTankThirsty te = null;

        try {
            te = (TileTankThirsty)world.getTileEntity(pos);
        } catch (Exception ignored) {
        }

        FluidStack fluidStack = null;
        if (te != null) {
            fluidStack = te.tank.getFluid();
        } else {
            fluidStack = this.lastFluidStack;
        }

        if (fluidStack != null) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }

            fluidStack.writeToNBT(stack.getTagCompound());
        }

        this.lastFluidStack = null;
        int[] glyphs;
        if (te != null) {
            glyphs = te.glyphs;
        } else {
            glyphs = this.lastGlyphs;
        }

        if (glyphs != null) {
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }

            stack.getTagCompound().setIntArray("Glyphs", glyphs);
        }

        this.lastGlyphs = null;
        drops.add(stack);
        return drops;
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, @Nonnull EnumFacing side, float hitX, float hitY, float hitZ) {
        if (super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ)) {
            return true;
        } else {
            ItemStack heldStack = player.inventory.getCurrentItem();
            if (!heldStack.isEmpty() && heldStack.getItem() == ModItems.tankGlyph) {
                int glyph = heldStack.getItemDamage();
                if (glyph > 0) {
                    if (!world.isRemote) {
                        TileTankThirsty te = (TileTankThirsty)world.getTileEntity(pos);
                        if (te.installGlyph(glyph, side)) {
                            if (!player.capabilities.isCreativeMode) {
                                TjUtil.consumePlayerItem(player, player.inventory.currentItem);
                            }

                            world.playSound(player, pos, SoundsTC.upgrade, SoundCategory.BLOCKS, 0.5F, 1.0F);
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    @Override
    public void onBlockAdded(@Nonnull World worldIn, @Nonnull BlockPos pos, IBlockState state) {
        TileTankThirsty te = (TileTankThirsty)worldIn.getTileEntity(pos);
        if (worldIn.isBlockPowered(pos)) {
            te.receivingSignal = true;
            te.markDirty();
        }

    }

    @Override
    public void onNeighborChange(IBlockAccess access, @Nonnull BlockPos pos, BlockPos neighbor) {
        if (access instanceof World) {
            World world = (World) access;
            if (!world.isRemote) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileTankThirsty) {
                    ((TileTankThirsty) te).updateRedstoneInput(world.isBlockPowered(pos));
                }
            }
        }
    }


    @Nonnull
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABB_BOUNDS;
    }
}