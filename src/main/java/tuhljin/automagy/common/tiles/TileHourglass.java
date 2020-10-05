package tuhljin.automagy.common.tiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.Timer;
import tuhljin.automagy.common.lib.RedstoneCalc;
import tuhljin.automagy.common.network.MessageHourglassFlipped;

import javax.annotation.Nonnull;

public class TileHourglass extends ModTileEntity implements ITickable {
    public final float SECONDS_COOLDOWN_BETWEEN_FLIPS = 0.25F;
    public final int REDSTONE_PULSE_LENGTH = 5;
    protected int ticksTarget = 100;
    protected int ticksCounted;
    protected int ticksSendingPulse;
    public boolean receivingSignal;
    public long timeDidFlip;
    public boolean flipped;
    public boolean modeRepeat;
    protected int comparatorSignalStrength;

    public TileHourglass() {
        this.ticksCounted = this.ticksTarget;
        this.ticksSendingPulse = 0;
        this.receivingSignal = false;
        this.timeDidFlip = 0L;
        this.flipped = false;
        this.modeRepeat = false;
        this.comparatorSignalStrength = 0;
    }

    public int getRedstoneSignalStrength() {
        return this.ticksSendingPulse > 0 ? RedstoneCalc.MAX_POWER : 0;
    }

    public int getComparatorSignalStrength() {
        return this.comparatorSignalStrength;
    }

    public int getTargetTimeSeconds() {
        return this.ticksTarget / 20;
    }

    public void setTargetTimeSeconds(int target) {
        target *= 20;
        if (target < 1) {
            target = 1;
        }

        if (target != this.ticksTarget) {
            if (this.ticksCounted >= this.ticksTarget) {
                this.ticksCounted = target;
            } else if (this.ticksCounted >= target) {
                this.ticksCounted = target - 1;
            }

            this.ticksTarget = target;
            if (!this.world.isRemote) {
                this.markForUpdate();
            }
        }

    }

    public int getRemainingSeconds() {
        return MathHelper.ceil((this.ticksTarget - this.ticksCounted) / 20.0F);
    }

    public float percentageComplete() {
        return (float)this.ticksCounted / (float)this.ticksTarget * 100.0F;
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
            this.startFlipOnServer(false);
        }

    }

    public boolean startFlipOnServer(boolean timeCheck) {
        float secondsCooldown = timeCheck ? 0.25F : 0.125F;
        long time = System.nanoTime();
        if ((float)(time - this.timeDidFlip) > secondsCooldown * 1.0E9F) {
            this.flipped = !this.flipped;
            this.timeDidFlip = time;
            this.ticksCounted = this.onFlipTickCount();
            if (this.ticksCounted > this.ticksTarget) {
                this.ticksCounted = this.ticksTarget;
                this.notifyNeighbors();
            }

            MessageHourglassFlipped.sendToClients(this.world, this.pos.getX(), this.pos.getY(), this.pos.getZ(), this.flipped);
            this.markForUpdate();
            return true;
        } else {
            return false;
        }
    }

    public void receiveFlipFromServer(boolean flipState) {
        this.flipped = flipState;
        this.timeDidFlip = System.nanoTime();
        this.ticksCounted = this.onFlipTickCount();
        if (this.ticksCounted > this.ticksTarget) {
            this.ticksCounted = this.ticksTarget;
        }

    }

    @Override
    public void update() {
        boolean neighborsNeedNotification = false;
        if (this.ticksSendingPulse > 0) {
            --this.ticksSendingPulse;
            if (this.ticksSendingPulse == 0) {
                neighborsNeedNotification = true;
            }
        }

        if (this.ticksCounted < this.ticksTarget) {
            ++this.ticksCounted;
            if (!this.world.isRemote && this.ticksCounted == this.ticksTarget) {
                this.ticksSendingPulse = 5;
                this.comparatorSignalStrength = 0;
                neighborsNeedNotification = true;
                this.markForUpdate();
                if (this.modeRepeat) {
                    this.startFlipOnServer(false);
                }
            } else {
                int css = RedstoneCalc.getRedstoneSignalStrengthFromValues(this.ticksCounted, this.ticksTarget);
                if (this.comparatorSignalStrength != css) {
                    this.comparatorSignalStrength = css;
                    neighborsNeedNotification = true;
                }
            }
        }

        if (neighborsNeedNotification) {
            this.notifyNeighbors();
        }

    }

    protected int onFlipTickCount() {
        return this.ticksTarget - this.ticksCounted;
    }

    protected void notifyNeighbors() {
        this.world.notifyNeighborsOfStateChange(this.getPos(), this.getBlockType(), true);
    }

    @Override
    public void writeCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("ticksTarget", this.ticksTarget);
        nbttagcompound.setInteger("ticksCounted", this.ticksCounted);
        nbttagcompound.setShort("ticksSendingSignal", (short)this.ticksSendingPulse);
        nbttagcompound.setBoolean("receivingSignal", this.receivingSignal);
        nbttagcompound.setShort("comparatorSignalStrength", (short)this.comparatorSignalStrength);
    }

    @Override
    public void readCommonNBT(@Nonnull NBTTagCompound nbttagcompound) {
        this.ticksTarget = nbttagcompound.getInteger("ticksTarget");
        this.ticksCounted = nbttagcompound.getInteger("ticksCounted");
        this.ticksSendingPulse = nbttagcompound.getShort("ticksSendingSignal");
        this.receivingSignal = nbttagcompound.getBoolean("receivingSignal");
        this.comparatorSignalStrength = nbttagcompound.getShort("comparatorSignalStrength");
    }
}
