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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileTankThirsty;

public class BlockTankThirsty extends BlockFillableByBucket {
    private FluidStack lastFluidStack = null;
    private int[] lastGlyphs = null;

    public BlockTankThirsty() {
        super(Material.field_151576_e, true);
        this.func_149711_c(3.0F);
        this.func_149752_b(10.0F);
        this.setHarvestLevel("pickaxe", 0);
        this.func_149676_a(0.064F, 0.0F, 0.064F, 0.936F, 0.875F, 0.936F);
    }

    public Class<? extends ItemBlock> getItemBlockClass() {
        return ItemBlockTankThirsty.class;
    }

    public TileEntity func_149915_a(World worldIn, int meta) {
        return new TileTankThirsty();
    }

    public int func_149645_b() {
        return 3;
    }

    @SideOnly(Side.CLIENT)
    public EnumWorldBlockLayer func_180664_k() {
        return EnumWorldBlockLayer.TRANSLUCENT;
    }

    public boolean func_149740_M() {
        return true;
    }

    public int func_180641_l(World worldIn, BlockPos pos) {
        return ((TileTankThirsty)worldIn.func_175625_s(pos)).getComparatorStrength();
    }

    public void func_180663_b(World worldIn, BlockPos pos, IBlockState state) {
        TileTankThirsty te = null;

        try {
            te = (TileTankThirsty)worldIn.func_175625_s(pos);
            this.lastFluidStack = te.tank.getFluid();
            this.lastGlyphs = null;

            for(int i = 0; i < te.glyphs.length; ++i) {
                if (te.glyphs[i] != 0) {
                    this.lastGlyphs = te.glyphs;
                    break;
                }
            }
        } catch (Exception var6) {
            this.lastFluidStack = null;
            this.lastGlyphs = null;
        }

        super.func_180663_b(worldIn, pos, state);
    }

    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList();
        Random rand = world instanceof World ? ((World)world).field_73012_v : RANDOM;
        Item item = this.func_180660_a(state, rand, fortune);
        ItemStack stack = new ItemStack(item, 1, this.func_180651_a(state));
        TileTankThirsty te = null;

        try {
            te = (TileTankThirsty)world.func_175625_s(pos);
        } catch (Exception var12) {
        }

        FluidStack fluidStack = null;
        if (te != null) {
            fluidStack = te.tank.getFluid();
        } else {
            fluidStack = this.lastFluidStack;
        }

        if (fluidStack != null) {
            if (!stack.func_77942_o()) {
                stack.func_77982_d(new NBTTagCompound());
            }

            fluidStack.writeToNBT(stack.func_77978_p());
        }

        this.lastFluidStack = null;
        int[] glyphs = null;
        int[] glyphs;
        if (te != null) {
            glyphs = te.glyphs;
        } else {
            glyphs = this.lastGlyphs;
        }

        if (glyphs != null) {
            if (!stack.func_77942_o()) {
                stack.func_77982_d(new NBTTagCompound());
            }

            stack.func_77978_p().func_74783_a("Glyphs", glyphs);
        }

        this.lastGlyphs = null;
        drops.add(stack);
        return drops;
    }

    public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (super.func_180639_a(world, pos, state, player, side, hitX, hitY, hitZ)) {
            return true;
        } else {
            ItemStack heldStack = player.field_71071_by.func_70448_g();
            if (heldStack != null && heldStack.func_77973_b() == ModItems.tankGlyph) {
                int glyph = heldStack.func_77952_i();
                if (glyph > 0) {
                    if (!world.field_72995_K) {
                        TileTankThirsty te = (TileTankThirsty)world.func_175625_s(pos);
                        if (te.installGlyph(glyph, side)) {
                            if (!player.field_71075_bZ.field_75098_d) {
                                TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c);
                            }

                            world.func_72956_a(player, "thaumcraft:upgrade", 0.5F, 1.0F);
                        }
                    }

                    return true;
                }
            }

            return false;
        }
    }

    public void func_176213_c(World worldIn, BlockPos pos, IBlockState state) {
        TileTankThirsty te = (TileTankThirsty)worldIn.func_175625_s(pos);
        if (worldIn.func_175640_z(pos)) {
            te.receivingSignal = true;
            te.func_70296_d();
        }

    }

    public void func_176204_a(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.field_72995_K) {
            TileEntity te = worldIn.func_175625_s(pos);
            if (te instanceof TileTankThirsty) {
                ((TileTankThirsty)te).updateRedstoneInput(worldIn.func_175640_z(pos));
            }
        }

    }
}