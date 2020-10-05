package tuhljin.automagy.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import thaumcraft.client.fx.FXDispatcher;
import tuhljin.automagy.client.lib.version.VersionChecker;
import tuhljin.automagy.client.renderers.ItemBlockSpecialRenderer;
import tuhljin.automagy.client.renderers.TileHourglassRenderer;
import tuhljin.automagy.client.renderers.TileMawRenderer;
import tuhljin.automagy.client.renderers.TileRedcrystalMercRenderer;
import tuhljin.automagy.client.renderers.TileRedcrystalRenderer;
import tuhljin.automagy.client.renderers.TileRemoteComparatorRenderer;
import tuhljin.automagy.client.renderers.TileTankThirstyRenderer;
import tuhljin.automagy.client.renderers.entities.ItemProjectileRenderFactory;
import tuhljin.automagy.common.ProxyCommon;
import tuhljin.automagy.common.entities.EntityAvaricePearl;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.AutomagyConfig;
import tuhljin.automagy.common.tiles.TileHourglass;
import tuhljin.automagy.common.tiles.TileMawBase;
import tuhljin.automagy.common.tiles.TileRedcrystal;
import tuhljin.automagy.common.tiles.TileRedcrystalMerc;
import tuhljin.automagy.common.tiles.TileRemoteComparator;
import tuhljin.automagy.common.tiles.TileTankThirsty;

import javax.annotation.Nonnull;
import java.util.Random;

public class ProxyClient extends ProxyCommon {

    public void preInit(@Nonnull FMLPreInitializationEvent event) {
        super.preInit(event);
        this.registerEntityRendering();
    }

    public void init(FMLInitializationEvent event) {
        super.init(event);
        this.bindTileEntityRenderers();
        ItemBlockSpecialRenderer.init();
        if (AutomagyConfig.versionChecking) {
            new VersionChecker();
        }

    }

    public EntityPlayer getCurrentPlayer() {
        return FMLClientHandler.instance().getClient().player;
    }

    private void bindTileEntityRenderers() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileRedcrystalMerc.class, new TileRedcrystalMercRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRedcrystal.class, new TileRedcrystalRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileHourglass.class, new TileHourglassRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileRemoteComparator.class, new TileRemoteComparatorRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileMawBase.class, new TileMawRenderer());
        ClientRegistry.bindTileEntitySpecialRenderer(TileTankThirsty.class, new TileTankThirstyRenderer());
    }

    private void registerEntityRendering() {
        RenderingRegistry.registerEntityRenderingHandler(EntityAvaricePearl.class, new ItemProjectileRenderFactory(ModItems.avaricePearl));
    }

    public void fxGhostlyBallStream(World worldObj, double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {
        //debug
        FXDispatcher.INSTANCE.drawWispyMotes(posX, posY + 1, posZ, posX2, posY2 + 1, posZ2, 0, gravity);
        FXDispatcher.INSTANCE.drawLineSparkle(worldObj.rand, posX, posY, posZ, posX2, posY2, posZ2, size, 0, 0, 0, 0, 0, gravity, 0);
    }

    public void fxWisp3(double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {
        FXDispatcher.INSTANCE.drawWispyMotes(posX, posY, posZ, posX2, posY2, posZ2, 0, gravity);
        //FXDispatcher.INSTANCE.wispFX3(posX, posY, posZ, posX2, posY2, posZ2, size, type, shrink, gravity);
    }


    public void fxWisp3NoClip(double posX, double posY, double posZ, double posX2, double posY2, double posZ2, float size, int type, boolean shrink, float gravity) {
        FXDispatcher.INSTANCE.drawWispyMotes(posX, posY, posZ, posX2, posY2, posZ2, 0, gravity);
        /*FXDispatcher disp = Thaumcraft.proxy.getFX();
        FXWisp ef = new FXWisp(disp.getWorld(), posX, posY, posZ, posX2, posY2, posZ2, size, type);;
        ef.setGravity(gravity);
        ef.shrink = shrink;
        ef.noClip = true;
        ParticleEngine.instance.addEffect(disp.getWorld(), ef);

         */
    }

    public void fxBurst(double x, double y, double z, float size) {
        //Thaumcraft.proxy.getFX().burst(x, y, z, size);
    }

    public void fxOldBlockSparkle(World world, int x, int y, int z, int color, int count) {
        /*
        if (world.isRemote) {
            for(int a = 0; a < Thaumcraft.proxy.getFX().particleCount(count); ++a) {
                FXSparkle fx = new FXSparkle(world, (double)((float)x + world.rand.nextFloat()), (double)((float)y + world.rand.nextFloat()), (double)((float)z + world.rand.nextFloat()), 1.75F, color == -1 ? world.rand.nextInt(5) : color, 3 + world.rand.nextInt(3));
                fx.setGravity(0.2F);
                ParticleEngine.instance.addEffect(world, fx);
            }

        }

         */
    }
}
