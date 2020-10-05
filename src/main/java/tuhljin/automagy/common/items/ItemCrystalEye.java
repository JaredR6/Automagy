package tuhljin.automagy.common.items;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;

import javax.annotation.Nonnull;

public class ItemCrystalEye extends ModItem implements IAutomagyLocationLink {

    public ItemCrystalEye() {
        super(References.ITEM_CRYSTALEYE);
    }

    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            int dim = world.provider.getDimension();
            if (!stack.hasTagCompound()) {
                stack.setTagCompound(new NBTTagCompound());
            }

            stack.getTagCompound().setInteger("lookX", pos.getX());
            stack.getTagCompound().setInteger("lookY", pos.getY());
            stack.getTagCompound().setInteger("lookZ", pos.getZ());
            stack.getTagCompound().setInteger("lookDim", dim);
            stack.getTagCompound().setString("lookDimName", DimensionManager.getProvider(dim).getDimensionType().getName());
            world.playSound(player, pos, SoundsTC.wand, SoundCategory.BLOCKS, 1.0F, 1.0F);
        }

        return true;
    }

    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("lookX")) {
            int x = stack.getTagCompound().getInteger("lookX");
            int y = stack.getTagCompound().getInteger("lookY");
            int z = stack.getTagCompound().getInteger("lookZ");
            int dim = stack.getTagCompound().getInteger("lookDim");
            String dimName = stack.getTagCompound().getString("lookDimName");
            tooltip.add(I18n.format("automagy.tip.crystalEye.link", x, y, z, dimName));
            if (player.world.provider.getDimension() == dim) {
                float distance = TjUtil.getDistanceBetweenPoints(MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ), x, y, z);
                String s = String.format("%.2f", distance);
                s = I18n.format("automagy.tip.crystalEye.linkDistance", s);
                String name = TjUtil.getBlockNameAt(player.world, new BlockPos(x, y, z));
                if (name != null) {
                    s = s + "  " + TextFormatting.DARK_AQUA + "[" + name + "]";
                }

                tooltip.add(TextFormatting.DARK_GRAY + s);
            }
        }

    }

    public boolean hasEffect(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("lookX");
    }

    @Nonnull
    public WorldSpecificCoordinates getLinkLocation(ItemStack stack) {
        if (stack != null && stack.hasTagCompound() && stack.getTagCompound().hasKey("lookX")) {
            int x = stack.getTagCompound().getInteger("lookX");
            int y = stack.getTagCompound().getInteger("lookY");
            int z = stack.getTagCompound().getInteger("lookZ");
            int dim = stack.getTagCompound().getInteger("lookDim");
            return new WorldSpecificCoordinates(dim, x, y, z);
        } else {
            return null;
        }
    }
}
