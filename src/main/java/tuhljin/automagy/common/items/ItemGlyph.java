package tuhljin.automagy.common.items;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;

import javax.annotation.Nonnull;

public class ItemGlyph extends ModVariantItem {
    public static final int VOID = 1;
    public static final int CONSUMPTION = 2;
    public static final int SIPHONING = 3;
    public static final int ENVY = 4;
    public static final int TEMPERANCE = 5;
    public static final int PRESERVATION = 6;
    public static final int GUZZLER = 7;
    public static final int RESERVOIR = 8;
    public static final int BOVINE = 9;
    private static String[] sprites = {"automagy:items/glyph-overlay/void", "automagy:items/glyph-overlay/consume", "automagy:items/glyph-overlay/siphon", "automagy:items/glyph-overlay/envy", "automagy:items/glyph-overlay/temperance", "automagy:items/glyph-overlay/preserve", "automagy:items/glyph-overlay/guzzler", "automagy:items/glyph-overlay/reservoir", "automagy:items/glyph-overlay/bovine"};

    public ItemGlyph() {
        super(ImmutableMap.<Integer, String>builder()
                .put(0, "ink")
                .put(1, "v")
                .put(2, "c")
                .put(3, "s")
                .put(4, "e")
                .put(5, "t")
                .put(6, "p")
                .put(7, "g")
                .put(8, "r")
                .put(9, "b")
                .build());
    }

    @Nonnull
    public Map<Integer, String> getVariantMetadataAndNames() {
        Map<Integer, String> map = new HashMap<>(super.getVariantMetadataAndNames());
        map.put(21, "ov");
        map.put(22, "oc");
        map.put(23, "os");
        map.put(24, "oe");
        map.put(25, "ot");
        map.put(26, "op");
        map.put(27, "og");
        map.put(28, "or");
        map.put(29, "ob");
        return map;
    }

    @Nonnull
    public String getLocalizedGlyphName(int id) {
        return I18n.format("item.automagy.tankGlyph." + this.variants.get(id) + ".name");
    }

    @Nonnull
    public static TextureAtlasSprite getGlyphTexture(int id) {
        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(sprites[id - 1]);
    }
}
