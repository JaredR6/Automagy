package tuhljin.automagy.common.entities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.TjUtil;

import javax.annotation.Nonnull;

public class EntityAvaricePearl extends EntityProjectileItem {
    public static final int LIMITEDMODE_MAX_STACKS = 10;
    @Nonnull
    public static Map<String, Integer> extraChanceForResearch = new HashMap<>();
    public static int grabRange = 5;
    public double startX;
    public double startY = -1.0D;
    public double startZ;
    public boolean noPearlOnImpact = false;
    public int stacksGrabbed = 0;
    protected int thrownFromSlot = -1;

    public EntityAvaricePearl(World world, int metadata, EntityLivingBase entity, int thrownFromSlot, boolean noPearlOnImpact, double x, double y, double z) {
        super(world, entity, metadata);
        this.noPearlOnImpact = noPearlOnImpact;
        this.thrownFromSlot = thrownFromSlot;
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        NBTTagCompound nbt = this.getEntityData();
        nbt.setDouble("startX", this.startX);
        nbt.setDouble("startY", this.startY);
        nbt.setDouble("startZ", this.startZ);
        nbt.setBoolean("noPearlOnImpact", noPearlOnImpact);
        nbt.setShort("thrownFromSlot", (short)thrownFromSlot);
    }

    @SideOnly(Side.CLIENT)
    public EntityAvaricePearl(World world, double x, double y, double z, int metadata) {
        super(world, x, y, z, metadata);
        this.startX = x;
        this.startY = y;
        this.startZ = z;
        NBTTagCompound nbt = this.getEntityData();
        nbt.setDouble("startX", this.startX);
        nbt.setDouble("startY", this.startY);
        nbt.setDouble("startZ", this.startZ);
    }

    public EntityAvaricePearl(World world) {
        super(world);
    }

    @Override
    protected void onImpact(@Nonnull RayTraceResult result) {
        for(int i = 0; i < 32; ++i) {
            this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), 0.5D, this.rand.nextGaussian());
            this.world.spawnParticle(EnumParticleTypes.PORTAL, this.posX, this.posY + this.rand.nextDouble() * 2.0D, this.posZ, this.rand.nextGaussian(), -0.5D, this.rand.nextGaussian());
        }

        if (!this.world.isRemote) {
            boolean shouldTriggerResearchUnlock = false;
            this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 0.6F + this.rand.nextFloat() / 4.0F);
            EntityPlayerMP player = null;
            String playerName;
            Entity homeEntity;
            if (this.getThrower() != null && this.getThrower() instanceof EntityPlayerMP && !(this.getThrower() instanceof FakePlayer)) {
                player = (EntityPlayerMP)this.getThrower();
                playerName = player.getName();
                if (!playerName.equals("Rcon") && TjUtil.isPlayerOnline(playerName)) {
                    homeEntity = player;
                } else {
                    player = null;
                    homeEntity = this;
                }
            } else {
                homeEntity = this;
            }

            AxisAlignedBB box = new AxisAlignedBB(this.posX - (double)grabRange, this.posY - 1.5D, this.posZ - (double)grabRange, this.posX + (double)grabRange, this.posY + (double)grabRange, this.posZ + (double)grabRange);
            List<EntityItem> entities = TjUtil.getItemsInArea(this.world, box);
            if (player == null && this.startY != -1.0D) {
                this.setPosition(this.startX, this.startY, this.startZ);
            }

            EntityItem newItem = null;
            int stacks = entities.size();
            if (!this.noPearlOnImpact) {
                newItem = this.entityDropItem(new ItemStack(ModItems.avaricePearl, 1, this.metadata), 0.0F);
            }

            if (player != null && newItem != null) {
                newItem.setNoPickupDelay();
                newItem.onCollideWithPlayer(player);
            }

            if (this.metadata != 1 || this.stacksGrabbed < LIMITEDMODE_MAX_STACKS) {

                for (Entity entity : entities) {
                    entity.setWorld(homeEntity.world);
                    entity.setPosition(homeEntity.posX, homeEntity.posY + 0.5D, homeEntity.posZ);
                    if (player != null) {
                        ((EntityItem) entity).setNoPickupDelay();
                        entity.onCollideWithPlayer(player);
                    }

                    ++this.stacksGrabbed;
                    if (this.metadata == 1 && this.stacksGrabbed == LIMITEDMODE_MAX_STACKS) {
                        break;
                    }
                }
            }

            if (player != null && stacks > 0) {
                this.setWorld(player.getEntityWorld());
                this.setPosition(player.posX, player.posY + 0.5D, player.posZ);
                this.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT,  1.0F, 0.6F + this.rand.nextFloat() / 4.0F);
            }
        }

        this.setDead();
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound tagCompound) {
        NBTTagCompound nbt = this.getEntityData();
        nbt.setShort("stacksGrabbed", (short)this.stacksGrabbed);
        return super.writeToNBT(tagCompound);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        NBTTagCompound nbt = this.getEntityData();
        this.startX = nbt.getDouble("startX");
        this.startY = nbt.hasKey("startY") ? nbt.getDouble("startY") : -1.0D;
        this.startZ = nbt.getDouble("startZ");
        this.noPearlOnImpact = nbt.getBoolean("noPearlOnImpact");
        this.stacksGrabbed = nbt.getShort("stacksGrabbed");
        this.thrownFromSlot = nbt.hasKey("thrownFromSlot") ? nbt.getShort("thrownFromSlot") : -1;
    }
}
