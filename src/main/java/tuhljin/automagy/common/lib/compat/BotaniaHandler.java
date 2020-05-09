package tuhljin.automagy.common.lib.compat;

import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.item.IPetalApothecary;

public class BotaniaHandler {
    public BotaniaHandler() {
    }

    public static boolean fillBotaniaPetalApothecary(TileEntity te, boolean doFill) {
        if (te instanceof IPetalApothecary) {
            IPetalApothecary ipa = (IPetalApothecary)te;
            if (!ipa.hasWater()) {
                if (doFill) {
                    ipa.setWater(true);
                }

                return true;
            }
        }

        return false;
    }

    public static boolean looniumBlacklist(Item item) {
        try {
            BotaniaAPI.blackListItemFromLoonium(item);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}
