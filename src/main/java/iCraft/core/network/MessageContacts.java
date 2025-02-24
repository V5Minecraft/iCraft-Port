package iCraft.core.network;

import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class MessageContacts extends MessageBase<MessageContacts> {
   private int status;
   private int number;

   public MessageContacts() {
   }

   public MessageContacts(int status) {
      this(status, 0);
   }

   public MessageContacts(int status, int number) {
      this.status = status;
      this.number = number;
   }

   public void fromBytes(ByteBuf buf) {
      this.status = buf.readInt();
      this.number = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.status);
      buf.writeInt(this.number);
   }

   public void handleClientSide(MessageContacts message, EntityPlayer player) {
   }

   public void handleServerSide(MessageContacts message, EntityPlayer player) {
      NBTTagCompound nbtTags = player.getHeldItemMainhand().getTagCompound();
      switch (message.status) {
         case 0:
            this.writeNBT(nbtTags, message.number);
            break;
         case 1:
            this.removeNBT(nbtTags, message.number);
      }

   }

   private List<Integer> readNBT(NBTTagCompound nbtTags) {
      NBTTagList tagList = nbtTags.getTagList("contacts", 10);
      List<Integer> contacts = new ArrayList();

      for (int i = 0; i < tagList.tagCount(); ++i) {
         NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
         int j = tagCompound.getInteger("number" + i);
         contacts.add(i, j);
      }

      return contacts;
   }

   private void writeNBT(NBTTagCompound nbtTags, int toWrite) {
      NBTTagList tagList = nbtTags.getTagList("contacts", 10);
      NBTTagCompound tagCompound;
      if (tagList != null && tagList.tagCount() > 0) {
         tagCompound = new NBTTagCompound();
         tagCompound.setInteger("number" + tagList.tagCount(), toWrite);
         tagList.appendTag(tagCompound);
      } else {
         tagCompound = new NBTTagCompound();
         tagCompound.setInteger("number0", toWrite);
         tagList.appendTag(tagCompound);
      }

      nbtTags.setTag("contacts", tagList);
   }

   private void removeNBT(NBTTagCompound nbtTags, int toRemove) {
      List<Integer> list = this.readNBT(nbtTags);
      if (list != null) {
         if (!list.isEmpty() && list.contains(toRemove)) {
            list.remove(list.indexOf(toRemove));
         }

         nbtTags.removeTag("contacts");
         NBTTagList tagList = new NBTTagList();

         for (int i = 0; i < list.size(); ++i) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setInteger("number" + i, (Integer) list.get(i));
            tagList.appendTag(tagCompound);
         }

         nbtTags.setTag("contacts", tagList);
      }
   }
}
