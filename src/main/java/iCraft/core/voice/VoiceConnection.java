package iCraft.core.voice;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class VoiceConnection extends Thread {
   public Socket socket;
   public String username;
   public boolean open = true;
   public DataInputStream input;
   public DataOutputStream output;
   public MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();

   public VoiceConnection(Socket s) {
      this.socket = s;
   }

   public void run() {
      try {
         this.input = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
         this.output = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
         synchronized (ICraft.voiceManager) {
            int retryCount = 0;

            while (this.username == null && retryCount <= 100) {
               try {
                  List l = Collections.synchronizedList((List) ((ArrayList) this.server.getPlayerList().getPlayers()).clone());
                  Iterator i = l.iterator();

                  while (i.hasNext()) {
                     Object obj = i.next();
                     if (obj instanceof EntityPlayerMP) {
                        EntityPlayerMP playerMP = (EntityPlayerMP) obj;
                        String playerIP = playerMP.getPlayerIP();
                        if (!this.server.isDedicatedServer() && playerIP.equals("local") && !ICraft.voiceManager.foundLocal) {
                           ICraft.voiceManager.foundLocal = true;
                           this.username = playerMP.getName();
                           break;
                        }

                        if (playerIP.equals(this.socket.getInetAddress().getHostAddress())) {
                           this.username = playerMP.getName();
                           break;
                        }
                     }
                  }

                  ++retryCount;
                  Thread.sleep(50L);
               } catch (Exception var9) {
               }
            }

            if (this.username == null) {
               ICraft.logger.error("VoiceServer: Unable to trace connection's IP address.");
               this.kill();
               return;
            }

            ICraft.logger.info("VoiceServer: Traced IP in " + retryCount + " attempts.");
         }
      } catch (Exception var11) {
         ICraft.logger.error("VoiceServer: Error while starting server-based connection.");
         var11.printStackTrace();
         this.open = false;
      }

      (new Thread(() -> {
         while (VoiceConnection.this.open) {
            try {
               short byteCount = VoiceConnection.this.input.readShort();
               byte[] audioData = new byte[byteCount];
               VoiceConnection.this.input.readFully(audioData);
               if (byteCount > 0) {
                  ICraft.voiceManager.sendToPlayers(byteCount, audioData, VoiceConnection.this);
               }
            } catch (Exception var3) {
               VoiceConnection.this.open = false;
            }
         }

         if (!VoiceConnection.this.open) {
            VoiceConnection.this.kill();
         }

      })).start();
   }

   public void kill() {
      try {
         this.input.close();
         this.output.close();
         this.socket.close();
         ICraft.voiceManager.connections.remove(this);
      } catch (Exception var2) {
         ICraft.logger.error("VoiceServer: Error while stopping server-based connection.");
         var2.printStackTrace();
      }

   }

   public void sendToPlayer(short byteCount, byte[] audioData, VoiceConnection connection) {
      if (!this.open) {
         this.kill();
      }

      try {
         this.output.writeShort(byteCount);
         this.output.write(audioData);
         this.output.flush();
      } catch (Exception var5) {
         ICraft.logger.error("VoiceServer: Error while sending data to player.");
         var5.printStackTrace();
      }

   }

   public boolean canListen(int callCode) {
      ItemStack[] arr = this.getPlayer().inventory.mainInventory.toArray(new ItemStack[0]);
      int len = arr.length;

      for (int i = 0; i < len; ++i) {
         ItemStack itemStack = arr[i];
         if (itemStack != null && itemStack.getItem() instanceof ItemiCraft && itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("called") && itemStack.getTagCompound().getInteger("called") == 2 && itemStack.getTagCompound().hasKey("callCode") && itemStack.getTagCompound().getInteger("callCode") == callCode) {
            return true;
         }
      }

      return false;
   }

   public int getCallCode() {
      ItemStack[] arr = this.getPlayer().inventory.mainInventory.toArray(new ItemStack[0]);
      int len = arr.length;

      for (int i = 0; i < len; ++i) {
         ItemStack itemStack = arr[i];
         if (itemStack != null && itemStack.getItem() instanceof ItemiCraft && itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("number") && itemStack.getTagCompound().hasKey("called") && itemStack.getTagCompound().getInteger("called") == 2 && itemStack.getTagCompound().hasKey("callCode")) {
            return itemStack.getTagCompound().getInteger("callCode");
         }
      }

      return 0;
   }

   public EntityPlayerMP getPlayer() {
      return this.server.getPlayerList().getPlayerByUsername(this.username);
   }
}
