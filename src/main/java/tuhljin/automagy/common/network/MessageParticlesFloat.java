package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import thaumcraft.client.fx.FXDispatcher;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.fx.particles.FXVisSparkle;
import thaumcraft.Thaumcraft;

public class MessageParticlesFloat extends FloatPosMessageToClient<MessageParticlesFloat> {
    public static final short GRABLIQUID = 2;
    protected short id;

    public MessageParticlesFloat() {
    }

    public MessageParticlesFloat(short id, int dim, float x, float y, float z) {
        super(dim, x, y, z);
        this.id = id;
    }

    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        this.id = buf.readShort();
    }

    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeShort(this.id);
    }

    public void onReceived(World world, float x, float y, float z) {
        if (this.id == GRABLIQUID) {
            int color = 7;
            this.sparkle(world, color, 7);
        }
    }

    @SideOnly(Side.CLIENT)
    private void sparkle(World world, int color, int count) {
        for(int a = 0; a < count; ++a) {
            FXVisSparkle fx = new FXVisSparkle(world, (this.x + world.rand.nextFloat()), (this.y + world.rand.nextFloat()), (this.z + world.rand.nextFloat()), 1.75F, color == -1 ? world.rand.nextInt(5) : color, 3 + world.rand.nextInt(3));
            fx.setGravity(0.2F);
            ParticleEngine.addEffect(world, fx);
        }

    }

    public static void sendToClients(short id, World world, float x, float y, float z) {
        if (!world.isRemote) {
            int dim = world.provider.getDimension();
            TargetPoint point = new TargetPoint(dim, x, y, z, PacketHandler.DEFAULT_PACKET_RANGE);
            PacketHandler.INSTANCE.sendToAllAround(new MessageParticlesFloat(id, dim, x, y, z), point);
        }
    }

    public static void sendToClients(short id, World world, double x, double y, double z) {
        sendToClients(id, world, (float)x, (float)y, (float)z);
    }
}
