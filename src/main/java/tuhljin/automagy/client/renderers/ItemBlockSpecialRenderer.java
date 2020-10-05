package tuhljin.automagy.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.tiles.TileHourglass;
import tuhljin.automagy.common.tiles.TileRedcrystal;
import tuhljin.automagy.common.tiles.TileRedcrystalMerc;
import tuhljin.automagy.common.tiles.TileRemoteComparator;
import tuhljin.automagy.common.tiles.TileTankThirsty;

public class ItemBlockSpecialRenderer extends TileEntityItemStackRenderer {
    public static boolean isRendering = false;
    public static Block currentBlock;
    public static ItemStack currentStack;
    private TileEntityItemStackRenderer hookedTEISR;
    private TileRedcrystal tRedcrystal = new TileRedcrystal();
    private TileRedcrystal tRedcrystalMerc = new TileRedcrystalMerc();
    private TileHourglass tHourglass = new TileHourglass();
    private TileRemoteComparator tRemoteComparator = new TileRemoteComparator();
    private TileTankThirsty tThirtstyTank = new TileTankThirsty();

    public ItemBlockSpecialRenderer(TileEntityItemStackRenderer currentTEISR) {
        this.hookedTEISR = currentTEISR;
    }

    public void renderByItem(ItemStack itemStack) {
        Block block = Block.getBlockFromItem(itemStack.getItem());
        if (block != ModBlocks.redcrystal && block != ModBlocks.redcrystalAmp && block != ModBlocks.redcrystalDim && block != ModBlocks.redcrystalDense && block != ModBlocks.redcrystalRes) {
            if (block == ModBlocks.redcrystalMerc) {
                this.renderTileEntity(this.tRedcrystalMerc, itemStack, block);
            } else if (block != ModBlocks.hourglass && block != ModBlocks.hourglassMagic) {
                if (block == ModBlocks.remoteComparator) {
                    this.renderTileEntity(this.tRemoteComparator, itemStack, block);
                } else if (block == ModBlocks.thirstyTank) {
                    this.renderTileEntity(this.tThirtstyTank, itemStack, block);
                } else {
                    this.hookedTEISR.renderByItem(itemStack);
                }
            } else {
                this.renderTileEntity(this.tHourglass, itemStack, block);
            }
        } else {
            this.renderTileEntity(this.tRedcrystal, itemStack, block);
        }

    }

    public void renderTileEntity(TileEntity te, ItemStack itemStack, Block block) {
        isRendering = true;
        currentBlock = block;
        currentStack = itemStack;
        TileEntityRendererDispatcher.instance.render(te, 0.0D, 0.0D, 0.0D, 0.0F);
        isRendering = false;
        currentBlock = null;
        currentStack = null;
    }

    public static void init() {
        TileEntityItemStackRenderer.instance = new ItemBlockSpecialRenderer(TileEntityItemStackRenderer.instance);
    }
}