package tuhljin.automagy.common.items;

import java.util.Locale;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;

public class ItemGlyph extends ModItem {
    public static final PropertyEnum<EnumGlyph> GlyphProperty = PropertyEnum.create("glyph", EnumGlyph.class);

    public ItemGlyph() {
        super(References.ITEM_TANKGLYPH);
        setHasSubtypes(true);
    }

    @Nonnull
    public String getLocalizedGlyphName(int id) {
        return I18n.format("item.automagy.tankGlyph." + EnumGlyph.getNameById(id) + ".name");
    }

    @Nonnull
    public static TextureAtlasSprite getGlyphTexture(int id) {
        String sprite = "automagy:items/glyph-overlay/" + EnumGlyph.getNameById(id);
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(sprite);
    }

    @Override
    public void getSubItems(@Nonnull CreativeTabs tab, @Nonnull NonNullList<ItemStack> stacks) {
        if (isInCreativeTab(tab)) {
            for (EnumGlyph glyph : EnumGlyph.values()) {
                stacks.add(new ItemStack(this, 1, glyph.ordinal()));
            }
        }
    }

    @Nonnull
    @Override
    public String getTranslationKey(ItemStack stack) {
        return super.getTranslationKey(stack) + EnumGlyph.getNameById(stack.getItemDamage());
    }
    
    public enum EnumGlyph implements IStringSerializable {
        INK,
        VOID,
        CONSUMPTION,
        SIPHONING,
        ENVY,
        TEMPERANCE,
        PRESERVATION,
        GUZZLER,
        RESERVOIR,
        BOVINE;

        @Override
        public String getName() {
            return name().toLowerCase(Locale.ROOT);
        }

        public static String getNameById(int id) { return values()[id].getName(); }

    }
}
