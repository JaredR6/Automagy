package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.common.tiles.misc.TileHole;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.ThaumcraftExtension;

import javax.annotation.Nonnull;

public class MessageParticles extends BlockTiedMessageToClient<MessageParticles> {
    public static final short BREAKBLOCK = 0;
    public static final short SMOKE = 1;
    public static final short GRABLIQUID = 2;
    public static final short TAINTSPLASH = 3;
    protected short id;
    protected short count;

    public MessageParticles(short id, short count, int dim, int x, int y, int z) {
        super(dim, x, y, z);
        this.id = id;
        this.count = count;
    }

    public MessageParticles(short id, int dim, int x, int y, int z) {
        this(id, (short)0, dim, x, y, z);
    }

    @Override
    public void fromBytes(@Nonnull ByteBuf buf) {
        super.fromBytes(buf);
        this.id = buf.readShort();
        this.count = buf.readShort();
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        super.toBytes(buf);
        buf.writeShort(this.id);
        buf.writeShort(this.count);
    }

    @Override
    public void onReceived(@Nonnull World world, @Nonnull BlockPos pos) {
        int i;
        switch(this.id) {
            case BREAKBLOCK:
                this.showBreakBlock(world, pos);
                break;
            case SMOKE:
                if (this.count == 0) {
                    this.count = 35;
                }

                for(i = 0; i < this.count; ++i) {
                    this.showSmoke(world);
                }

                return;
            case GRABLIQUID:
                int color = 7;
                this.blockSparkle(world, color, 7);
                break;
            case TAINTSPLASH:
                Automagy.logDebug("TAINTSPLASH");
                if (this.count == 0) {
                    this.count = 20;
                }

                for(i = 0; i < this.count; ++i) {
                    ThaumcraftExtension.taintSplash(pos);
                    ThaumcraftExtension.taintSplash(pos.up());
                    ThaumcraftExtension.taintSplash(pos.up(2));
                }
        }

    }

    @SideOnly(Side.CLIENT)
    private void showBreakBlock(@Nonnull World world, @Nonnull BlockPos pos) {
        try {
            IBlockState state = world.getBlockState(pos);
            if (state.getBlock() == BlocksTC.hole) {
                TileEntity te = world.getTileEntity(pos);
                if (te instanceof TileHole) {
                    state = ((TileHole)te).oldblock;
                }
            }

            Minecraft.getMinecraft().effectRenderer.addBlockDestroyEffects(pos, state);
        } catch (Exception ignored) {
        }

    }

    private void showSmoke(@Nonnull World world) {
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.x + 0.5D, this.y + 0.25D + world.rand.nextDouble(), this.z + 0.5D, world.rand.nextGaussian() * 0.05D, 0.005D, world.rand.nextGaussian() * 0.05D);
    }

    private void blockSparkle(World world, int color, int count) {
        Automagy.proxy.fxOldBlockSparkle(world, this.x, this.y, this.z, color, count);
    }

    public static void sendToClients(short id, int count, @Nonnull World world, int x, int y, int z) {
        if (!world.isRemote) {
            int dim = world.provider.getDimension();
            TargetPoint point = new TargetPoint(dim, x, y, z, PacketHandler.DEFAULT_PACKET_RANGE);
            PacketHandler.INSTANCE.sendToAllAround(new MessageParticles(id, (short)count, dim, x, y, z), point);
        }
    }

    public static void sendToClients(short id, @Nonnull World world, int x, int y, int z) {
        if (!world.isRemote) {
            int dim = world.provider.getDimension();
            TargetPoint point = new TargetPoint(dim, x, y, z, PacketHandler.DEFAULT_PACKET_RANGE);
            PacketHandler.INSTANCE.sendToAllAround(new MessageParticles(id, dim, x, y, z), point);
        }
    }

    public static void sendToClients(short id, @Nonnull World world, @Nonnull BlockPos pos) {
        sendToClients(id, world, pos.getX(), pos.getY(), pos.getZ());
    }

    public static class Handler extends MessageToClient.Handler<MessageParticles> {}
}
