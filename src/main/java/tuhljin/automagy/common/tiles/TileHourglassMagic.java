package tuhljin.automagy.common.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileHourglassMagic extends TileHourglass {
    public int modeSignal = 1;
    public int modeSandFlip = 0;
    public int modeSignalReact = 1;

    public TileHourglassMagic() {
    }

    @Override
    public void writeServerNBT(NBTTagCompound nbttagcompound) {
        super.writeServerNBT(nbttagcompound);
        nbttagcompound.setShort("modeSignal", (short)this.modeSignal);
        nbttagcompound.setShort("modeSandFlip", (short)this.modeSandFlip);
        nbttagcompound.setShort("modeSignalReact", (short)this.modeSignalReact);
        nbttagcompound.setBoolean("modeRepeat", this.modeRepeat);
        nbttagcompound.setBoolean("flipped", this.flipped);
    }

    @Override
    public void readServerNBT(NBTTagCompound nbttagcompound) {
        super.readServerNBT(nbttagcompound);
        this.modeSignal = nbttagcompound.getShort("modeSignal");
        this.modeSandFlip = nbttagcompound.getShort("modeSandFlip");
        if (nbttagcompound.hasKey("modeSignalReact")) {
            this.modeSignalReact = nbttagcompound.getShort("modeSignalReact");
        }

        this.modeRepeat = nbttagcompound.getBoolean("modeRepeat");
        this.flipped = nbttagcompound.getBoolean("flipped");
    }

    @Override
    public int getRedstoneSignalStrength() {
        switch(this.modeSignal) {
            case 1:
                return super.getRedstoneSignalStrength();
            case 2:
                return this.flipped && this.ticksCounted < this.ticksTarget ? 15 : 0;
            case 4:
                if (this.modeRepeat && super.getRedstoneSignalStrength() > 0) {
                    return 0;
                }
            case 3:
                return this.ticksCounted < this.ticksTarget ? 15 : 0;
            default:
                return 0;
        }
    }

    @Override
    protected int onFlipTickCount() {
        if (this.modeSignalReact == 2 && this.receivingSignal || this.modeSignalReact == 3 && !this.receivingSignal) {
            this.markForUpdate();
            if (this.ticksCounted < this.ticksTarget) {
                if (this.comparatorSignalStrength == 0 && this.getRedstoneSignalStrength() == 0) {
                    return this.ticksTarget;
                } else {
                    this.comparatorSignalStrength = 0;
                    return this.ticksTarget + 1;
                }
            } else {
                return 0;
            }
        } else if (this.modeSandFlip == 1) {
            return 0;
        } else if (this.modeSandFlip == 2) {
            if (this.ticksCounted < this.ticksTarget) {
                if (this.comparatorSignalStrength == 0 && this.getRedstoneSignalStrength() == 0) {
                    return this.ticksTarget;
                } else {
                    this.comparatorSignalStrength = 0;
                    return this.ticksTarget + 1;
                }
            } else {
                return 0;
            }
        } else {
            return super.onFlipTickCount();
        }
    }

    @Override
    public void updateRedstoneInput(boolean newSignal) {
        if (this.modeSignalReact == 1) {
            super.updateRedstoneInput(newSignal);
        } else {
            if (this.receivingSignal) {
                if (!newSignal) {
                    this.receivingSignal = false;
                    this.markDirty();
                    if (this.modeSignalReact == 0) {
                        return;
                    }

                    if (this.modeSignalReact == 2) {
                        this.startFlipOnServer(false);
                    } else if (this.ticksCounted < this.ticksTarget) {
                        this.startFlipOnServer(false);
                    }
                }
            } else if (newSignal) {
                this.receivingSignal = true;
                this.markDirty();
                if (this.modeSignalReact == 0) {
                    return;
                }

                if (this.modeSignalReact == 3) {
                    this.startFlipOnServer(false);
                } else if (this.ticksCounted < this.ticksTarget) {
                    this.startFlipOnServer(false);
                }
            }

        }
    }

    @Override
    public void update() {
        if (this.ticksCounted < this.ticksTarget) {
            if (this.receivingSignal) {
                if (this.modeSignalReact == 2) {
                    this.startFlipOnServer(false);
                    return;
                }
            } else if (this.modeSignalReact == 3) {
                this.startFlipOnServer(false);
                return;
            }
        }

        super.update();
    }

    public void adjustTargetSeconds(int amount) {
        int target = this.getTargetTimeSeconds() + amount;
        target = Math.min(target, 3600);
        target = Math.max(target, 1);
        this.setTargetTimeSeconds(target);
    }

    public void incModeSignal() {
        ++this.modeSignal;
        if (this.modeSignal > 4) {
            this.modeSignal = 0;
        }

        this.markDirty();
    }

    public void decModeSignal() {
        --this.modeSignal;
        if (this.modeSignal < 0) {
            this.modeSignal = 4;
        }

        this.markDirty();
    }

    public void incModeSandFlip() {
        ++this.modeSandFlip;
        if (this.modeSandFlip > 2) {
            this.modeSandFlip = 0;
        }

        this.markDirty();
    }

    public void decModeSandFlip() {
        --this.modeSandFlip;
        if (this.modeSandFlip < 0) {
            this.modeSandFlip = 0;
        }

        this.markDirty();
    }

    public void incModeSignalReact() {
        ++this.modeSignalReact;
        if (this.modeSignalReact > 3) {
            this.modeSignalReact = 0;
        }

        this.markDirty();
    }

    public void decModeSignalReact() {
        --this.modeSignalReact;
        if (this.modeSignalReact < 0) {
            this.modeSignalReact = 3;
        }

        this.markDirty();
    }

    public void setModeRepeat(boolean repeat) {
        this.modeRepeat = repeat;
        this.markDirty();
    }
}
