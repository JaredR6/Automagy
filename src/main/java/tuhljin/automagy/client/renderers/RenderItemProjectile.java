package tuhljin.automagy.client.renderers;

import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import tuhljin.automagy.common.entities.EntityProjectileItem;
import tuhljin.automagy.common.entities.IItemMetadata;

import javax.annotation.Nonnull;

public class RenderItemProjectile extends RenderSnowball<EntityProjectileItem> {
    public RenderItemProjectile(RenderManager renderManagerIn, Item item, RenderItem renderItem) {
        super(renderManagerIn, item, renderItem);
    }

    @Nonnull
    public ItemStack getStackToRender(EntityProjectileItem entityIn) {
        return entityIn != null ? new ItemStack(this.item, 1, entityIn.getItemMetadata()) : super.getStackToRender(entityIn);
    }
}
