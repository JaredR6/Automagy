package tuhljin.automagy.common.blocks;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXSlimyBubble;
import thaumcraft.common.lib.potions.PotionBlurredVision;
import thaumcraft.common.lib.potions.PotionWarpWard;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.network.MessageParticles;

import javax.annotation.Nonnull;

public class BlockVishroomSoup extends ModBlockFluid {
    public static int INTERVAL = 60;
    public static int MYCELIUM_FROM_STACK_RATE = 20;
    public static int MOOSHROOM_TRANSFORM_TIME = 600;
    public static int WARPING_REMOVES_CHANCE = 8;
    public static int POOF_CHANCE_MYCELIUM_ITEM = 5;
    public static int POOF_CHANCE_MYCELIUM_BLOCK = 6;
    public static final String TAG_TIMER = "fluidVishroomTimer";
    public static final String TAG_WARPTIMER = "fluidVishroomWarpTimer";
    public static final String TAG_WARPSEEN = "fluidVishroomWarpSeen";
    public static final String TAG_COWTIMER = "fluidVishroomCowTime";

    public BlockVishroomSoup() {
        super(new BlockVishroomSoup.FluidVishroomSoup(References.FLUID_VISHROOMSOUP, References.FLUIDTEXTURE_VISHROOMSOUP), Material.WATER);
        this.setLightOpacity(5);
    }

    @Override
    public void onEntityCollidedWithBlock(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, Entity entity) {
        if (!world.isRemote) {
            NBTTagCompound data;
            long t;
            int warp;
            if (entity instanceof EntityLivingBase) {
                data = entity.getEntityData();
                t = world.getTotalWorldTime();
                if (t - data.getLong(TAG_TIMER) >= INTERVAL) {
                    data.setLong(TAG_TIMER, t);
                    EntityLivingBase e = (EntityLivingBase)entity;
                    EntityPlayer player = e instanceof EntityPlayer ? (EntityPlayer)entity : null;
                    if (!e.isPotionActive(MobEffects.WEAKNESS)) {
                        e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1));
                    } else if (player != null && !e.isPotionActive(PotionBlurredVision.instance)) {
                        e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1));
                        e.addPotionEffect(new PotionEffect(PotionBlurredVision.instance, 200, 1));
                    } else {
                        e.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 200, 1));
                        if (world.rand.nextInt(10) == 0) {
                            if (e instanceof EntityMooshroom) {
                                e.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 320, 0));
                            } else {
                                e.addPotionEffect(new PotionEffect(MobEffects.POISON, 80, 2));
                            }
                        }

                        if (player != null) {
                            player.addPotionEffect(new PotionEffect(PotionBlurredVision.instance, 200, 1));
                        }
                    }

                    if (player != null && this.isSourceBlock(world, pos)) {
                        player.removePotionEffect(PotionWarpWard.instance);
                        if (ThaumcraftExtension.isWarpEnabled()) {
                            boolean doWarpCheck = false;
                            boolean usedUp = false;
                            warp = ThaumcraftExtension.getTemporaryWarp(player) + ThaumcraftExtension.getStickyWarp(player);
                            int diff;
                            int z;
                            if (t - data.getLong(TAG_WARPTIMER) <= INTERVAL) {
                                diff = warp - data.getInteger(TAG_WARPSEEN);
                                if (diff > 0) {
                                    warp += diff;
                                    doWarpCheck = true;
                                    boolean playedSound = false;

                                    for(z = 1; z <= diff; ++z) {
                                        if (world.rand.nextInt(WARPING_REMOVES_CHANCE) == 0) {
                                            usedUp = true;
                                            break;
                                        }
                                    }

                                    if (world.rand.nextInt(4) == 0) {
                                        ThaumcraftExtension.addNormalWarpToPlayer(player, 1);
                                        playedSound = true;
                                        --diff;
                                    }

                                    if (diff > 0) {
                                        ThaumcraftExtension.addTemporaryWarpToPlayer(player, diff, !playedSound);
                                    }

                                    player.addPotionEffect(new PotionEffect(MobEffects.HUNGER, MOOSHROOM_TRANSFORM_TIME, 1));
                                }
                            }

                            data.setLong(TAG_WARPTIMER, t);
                            data.setInteger(TAG_WARPSEEN, warp);
                            if (doWarpCheck || world.rand.nextInt(INTERVAL) == 0) {
                                if (!doWarpCheck) {
                                    ThaumcraftExtension.resetWarpCounter(player);
                                }

                                ThaumcraftExtension.forceWarpCheck(player);
                            }

                            if (usedUp) {
                                diff = pos.getX();
                                int y = pos.getY();
                                z = pos.getZ();
                                MessageParticles.sendToClients((short)1, 300, world, diff, y, z);
                                MessageParticles.sendToClients((short)3, 50, world, diff, y, z);
                                world.playSound(player, diff + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                                world.playSound(player, diff + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                                this.poof(world, pos);
                                return;
                            }
                        }
                    }
                }

                if (entity instanceof EntityCow && !(entity instanceof EntityMooshroom) && !entity.isDead && this.isSourceBlock(world, pos)) {
                    int c = data.getInteger(TAG_COWTIMER);
                    if (c >= MOOSHROOM_TRANSFORM_TIME) {
                        EntityMooshroom mooshroom = turnCowIntoMooshroom((EntityCow)entity);
                        mooshroom.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 320, 0));
                        this.poof(world, pos);
                        if (world.isAirBlock(pos.up())) {
                            this.poof(world, pos.up());
                        }

                        return;
                    }

                    data.setInteger(TAG_COWTIMER, c + 1);
                    if (c % 25 == 0) {
                        MessageParticles.sendToClients((short)1, 100, world, pos.getX(), pos.getY(), pos.getZ());
                    }
                }
            } else if (entity instanceof EntityItem && this.isSourceBlock(world, pos)) {
                data = entity.getEntityData();
                t = world.getTotalWorldTime();
                if (t - data.getLong(TAG_TIMER) >= (long)MYCELIUM_FROM_STACK_RATE) {
                    data.setLong(TAG_TIMER, t);
                    EntityItem item = (EntityItem)entity;
                    ItemStack stack = item.getItem();
                    if (TjUtil.areItemsEqualIgnoringSize(stack, new ItemStack(Blocks.GRASS))) {
                        int x = pos.getX();
                        int y = pos.getY();
                        warp = pos.getZ();
                        if (stack.getCount() > 1) {
                            stack.shrink(1);
                            item.setItem(stack);
                        } else {
                            item.setDead();
                        }

                        EntityItem newItem = TjUtil.dropItemIntoWorldSimple(new ItemStack(Blocks.MYCELIUM), world, x, y, warp);
                        newItem.motionY += .25D;
                        newItem.motionX *= .1D;
                        newItem.motionZ *= .1D;
                        world.playSound(null, x + 0.5D, y + 0.5D, warp + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                        MessageParticles.sendToClients((short)1, world, x, y, warp);
                        if (world.rand.nextInt(POOF_CHANCE_MYCELIUM_ITEM) == 0) {
                            this.poof(world, pos);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void updateTick(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
        if (this.isSourceBlock(world, pos)) {
            if (rand.nextInt(5) == 0) {
                boolean didSpread = false;
                int r = rand.nextInt(10);
                if (r < 6) {
                    EnumFacing dir = EnumFacing.getFront(r);
                    BlockPos pos2 = pos.offset(dir);
                    didSpread = this.doSpread(world, pos2.getX(), pos2.getY(), pos2.getZ(), world.rand, 0);
                } else {
                    switch(r) {
                        case 6:
                            didSpread = this.doSpread(world, pos.getX() + 1, pos.getY(), pos.getZ() + 1, world.rand, 0);
                            break;
                        case 7:
                            didSpread = this.doSpread(world, pos.getX() + 1, pos.getY(), pos.getZ() - 1, world.rand, 0);
                            break;
                        case 8:
                            didSpread = this.doSpread(world, pos.getX() - 1, pos.getY(), pos.getZ() - 1, world.rand, 0);
                            break;
                        case 9:
                            didSpread = this.doSpread(world, pos.getX() - 1, pos.getY(), pos.getZ() + 1, world.rand, 0);
                    }
                }

                if (didSpread && rand.nextInt(POOF_CHANCE_MYCELIUM_BLOCK) == 0) {
                    this.poof(world, pos);
                    return;
                }
            }

            world.scheduleBlockUpdate(pos, this, this.tickRate, 0);
        }

        super.updateTick(world, pos, state, rand);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random rand) {
        if (this.isSourceBlock(world, pos)) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();
            AxisAlignedBB box = new AxisAlignedBB(x, y, z, (x + 1), (y + 1), (z + 1));
            List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, box);
            if (list.size() != 0) {
                float h = rand.nextFloat() * 0.075F;
                FXSlimyBubble ef = new FXSlimyBubble(world, x + rand.nextFloat(), y + 0.1F, z + rand.nextFloat(), 0.075F + h);
                ef.setAlphaF(0.8F);
                ef.setRBGColorF(1.0F, 0.28F, 0.53F);
                ParticleEngine.addEffect(world, ef);
                if (rand.nextInt(8) == 0) {
                    double sx = x + rand.nextFloat();
                    double sy = y /* + this.maxY */;
                    double sz = z + rand.nextFloat();
                    world.playSound(sx, sy, sz, SoundEvents.BLOCK_LAVA_POP, SoundCategory.BLOCKS, 0.1F + rand.nextFloat() * 0.1F, 0.9F + rand.nextFloat() * 0.15F, false);
                }

            }
        }
    }

    private boolean doSpread(@Nonnull World world, int x, int y, int z, @Nonnull Random rand, int count) {
        BlockPos pos = new BlockPos(x, y, z);
        Block block = TjUtil.getBlock(world, pos);
        if (block == this) {
            if (count > 2) {
                return false;
            } else if (this.isSourceBlock(world, new BlockPos(x, y, z))) {
                return false;
            } else {
                int r = rand.nextInt(10);
                EnumFacing dir = EnumFacing.getFront(r < 6 ? r : r - 4);
                BlockPos pos2 = pos.offset(dir);
                return r < 6 ? this.doSpread(world, pos2.getX(), pos2.getY(), pos2.getZ(), world.rand, count + 1) : this.doSpread(world, pos2.getX() + (rand.nextInt(3) - 1), pos2.getY(), pos2.getZ() + (rand.nextInt(3) - 1), world.rand, count + 1);
            }
        } else {
            IBlockState state;
            if (block == Blocks.GRASS) {
                Block block2 = TjUtil.getBlock(world, new BlockPos(x, y + 1, z));
                if (block2 == Blocks.TALLGRASS || block2 == Blocks.DOUBLE_PLANT || block2 == Blocks.RED_FLOWER || block2 == Blocks.YELLOW_FLOWER || world.isAirBlock(pos.up()) && rand.nextInt(8) == 0) {
                    state = rand.nextBoolean() ? Blocks.RED_MUSHROOM.getDefaultState() : Blocks.BROWN_MUSHROOM.getDefaultState();
                    world.setBlockState(pos.up(), state);
                    MessageParticles.sendToClients((short)1, world, x, y + 1, z);
                }

                world.setBlockState(pos, Blocks.MYCELIUM.getDefaultState());
                world.playSound(null,x + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                MessageParticles.sendToClients((short)1, world, x, y, z);
                return true;
            } else {
                if (block == Blocks.TALLGRASS || block == Blocks.DOUBLE_PLANT || block == Blocks.RED_FLOWER || block == Blocks.YELLOW_FLOWER) {
                    BlockBush mushroom = rand.nextBoolean() ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM;
                    state = mushroom.getDefaultState();
                    if (!mushroom.canBlockStay(world, pos, state)) {
                        return this.doSpread(world, x, y - 1, z, rand, count + 1);
                    }

                    world.setBlockState(pos, state);
                    world.playSound(null, x + 0.5D, y + 0.5D, z + 0.5D, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (world.rand.nextFloat() - world.rand.nextFloat()) * 0.8F);
                    MessageParticles.sendToClients((short)1, world, x, y, z);
                }

                return false;
            }
        }
    }

    private void poof(@Nonnull World world, @Nonnull BlockPos pos) {
        world.setBlockToAir(pos);
        ThaumcraftExtension.bamf(world, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        AxisAlignedBB box = new AxisAlignedBB(pos.getX() - 0.5D, pos.getY() - 0.1D, pos.getZ() - 0.5D, pos.getX() + 1.5D, pos.getY() + 1.1D, pos.getZ() + 1.5D);
        List<EntityPlayer> list = world.getEntitiesWithinAABB(EntityPlayer.class, box);

        for (EntityPlayer player : list) {
            player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 180, 1));
        }

    }

    @Nonnull
    public static EntityMooshroom turnCowIntoMooshroom(@Nonnull EntityCow cow) {
        cow.setDead();
        EntityMooshroom mooshroom = new EntityMooshroom(cow.world);
        mooshroom.setLocationAndAngles(cow.posX, cow.posY, cow.posZ, cow.rotationYaw, cow.rotationPitch);
        mooshroom.setHealth(cow.getHealth());
        mooshroom.renderYawOffset = cow.renderYawOffset;
        cow.world.spawnEntity(mooshroom);
        cow.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, cow.posX, cow.posY + (cow.height / 2.0F), cow.posZ, 0.0D, 0.0D, 0.0D);
        cow.playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 0.5F, 2.6F + (cow.world.rand.nextFloat() - cow.world.rand.nextFloat()) * 0.8F);
        MessageParticles.sendToClients((short)3, cow.world, (int)cow.posX, (int)cow.posY, (int)cow.posZ);
        return mooshroom;
    }

    public static class FluidVishroomSoup extends ModFluid {
        public FluidVishroomSoup(@Nonnull String fluidName, @Nonnull String texture) {
            super(fluidName, texture);
            this.setGaseous(false);
            this.setViscosity(1500);
            this.setLuminosity(8);
            this.setRarity(EnumRarity.UNCOMMON);
        }

        public int getColor() {
            return Color.PINK.getRGB();
        }
    }
}
