package tuhljin.automagy.common.tiles;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.network.MessageParticles;
import tuhljin.automagy.common.network.MessageParticlesTargeted;

public class TileProcess extends ModTileEntity implements ITickable {
    int completedActions = 0;
    int ticksSinceAction = 0;
    int blocksConsumed = 0;

    public TileProcess() {
    }

    @Override
    public void update() {
        this.ticksSinceAction++;
        int xCoord;
        int yCoord;
        int zCoord;
        if (this.world.isRemote) {
            if (this.ticksSinceAction == 2) {
                xCoord = this.pos.getX();
                yCoord = this.pos.getY();
                zCoord = this.pos.getZ();
                Automagy.proxy.fxWisp3(xCoord + 0.5F + this.world.rand.nextFloat() / 2.0F, yCoord + 0.5F + this.world.rand.nextFloat() / 2.0F, zCoord + 0.5F + this.world.rand.nextFloat() / 2.0F, xCoord + 0.3F + this.world.rand.nextFloat() * 0.4F, yCoord + 0.5F, zCoord + 0.3F + this.world.rand.nextFloat() * 0.4F, 0.5F, 5, true, -0.025F);
            } else if (this.ticksSinceAction >= 4) {
                xCoord = this.pos.getX();
                yCoord = this.pos.getY();
                zCoord = this.pos.getZ();
                Automagy.proxy.fxWisp3(xCoord + 0.5F - this.world.rand.nextFloat() / 2.0F, yCoord + 0.5F - this.world.rand.nextFloat() / 2.0F, zCoord + 0.5F - this.world.rand.nextFloat() / 2.0F, xCoord + 0.3F + this.world.rand.nextFloat() * 0.4F, yCoord + 0.5F, zCoord + 0.3F + this.world.rand.nextFloat() * 0.4F, 0.5F, 5, true, -0.025F);
                this.ticksSinceAction = 0;
            }
        } else if (this.ticksSinceAction > 9) {
            if (this.completedActions < 5 && this.blocksConsumed < 4) {
                EnumFacing dir = EnumFacing.VALUES[this.completedActions + 1];
                BlockPos pos2 = this.pos.offset(dir);
                Block b = TjUtil.getBlock(this.world, pos2);
                if (b == Blocks.OBSIDIAN || ThaumcraftExtension.tileIsPortableHole(this.world.getTileEntity(pos2), Blocks.OBSIDIAN.getDefaultState())) {
                    MessageParticles.sendToClients((short)0, this.world, pos2);
                    MessageParticlesTargeted.sendToClients((short)0, this.world, this.pos, pos2);
                    this.world.setBlockToAir(pos2);
                    ++this.blocksConsumed;
                    this.ticksSinceAction = 0;
                }
            }

            this.completedActions++;
            if (this.completedActions > 9) {
                xCoord = this.pos.getX();
                yCoord = this.pos.getY();
                zCoord = this.pos.getZ();
                this.world.setBlockToAir(this.pos);

                for(int i = 0; i < this.blocksConsumed; i++) {
                    ItemStack stack = new ItemStack(ModBlocks.hungryMaw);
                    float f = this.world.rand.nextFloat() * 0.8F + 0.1F;
                    float f1 = this.world.rand.nextFloat() * 0.8F + 0.1F;
                    float f2 = this.world.rand.nextFloat() * 0.8F + 0.1F;
                    EntityItem entityitem = new EntityItem(this.world, (double)xCoord + (double)f, (double)yCoord + (double)f1, (double)zCoord + (double)f2, stack);
                    float f3 = 0.05F;
                    entityitem.motionX = this.world.rand.nextGaussian() * f3;
                    entityitem.motionY = Math.max(this.world.rand.nextGaussian() * f3 + 0.3F, 0.5D);
                    entityitem.motionZ = this.world.rand.nextGaussian() * f3;
                    this.world.spawnEntity(entityitem);
                }

                this.world.playSound(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D, SoundEvents.ENTITY_PLAYER_BURP, SoundCategory.BLOCKS, 0.5F, this.world.rand.nextFloat() * 0.2F + 0.7F, false);
                MessageParticles.sendToClients((short)1, this.world, xCoord, yCoord, zCoord);
                Automagy.proxy.fxBurst(xCoord + 0.5D, yCoord - 0.5D, zCoord + 0.5D, 2.0F);
            } else {
                this.markDirty();
            }
        }

    }

    @Override
    public void readServerNBT(NBTTagCompound nbttagcompound) {
        this.completedActions = nbttagcompound.getInteger("completedActions");
        this.blocksConsumed = nbttagcompound.getInteger("blocksConsumed");
    }

    @Override
    public void writeServerNBT(NBTTagCompound nbttagcompound) {
        nbttagcompound.setInteger("completedActions", this.completedActions);
        nbttagcompound.setInteger("blocksConsumed", this.blocksConsumed);
    }
}
