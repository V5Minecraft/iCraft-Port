package iCraft.client;

import iCraft.core.utils.ICraftUtils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ICraftKeyHandler extends KeyHandler {
   public static boolean canTalk = true;
   public static final String keybindCategory = "iCraft";
   public static KeyBinding voiceMuteKey = new KeyBinding("iCraft " + ICraftUtils.localize("key.mute"), 50, "iCraft");

   public ICraftKeyHandler() {
      super(new KeyBinding[]{voiceMuteKey}, new boolean[]{false, false});
      ClientRegistry.registerKeyBinding(voiceMuteKey);
      FMLCommonHandler.instance().bus().register(this);
   }

   @SubscribeEvent
   public void onTick(ClientTickEvent event) {
      this.keyTick();
   }

   public void keyDown(KeyBinding kb, boolean isRepeat) {
      if (kb == voiceMuteKey) {
         canTalk = !canTalk;
      }
   }

   public void keyUp(KeyBinding kb) {
   }
}
