package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public abstract class FloatPosMessageToClient<T extends FloatPosMessageToClient> extends MessageToClient<T> {
    protected int dim;
    protected float x;
    protected float y;
    protected float z;

    public FloatPosMessageToClient(int dim, float x, float y, float z) {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void fromBytes(@Nonnull ByteBuf buf) {
        this.dim = buf.readInt();
        this.x = buf.readFloat();
        this.y = buf.readFloat();
        this.z = buf.readFloat();
    }

    @Override
    public void toBytes(@Nonnull ByteBuf buf) {
        buf.writeInt(this.dim);
        buf.writeFloat(this.x);
        buf.writeFloat(this.y);
        buf.writeFloat(this.z);
    }

    @SideOnly(Side.CLIENT)
    public void processMessage() {
        World world = FMLClientHandler.instance().getClient().world;
        if (world.provider.getDimension() == this.dim) {
            this.onReceived(world, this.x, this.y, this.z);
        }

    }

    public abstract void onReceived(World var1, float var2, float var3, float var4);
}
