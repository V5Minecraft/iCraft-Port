package iCraft.core.network;

import iCraft.core.ICraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class MessageBase<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ> {
   public REQ onMessage(REQ message, MessageContext ctx) {
      if (ctx.side == Side.SERVER) {
         this.handleServerSide(message, ctx.getServerHandler().player);
      } else {
         this.handleClientSide(message, ICraft.proxy.getClientPlayer());
      }

      return null;
   }

   public abstract void handleClientSide(REQ var1, EntityPlayer var2);

   public abstract void handleServerSide(REQ var1, EntityPlayer var2);
}
