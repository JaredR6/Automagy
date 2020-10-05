package tuhljin.automagy.common.blocks;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;

public class ModBlock extends Block {
    public ModBlock(@Nonnull Material material, @Nonnull MapColor mapColor) {
        super(material, mapColor);
    }

    public ModBlock(@Nonnull Material material) {
        this(material, material.getMaterialMapColor());
    }

    @Nullable
    public Map<String, Integer> getVariantNamesAndMetadata() {
        return null;
    }

    @Nullable
    public Class<? extends ItemBlock> getItemBlockClass() {
        return null;
    }
}
