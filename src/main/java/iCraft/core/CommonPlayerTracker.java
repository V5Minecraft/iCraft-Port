package iCraft.core;

import iCraft.core.item.ItemiCraft;
import iCraft.core.network.MessageConfigSync;
import iCraft.core.network.NetworkHandler;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class CommonPlayerTracker {
   public CommonPlayerTracker() {
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void onPlayerLogin(PlayerLoggedInEvent event) {
      if (!event.player.world.isRemote) {
         NetworkHandler.sendTo(new MessageConfigSync(), (EntityPlayerMP) event.player);
         ICraft.logger.info("Sent config to '" + event.player.getDisplayName() + ".'");
      }
   }

   @SubscribeEvent
   public void onPlayerLogout(PlayerLoggedOutEvent event) {
      List<NonNullList<ItemStack>> itemStacks = Arrays.asList(event.player.inventory.mainInventory);
      Iterator i = itemStacks.iterator();

      label80:
      while (true) {
         ItemStack itemStack;
         do {
            do {
               do {
                  do {
                     do {
                        do {
                           if (!i.hasNext()) {
                              return;
                           }

                           itemStack = (ItemStack) i.next();
                        } while (itemStack == null);
                     } while (!(itemStack.getItem() instanceof ItemiCraft));
                  } while (itemStack.getTagCompound() == null);
               } while (!itemStack.getTagCompound().hasKey("called"));
            } while (itemStack.getTagCompound().getInteger("called") == 0);
         } while (!itemStack.getTagCompound().hasKey("isCalling"));

         i = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().iterator();

         while (true) {
            EntityPlayerMP players;
            do {
               if (!i.hasNext()) {
                  continue label80;
               }

               players = (EntityPlayerMP) i.next();
            } while (!players.getName().equals(itemStack.getTagCompound().getBoolean("isCalling") ? itemStack.getTagCompound().getString("calledPlayer") : itemStack.getTagCompound().getString("callingPlayer")));

            List<NonNullList<ItemStack>> stacks = Arrays.asList(players.inventory.mainInventory);
            i = stacks.iterator();

            while (i.hasNext()) {
               ItemStack stack = (ItemStack) i.next();
               if (stack != null && stack.getItem() instanceof ItemiCraft) {
                  ItemiCraft iCraft = (ItemiCraft) stack.getItem();
                  if (stack.getTagCompound() != null && iCraft.getNumber(stack) == (itemStack.getTagCompound().getBoolean("isCalling") ? itemStack.getTagCompound().getInteger("calledNumber") : itemStack.getTagCompound().getInteger("callingNumber"))) {
                     stack.getTagCompound().setInteger("called", 0);
                     players.closeScreen();
                  }
               }
            }
         }
      }
   }
}
