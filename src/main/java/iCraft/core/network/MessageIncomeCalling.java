package iCraft.core.network;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class MessageIncomeCalling extends MessageBase<MessageIncomeCalling> {
   private int number;
   private int calledNumber;

   public MessageIncomeCalling() {
   }

   public MessageIncomeCalling(int number, int calledNumber) {
      this.number = number;
      this.calledNumber = calledNumber;
   }

   public void fromBytes(ByteBuf buf) {
      this.number = buf.readInt();
      this.calledNumber = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.number);
      buf.writeInt(this.calledNumber);
   }

   public void handleClientSide(MessageIncomeCalling message, EntityPlayer player) {
      player.closeScreen();
      player.openGui(ICraft.instance, 6, player.world, 0, 0, 0);
   }

   public void handleServerSide(MessageIncomeCalling message, EntityPlayer player) {
      World world = player.world;
      Iterator i = world.playerEntities.iterator();

      label78:
      while (i.hasNext()) {
         EntityPlayer players = (EntityPlayer) i.next();
         if (players != player) {
            List<NonNullList<ItemStack>> itemStacks = Arrays.asList(players.inventory.mainInventory);
            i = itemStacks.iterator();

            while (true) {
               ItemStack itemStack;
               List blackList;
               do {
                  do {
                     do {
                        do {
                           do {
                              do {
                                 do {
                                    if (!i.hasNext()) {
                                       continue label78;
                                    }

                                    itemStack = (ItemStack) i.next();
                                 } while (itemStack == null);
                              } while (!(itemStack.getItem() instanceof ItemiCraft));
                           } while (itemStack.getTagCompound() == null);
                        } while (!itemStack.getTagCompound().hasKey("number"));
                     } while (itemStack.getTagCompound().getInteger("number") != message.calledNumber);

                     blackList = this.readNBT(itemStack.getTagCompound());
                  } while (blackList.contains(player.getName()));
               } while (itemStack.getTagCompound().hasKey("called") && itemStack.getTagCompound().getInteger("called") != 0);

               itemStack.getTagCompound().setInteger("called", 1);
               itemStack.getTagCompound().setInteger("callingNumber", message.number);
               itemStack.getTagCompound().setString("callingPlayer", player.getName());
               itemStack.getTagCompound().setBoolean("isCalling", false);
               List<NonNullList<ItemStack>> stacks = Arrays.asList(player.inventory.mainInventory);
               i = stacks.iterator();

               while (i.hasNext()) {
                  ItemStack stack = (ItemStack) i.next();
                  if (stack != null && stack.getItem() instanceof ItemiCraft && stack.getTagCompound() != null && stack.getTagCompound().hasKey("number") && stack.getTagCompound().getInteger("number") == message.number) {
                     stack.getTagCompound().setInteger("called", 1);
                     stack.getTagCompound().setInteger("calledNumber", itemStack.getTagCompound().getInteger("number"));
                     stack.getTagCompound().setString("calledPlayer", players.getName());
                     stack.getTagCompound().setBoolean("isCalling", true);
                     NetworkHandler.sendTo(this, (EntityPlayerMP) player);
                     return;
                  }
               }
            }
         }
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
}
