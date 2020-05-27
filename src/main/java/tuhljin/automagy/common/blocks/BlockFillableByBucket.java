package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.AutomagyConfig;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.ITileWithTank;

public abstract class BlockFillableByBucket extends ModTileRenderedBlock {
    public boolean useWaterBottles;

    public BlockFillableByBucket(Material material, MapColor mapColor, boolean useWaterBottles) {
        super(material, mapColor);
        this.useWaterBottles = useWaterBottles && AutomagyConfig.waterBottleAmount != -1;
    }

    public BlockFillableByBucket(Material material, boolean useWaterBottles) {
        this(material, material.func_151565_r(), useWaterBottles);
    }

    public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldStack = player.field_71071_by.func_70448_g();
        if (heldStack != null) {
            FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(heldStack);
            ITileWithTank te;
            if (liquid != null) {
                if (!world.field_72995_K) {
                    te = (ITileWithTank)world.func_175625_s(pos);
                    boolean returnBottle = false;
                    if (heldStack.func_77969_a(new ItemStack(Items.field_151068_bn))) {
                        if (!this.useWaterBottles) {
                            return false;
                        }

                        if (AutomagyConfig.getRealWaterBottleAmount() == 0) {
                            FluidStack fillFluid = te.getTank().getFluid();
                            if (fillFluid != null && fillFluid.isFluidEqual(FluidRegistry.getFluidStack(FluidRegistry.WATER.getName(), 1))) {
                                TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151069_bo));
                            }

                            return true;
                        }

                        if (AutomagyConfig.getRealWaterBottleAmount() > 0) {
                            liquid.amount = AutomagyConfig.getRealWaterBottleAmount();
                            returnBottle = true;
                        }
                    }

                    int amount = te.fill(side, liquid, false);
                    if (amount == liquid.amount) {
                        te.fill(side, liquid, true);
                        if (!player.field_71075_bZ.field_75098_d) {
                            if (returnBottle) {
                                TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151069_bo), true);
                            } else if (FluidContainerRegistry.isBucket(heldStack)) {
                                TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151133_ar), true);
                            } else if (heldStack.func_77969_a(new ItemStack(Items.field_151009_A))) {
                                TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151054_z), true);
                            } else {
                                TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c);
                            }
                        }
                    }
                } else if (!this.useWaterBottles && heldStack.func_77969_a(new ItemStack(Items.field_151068_bn))) {
                    return false;
                }

                return true;
            }

            FluidStack fillFluid;
            if (FluidContainerRegistry.isBucket(heldStack)) {
                if (!world.field_72995_K) {
                    te = (ITileWithTank)world.func_175625_s(pos);
                    fillFluid = te.getTank().getFluid();
                    if (fillFluid == null) {
                        return true;
                    }

                    ItemStack fillStack = FluidContainerRegistry.fillFluidContainer(fillFluid, heldStack);
                    if (fillStack != null) {
                        int amount = FluidContainerRegistry.getFluidForFilledItem(fillStack).amount;
                        FluidStack drained = te.drain(side, amount, false);
                        if (amount == drained.amount) {
                            te.drain(side, amount, true);
                            if (!player.field_71075_bZ.field_75098_d) {
                                if (heldStack.field_77994_a == 1) {
                                    player.field_71071_by.func_70299_a(player.field_71071_by.field_70461_c, fillStack);
                                } else {
                                    TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c);
                                    if (!player.field_71071_by.func_70441_a(fillStack)) {
                                        player.func_71019_a(fillStack, false);
                                    } else if (player instanceof EntityPlayerMP) {
                                        ((EntityPlayerMP)player).func_71120_a(player.field_71069_bz);
                                    }
                                }
                            }
                        }
                    } else if (fillFluid.amount >= 1000) {
                        if (fillFluid.isFluidEqual(new FluidStack(ModBlocks.milk.getFluid(), 1))) {
                            te.drain(side, 1000, true);
                            TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151117_aB));
                        } else if (fillFluid.isFluidEqual(new FluidStack(ModBlocks.mushroomSoup.getFluid(), 1))) {
                            te.drain(side, 1000, true);
                            TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(ModItems.bucketMushroom));
                        }
                    } else if (player.field_71075_bZ.field_75098_d && heldStack.func_77969_a(new ItemStack(Items.field_151133_ar))) {
                        te.drain(side, 1000, true);
                    }
                }

                return true;
            }

            if (this.useWaterBottles && heldStack.func_77969_a(new ItemStack(Items.field_151069_bo))) {
                if (!world.field_72995_K) {
                    te = (ITileWithTank)world.func_175625_s(pos);
                    fillFluid = te.getTank().getFluid();
                    if (fillFluid != null && fillFluid.isFluidEqual(FluidRegistry.getFluidStack(FluidRegistry.WATER.getName(), 1))) {
                        if (AutomagyConfig.getRealWaterBottleAmount() == 0) {
                            TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151068_bn));
                        } else if (te.drainExactAmount(side, AutomagyConfig.getRealWaterBottleAmount(), true)) {
                            TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151068_bn));
                        }
                    }
                }

                return true;
            }

            if (heldStack.func_77969_a(new ItemStack(Items.field_151054_z))) {
                if (!world.field_72995_K) {
                    te = (ITileWithTank)world.func_175625_s(pos);
                    fillFluid = te.getTank().getFluid();
                    if (fillFluid != null) {
                        if (fillFluid.isFluidEqual(new FluidStack(ModBlocks.mushroomSoup.getFluid(), 1))) {
                            if (te.drainExactAmount(side, 1000, true)) {
                                TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151009_A));
                            }
                        } else if (fillFluid.isFluidEqual(new FluidStack(ModBlocks.vishroomSoup.getFluid(), 1)) && te.drainExactAmount(side, 1000, true)) {
                            TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(ModItems.food, 1, 0));
                        }
                    }
                }

                return true;
            }

            if (heldStack.func_77969_a(new ItemStack(Items.field_151009_A))) {
                if (!world.field_72995_K) {
                    te = (ITileWithTank)world.func_175625_s(pos);
                    if (te.fillExactAmount(side, new FluidStack(ModBlocks.mushroomSoup.getFluid(), 1000))) {
                        TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151054_z));
                    }
                }

                return true;
            }

            if (heldStack.func_77969_a(new ItemStack(ModItems.food, 1, 0))) {
                if (!world.field_72995_K) {
                    te = (ITileWithTank)world.func_175625_s(pos);
                    if (te.fillExactAmount(side, new FluidStack(ModBlocks.vishroomSoup.getFluid(), 1000))) {
                        TjUtil.consumePlayerItem(player, player.field_71071_by.field_70461_c, new ItemStack(Items.field_151054_z));
                    }
                }

                return true;
            }
        }

        return false;
    }
}
