package tuhljin.automagy.common.tiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.casters.IInteractWithCaster;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.items.ItemGlyph;
import tuhljin.automagy.common.items.ModItems;
import tuhljin.automagy.common.lib.AutomagyConfig;
import tuhljin.automagy.common.lib.RedstoneCalc;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjMath;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.lib.compat.CompatibilityManager;
import tuhljin.automagy.common.network.MessageParticles;
import tuhljin.automagy.common.network.MessageParticlesFloat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileTankThirsty extends ModTileEntity implements ITileWithTank, IInteractWithCaster, ITickable {
    public static final int MAX_STEPS = 32;
    public static final int CAPACITY_IN_BUCKETS_DEFAULT = 16;
    public static final int CAPACITY_IN_BUCKETS_PER_UPGRADE = 32;
    public static int DRINK_FREQUENCY_DEFAULT = 30;
    public static float DRINK_FREQUENCY_UPGRADE_MULTIPLIER = 0.6F;
    public static int DRINK_EXTRA_COOLDOWN_OVER_TICK = 600;
    public static int DRINK_FREQUENCY_LONG = 80;
    public static int CONSUME_RATE = 5;
    public static int CONSUME_AMT_PER_UPGRADE = 100;
    public static int SIPHON_RATE = 5;
    public static int SIPHON_AMT_MAX = 150;
    public static int DRINK_FREQUENCY_RAIN = 5;
    public static int DRINK_FREQUENCY_RAIN_SOUND = 80;
    public static FluidStack rainwater;
    public static int MILKING_RANGE;
    public static String KEY_TIME_MILKED;
    public static FluidStack milk;
    public static FluidStack mushroomSoup;
    private static EnumFacing defaultDir;
    public FluidTank tank;
    public boolean receivingSignal = false;
    protected int cooldown;
    protected int lastDirTry;
    protected int rainTicks;
    protected int cooldownConsume;
    protected int cooldownSiphon;
    protected int ticksSinceDrink;
    @Nullable
    public int[] glyphs;
    public boolean voids;
    public boolean redstoneControlled;
    public boolean preserves;
    public boolean siphons;
    public boolean milks;
    public int drinkFrequency;
    public int consumeAmt;
    public double liquidRenderHeight;
    private long nextZap;

    public TileTankThirsty() {
        this.cooldown = DRINK_FREQUENCY_DEFAULT;
        this.lastDirTry = 0;
        this.rainTicks = 0;
        this.cooldownConsume = 0;
        this.cooldownSiphon = 0;
        this.ticksSinceDrink = 0;
        this.glyphs = new int[6];
        this.drinkFrequency = DRINK_FREQUENCY_DEFAULT;
        this.consumeAmt = 0;
        this.liquidRenderHeight = -1.0D;
        this.tank = new FluidTank(CAPACITY_IN_BUCKETS_DEFAULT * 1000);
    }

    public double getFractionFull() {
        return (double)this.tank.getFluidAmount() / (double)this.tank.getCapacity();
    }

    public double getFractionFullRendered() {
        return this.liquidRenderHeight;
    }

    @Override
    public int fill(@Nonnull FluidStack resource, boolean doFill) {
        if (this.voids) {
            return this.fillVoiding(resource, doFill);
        } else {
            int i = this.tank.fill(resource, doFill);
            if (doFill && i != 0) {
                this.markForUpdate();
            }

            return i;
        }
    }

    public int fillVoiding(@Nonnull FluidStack resource, boolean doFill) {
        FluidStack fluid = this.tank.getFluid();
        int prevAmount = 0;
        if (fluid != null) {
            prevAmount = fluid.amount;
            fluid.amount = 0;
        }

        int i = this.tank.fill(resource, doFill);
        if (fluid != null) {
            fluid.amount = Math.min(fluid.amount + prevAmount, this.tank.getCapacity());
        }

        if (i == 0) {
            return 0;
        } else {
            if (doFill && (fluid == null || fluid.amount != prevAmount)) {
                this.markForUpdate();
            }

            return resource.amount;
        }
    }

    @Override
    public FluidStack drain(@Nullable FluidStack resource, boolean doDrain) {
        return resource != null && resource.isFluidEqual(this.tank.getFluid()) ? this.drain(resource.amount, doDrain) : null;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        if (this.preserves) {
            int amount = this.tank.getFluidAmount();
            if (amount > 0) {
                maxDrain = Math.min(maxDrain, amount - 1);
            }
        }

        FluidStack fluid = this.tank.drain(maxDrain, doDrain);
        if (doDrain && fluid != null) {
            this.markForUpdate();
        }

        return fluid;
    }

    public boolean canFill(EnumFacing from, Fluid fluid) {
        return true;
    }

    public boolean canDrain(EnumFacing from, Fluid fluid) {
        return true;
    }

    @Nonnull
    public IFluidTankProperties[] getTankProperties() {
        return this.tank.getTankProperties();
    }

    @Nonnull
    public FluidTankInfo[] getTankInfo(EnumFacing from) {
        return new FluidTankInfo[]{this.tank.getInfo()};
    }

    public FluidTank getTank() {
        return this.tank;
    }

    public boolean drainExactAmount(int amount, boolean doDrain) {
        FluidStack drained = this.drain(amount, false);
        if (drained != null && drained.amount == amount) {
            if (doDrain) {
                this.drain(amount, true);
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean fillExactAmount(EnumFacing facing, @Nonnull FluidStack fluid) {
        return fillExactAmount(fluid);
    }

    public boolean fillExactAmount(@Nonnull FluidStack fluid) {
        int amount = this.fill(fluid, false);
        if (amount == fluid.amount) {
            this.fill(fluid, true);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean drainExactAmount(EnumFacing dir, int amount, boolean doDrain) {

        return drainExactAmount(amount, doDrain);
    }

    public int getComparatorStrength() {
        return RedstoneCalc.getRedstoneSignalStrengthFromValues(this.tank.getFluidAmount(), this.tank.getCapacity());
    }

    @Override
    public void readCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKey("Empty")) {
            this.tank.setFluid(null);
        } else {
            this.tank.readFromNBT(nbttagcompound);
        }

        this.glyphs = getIntArrayFromNbtOrDefault(nbttagcompound, "glyphs", 0, 6);
        this.updateGlyphEffects();
        this.receivingSignal = nbttagcompound.getBoolean("receivingSignal");
    }

    @Override
    public void writeCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        this.tank.writeToNBT(nbttagcompound);
        nbttagcompound.setIntArray("glyphs", this.glyphs);
        nbttagcompound.setBoolean("receivingSignal", this.receivingSignal);
    }

    public void updateGlyphEffects() {
        int capacity = CAPACITY_IN_BUCKETS_DEFAULT;
        this.consumeAmt = 0;
        this.voids = false;
        this.redstoneControlled = false;
        this.preserves = false;
        this.siphons = false;
        this.milks = false;
        float drinkRate = DRINK_FREQUENCY_DEFAULT;

        for (int glyph : this.glyphs) {
            switch (glyph) {
                case ItemGlyph.VOID:
                    this.voids = true;
                    break;
                case ItemGlyph.CONSUMPTION:
                    this.consumeAmt += CONSUME_AMT_PER_UPGRADE;
                    break;
                case ItemGlyph.SIPHONING:
                    this.siphons = true;
                case ItemGlyph.ENVY:
                default:
                    break;
                case ItemGlyph.TEMPERANCE:
                    this.redstoneControlled = true;
                    break;
                case ItemGlyph.PRESERVATION:
                    this.preserves = true;
                    break;
                case ItemGlyph.GUZZLER:
                    drinkRate *= DRINK_FREQUENCY_UPGRADE_MULTIPLIER;
                    break;
                case ItemGlyph.RESERVOIR:
                    capacity += MAX_STEPS;
                    break;
                case ItemGlyph.BOVINE:
                    this.milks = true;
            }
        }

        this.tank.setCapacity(1000 * capacity);
        this.drinkFrequency = Math.round(drinkRate);
    }

    public int getGlyph(@Nonnull EnumFacing side) {
        return this.glyphs[side.getIndex()];
    }

    public void setFluid(FluidStack fluidStack) {
        this.tank.setFluid(fluidStack);
        this.markForUpdate();
    }

    public void setGlyphs(@Nullable int[] glyphs) {
        if (glyphs != null && glyphs.length == 6) {
            this.glyphs = glyphs;
            if (this.world != null) {
                this.updateGlyphEffects();
                this.markForUpdate();
            }

        } else {
            throw new IllegalArgumentException("Argument must be an array of length 6.");
        }
    }

    public boolean installGlyph(int glyph, @Nonnull EnumFacing side) {
        int i = side.getIndex();
        if (glyph != 0 && this.glyphs[i] == 0) {
            this.glyphs[i] = glyph;
            this.updateGlyphEffects();
            this.markForUpdate();
            return true;
        } else {
            return false;
        }
    }

    public int removeGlyph(@Nonnull EnumFacing side) {
        int i = side.getIndex();
        int drop = this.glyphs[i];
        if (drop != 0) {
            if (drop == 8) {
                int newCapacity = this.tank.getCapacity() - CAPACITY_IN_BUCKETS_PER_UPGRADE * 1000;
                if (newCapacity < this.tank.getFluidAmount()) {
                    return -1;
                }
            }

            this.glyphs[i] = 0;
            this.updateGlyphEffects();
            this.markForUpdate();
        }

        return drop;
    }

    public void update() {
        if (!this.world.isRemote) {
            int dirTry;
            if (this.consumeAmt > 0) {
                --this.cooldownConsume;
                if (this.cooldownConsume < 1) {
                    this.cooldownConsume = CONSUME_RATE;
                    dirTry = this.tank.getFluidAmount();
                    if (dirTry > (this.preserves ? 1 : 0)) {
                        int toConsume = this.preserves ? Math.min(this.consumeAmt, dirTry - 1) : this.consumeAmt;
                        this.drain(toConsume, true);
                    }
                }
            }

            EnumFacing dir;
            if (this.siphons) {
                --this.cooldownSiphon;
                if (this.cooldownSiphon < 1) {
                    this.cooldownSiphon = SIPHON_RATE;
                    dirTry = this.tank.getFluidAmount();
                    if (this.preserves) {
                        --dirTry;
                    }

                    if (dirTry > 0) {
                        Set<EnumFacing> siphonDirs = new HashSet<>();

                        int amtTaken;
                        for(amtTaken = 0; amtTaken < this.glyphs.length; ++amtTaken) {
                            if (this.glyphs[amtTaken] == 3) {
                                dir = EnumFacing.getFront(amtTaken);
                                if (this.siphonInto(dir, 1, false) > 0) {
                                    siphonDirs.add(dir);
                                }
                            }
                        }

                        if (siphonDirs.size() > 0) {
                            amtTaken = 0;
                            int share = Math.min(dirTry, SIPHON_AMT_MAX) / siphonDirs.size();
                            if (share > 0) {
                                for (EnumFacing dir2 : siphonDirs) {
                                    amtTaken += this.siphonInto(dir2, share, true);
                                }

                                this.drain(amtTaken, true);
                            }
                        }
                    }
                }
            }

            if (this.redstoneControlled && !this.receivingSignal) {
                if (this.cooldown > 0) {
                    --this.cooldown;
                }

                return;
            }

            --this.cooldown;
            ++this.ticksSinceDrink;
            if (this.cooldown < 1) {
                if (this.milks) {
                    if (this.canMilk()) {
                        dirTry = this.lastDirTry;

                        do {
                            ++dirTry;
                            if (dirTry > 5) {
                                dirTry = 0;
                            }

                            if (this.glyphs[dirTry] == 9) {
                                if (this.doMilking(dirTry)) {
                                    this.ticksSinceDrink = 0;
                                }
                                break;
                            }
                        } while(dirTry != this.lastDirTry);

                        this.lastDirTry = dirTry;
                    }
                } else if (this.voids || this.tank.getCapacity() - this.tank.getFluidAmount() >= 1000) {
                    dirTry = this.lastDirTry;
                    Block block = null;
                    Fluid fluidFound = null;
                    dir = null;
                    BlockPos thePos = null;

                    do {
                        ++dirTry;
                        if (dirTry > 5) {
                            dirTry = 0;
                        }

                        if (this.glyphs[dirTry] != 3) {
                            dir = EnumFacing.getFront(dirTry);
                            thePos = this.pos.offset(dir);
                            if (this.glyphs[dirTry] == 4) {
                                TileEntity te = this.world.getTileEntity(thePos);
                                if (te instanceof IFluidHandler) {
                                    if (this.pilferFluid((IFluidHandler)te, dir)) {
                                        this.ticksSinceDrink = 0;
                                    }
                                    break;
                                }
                            } else {
                                block = TjUtil.getBlock(this.world, thePos);
                                fluidFound = this.getFluidIfValidBlock(block);
                            }
                        }
                    } while(fluidFound == null && dirTry != this.lastDirTry);

                    this.lastDirTry = dirTry;
                    if (fluidFound != null) {
                        boolean ok = false;
                        int added = 1000;
                        if (AutomagyConfig.thirstyTankPreserveInfiniteWater && fluidFound.getBlock() == Blocks.WATER && this.isSourceBlock(thePos)) {
                            if (!this.voids || this.tank.getFluidAmount() < this.tank.getCapacity()) {
                                ok = true;
                                MessageParticles.sendToClients((short)2, this.world, thePos);
                            }
                        } else {
                            TileTankThirsty.SourceLiquidResult result = this.findSourceLiquid(block, thePos, dir);
                            if (result != null) {
                                Block source = TjUtil.getBlock(this.world, result.pos);
                                if (source instanceof BlockFluidBase) {
                                    ((BlockFluidBase)source).drain(this.world, result.pos, true);
                                } else {
                                    this.world.setBlockToAir(result.pos);
                                }

                                ok = true;
                                added = result.amount;
                                this.playSoundEffect(SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.2F, this.world.rand.nextFloat() * 0.5F + this.world.rand.nextFloat() * 0.25F + 0.5F);
                                MessageParticles.sendToClients((short)2, this.world, result.pos);
                            }
                        }

                        if (ok) {
                            this.fill(new FluidStack(fluidFound, added), true);
                            this.ticksSinceDrink = 0;
                            this.playSoundEffect(SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.2F, this.world.rand.nextFloat() * 0.25F + this.world.rand.nextFloat() * 0.25F + 0.3F);
                        }
                    }
                }

                this.cooldown = this.ticksSinceDrink > DRINK_EXTRA_COOLDOWN_OVER_TICK ? DRINK_FREQUENCY_LONG : this.drinkFrequency;
            }

            if (!this.milks && AutomagyConfig.thirstyTankDrinksRain && TjUtil.isPrecipitationAt(this.world, this.pos) && this.world.getPrecipitationHeight(this.pos).getY() == this.pos.getY() + 1) {
                ++this.rainTicks;
                if (this.rainTicks % DRINK_FREQUENCY_RAIN == 0) {
                    dirTry = this.fill(rainwater, true);
                    if (dirTry != 0 && this.rainTicks % DRINK_FREQUENCY_RAIN_SOUND == 0) {
                        this.playSoundEffect(SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.2F, this.world.rand.nextFloat() * 0.25F + this.world.rand.nextFloat() * 0.25F + 0.3F);
                    }

                    if (this.drinkFrequency < DRINK_FREQUENCY_DEFAULT && this.glyphs[this.world.rand.nextInt(this.glyphs.length)] == 7) {
                        this.rainTicks = (int)((float)this.rainTicks + (float)DRINK_FREQUENCY_RAIN * 0.6666667F);
                    }
                }
            }
        } else {
            double height = this.getFractionFull();
            if (this.liquidRenderHeight != height) {
                if (this.liquidRenderHeight != -1.0D && !TjUtil.areDoublesAlmostEqual_lowprec(height, this.liquidRenderHeight)) {
                    this.liquidRenderHeight = TjMath.approachLinear(this.liquidRenderHeight, height, 0.03D);
                } else {
                    this.liquidRenderHeight = height;
                }
            }
        }

    }

    public boolean canMilk() {
        if (this.voids) {
            if (this.tank.getFluidAmount() >= this.tank.getCapacity()) {
                return false;
            }
        } else if (this.tank.getFluidAmount() + 1000 > this.tank.getCapacity()) {
            return false;
        }

        FluidStack fluid = this.tank.getFluid();
        return fluid == null || fluid.getFluid() == this.getFluidStackMilk().getFluid() || fluid.getFluid() == this.getFluidStackMushroomSoup().getFluid();
    }

    public boolean doMilking(int dir) {
        int xCoord = this.pos.getX();
        int yCoord = this.pos.getY();
        int zCoord = this.pos.getZ();
        double minx = xCoord + 0.5D - (MILKING_RANGE + 0.5D);
        double miny = yCoord + 0.5D - (MILKING_RANGE + 0.5D);
        double minz = zCoord + 0.5D - (MILKING_RANGE + 0.5D);
        double maxx = xCoord + 0.5D + MILKING_RANGE + 0.5D;
        double maxy = yCoord + 0.5D + MILKING_RANGE + 0.5D;
        double maxz = zCoord + 0.5D + MILKING_RANGE + 0.5D;
        switch(dir) {
            case 0:
                maxy = yCoord;
                miny = yCoord - 1 - MILKING_RANGE * 2;
                break;
            case 1:
                miny = yCoord + 1;
                maxy = yCoord + 2 + MILKING_RANGE * 2;
                break;
            case 2:
                maxz = zCoord;
                minz = zCoord - 1 - MILKING_RANGE * 2;
                break;
            case 3:
                minz = zCoord + 1;
                maxz = zCoord + 2 + MILKING_RANGE * 2;
                break;
            case 4:
                maxx = xCoord;
                minx = xCoord - 1 - MILKING_RANGE * 2;
                break;
            case 5:
                minx = xCoord + 1;
                maxx = xCoord + 2 + MILKING_RANGE * 2;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + dir);
        }

        AxisAlignedBB box = new AxisAlignedBB(minx, miny, minz, maxx, maxy, maxz);
        List<EntityCow> entities = this.world.getEntitiesWithinAABB(EntityCow.class, box);
        if (entities.size() > 0) {
            long t = this.world.getTotalWorldTime();

            for (EntityCow cow : entities) {
                NBTTagCompound data = cow.getEntityData();
                if (t - data.getLong(KEY_TIME_MILKED) >= (long) AutomagyConfig.milkingCooldownPerCow) {
                    int amt = this.fill(cow instanceof EntityMooshroom ? this.getFluidStackMushroomSoup() : this.getFluidStackMilk(), true);
                    if (amt > 0) {
                        data.setLong(KEY_TIME_MILKED, t);
                        cow.playSound(SoundEvents.ENTITY_COW_HURT, 0.4F, (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F + 1.0F);
                        MessageParticlesFloat.sendToClients((short) 2, this.world, cow.posX - cow.width / 2.0D, cow.posY + cow.height / 2.0D, cow.posZ - cow.width / 2.0D);
                        this.playSoundEffect(SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.2F, this.world.rand.nextFloat() * 0.25F + this.world.rand.nextFloat() * 0.25F + 0.3F);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean pilferFluid(@Nonnull IFluidHandler otherTank, @Nonnull EnumFacing dir) {
        int amount = 1000;
        if (!this.voids) {
            amount = Math.min(amount, this.tank.getCapacity() - this.tank.getFluidAmount());
        }

        if (amount > 0) {
            if (this.tank.getFluid() != null) {
                Fluid drainable = TjUtil.canDrainTank(otherTank, dir.getOpposite(), this.tank.getFluid().getFluid());
                if (drainable != null) {
                    FluidStack gained = otherTank.drain(new FluidStack(drainable, amount), true);
                    if (gained != null && gained.amount > 0) {
                        this.fill(gained, true);
                        MessageParticles.sendToClients((short) 2, this.world, this.pos.offset(dir));
                        this.playSoundEffect(SoundEvents.ENTITY_GENERIC_DRINK, SoundCategory.BLOCKS, 0.2F, this.world.rand.nextFloat() * 0.25F + this.world.rand.nextFloat() * 0.25F + 0.3F);
                        return true;
                    }
                }
            }
        }

        return false;
    }

    protected int siphonInto(@Nonnull EnumFacing dir, int amount, boolean doFill) {
        TileEntity te = this.world.getTileEntity(this.pos.offset(dir));
        if (te instanceof IFluidHandler) {
            FluidStack fluid = this.tank.getFluid();
            if (fluid == null) {
                return 0;
            } else {
                IFluidHandler otherTank = (IFluidHandler)te;
                if (otherTank.fill(fluid, false) == 0) {
                    return 0;
                } else {
                    fluid = fluid.copy();
                    fluid.amount = amount;
                    return otherTank.fill(fluid, doFill);
                }
            }
        } else {
            return CompatibilityManager.fillBotaniaPetalApothecary(te, doFill) ? amount : 0;
        }
    }

    @Nullable
    public Fluid getFluidIfValidBlock(Block block) {
        Fluid fluid = FluidRegistry.lookupFluidForBlock(block);
        if (fluid != null) {
            FluidStack tankFluid = this.tank.getFluid();
            if (tankFluid == null) {
                return fluid;
            } else {
                return tankFluid.getFluid() == fluid ? fluid : null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public TileTankThirsty.SourceLiquidResult findSourceLiquid(Block fluidBlock, @Nonnull BlockPos thePos, @Nonnull EnumFacing dir) {
        ArrayList<EnumFacing> list = new ArrayList<>();

        for (EnumFacing d : EnumFacing.VALUES) {
            if (d != dir && d != EnumFacing.UP) {
                list.add(d);
            }
        }

        Collections.shuffle(list);
        list.add(0, dir);
        if (dir != EnumFacing.UP) {
            list.add(1, EnumFacing.UP);
        }

        return this.findSourceLiquid(fluidBlock, thePos, list, dir, 1, new HashSet<>());
    }

    @Nullable
    private TileTankThirsty.SourceLiquidResult findSourceLiquid(Block fluidBlock, @Nonnull BlockPos coord, @Nonnull ArrayList<EnumFacing> dirs, @Nonnull EnumFacing nextDir, int steps, @Nonnull HashSet<BlockPos> seen) {
        if (seen.contains(coord)) {
            return null;
        } else {
            seen.add(coord);
            Block block = TjUtil.getBlock(this.world, coord);
            if (block == fluidBlock) {
                double distFurthest = this.pos.distanceSq(coord);
                if (steps < MAX_STEPS) {
                    TileTankThirsty.SourceLiquidResult bestResult = null;
                    TileTankThirsty.SourceLiquidResult result = this.findSourceLiquid(fluidBlock, coord.offset(nextDir), dirs, nextDir, steps + 1, seen);
                    if (result != null) {
                        distFurthest = result.distance;
                        bestResult = result;
                    }

                    while (true) {
                        for (EnumFacing dir : dirs) {
                            if (dir != nextDir) {
                                result = this.findSourceLiquid(fluidBlock, coord.offset(dir), dirs, dir, steps + 1, seen);
                                if (result != null) {
                                    if (bestResult == null || result.distance > distFurthest) {
                                        distFurthest = result.distance;
                                        bestResult = result;
                                    }
                                }
                            }
                        }

                        if (bestResult != null) {
                            return bestResult;
                        }
                    }
                }

                if (this.isSourceBlock(coord)) {
                    int added = 1000;
                    if (block instanceof BlockFluidBase) {
                        if (!((BlockFluidBase)block).canDrain(this.world, coord)) {
                            return null;
                        }

                        FluidStack amt = ((BlockFluidBase)block).drain(this.world, coord, false);
                        added = amt.amount;
                        if (added < 1) {
                            return null;
                        }
                    }

                    return new SourceLiquidResult(block, coord, distFurthest, added);
                }
            }

            return null;
        }
    }

    public boolean onCasterRightClick(@Nonnull World world, ItemStack caster, @Nonnull EntityPlayer player, @Nonnull BlockPos pos, @Nonnull EnumFacing side, EnumHand hand) {
        if (player.isSneaking() && this.getGlyph(side) != 0) {
            if (!world.isRemote) {
                int glyph = this.removeGlyph(side);
                if (glyph > 0) {
                    TjUtil.dropItemIntoWorldSimple(new ItemStack(ModItems.tankGlyph, 1, glyph), world, pos.getX() + 0.5D + side.getFrontOffsetX() * 0.6D, pos.getY() + 0.5D + side.getFrontOffsetY() * 0.6D, pos.getZ() + 0.5D + side.getFrontOffsetZ() * 0.6D);
                    world.playSound(null, player.getPosition(), SoundsTC.zap, SoundCategory.BLOCKS, 0.5F, 1.0F);
                } else if (glyph == -1) {
                    long currentTime = System.currentTimeMillis();
                    if (currentTime >= this.nextZap) {
                        this.nextZap = currentTime + 1000L;
                        player.attackEntityFrom(DamageSource.MAGIC, 1.0F);
                        ThaumcraftExtension.zapEntity(this.pos, player, Aspect.WATER);
                        world.playSound(null, player.getPosition(), SoundsTC.zap, SoundCategory.BLOCKS, 0.5F, 1.0F);
                    }
                }
            }
            player.swingArm(hand);
            return false;
        } else {
            return true;
        }
    }

    public void onUsingCasterTick(ItemStack caster, EntityPlayer player, int count) {
    }

    public void onCasterStoppedUsing(ItemStack caster, World world, EntityPlayer player, int count) {
    }

    protected FluidStack getFluidStackMilk() {
        if (milk == null) {
            milk = new FluidStack(FluidRegistry.getFluid("milk"), 1000);
        }

        return milk;
    }

    protected FluidStack getFluidStackMushroomSoup() {
        if (mushroomSoup == null) {
            mushroomSoup = new FluidStack(FluidRegistry.getFluid("mushroomsoup"), 1000);
        }

        return mushroomSoup;
    }

    public void updateRedstoneInput(boolean newSignal) {
        if (this.receivingSignal) {
            if (!newSignal) {
                this.receivingSignal = false;
                this.markDirty();
            }
        } else if (newSignal) {
            this.receivingSignal = true;
            this.markDirty();
        }

    }

    private boolean isSourceBlock(@Nonnull BlockPos blockPos) {
        return TjUtil.isSourceBlock(this.world, blockPos);
    }

    static {
        rainwater = new FluidStack(FluidRegistry.WATER, 10);
        MILKING_RANGE = 2;
        KEY_TIME_MILKED = "timeMilked";
        defaultDir = EnumFacing.DOWN;
    }

    public static class SourceLiquidResult {
        Block block;
        BlockPos pos;
        double distance;
        int amount;

        public SourceLiquidResult(Block block, BlockPos pos, double distance, int amount) {
            this.block = block;
            this.pos = pos;
            this.distance = distance;
            this.amount = amount;
        }
    }
}
