package tuhljin.automagy.common.lib;

import java.awt.Color;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import thaumcraft.Thaumcraft;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.capabilities.IPlayerWarp;
import thaumcraft.api.capabilities.ThaumcraftCapabilities;
import thaumcraft.api.casters.ICaster;
import thaumcraft.api.golems.IGolemAPI;
import thaumcraft.api.golems.tasks.Task;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.common.blocks.devices.BlockPedestal;
import thaumcraft.common.config.ModConfig;
import thaumcraft.common.golems.tasks.TaskHandler;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.lib.events.WarpEvents;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.fx.PacketFXBlockArc;
import thaumcraft.common.lib.network.fx.PacketFXBlockBamf;
import thaumcraft.common.lib.utils.InventoryUtils;
import thaumcraft.common.tiles.devices.TileMirror;
import thaumcraft.common.tiles.devices.TileMirrorEssentia;
import thaumcraft.common.tiles.misc.TileHole;
import tuhljin.automagy.common.lib.struct.WorldSpecificCoordinates;
import tuhljin.automagy.common.network.MessageSound;

public class ThaumcraftExtension {
    public ThaumcraftExtension() {
    }

    public static void modComms() {
    }

    public static boolean isWarpEnabled() {
        return !ModConfig.CONFIG_MISC.wussMode;
    }

    public static void addTemporaryWarpToPlayer(EntityPlayer player, int amount, boolean playSound) {
        ThaumcraftApi.internalMethods.addWarpToPlayer(player, amount, IPlayerWarp.EnumWarpType.TEMPORARY);
        if (playSound) {
            MessageSound.sendToClient((short)0, player);
        }

    }

    public static void addNormalWarpToPlayer(EntityPlayer player, int amount) {
        ThaumcraftApi.internalMethods.addWarpToPlayer(player, amount, IPlayerWarp.EnumWarpType.NORMAL);
    }

    public static void forceWarpCheck(EntityPlayer player) {
        WarpEvents.checkWarpEvent(player);
    }

    public static int getTemporaryWarp(EntityPlayer player) {
        return ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.TEMPORARY);
    }

    public static int getStickyWarp(EntityPlayer player) {
        return ThaumcraftCapabilities.getWarp(player).get(IPlayerWarp.EnumWarpType.PERMANENT);
    }

    public static void resetWarpCounter(EntityPlayer player) {
        IPlayerWarp wc = ThaumcraftCapabilities.getWarp(player);
        int total = wc.get(IPlayerWarp.EnumWarpType.TEMPORARY) + wc.get(IPlayerWarp.EnumWarpType.NORMAL) + wc.get(IPlayerWarp.EnumWarpType.PERMANENT);
        wc.setCounter(total);
    }

    // Not disabled, maybe
    /*
    public static boolean isMirrorResearchEnabled() {
        return Config.allowMirrors;
    }
    */

    public static boolean playerHasCasterEquipped(EntityPlayer player) {
        for (ItemStack stack : player.getHeldEquipment()) {
            if (stack.getItem() instanceof ICaster) {
                return true;
            }
        }
        return false;
    }

    // TODO: can the caster focus be null?
    public static ItemFocus getCasterFocus(ItemStack caster) {
        Item ci = caster.getItem();
        return ci instanceof ICaster ? (ItemFocus) ((ICaster)ci).getFocus(caster) : null;
    }

    public static boolean blockIsPedestal(Block block) {
        return block instanceof BlockPedestal;
    }

    public static boolean tileIsMirror(TileEntity te) {
        return te instanceof TileMirror || te instanceof TileMirrorEssentia;
    }

    public static boolean tileIsPortableHole(TileEntity te, IBlockState state) {
        return te instanceof TileHole && (state == null || ((TileHole)te).oldblock == state);
    }

    public static WorldSpecificCoordinates getLinkedMirrorCoordinates(TileEntity te) {
        if (te instanceof TileMirror) {
            TileMirror tem = (TileMirror)te;
            if (tem.linked) {
                return new WorldSpecificCoordinates(tem.linkDim, tem.linkX, tem.linkY, tem.linkZ);
            }
        } else if (te instanceof TileMirrorEssentia) {
            TileMirrorEssentia tem = (TileMirrorEssentia)te;
            if (tem.linked) {
                return new WorldSpecificCoordinates(tem.linkDim, tem.linkX, tem.linkY, tem.linkZ);
            }
        }

        return null;
    }

    public static boolean isResearchComplete(EntityPlayer player, String... keys) {
        for (String key : keys) {
            if (!ThaumcraftCapabilities.getKnowledge(player).isResearchComplete(key)) {
                return false;
            }
        }
        return true;
    }

    // TODO: int mode no longer passed in
    public static void sparkle(float x, float y, float z, float r, float g, float b) {
        FXDispatcher.INSTANCE.sparkle(x, y, z, r, g, b);
    }

    // TODO: No longer exists in thaumcraft
    /*
    public static void sparkle(float f1, float f2, float f3, float f4, int i, float f5) {
        FXDispatcher.INSTANCE.sparkle(f1, f2, f3, f4, i, f5);
    }
    */

    public static void zapBlockInfusion(World world, BlockPos source, BlockPos target) {
        zapBlock(world, source, target, 0.3F - world.rand.nextFloat() * 0.1F, 0.0F, 0.3F - world.rand.nextFloat() * 0.1F, 1.0F);
    }

    public static void zapBlock(World world, BlockPos source, BlockPos target) {
        zapBlock(world, source, target, 0.5F, 1.0F, 1.0F, 1.0F);
    }

    public static void zapBlock(World world, BlockPos source, BlockPos target, Aspect aspect, float width) {
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        if (aspect != null) {
            Color c = new Color(aspect.getColor());
            r = (float)c.getRed() / 255.0F;
            g = (float)c.getGreen() / 255.0F;
            b = (float)c.getBlue() / 255.0F;
        }

        zapBlock(world, source, target, r, g, b, width);
    }

    public static void zapBlock(World world, BlockPos source, BlockPos target, float r, float g, float b, float width) {
        FXDispatcher.INSTANCE.arcBolt((double)source.getX() + 0.5D, (double)source.getY() + 0.5D, (double)source.getZ() + 0.5D, (double)target.getX() + 0.25D + (double)(world.rand.nextFloat() / 2.0F), (double)target.getY() + 0.25D + (double)(world.rand.nextFloat() / 2.0F), (double)target.getZ() + 0.25D + (double)(world.rand.nextFloat() / 2.0F), r, g, b, width);
    }

    public static void zapEntity(BlockPos pos, Entity target, Aspect aspect) {
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        if (aspect != null) {
            Color c = new Color(aspect.getColor());
            r = (float)c.getRed() / 255.0F;
            g = (float)c.getGreen() / 255.0F;
            b = (float)c.getBlue() / 255.0F;
        }

        zapEntity(pos, target, r, g, b);
    }

    public static void zapEntity(BlockPos pos, Entity target) {
        zapEntity(pos, target, 0.3F - target.world.rand.nextFloat() * 0.1F, 0.0F, 0.3F - target.world.rand.nextFloat() * 0.1F);
    }

    public static void zapEntity(BlockPos pos, Entity target, float r, float g, float b) {
        PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockArc(pos, target, r, g, b), new TargetPoint(target.world.provider.getDimension(), (double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), 32.0D));
    }

    public static void lightning(double sx, double sy, double sz, double dx, double dy, double dz, Aspect aspect) {
        float r = 1.0F;
        float g = 1.0F;
        float b = 1.0F;
        if (aspect != null) {
            Color c = new Color(aspect.getColor());
            r = (float)c.getRed() / 255.0F;
            g = (float)c.getGreen() / 255.0F;
            b = (float)c.getBlue() / 255.0F;
        }

        lightning(sx, sy, sz, dx, dy, dz, r, g, b);
    }

    public static void lightning(double sx, double sy, double sz, double dx, double dy, double dz, float r, float g, float b) {
        FXDispatcher.INSTANCE.arcLightning(sx, sy, sz, dx, dy, dz, r, g, b, 0.5F);
    }


    // TODO: Figure out correct FX
    public static void taintSplash(BlockPos pos) {
        FXDispatcher.INSTANCE.drawPollutionParticles(pos);
        // FXDispatcher.INSTANCE.fluxRainSplashFX(pos);
    }

    public static void bamf(World world, double x, double y, double z) {
        bamf(world, x, y, z, 5770890, true, false, null);
    }

    public static void bamf(World world, double x, double y, double z, int color, boolean sound, boolean flair, EnumFacing side) {
        PacketHandler.INSTANCE.sendToAllAround(new PacketFXBlockBamf(x, y, z, color, sound, flair, side), new TargetPoint(world.provider.getDimension(), x, y, z, 32.0D));
    }

    public static boolean isGolemCarryingNothing(IGolemAPI golem) {
        NonNullList<ItemStack> carrying = golem.getCarrying();
        for (ItemStack stack : carrying) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public static void golemMatchFakePlayerInventory(FakePlayer fp, IGolemAPI golem) {
        int i;
        for(i = 0; i < fp.inventory.mainInventory.size(); ++i) {
            if (!fp.inventory.mainInventory.get(i).isEmpty()) {
                if (golem.canCarry(fp.inventory.mainInventory.get(i), true)) {
                    fp.inventory.mainInventory.set(i, golem.holdItem(fp.inventory.mainInventory.get(i)));
                }

                if (fp.inventory.mainInventory.get(i).getCount() > 0) {
                    InventoryUtils.dropItemAtEntity(golem.getGolemWorld(), fp.inventory.mainInventory.get(i), golem.getGolemEntity());
                }

                fp.inventory.mainInventory.set(i, ItemStack.EMPTY);
            }
        }


        for(i = 0; i < fp.inventory.armorInventory.size(); ++i) {
            if (!fp.inventory.armorInventory.get(i).isEmpty()) {
                if (golem.canCarry(fp.inventory.armorInventory.get(i), true)) {
                    fp.inventory.armorInventory.set(i, golem.holdItem(fp.inventory.armorInventory.get(i)));
                }

                if (fp.inventory.armorInventory.get(i).getCount() > 0) {
                    InventoryUtils.dropItemAtEntity(golem.getGolemWorld(), fp.inventory.armorInventory.get(i), golem.getGolemEntity());
                }

                fp.inventory.armorInventory.set(i, ItemStack.EMPTY);
            }
        }

    }

    public static Task getGolemTask(World world, int id) {
        return TaskHandler.getTask(world.provider.getDimension(), id);
    }

    // TODO: This is probably waaaaaaay different

    public static enum ShardAspect {
        AIR(0, "air", Aspect.AIR),
        FIRE(1, "fire", Aspect.FIRE),
        WATER(2, "water", Aspect.WATER),
        EARTH(3, "earth", Aspect.EARTH),
        ORDER(4, "order", Aspect.ORDER),
        ENTROPY(5, "entropy", Aspect.ENTROPY);

        private static final ThaumcraftExtension.ShardAspect[] METADATA_LOOKUP = new ThaumcraftExtension.ShardAspect[values().length];
        private final int metadata;
        private String unlocalizedName;
        private final Aspect aspect;

        private ShardAspect(int metadata, String unlocalizedName, Aspect aspect) {
            this.metadata = metadata;
            this.unlocalizedName = unlocalizedName;
            this.aspect = aspect;
        }

        public int getMetadata() {
            return this.metadata;
        }

        public String getUnlocalizedName() {
            return this.unlocalizedName;
        }

        public Aspect getAspect() {
            return this.aspect;
        }

        public int getColor() {
            return this.aspect.getColor();
        }

        public static ThaumcraftExtension.ShardAspect byMetadata(int metadata) {
            return metadata >= 0 && metadata <= METADATA_LOOKUP.length ? METADATA_LOOKUP[metadata] : null;
        }

        public static int getColor(int metadata) {
            ThaumcraftExtension.ShardAspect sa = byMetadata(metadata);
            if (sa == null) {
                sa = byMetadata(0);
            }

            return sa.getColor();
        }

        static {
            ThaumcraftExtension.ShardAspect[] var0 = values();
            int var1 = var0.length;

            for(int var2 = 0; var2 < var1; ++var2) {
                ThaumcraftExtension.ShardAspect sa = var0[var2];
                METADATA_LOOKUP[sa.getMetadata()] = sa;
            }

        }
    }
}