package tuhljin.automagy.common.items;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.client.FMLClientHandler;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;

import javax.annotation.Nonnull;

public class ItemCrystalEye extends ModItem implements IAutomagyLocationLink {

    public ItemCrystalEye() {
        super(References.ITEM_CRYSTALEYE);
    }

    @Nonnull
    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
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

        return EnumActionResult.PASS;
    }

    @Override
    public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag) {
        EntityPlayer player = FMLClientHandler.instance().getClientPlayerEntity();
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("lookX")) {
            int x = stack.getTagCompound().getInteger("lookX");
            int y = stack.getTagCompound().getInteger("lookY");
            int z = stack.getTagCompound().getInteger("lookZ");
            int dim = stack.getTagCompound().getInteger("lookDim");
            String dimName = stack.getTagCompound().getString("lookDimName");
            tooltip.add(I18n.format("automagy.tip.crystalEye.link", x, y, z, dimName));
            if (world.provider.getDimension() == dim) {
                float distance = TjUtil.getDistanceBetweenPoints(MathHelper.floor(player.posX), MathHelper.floor(player.posY), MathHelper.floor(player.posZ), x, y, z);
                String s = String.format("%.2f", distance);
                s = I18n.format("automagy.tip.crystalEye.linkDistance", s);
                String name = TjUtil.getBlockNameAt(world, new BlockPos(x, y, z));
                if (name != null) {
                    s = s + "  " + TextFormatting.DARK_AQUA + "[" + name + "]";
                }

                tooltip.add(TextFormatting.DARK_GRAY + s);
            }
        }

    }

    @Override
    public boolean hasEffect(ItemStack stack) {
        return stack.hasTagCompound() && stack.getTagCompound().hasKey("lookX");
    }

    @Override
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
