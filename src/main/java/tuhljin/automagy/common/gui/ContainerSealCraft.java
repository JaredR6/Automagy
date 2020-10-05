package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.world.World;
import thaumcraft.api.golems.seals.ISealEntity;
import thaumcraft.common.container.slot.SlotGhost;
import thaumcraft.common.golems.client.gui.SealBaseContainer;

import javax.annotation.Nonnull;

public class ContainerSealCraft extends SealBaseContainer {
    protected World world;

    public ContainerSealCraft(@Nonnull InventoryPlayer iinventory, World world, @Nonnull ISealEntity seal) {
        super(iinventory, world, seal);
        this.world = world;
    }

    @Override
    @Nonnull
    protected Slot addSlotToContainer(@Nonnull Slot slotIn) {
        return slotIn.getClass() == SlotGhost.class ? super.addSlotToContainer(new SlotGhostRecipe(slotIn, this.world)) : super.addSlotToContainer(slotIn);
    }
}
