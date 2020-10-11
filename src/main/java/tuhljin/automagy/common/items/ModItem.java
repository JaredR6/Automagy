package tuhljin.automagy.common.items;

import net.minecraft.item.Item;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.References;

public class ModItem extends Item {
    public ModItem(String registryName) {
        setTranslationKey(registryName);
        setRegistryName(References.MOD_ID, registryName);
        setCreativeTab(Automagy.creativeTab);
    }
}
