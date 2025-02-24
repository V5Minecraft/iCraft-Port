package iCraft.client.voice;

import iCraft.client.ICraftKeyHandler;
import iCraft.core.ICraft;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.DataLine.Info;

public class VoiceInput extends Thread {
   public VoiceClient voiceClient;
   public Info microphone;
   public TargetDataLine targetLine;

   public VoiceInput(VoiceClient client) {
      this.voiceClient = client;
      this.microphone = new Info(TargetDataLine.class, this.voiceClient.format, 2200);
      this.setDaemon(true);
      this.setName("VoiceServer Client Input Thread");
   }

   public void run() {
      try {
         this.targetLine = (TargetDataLine) AudioSystem.getLine(this.microphone);
         this.targetLine.open(this.voiceClient.format, 2200);
         this.targetLine.start();
         AudioInputStream audioInput = new AudioInputStream(this.targetLine);
         boolean doFlush = false;

         while (this.voiceClient.running) {
            if (!ICraftKeyHandler.canTalk) {
               if (doFlush) {
                  try {
                     this.voiceClient.output.flush();
                  } catch (Exception var7) {
                  }

                  doFlush = false;
               }
            } else {
               this.targetLine.flush();

               while (this.voiceClient.running && ICraftKeyHandler.canTalk) {
                  try {
                     int availableBytes = audioInput.available();
                     byte[] audioData = new byte[availableBytes > 2200 ? 2200 : availableBytes];
                     int bytesRead = audioInput.read(audioData, 0, audioData.length);
                     if (bytesRead > 0) {
                        this.voiceClient.output.writeShort(audioData.length);
                        this.voiceClient.output.write(audioData);
                     }
                  } catch (Exception var9) {
                  }
               }

               try {
                  Thread.sleep(200L);
               } catch (Exception var8) {
               }

               doFlush = true;
            }

            try {
               Thread.sleep(20L);
            } catch (Exception var6) {
            }
         }

         audioInput.close();
      } catch (Exception var10) {
         ICraft.logger.error("VoiceServer: Error while running client input thread.");
         var10.printStackTrace();
      }

   }

   public void close() {
      this.targetLine.flush();
      this.targetLine.close();
   }
}
