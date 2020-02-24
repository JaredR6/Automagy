package tuhljin.automagy.common.blocks;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.MaterialLogic;

public class MaterialWaterproofCircuit extends MaterialLogic {
    public MaterialWaterproofCircuit() {
        super(MapColor.AIR);
        this.setNoPushMobility();
    }

    @Override
    public boolean blocksMovement() {
        return true;
    }
}
