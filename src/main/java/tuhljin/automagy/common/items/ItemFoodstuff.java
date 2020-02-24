package tuhljin.automagy.common.items;

import java.util.List;

import com.sun.org.apache.xml.internal.security.utils.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.common.lib.potions.PotionBlurredVision;
import tuhljin.automagy.common.lib.ThaumcraftExtension;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemFoodstuff extends ItemFood {
    public ItemFoodstuff() {
        super(6, false);
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setAlwaysEdible();
    }

    @Nonnull
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return super.getUnlocalizedName() + ".vishroomStew";
    }

    @Nonnull
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, @Nonnull World world, EntityLivingBase entityLiving) {
        super.onItemUseFinish(stack, world, entityLiving);
        return new ItemStack(Items.BOWL);
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, @Nonnull EntityPlayer player) {
        if (!world.isRemote) {
            if (ThaumcraftExtension.isWarpEnabled()) {
                ThaumcraftExtension.addTemporaryWarpToPlayer(player, world.rand.nextInt(10) + 8, true);
                ThaumcraftExtension.forceWarpCheck(player);
            }

            player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("weakness")), 800, 1));
            player.addPotionEffect(new PotionEffect(PotionBlurredVision.instance, 800, 1));
            player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("confusion")), 400, 1));
            player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("blindness")), 160, 1));
            player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("night_vision")), 60, 1));
            player.addPotionEffect(new PotionEffect(Potion.REGISTRY.getObject(new ResourceLocation("poison")), 60, 2));
        }

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(I18n.translate("Automagy.tip.foodstuff.0"));
    }
}
