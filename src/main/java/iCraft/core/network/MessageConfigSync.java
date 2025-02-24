package iCraft.core.network;

import iCraft.core.ICraft;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageConfigSync extends MessageBase<MessageConfigSync> {
   public void fromBytes(ByteBuf buf) {
      ICraft.isVoiceEnabled = buf.readBoolean();
      ICraft.VOICE_PORT = buf.readInt();
      ICraft.isIBayActive = buf.readBoolean();
      int size = buf.readInt();
      ICraft.buyableItems = new String[size];

      for (int i = 0; i < size; ++i) {
         ICraft.buyableItems[i] = ByteBufUtils.readUTF8String(buf);
      }

   }

   public void toBytes(ByteBuf buf) {
      buf.writeBoolean(ICraft.isVoiceEnabled);
      buf.writeInt(ICraft.VOICE_PORT);
      buf.writeBoolean(ICraft.isIBayActive);
      buf.writeInt(ICraft.buyableItems.length);
      String[] arr = ICraft.buyableItems;
      int len = arr.length;

      for (int i = 0; i < len; ++i) {
         String s = arr[i];
         ByteBufUtils.writeUTF8String(buf, s);
      }
   }

   public void handleClientSide(MessageConfigSync message, EntityPlayer player) {
      ICraft.proxy.onConfigSync();
   }

   public void handleServerSide(MessageConfigSync message, EntityPlayer player) {
   }
}
