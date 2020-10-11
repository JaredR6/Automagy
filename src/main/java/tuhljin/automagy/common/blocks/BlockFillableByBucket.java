package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.AutomagyConfig;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.ITileWithTank;

import javax.annotation.Nonnull;

public abstract class BlockFillableByBucket extends ModTileRenderedBlock {
    public boolean useWaterBottles;

    public BlockFillableByBucket(@Nonnull Material material, @Nonnull MapColor mapColor, String name, boolean useWaterBottles) {
        super(material, mapColor, name);
        this.useWaterBottles = useWaterBottles && AutomagyConfig.waterBottleAmount != -1;
    }

    public BlockFillableByBucket(@Nonnull Material material, String name, boolean useWaterBottles) {
        this(material, material.getMaterialMapColor(), name, useWaterBottles);
    }

    @Override
    public boolean onBlockActivated(@Nonnull World world, @Nonnull BlockPos pos, IBlockState state, @Nonnull EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = player.getHeldItem(hand);
        if (heldStack.isEmpty())
            return false;

        FluidStack liquid = FluidUtil.getFluidContained(heldStack);
        ITileWithTank te;
        if (liquid != null) {
            if (world.isRemote)
                return this.useWaterBottles || !heldStack.isItemEqual(new ItemStack(Items.POTIONITEM));

            te = (ITileWithTank) world.getTileEntity(pos);
            boolean returnBottle = false;
            if (heldStack.isItemEqual(new ItemStack(Items.POTIONITEM))) {
                if (!this.useWaterBottles) {
                    return false;
                }

                if (AutomagyConfig.getRealWaterBottleAmount() == 0) {
                    FluidStack fillFluid = te.getTank().getFluid();
                    if (fillFluid != null && fillFluid.isFluidEqual(FluidRegistry.getFluidStack(FluidRegistry.WATER.getName(), 1))) {
                        TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.GLASS_BOTTLE));
                    }

                    return true;
                }

                if (AutomagyConfig.getRealWaterBottleAmount() > 0) {
                    liquid.amount = AutomagyConfig.getRealWaterBottleAmount();
                    returnBottle = true;
                }
            }

            int amount = te.fill(liquid, false);
            if (amount == liquid.amount) {
                te.fill(liquid, true);
                if (!player.capabilities.isCreativeMode) {
                    if (returnBottle) {
                        TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.GLASS_BOTTLE), true);
                    } else if (heldStack.getItem() instanceof ItemBucket) {
                        TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.BUCKET), true);
                    } else if (heldStack.isItemEqual(new ItemStack(Items.MUSHROOM_STEW))) {
                        TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.BOWL), true);
                    } else {
                        TjUtil.consumePlayerItem(player, player.inventory.currentItem);
                    }
                }
            }

            return true;

        } else { // FluidUtil.getFluidContained(heldStack) == null

            FluidStack fillFluid;
            if (heldStack.getItem() instanceof ItemBucket) {
                if (world.isRemote)
                    return true;

                te = (ITileWithTank) world.getTileEntity(pos);
                fillFluid = te.getTank().getFluid();
                if (fillFluid == null) {
                    return true;
                }

                IFluidHandlerItem bucket = heldStack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
                int fillValue = bucket.fill(fillFluid, false);

                if (fillValue > 0) {
                    FluidStack drained = te.drain(Fluid.BUCKET_VOLUME, false);
                    if (drained.amount == Fluid.BUCKET_VOLUME) {
                        te.drain(Fluid.BUCKET_VOLUME, true);
                        if (!player.capabilities.isCreativeMode) {
                            bucket.fill(fillFluid, true);
                            /*
                            TODO: Fix creative mode bucket logic
                            if (heldStack.getCount() == 1) {
                                player.inventory.setInventorySlotContents(player.inventory.currentItem, fillStack);
                            } else {
                                TjUtil.consumePlayerItem(player, player.inventory.currentItem);
                                if (!player.inventory.addItemStackToInventory(fillStack)) {
                                    player.dropItem(fillStack, false);
                                } else if (player instanceof EntityPlayerMP) {
                                    ((EntityPlayerMP) player).sendContainerToPlayer(player.inventoryContainer);
                                }
                            }
                             */
                        }
                    }
                } else if (fillFluid.amount >= 1000) {
                    if (fillFluid.isFluidEqual(new FluidStack(ModBlocks.milk.getFluid(), 1))) {
                        te.drain(Fluid.BUCKET_VOLUME, true);
                        bucket.fill(new FluidStack(ModBlocks.milk.getFluid(), Fluid.BUCKET_VOLUME), true);
                        //TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.MILK_BUCKET));
                    } else if (fillFluid.isFluidEqual(new FluidStack(ModBlocks.mushroomSoup.getFluid(), 1))) {
                        te.drain(Fluid.BUCKET_VOLUME, true);
                        bucket.fill(new FluidStack(ModBlocks.mushroomSoup.getFluid(), Fluid.BUCKET_VOLUME), true);
                        //TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(ModItems.bucketMushroom));
                    }
                } else if (player.capabilities.isCreativeMode && heldStack.isItemEqual(new ItemStack(Items.BUCKET))) {
                    te.drain(1000, true);
                }

                return true;
            }

            if (this.useWaterBottles && heldStack.isItemEqual(new ItemStack(Items.GLASS_BOTTLE))) {
                if (!world.isRemote) {
                    te = (ITileWithTank) world.getTileEntity(pos);
                    fillFluid = te.getTank().getFluid();
                    if (fillFluid != null && fillFluid.isFluidEqual(FluidRegistry.getFluidStack(FluidRegistry.WATER.getName(), 1))) {
                        if (AutomagyConfig.getRealWaterBottleAmount() == 0) {
                            TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.POTIONITEM));
                        } else if (te.drainExactAmount(side, AutomagyConfig.getRealWaterBottleAmount(), true)) {
                            TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.POTIONITEM));
                        }
                    }
                }

                return true;
            }

            if (heldStack.isItemEqual(new ItemStack(Items.BOWL))) {
                if (!world.isRemote) {
                    te = (ITileWithTank) world.getTileEntity(pos);
                    fillFluid = te.getTank().getFluid();
                    if (fillFluid != null) {
                        if (fillFluid.isFluidEqual(new FluidStack(ModBlocks.mushroomSoup.getFluid(), 1))) {
                            if (te.drainExactAmount(side, 1000, true)) {
                                TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.MUSHROOM_STEW));
                            }
                        } else if (fillFluid.isFluidEqual(new FluidStack(ModBlocks.vishroomSoup.getFluid(), 1)) && te.drainExactAmount(side, 1000, true)) {
                            TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(ModItems.food, 1, 0));
                        }
                    }
                }

                return true;
            }

            if (heldStack.isItemEqual(new ItemStack(Items.MUSHROOM_STEW))) {
                if (!world.isRemote) {
                    te = (ITileWithTank) world.getTileEntity(pos);
                    if (te.fillExactAmount(side, new FluidStack(ModBlocks.mushroomSoup.getFluid(), 1000))) {
                        TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.BOWL));
                    }
                }

                return true;
            }

            if (heldStack.isItemEqual(new ItemStack(ModItems.food, 1, 0))) {
                if (!world.isRemote) {
                    te = (ITileWithTank) world.getTileEntity(pos);
                    if (te.fillExactAmount(side, new FluidStack(ModBlocks.vishroomSoup.getFluid(), 1000))) {
                        TjUtil.consumePlayerItem(player, player.inventory.currentItem, new ItemStack(Items.BOWL));
                    }
                }

                return true;
            }

            return true;
        }
    }

}
