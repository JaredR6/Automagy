package tuhljin.automagy.common.blocks;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;

public class ModBlock extends Block {
    public ModBlock(Material material, MapColor mapColor) {
        super(material, mapColor);
    }

    public ModBlock(Material material) {
        this(material, material.getMaterialMapColor());
    }

    public Map<String, Integer> getVariantNamesAndMetadata() {
        return null;
    }

    public Class<? extends ItemBlock> getItemBlockClass() {
        return null;
    }
}
