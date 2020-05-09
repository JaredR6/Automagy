package tuhljin.automagy.common.tiles;

import net.minecraft.nbt.NBTTagCompound;

public class TileTorchInversion extends ModTileEntity {
    public int redstoneSignalStrength = 0;

    public int getRedstoneSignalStrength() {
        return this.redstoneSignalStrength;
    }

    public void setRedstoneSignalStrength(int strength) {
        this.redstoneSignalStrength = strength;
        this.markForUpdate();
    }

    @Override
    public void writeCommonNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("redstoneSignalStrength", this.redstoneSignalStrength);
    }

    @Override
    public void readCommonNBT(NBTTagCompound nbttagcompound) {
        this.redstoneSignalStrength = nbttagcompound.getInteger("redstoneSignalStrength");
    }
}