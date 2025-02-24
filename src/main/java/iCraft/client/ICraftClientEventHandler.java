package iCraft.client;

import iCraft.client.voice.VoiceClient;
import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import java.net.InetSocketAddress;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ICraftClientEventHandler {
   public ICraftClientEventHandler() {
      MinecraftForge.EVENT_BUS.register(this);
   }

   @SubscribeEvent
   public void onConnection(ClientConnectedToServerEvent event) {
      if (ICraft.isVoiceEnabled) {
         if (event.isLocal()) {
            try {
               ICraft.voiceClient = new VoiceClient("127.0.0.1");
            } catch (Throwable var4) {
               ICraft.logger.error("Unable to establish VoiceClient on local connection.");
               var4.printStackTrace();
            }
         } else {
            try {
               ICraft.voiceClient = new VoiceClient(((InetSocketAddress) event.getManager().getRemoteAddress()).getHostString());
            } catch (Throwable var3) {
               ICraft.logger.error("Unable to establish VoiceClient on remote connection.");
               var3.printStackTrace();
            }
         }
      }

   }

   @SubscribeEvent
   public void onDisconnection(ClientDisconnectionFromServerEvent event) {
      if (ICraft.mp3Player != null) {
         ICraft.mp3Player.setRepeatType(0);
         ICraft.mp3Player.close();
         ICraft.logger.info("MP3 Player: Successfully stopped the player.");
      }

   }

   @SubscribeEvent
   public void onItemToss(ItemTossEvent event) {
      if (event.getEntityItem().getItem() != null && event.getEntityItem().getItem().getItem() instanceof ItemiCraft) {
         ItemStack iCraft = event.getEntityItem().getItem();
         if (iCraft.getTagCompound() != null && iCraft.getTagCompound().hasKey("called") && iCraft.getTagCompound().getInteger("called") != 0) {
            event.getPlayer().inventory.addItemStackToInventory(iCraft);
            event.setCanceled(true);
         }

         if (ICraft.mp3Player != null) {
            ICraft.mp3Player.setRepeatType(0);
            ICraft.mp3Player.close();
            ICraft.logger.info("MP3 Player: Successfully stopped the player.");
         }
      }
   }
}
