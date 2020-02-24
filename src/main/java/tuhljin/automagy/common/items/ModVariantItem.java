package tuhljin.automagy.common.items;

import com.google.common.collect.ImmutableMap;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ModVariantItem extends Item implements IItemVariants {
    protected final ImmutableMap<Integer, String> variants;

    public ModVariantItem(ImmutableMap<Integer, String> variants) {
        this.variants = variants;
        this.setHasSubtypes(true);
    }

    public Map<Integer, String> getVariantMetadataAndNames() {
        return this.variants;
    }

    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        for (Integer metadata : this.variants.keySet()) {
            subItems.add(new ItemStack(itemIn, 1, metadata));
        }
    }

    @Override
    @Nonnull
    public String getUnlocalizedName(ItemStack stack) {
        int d = stack.getItemDamage();
        String name = this.variants.get(d);
        if (name == null) {
            return super.getUnlocalizedName() + "." + d;
        } else {
            return name.isEmpty() ? super.getUnlocalizedName(stack) : super.getUnlocalizedName() + "." + name;
        }
    }
}
