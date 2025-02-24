package iCraft.core.network;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageBlacklist extends MessageBase<MessageBlacklist> {
   private int status;
   private String player;

   public MessageBlacklist() {
   }

   public MessageBlacklist(int status) {
      this(status, "");
   }

   public MessageBlacklist(int status, String player) {
      this.status = status;
      this.player = player;
   }

   public void fromBytes(ByteBuf buf) {
      this.status = buf.readInt();
      this.player = ByteBufUtils.readUTF8String(buf);
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.status);
      ByteBufUtils.writeUTF8String(buf, this.player);
   }

   public void handleClientSide(MessageBlacklist message, EntityPlayer player) {
   }

   public void handleServerSide(MessageBlacklist message, EntityPlayer player) {
      NBTTagCompound nbtTags = player.getHeldItemMainhand().getTagCompound();
      switch (message.status) {
         case 0:
            this.writeNBT(nbtTags, message.player);
            break;
         case 1:
            this.removeNBT(nbtTags, message.player);
      }

   }

   private List<String> readNBT(NBTTagCompound nbtTags) {
      NBTTagList tagList = nbtTags.getTagList("blacklist", 10);
      List<String> blackList = new ArrayList();

      for (int i = 0; i < tagList.tagCount(); ++i) {
         NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
         String str = tagCompound.getString("player" + i);
         blackList.add(i, str);
      }

      return blackList;
   }

   private void writeNBT(NBTTagCompound nbtTags, String toWrite) {
      NBTTagList tagList = nbtTags.getTagList("blacklist", 10);
      NBTTagCompound tagCompound;
      if (tagList != null && tagList.tagCount() > 0) {
         tagCompound = new NBTTagCompound();
         tagCompound.setString("player" + tagList.tagCount(), toWrite);
         tagList.appendTag(tagCompound);
      } else {
         tagCompound = new NBTTagCompound();
         tagCompound.setString("player0", toWrite);
         tagList.appendTag(tagCompound);
      }

      nbtTags.setTag("blacklist", tagList);
   }

   private void removeNBT(NBTTagCompound nbtTags, String toRemove) {
      List<String> list = this.readNBT(nbtTags);
      if (list != null) {
         if (!list.isEmpty() && list.contains(toRemove)) {
            list.remove(list.indexOf(toRemove));
         }

         nbtTags.removeTag("blacklist");
         NBTTagList tagList = new NBTTagList();

         for (int i = 0; i < list.size(); ++i) {
            String str = (String) list.get(i);
            if (str != null) {
               NBTTagCompound tagCompound = new NBTTagCompound();
               tagCompound.setString("player" + i, str);
               tagList.appendTag(tagCompound);
            }
         }

         nbtTags.setTag("blacklist", tagList);
      }
   }
}
