package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.api.aspects.Aspect;
import tuhljin.automagy.common.lib.ThaumcraftExtension;

public class MessageZap extends MessageToClient<MessageZap> {
    protected int dim;
    protected double sx;
    protected double sy;
    protected double sz;
    protected double dx;
    protected double dy;
    protected double dz;

    public MessageZap(int dim, double sx, double sy, double sz, double dx, double dy, double dz) {
        this.dim = dim;
        this.sx = sx;
        this.sy = sy;
        this.sz = sz;
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.dim = buf.readInt();
        this.sx = buf.readDouble();
        this.sy = buf.readDouble();
        this.sz = buf.readDouble();
        this.dx = buf.readDouble();
        this.dy = buf.readDouble();
        this.dz = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.dim);
        buf.writeDouble(this.sx);
        buf.writeDouble(this.sy);
        buf.writeDouble(this.sz);
        buf.writeDouble(this.dx);
        buf.writeDouble(this.dy);
        buf.writeDouble(this.dz);
    }

    @SideOnly(Side.CLIENT)
    public void processMessage() {
        World world = FMLClientHandler.instance().getClient().world;
        if (world.provider.getDimension() == this.dim) {
            ThaumcraftExtension.lightning(this.sx, this.sy, this.sz, this.dx, this.dy, this.dz, Aspect.WATER);
        }

    }

    public static void sendToClients(World world, double sx, double sy, double sz, double dx, double dy, double dz) {
        if (!world.isRemote) {
            int dim = world.provider.getDimension();
            TargetPoint point = new TargetPoint(dim, sx, sy, sz, (double)PacketHandler.DEFAULT_PACKET_RANGE);
            PacketHandler.INSTANCE.sendToAllAround(new MessageZap(dim, sx, sy, sz, dx, dy, dz), point);
        }
    }

    public static class Handler extends MessageToClient.Handler<MessageZap> {}
}
