package tuhljin.automagy.common.lib.compat.waila;

import java.util.List;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.tiles.TileTankThirsty;

import javax.annotation.Nonnull;

public class ProviderForThirstyTank extends ProviderForTank {
    public ProviderForThirstyTank() {
    }

    @Nonnull
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        TileEntity te = accessor.getTileEntity();
        if (!(te instanceof TileTankThirsty)) {
            return super.getWailaBody(itemStack, currenttip, accessor, config);
        } else {
            currenttip = super.getWailaBody(itemStack, currenttip, accessor, config);
            int side = accessor.getSide().getIndex();
            int glyph = ((TileTankThirsty)te).getGlyph(EnumFacing.fromAngle(side));
            if (glyph > 0) {
                String s = ModItems.tankGlyph.getLocalizedGlyphName(glyph);
                currenttip.add(TextFormatting.AQUA + s);
            }

            return currenttip;
        }
    }
}
