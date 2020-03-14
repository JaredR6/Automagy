package tuhljin.automagy.common.tiles;

public class TileMawBase extends ModTileEntity {
    public TileMawBase() {
    }

    public boolean blockedBySignal() {
        return this.isRedstoneSensitive() && this.world.isBlockPowered(this.pos);
    }

    public boolean isRedstoneSensitive() {
        return false;
    }

    public void setRedstoneSensitive(boolean enabled) {
    }
}