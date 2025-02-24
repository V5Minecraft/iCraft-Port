package iCraft.core.network;

import iCraft.core.ICraft;
import iCraft.core.tile.TileBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

@Sharable
public class DescriptionHandler extends SimpleChannelInboundHandler<FMLProxyPacket> {
   public static final String CHANNEL = "iDescription";

   public static void init() {
   }

   protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {
      ByteBuf buf = msg.payload();
      int x = buf.readInt();
      int y = buf.readInt();
      int z = buf.readInt();
      TileEntity te = ICraft.proxy.getClientPlayer().world.getTileEntity(new BlockPos(x, y, z));
      if (te instanceof TileBase) {
         ((TileBase) te).readFromPacket(buf);
      }

   }

   static {
      NetworkRegistry.INSTANCE.newChannel("iDescription", new DescriptionHandler());
   }
}
