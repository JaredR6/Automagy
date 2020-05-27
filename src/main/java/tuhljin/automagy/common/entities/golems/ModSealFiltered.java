package tuhljin.automagy.common.entities.golems;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import thaumcraft.common.golems.seals.SealFiltered;
import tuhljin.automagy.common.lib.TjUtil;

public abstract class ModSealFiltered extends SealFiltered {
    public ResourceLocation icon;
    public String key;

    public ModSealFiltered(String key, ResourceLocation icon) {
        this.icon = icon;
        this.key = "automagy:" + key;
    }

    public ModSealFiltered(String key) {
        this(key, new ResourceLocation("automagy", "items/seals/seal_" + key));
    }

    public String getKey() {
        return this.key;
    }

    public ResourceLocation getSealIcon() {
        return this.icon;
    }

    public boolean canPlaceAt(World world, BlockPos pos, EnumFacing side) {
        return !world.isAirBlock(pos);
    }

    public boolean isFilterEmpty() {
        for(int i = 0; i < this.getFilterSize(); ++i) {
            if (this.getFilterSlot(i) != null) {
                return false;
            }
        }

        return true;
    }

    public boolean isInFilter(ItemStack stack) {
        for(int i = 0; i < this.getFilterSize(); ++i) {
            ItemStack filterStack = this.getFilterSlot(i);
            if (filterStack != null && TjUtil.areItemsEqualIgnoringSize(stack, filterStack)) {
                return true;
            }
        }

        return false;
    }
}
