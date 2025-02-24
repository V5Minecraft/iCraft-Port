package iCraft.core.voice;

import iCraft.core.ICraft;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class VoiceManager {
   public Set<VoiceConnection> connections = new HashSet();
   public ServerSocket serverSocket;
   public boolean running;
   public boolean foundLocal = false;
   public Thread listenThread;

   public void start() {
      ICraft.logger.info("VoiceServer: Starting up server...");

      try {
         this.running = true;
         this.serverSocket = new ServerSocket(ICraft.VOICE_PORT);
         (this.listenThread = new VoiceManager.ListenThread()).start();
      } catch (Exception var2) {
      }

   }

   public void stop() {
      try {
         ICraft.logger.info("VoiceServer: Shutting down server...");

         try {
            this.listenThread.interrupt();
         } catch (Exception var3) {
         }

         this.foundLocal = false;

         try {
            this.serverSocket.close();
            this.serverSocket = null;
         } catch (Exception var2) {
         }
      } catch (Exception var4) {
         ICraft.logger.error("VoiceServer: Error while shutting down server.");
         var4.printStackTrace();
      }

      this.running = false;
   }

   public void sendToPlayers(short byteCount, byte[] audioData, VoiceConnection connection) {
      if (connection.getPlayer() != null) {
         int callCode = connection.getCallCode();
         if (callCode != 0) {
            Iterator i = this.connections.iterator();

            while (i.hasNext()) {
               VoiceConnection iterConn = (VoiceConnection) i.next();
               if (iterConn.getPlayer() != null && iterConn != connection && iterConn.canListen(callCode)) {
                  iterConn.sendToPlayer(byteCount, audioData, connection);
               }
            }

         }
      }
   }

   public class ListenThread extends Thread {
      public ListenThread() {
         this.setDaemon(true);
         this.setName("VoiceServer Listen Thread");
      }

      public void run() {
         while (VoiceManager.this.running) {
            try {
               Socket s = VoiceManager.this.serverSocket.accept();
               VoiceConnection connection = new VoiceConnection(s);
               connection.start();
               VoiceManager.this.connections.add(connection);
               ICraft.logger.info("VoiceServer: Accepted new connection.");
            } catch (Exception var3) {
               ICraft.logger.error("VoiceServer: Error while accepting connection.");
               var3.printStackTrace();
            }
         }
      }
   }
}
