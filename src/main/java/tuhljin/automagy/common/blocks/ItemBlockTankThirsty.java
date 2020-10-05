package tuhljin.automagy.common.blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.tiles.ModTileEntity;
import tuhljin.automagy.common.tiles.TileTankThirsty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemBlockTankThirsty extends ItemBlock {
    public static final String GLYPH_KEY = "Glyphs";

    public ItemBlockTankThirsty(@Nonnull Block block) {
        super(block);
    }

    @Override
    public boolean placeBlockAt(@Nonnull ItemStack stack, @Nonnull EntityPlayer player, @Nonnull World world, @Nonnull BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, @Nonnull IBlockState newState) {
        boolean placed = super.placeBlockAt(stack, player, world, pos, side, hitX, hitY, hitZ, newState);
        if (placed && stack.hasTagCompound()) {
            FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
            if (fluidStack != null) {
                TileTankThirsty te = (TileTankThirsty)world.getTileEntity(pos);
                te.setFluid(fluidStack);
            }

            if (stack.getTagCompound().hasKey("Glyphs")) {
                int[] glyphs = ModTileEntity.getIntArrayFromNbtOrDefault(stack.getTagCompound(), "Glyphs", 0, 6);
                TileTankThirsty te = (TileTankThirsty)world.getTileEntity(pos);
                te.setGlyphs(glyphs);
            }
        }

        return placed;
    }

    @Override
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip, @Nonnull ITooltipFlag flagIn) {
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("Glyphs")) {
                int[] glyphs = ModTileEntity.getIntArrayFromNbtOrDefault(stack.getTagCompound(), "Glyphs", 0, 6);
                Map<Integer, Integer> glyphCount = new HashMap<>();


                for (int id : glyphs) {
                    if (id > 0) {
                        Integer c = glyphCount.get(id);
                        glyphCount.put(id, (c == null ? 0 : c) + 1);
                    }
                }

                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
                if (fluidStack != null) {
                    Integer capacity = glyphCount.get(8);
                    capacity = 16 + (capacity == null ? 0 : capacity) * 32;
                    tooltip.add(I18n.format("automagy.tip.tankFluidData", fluidStack.amount, 1000 * capacity, fluidStack.getLocalizedName()));
                }
                /*
                String s;
                for (Entry<Integer, Integer> entry : glyphCount.entrySet()) {
                    s = ModItems.tankGlyph.getLocalizedGlyphName(entry.getKey());
                    int c = entry.getValue();
                    if (c > 1) {
                        s = s + EnumChatFormatting.DARK_GRAY + " (" + c + ")";
                    }
                }*/
            } else {
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(stack.getTagCompound());
                if (fluidStack != null) {
                    tooltip.add(I18n.format("automagy.tip.tankFluidData", fluidStack.amount, 16000, fluidStack.getLocalizedName()));
                }
            }
        }

    }
}
