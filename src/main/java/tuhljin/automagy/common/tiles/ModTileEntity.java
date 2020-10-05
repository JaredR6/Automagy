package tuhljin.automagy.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;
import tuhljin.automagy.common.blocks.ModBlockWithFacing;

import javax.annotation.Nonnull;

public abstract class ModTileEntity extends TileEntity {
    public ModTileEntity() {
    }

    public void markForUpdate() {
        this.markDirty();
        this.world.notifyBlockUpdate(this.pos, this.world.getBlockState(this.pos), this.world.getBlockState(this.pos), 3);
    }

    @Override
    public void readFromNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.readFromNBT(nbttagcompound);
        this.readServerNBT(nbttagcompound);
        this.readCommonNBT(nbttagcompound);
    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbttagcompound) {
        super.writeToNBT(nbttagcompound);
        this.writeServerNBT(nbttagcompound);
        this.writeCommonNBT(nbttagcompound);
        return nbttagcompound;
    }

    public void readServerNBT(NBTTagCompound nbttagcompound) {
    }

    public void writeServerNBT(NBTTagCompound nbttagcompound) {
    }

    public void readCommonNBT(NBTTagCompound nbttagcompound) {
    }

    public void writeCommonNBT(NBTTagCompound nbttagcompound) {
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbttagcompound = new NBTTagCompound();
        this.writeCommonNBT(nbttagcompound);
        return new SPacketUpdateTileEntity(this.getPos(), -999, nbttagcompound);
    }

    @Override
    public void onDataPacket(NetworkManager net, @Nonnull SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        this.readCommonNBT(pkt.getNbtCompound());
    }

    public void playSoundEffect(@Nonnull SoundEvent soundIn, @Nonnull SoundCategory category, float volume, float pitch) {
        this.world.playSound(null, this.pos, soundIn, category, volume, pitch);
    }

    public boolean shouldRefresh(World world, BlockPos pos, @Nonnull IBlockState oldState, @Nonnull IBlockState newState) {
        return oldState.getBlock() != newState.getBlock();
    }

    @Nullable
    public EnumFacing getFacing() {
        Block block = this.getBlockType();
        if (block instanceof ModBlockWithFacing) {
            IBlockState state = this.world.getBlockState(this.pos);
            if (state.getBlock() == block) {
                return state.getValue(((ModBlockWithFacing)block).FACING);
            }
        }

        return null;
    }

    @Nonnull
    public static int[] getIntArrayFromNbtOrDefault(@Nonnull NBTTagCompound nbttagcompound, @Nonnull String key, int defaultValue, int expectedSize) {
        int[] arr = nbttagcompound.getIntArray(key);
        if (arr.length != expectedSize) {
            int[] newArr = new int[expectedSize];

            for(int i = 0; i < expectedSize; ++i) {
                if (i < arr.length) {
                    newArr[i] = arr[i];
                } else {
                    newArr[i] = defaultValue;
                }
            }

            return newArr;
        } else {
            return arr;
        }
    }

    @Nonnull
    public static boolean[] getBooleanArrayFromNbtOrDefault(@Nonnull NBTTagCompound nbttagcompound, @Nonnull String key, boolean defaultValue, int expectedSize) {
        int[] arr = getIntArrayFromNbtOrDefault(nbttagcompound, key, defaultValue ? 1 : 0, expectedSize);
        boolean[] newArr = new boolean[arr.length];

        for(int i = 0; i < arr.length; ++i) {
            newArr[i] = arr[i] == 1;
        }

        return newArr;
    }

    public static void setBooleanArrayInNbt(@Nonnull NBTTagCompound nbttagcompound, @Nonnull String key, @Nonnull boolean[] arr) {
        int[] intArr = new int[arr.length];

        for(int i = 0; i < arr.length; ++i) {
            intArr[i] = arr[i] ? 1 : 0;
        }

        nbttagcompound.setIntArray(key, intArr);
    }

    @Nullable
    public static EnumFacing getEnumFacingFromNBT(@Nonnull NBTTagCompound nbt, @Nonnull String key, EnumFacing defaultValue, boolean readNull) {
        if (!nbt.hasKey(key)) {
            return defaultValue;
        } else {
            short s = nbt.getShort(key);
            if (readNull && s == -1) {
                return null;
            } else {
                return s >= 0 && s <= EnumFacing.VALUES.length ? EnumFacing.VALUES[s] : defaultValue;
            }
        }
    }

    @Nullable
    public static EnumFacing getEnumFacingFromNBT(@Nonnull NBTTagCompound nbt, @Nonnull String key, EnumFacing defaultValue) {
        return getEnumFacingFromNBT(nbt, key, defaultValue, false);
    }

    public static void setEnumFacingInNBT(@Nonnull NBTTagCompound nbt, @Nonnull String key, @Nullable EnumFacing value) {
        nbt.setShort(key, (short)(value == null ? -1 : value.getIndex()));
    }
}
