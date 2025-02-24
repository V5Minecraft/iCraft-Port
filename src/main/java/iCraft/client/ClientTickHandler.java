package iCraft.client;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientTickHandler {
   public static Minecraft mc = FMLClientHandler.instance().getClient();
   private boolean hasNotified = false;
   public static SoundEvent phoneRing = new SoundEvent(new ResourceLocation("icraft", "ringphone")).setRegistryName("ringphone");

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      if (event.phase == Phase.START) {
         this.tickStart();
      }

   }

   public void tickStart() {
      if (!this.hasNotified && mc.world != null && ICraft.newestVersion != null) {
         ICraftClientUtils.checkUpdates(mc.player);
         this.hasNotified = true;
      }
   }
}
