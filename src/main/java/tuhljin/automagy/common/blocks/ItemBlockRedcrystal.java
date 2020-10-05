package tuhljin.automagy.common.blocks;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystal;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalAmp;
import tuhljin.automagy.common.blocks.redcrystal.BlockRedcrystalMerc;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileRedcrystalMerc;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBlockRedcrystal extends ItemBlock {
    public ItemBlockRedcrystal(@Nonnull Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {
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
                        EnumFacing opposite = connectSide.getOpposite();
                        if (side.getIndex() > 1 && opposite == side) {
                            connectSide = TjUtil.isEntityLookingDown(player) ? EnumFacing.DOWN : EnumFacing.UP;
                        }

                        blockRC.setConnection(world, pos, true, connectSide);
                    }
                } else if (this.block instanceof BlockRedcrystalMerc && stack.hasTagCompound() && stack.getTagCompound().hasKey("mirrorX")) {
                    TileEntity te = world.getTileEntity(pos);
                    if (te instanceof TileRedcrystalMerc) {
                        int mx = stack.getTagCompound().getInteger("mirrorX");
                        int my = stack.getTagCompound().getInteger("mirrorY");
                        int mz = stack.getTagCompound().getInteger("mirrorZ");
                        int mdim = stack.getTagCompound().getInteger("mirrorDim");
                        ((TileRedcrystalMerc)te).setMirrorLink(mdim, mx, my, mz);
                    }
                }
            }

            return placed;
        }
    }

    @Nonnull
    @Override
    public EnumRarity getRarity(@Nonnull ItemStack stack) {
        return stack.getItem() == Item.getItemFromBlock(ModBlocks.redcrystalMerc) ? EnumRarity.UNCOMMON : EnumRarity.COMMON;
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUseFirst(@Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!world.isRemote && stack.getItem() == Item.getItemFromBlock(ModBlocks.redcrystalMerc)) {
            TileEntity te = world.getTileEntity(pos);
            if (ThaumcraftExtension.tileIsMirror(te)) {
                ItemStack newStack = stack.copy();
                newStack.setCount(1);
                int dim = world.provider.getDimension();
                newStack.setTagInfo("mirrorX", new NBTTagInt(pos.getX()));
                newStack.setTagInfo("mirrorY", new NBTTagInt(pos.getY()));
                newStack.setTagInfo("mirrorZ", new NBTTagInt(pos.getZ()));
                newStack.setTagInfo("mirrorDim", new NBTTagInt(dim));

                newStack.setTagInfo("mirrorDimName", new NBTTagString(DimensionManager.getProviderType(dim).getName()));
                world.playSound(null, pos, SoundsTC.jar, SoundCategory.BLOCKS, 1.0F, 2.0F);
                if (!player.inventory.addItemStackToInventory(newStack)) {
                    world.spawnEntity(new EntityItem(world, player.posX, player.posY, player.posZ, newStack));
                }

                if (!player.capabilities.isCreativeMode) {
                    stack.shrink(1);
                }

                player.inventoryContainer.detectAndSendChanges();
            }
        }

        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("mirrorX")) {
            int x = stack.getTagCompound().getInteger("mirrorX");
            int y = stack.getTagCompound().getInteger("mirrorY");
            int z = stack.getTagCompound().getInteger("mirrorZ");
            String dimName = stack.getTagCompound().getString("mirrorDimName");
            tooltip.add(I18n.format("automagy.tip.redcrystalMerc.link", x, y, z, dimName));
        }

    }
}
