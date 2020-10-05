package tuhljin.automagy.client.renderers.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import tuhljin.automagy.client.renderers.RenderItemProjectile;
import tuhljin.automagy.common.entities.EntityProjectileItem;

public class ItemProjectileRenderFactory<T extends EntityProjectileItem> implements IRenderFactory<T> {
    private Item item;

    public ItemProjectileRenderFactory(Item item) {
        this.item = item;
    }

    public Render<? super T> createRenderFor(RenderManager manager) {
        return new RenderItemProjectile(manager, this.item, Minecraft.getMinecraft().getRenderItem());
    }
}
