package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tuhljin.automagy.common.tiles.TileHourglassMagic;

public class ContainerHourglassMagic extends ModContainerAttached<TileHourglassMagic> {
    private int oldModeSignal = -1;
    private int oldModeSandFlip = -1;
    private boolean oldRepeat = false;
    private int oldModeSignalReact = -1;
    public static final int MINUTES_ADJ = 1;
    public static final int SECONDS_AJD = 2;
    public static final int SIGNAL_INC = 3;
    public static final int SIGNAL_DEC = -3;
    public static final int FLIP_INC = 4;
    public static final int FLIP_DEC = -4;
    public static final int REACT_INC = 5;
    public static final int REACT_DEC = -5;
    public static final int REPEAT_ON = 6;
    public static final int REPEAT_OFF = -6;

    public ContainerHourglassMagic(TileHourglassMagic tile, InventoryPlayer invPlayer) {
        super(tile, invPlayer);
        this.addPlayerInventorySlots(20);
    }

    @Override
    public void addListener(IContainerListener player) {
        super.addListener(player);
        player.sendWindowProperty(this, SIGNAL_INC, this.tile.modeSignal);
        player.sendWindowProperty(this, FLIP_INC, this.tile.modeSandFlip);
        player.sendWindowProperty(this, REPEAT_ON, this.tile.modeRepeat ? 1 : 0);
        player.sendWindowProperty(this, REACT_INC, this.tile.modeSignalReact);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data) {
        switch(id) {
            case SIGNAL_INC:
                this.tile.modeSignal = data;
                break;
            case FLIP_INC:
                this.tile.modeSandFlip = data;
                break;
            case REACT_INC:
                this.tile.modeSignalReact = data;
                break;
            case REPEAT_ON:
                this.tile.modeRepeat = data == 1;
        }

        this.sendDataToGui(id, data);
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener player : this.listeners) {
            if (this.tile.modeSignal != this.oldModeSignal) {
                player.sendWindowProperty(this, SIGNAL_INC, this.tile.modeSignal);
            }

            if (this.tile.modeSandFlip != this.oldModeSandFlip) {
                player.sendWindowProperty(this, FLIP_INC, this.tile.modeSandFlip);
            }

            if (this.tile.modeRepeat != this.oldRepeat) {
                player.sendWindowProperty(this, REACT_INC, this.tile.modeRepeat ? 1 : 0);
            }

            if (this.tile.modeSignalReact != this.oldModeSignalReact) {
                player.sendWindowProperty(this, REPEAT_ON, this.tile.modeSignalReact);
            }
        }

        this.oldModeSignal = this.tile.modeSignal;
        this.oldModeSandFlip = this.tile.modeSandFlip;
        this.oldRepeat = this.tile.modeRepeat;
        this.oldModeSignalReact = this.tile.modeSignalReact;
    }

    @Override
    public boolean enchantItem(EntityPlayer player, int id) {
        if (id > 100) {
            this.tile.adjustTargetSeconds(id - 100);
            return true;
        } else if (id < -100) {
            this.tile.adjustTargetSeconds(id + 100);
            return true;
        } else {
            switch(id) {
                case REPEAT_OFF:
                    this.tile.setModeRepeat(false);
                    return true;
                case REACT_DEC:
                    this.tile.decModeSignalReact();
                    return true;
                case FLIP_DEC:
                    this.tile.decModeSandFlip();
                    return true;
                case SIGNAL_DEC:
                    this.tile.decModeSignal();
                    return true;
                case -2:
                case -1:
                case 0:
                case 1:
                case 2:
                default:
                    return false;
                case SIGNAL_INC:
                    this.tile.incModeSignal();
                    return true;
                case FLIP_INC:
                    this.tile.incModeSandFlip();
                    return true;
                case REACT_INC:
                    this.tile.incModeSignalReact();
                    return true;
                case REPEAT_ON:
                    this.tile.setModeRepeat(true);
                    return true;
            }
        }
    }
}
