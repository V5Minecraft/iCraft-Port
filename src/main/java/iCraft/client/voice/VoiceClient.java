package iCraft.client.voice;

import iCraft.core.ICraft;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import javax.sound.sampled.AudioFormat;

public class VoiceClient extends Thread {
   public Socket socket;
   public String ip;
   public AudioFormat format = new AudioFormat(16000.0F, 16, 1, true, true);
   public VoiceInput inputThread;
   public VoiceOutput outputThread;
   public DataInputStream input;
   public DataOutputStream output;
   public boolean running;

   public VoiceClient(String s) {
      this.ip = s;
   }

   public void run() {
      ICraft.logger.info("VoiceServer: Starting client connection...");

      try {
         this.socket = new Socket(this.ip, ICraft.VOICE_PORT);
         this.running = true;
         this.input = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
         this.output = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
         (this.outputThread = new VoiceOutput(this)).start();
         (this.inputThread = new VoiceInput(this)).start();
         ICraft.logger.info("VoiceServer: Successfully connected to server.");
      } catch (ConnectException var2) {
         ICraft.logger.error("VoiceServer: Server's VoiceServer is disabled.");
      } catch (Exception var3) {
         ICraft.logger.error("VoiceServer: Error while starting client connection.");
         var3.printStackTrace();
      }

   }

   public void disconnect() {
      ICraft.logger.info("VoiceServer: Stopping client connection...");

      try {
         try {
            this.inputThread.interrupt();
            this.outputThread.interrupt();
         } catch (Exception var7) {
         }

         try {
            this.interrupt();
         } catch (Exception var6) {
         }

         try {
            this.inputThread.close();
            this.outputThread.close();
         } catch (Exception var5) {
         }

         try {
            this.output.flush();
            this.output.close();
            this.output = null;
         } catch (Exception var4) {
         }

         try {
            this.input.close();
            this.input = null;
         } catch (Exception var3) {
         }

         try {
            this.socket.close();
            this.socket = null;
         } catch (Exception var2) {
         }

         this.running = false;
      } catch (Exception var8) {
         ICraft.logger.error("VoiceServer: Error while stopping client connection.");
         var8.printStackTrace();
      }
   }
}
