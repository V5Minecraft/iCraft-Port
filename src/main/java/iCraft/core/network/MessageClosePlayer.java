package iCraft.core.network;

import iCraft.core.ICraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

public class MessageClosePlayer extends MessageBase<MessageClosePlayer> {
   public void fromBytes(ByteBuf buf) {
   }

   public void toBytes(ByteBuf buf) {
   }

   public void handleClientSide(MessageClosePlayer message, EntityPlayer player) {
      if (ICraft.mp3Player != null) {
         ICraft.mp3Player.setRepeatType(0);
         ICraft.mp3Player.close();
      }
   }

   public void handleServerSide(MessageClosePlayer message, EntityPlayer player) {
   }
}
