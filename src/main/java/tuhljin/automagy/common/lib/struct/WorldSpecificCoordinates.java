package tuhljin.automagy.common.lib.struct;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import javax.annotation.Nullable;

import javax.annotation.Nonnull;

public class WorldSpecificCoordinates {
    public final int dim;
    public final int x;
    public final int y;
    public final int z;

    public WorldSpecificCoordinates(int dimension, int x, int y, int z) {
        this.dim = dimension;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public WorldSpecificCoordinates(@Nonnull World world, int x, int y, int z) {
        this(world.provider.getDimension(), x, y, z);
    }

    public int hashCode() {
        int result = 1;
        result = 31 * result + this.dim;
        result = 31 * result + this.x;
        result = 31 * result + this.y;
        result = 31 * result + this.z;
        return result;
    }

    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (this.getClass() != obj.getClass()) {
            return false;
        } else {
            WorldSpecificCoordinates other = (WorldSpecificCoordinates)obj;
            if (this.dim != other.dim) {
                return false;
            } else if (this.x != other.x) {
                return false;
            } else if (this.y != other.y) {
                return false;
            } else {
                return this.z == other.z;
            }
        }
    }

    @Nonnull
    public BlockPos toBlockPos() {
        return new BlockPos(this.x, this.y, this.z);
    }
}
