package tuhljin.automagy.common.lib.inventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

public class ItemHandlerUtil {
    public ItemHandlerUtil() {
    }

    public static IItemHandler getHandler(TileEntity te, EnumFacing side) {
        if (te != null) {
            if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
                return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
            }

            if (te instanceof ISidedInventory) {
                if (side == null) {
                    side = EnumFacing.DOWN;
                }

                return new SidedInvWrapper((ISidedInventory)te, side);
            }

            if (te instanceof IInventory) {
                return new InvWrapper((IInventory)te);
            }
        }

        return null;
    }

    public static boolean isValid(TileEntity te, EnumFacing side) {
        if (te != null) {
            if (te.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
                return true;
            }

            return te instanceof IInventory;
        }

        return false;
    }
}
