package tuhljin.automagy.client;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.blocks.ModBlock;
import tuhljin.automagy.common.items.IItemVariants;
import tuhljin.automagy.common.lib.References;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Map.Entry;

@SideOnly(Side.CLIENT)
public class ModelRegistrar {

    public static void registerItemModel(Item item, int metadata, String name, String variantName) {
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(new ResourceLocation(References.MOD_ID, name), variantName));
    }

    public static void registerItemModel(Item item, String name) {
        if (item instanceof IItemVariants) {
            Map<Integer, String> variants = ((IItemVariants)item).getVariantMetadataAndNames();

            for (Entry<Integer, String> integerStringEntry : variants.entrySet()) {
                int metadata = integerStringEntry.getKey();
                String variantName = integerStringEntry.getValue();
                if (variantName.isEmpty()) {
                    registerItemModel(item, metadata, name, "inventory");
                } else {
                    registerItemModel(item, metadata, "item/" + name, "type=" + variantName);
                }
            }

        } else {
            registerItemModel(item, 0, name, "inventory");
        }
    }

    public static void registerItemModel(Block block, int metadata, String name) {
        registerItemModel(Item.getItemFromBlock(block), metadata, name, "inventory");
    }

    public static void registerItemModel(Block block, String name) {
        registerItemModel(block, 0, name);
    }

    public static void registerVariantModels(ModBlock block, String name) {
        Map<String, Integer> variants = block.getVariantNamesAndMetadata();
        if (variants == null) {
            registerItemModel(block, name);
        } else {
            Item item = Item.getItemFromBlock(block);

            for (Entry<String, Integer> stringIntegerEntry : variants.entrySet()) {
                String variantName = stringIntegerEntry.getKey();
                int metadata = stringIntegerEntry.getValue();
                ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(new ResourceLocation(References.MOD_ID, name), "type=" + variantName));
            }
        }

    }

    public static void registerFluidModel(Block block, final String name) {
        Item item = Item.getItemFromBlock(block);
        ModelBakery.registerItemVariants(item);
        ModelLoader.setCustomMeshDefinition(item, stack -> new ModelResourceLocation(new ResourceLocation(References.MOD_ID, name), "fluid"));
        ModelLoader.setCustomStateMapper(block,
                new StateMapperBase() {
                    @Nonnull
                    protected ModelResourceLocation getModelResourceLocation(@Nonnull IBlockState state) {
                        return new ModelResourceLocation(new ResourceLocation(References.MOD_ID, name), "fluid");
                    }
                });
    }
}
