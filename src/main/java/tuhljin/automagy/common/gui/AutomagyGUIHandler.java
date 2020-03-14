package tuhljin.automagy.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;
import tuhljin.automagy.client.gui.GuiFilter;
import tuhljin.automagy.client.gui.GuiGolemWorkbench;
import tuhljin.automagy.client.gui.GuiHourglassMagic;
import tuhljin.automagy.client.gui.GuiMaw;
import tuhljin.automagy.client.gui.GuiRecipe;
import tuhljin.automagy.client.gui.GuiTallyBox;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.References;
import tuhljin.automagy.common.lib.inventory.IContainsFilter;
import tuhljin.automagy.common.tiles.TileGolemWorkbench;
import tuhljin.automagy.common.tiles.TileHourglassMagic;
import tuhljin.automagy.common.tiles.TileMawBase;
import tuhljin.automagy.common.tiles.TileTallyBase;

public class AutomagyGUIHandler implements IGuiHandler {
    public AutomagyGUIHandler() {
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID) {
            case References.guiIDFilterWhite:
                return new ContainerFilter(false, player);
            case References.guiIDFilterBlack:
                return new ContainerFilter(true, player);
            case References.guiIDRecipe:
                return new ContainerRecipe(player);
            default:
                TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
                switch (ID) {
                    case References.guiIDHourglassMagic:
                        if (te instanceof TileHourglassMagic) {
                            return new ContainerHourglassMagic((TileHourglassMagic) te, player.inventory);
                        }
                    case References.guiIDTally:
                        if (te instanceof TileTallyBase) {
                            return new ContainerTallyBox((TileTallyBase) te, player.inventory);
                        }
                        break;
                    case References.guiIDGolemWorkbench:
                        if (te instanceof TileGolemWorkbench) {
                            return new ContainerGolemWorkbench((TileGolemWorkbench) te, player.inventory);
                        }
                        break;
                    case References.guiIDMaw:
                        if (te instanceof TileMawBase && te instanceof IContainsFilter) {
                            return new ContainerMaw((TileMawBase) te, player.inventory);
                        }
                    default:
                        break;
                }
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case References.guiIDFilterWhite:
                return new GuiFilter(false, player);
            case References.guiIDFilterBlack:
                return new GuiFilter(true, player);
            case References.guiIDRecipe:
                return new GuiRecipe(player);
            default:
                TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
                switch (ID) {
                    case References.guiIDHourglassMagic:
                        if (te instanceof TileHourglassMagic) {
                            return new GuiHourglassMagic((TileHourglassMagic) te, player.inventory);
                        }
                    case References.guiIDTally:
                        if (te instanceof TileTallyBase) {
                            return new GuiTallyBox((TileTallyBase) te, player.inventory);
                        }
                        break;
                    case References.guiIDGolemWorkbench:
                        if (te instanceof TileGolemWorkbench) {
                            return new GuiGolemWorkbench((TileGolemWorkbench) te, player.inventory);
                        }
                        break;
                    case References.guiIDMaw:
                        if (te instanceof TileMawBase && te instanceof IContainsFilter) {
                            return new GuiMaw((TileMawBase) te, player.inventory);
                        }
                    default:
                        break;
                }
                return null;
        }
    }

    public static void openGUI(int id, EntityPlayer player, World world, BlockPos pos) {
        FMLNetworkHandler.openGui(player, Automagy.instance, id, world, pos.getX(), pos.getY(), pos.getZ());
    }
}
