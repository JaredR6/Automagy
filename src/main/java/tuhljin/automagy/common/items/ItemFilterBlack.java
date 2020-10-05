package tuhljin.automagy.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import tuhljin.automagy.common.gui.AutomagyGUIHandler;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;

public class ItemFilterBlack extends ItemFilter {

    public ItemFilterBlack() {
        super(References.ITEM_FILTER_BLACK);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(@Nonnull World world, @Nonnull EntityPlayer player, @Nonnull EnumHand hand) {
        if (!world.isRemote) {
            AutomagyGUIHandler.openGUI(3, player, world, player.getPosition());
        }

        return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }
}
