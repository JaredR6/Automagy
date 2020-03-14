package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockTiedMessageToClient<T extends BlockTiedMessageToClient> extends MessageToClient<T> {
    protected int dim;
    protected int x;
    protected int y;
    protected int z;

    public BlockTiedMessageToClient() {
    }

    public BlockTiedMessageToClient(int dim, int x, int y, int z) {
        this.dim = dim;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void fromBytes(ByteBuf buf) {
        this.dim = buf.readInt();
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.dim);
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
    }

    @SideOnly(Side.CLIENT)
    public void processMessage() {
        World world = FMLClientHandler.instance().getClient().world;
        if (world.provider.getDimension() == this.dim) {
            this.onReceived(world, new BlockPos(this.x, this.y, this.z));
        }

    }

    public abstract void onReceived(World var1, BlockPos var2);
}
