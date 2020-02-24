package tuhljin.automagy.common.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StatCollector;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystal;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalAmp;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalMerc;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileRedcrystalMerc;

import javax.annotation.Nonnull;

public class ItemBlockRedcrystal extends ItemBlock {
    public ItemBlockRedcrystal(Block block) {
        super(block);
    }

    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {
        if (!this.block.canPlaceBlockOnSide(world, pos, side)) {
            return false;
        } else {
            BlockRedcrystal blockRC = (BlockRedcrystal)this.block;
            if (blockRC instanceof BlockRedcrystalMerc) {
                side = EnumFacing.UP;
            }

            blockRC.nextTEOrientation = side;
            if (blockRC instanceof BlockRedcrystalAmp) {
                blockRC.nextTENoConnections = true;
            }

            boolean placed = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
            if (placed) {
                if (blockRC instanceof BlockRedcrystalAmp) {
                    EnumFacing connectSide = TjUtil.getSideFromEntityFacing(player);
                    if (connectSide != null) {
                        EnumFacing opposite = connectSide.func_176734_d();
                        if (side.func_176745_a() > 1 && opposite == side) {
                            connectSide = TjUtil.isEntityLookingDown(player) ? EnumFacing.DOWN : EnumFacing.UP;
                        }

                        blockRC.setConnection(world, pos, true, new EnumFacing[]{connectSide});
                    }
                } else if (this.field_150939_a instanceof BlockRedcrystalMerc && stack.func_77942_o() && stack.func_77978_p().func_74764_b("mirrorX")) {
                    TileEntity te = world.func_175625_s(pos);
                    if (te instanceof TileRedcrystalMerc) {
                        int mx = stack.func_77978_p().func_74762_e("mirrorX");
                        int my = stack.func_77978_p().func_74762_e("mirrorY");
                        int mz = stack.func_77978_p().func_74762_e("mirrorZ");
                        int mdim = stack.func_77978_p().func_74762_e("mirrorDim");
                        ((TileRedcrystalMerc)te).setMirrorLink(mdim, mx, my, mz);
                    }
                }
            }

            return placed;
        }
    }

    public EnumRarity func_77613_e(ItemStack stack) {
        return stack.func_77973_b() == Item.func_150898_a(ModBlocks.redcrystalMerc) ? EnumRarity.UNCOMMON : EnumRarity.COMMON;
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.field_72995_K && stack.func_77973_b() == Item.func_150898_a(ModBlocks.redcrystalMerc)) {
            TileEntity te = world.func_175625_s(pos);
            if (ThaumcraftExtension.tileIsMirror(te)) {
                ItemStack newStack = stack.func_77946_l();
                newStack.field_77994_a = 1;
                int dim = world.field_73011_w.func_177502_q();
                newStack.func_77983_a("mirrorX", new NBTTagInt(pos.func_177958_n()));
                newStack.func_77983_a("mirrorY", new NBTTagInt(pos.func_177956_o()));
                newStack.func_77983_a("mirrorZ", new NBTTagInt(pos.func_177952_p()));
                newStack.func_77983_a("mirrorDim", new NBTTagInt(dim));
                newStack.func_77983_a("mirrorDimName", new NBTTagString(DimensionManager.getProvider(dim).func_80007_l()));
                world.func_72908_a((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p(), "thaumcraft:jar", 1.0F, 2.0F);
                if (!player.field_71071_by.func_70441_a(newStack) && !world.field_72995_K) {
                    world.func_72838_d(new EntityItem(world, player.field_70165_t, player.field_70163_u, player.field_70161_v, newStack));
                }

                if (!player.field_71075_bZ.field_75098_d) {
                    --stack.field_77994_a;
                }

                player.field_71069_bz.func_75142_b();
            }
        }

        return super.onItemUseFirst(stack, player, world, pos, side, hitX, hitY, hitZ);
    }

    public void func_77624_a(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        if (stack.func_77942_o() && stack.func_77978_p().func_74764_b("mirrorX")) {
            int x = stack.func_77978_p().func_74762_e("mirrorX");
            int y = stack.func_77978_p().func_74762_e("mirrorY");
            int z = stack.func_77978_p().func_74762_e("mirrorZ");
            String dimName = stack.func_77978_p().func_74779_i("mirrorDimName");
            tooltip.add(StatCollector.func_74837_a("Automagy.tip.redcrystalMerc.link", new Object[]{x, y, z, dimName}));
        }

    }
}
