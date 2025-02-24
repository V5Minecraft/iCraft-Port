package iCraft.client.voice;

import iCraft.core.ICraft;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.DataLine.Info;

public class VoiceOutput extends Thread {
   public VoiceClient voiceClient;
   public Info speaker;
   public SourceDataLine sourceLine;

   public VoiceOutput(VoiceClient client) {
      this.voiceClient = client;
      this.speaker = new Info(SourceDataLine.class, this.voiceClient.format, 2200);
      this.setDaemon(true);
      this.setName("VoiceServer Client Output Thread");
   }

   public void run() {
      try {
         this.sourceLine = (SourceDataLine) AudioSystem.getLine(this.speaker);
         this.sourceLine.open(this.voiceClient.format, 2200);
         this.sourceLine.start();

         while (this.voiceClient.running) {
            try {
               short byteCount = this.voiceClient.input.readShort();
               byte[] audioData = new byte[byteCount];
               this.voiceClient.input.readFully(audioData);
               this.sourceLine.write(audioData, 0, audioData.length);
            } catch (Exception var3) {
            }
         }
      } catch (Exception var4) {
         ICraft.logger.error("VoiceServer: Error while running client output thread.");
         var4.printStackTrace();
      }
   }

   public void close() {
      this.sourceLine.flush();
      this.sourceLine.close();
   }
}
