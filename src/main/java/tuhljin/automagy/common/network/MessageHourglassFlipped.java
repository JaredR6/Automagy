package tuhljin.automagy.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import tuhljin.automagy.common.tiles.TileHourglass;

public class MessageHourglassFlipped extends BlockTiedMessageToClient<MessageHourglassFlipped> {
    boolean flipState;

    public MessageHourglassFlipped() {
    }

    public MessageHourglassFlipped(int dim, int x, int y, int z, boolean flipState) {
        super(dim, x, y, z);
        this.flipState = flipState;
    }

    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);
        this.flipState = buf.readBoolean();
    }

    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);
        buf.writeBoolean(this.flipState);
    }

    public void onReceived(World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileHourglass) {
            ((TileHourglass)te).receiveFlipFromServer(this.flipState);
        }

    }

    public static void sendToClients(World world, int x, int y, int z, boolean flipState) {
        if (!world.isRemote) {
            int dim = world.provider.getDimension();
            TargetPoint point = new TargetPoint(dim, x, y, z, PacketHandler.DEFAULT_PACKET_RANGE);
            PacketHandler.INSTANCE.sendToAllAround(new MessageHourglassFlipped(dim, x, y, z, flipState), point);
        }
    }
}
