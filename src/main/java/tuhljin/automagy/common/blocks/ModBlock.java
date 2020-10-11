package tuhljin.automagy.common.blocks;

import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nullable;

import javax.annotation.Nonnull;

public class ModBlock extends Block {
    public ModBlock(@Nonnull Material material, @Nonnull MapColor mapColor, String name) {
        super(material, mapColor);
        this.setTranslationKey(name);
        this.setRegistryName(new ResourceLocation(References.MOD_ID, name));
    }

    public ModBlock(@Nonnull Material material, String name) {
        this(material, material.getMaterialMapColor(), name);
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
