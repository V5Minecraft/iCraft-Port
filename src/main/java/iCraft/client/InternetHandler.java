package iCraft.client;

import iCraft.client.gui.GuiiCraftBrowser;
import net.minecraft.client.Minecraft;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.IDisplayHandler;
import net.montoyo.mcef.api.IJSQueryCallback;
import net.montoyo.mcef.api.IJSQueryHandler;
import net.montoyo.mcef.api.MCEFApi;

public class InternetHandler implements IDisplayHandler, IJSQueryHandler {
   private Minecraft mc = Minecraft.getMinecraft();
   public static GuiiCraftBrowser backup = null;

   public InternetHandler() {
      API api = MCEFApi.getAPI();
      if (api != null) {
         api.registerDisplayHandler(this);
         api.registerJSQueryHandler(this);
      }
   }

   public static void setBackup(GuiiCraftBrowser bu) {
      backup = bu;
   }

   public static boolean hasBackup() {
      return backup != null;
   }

   public IBrowser getBrowser() {
      if (this.mc.currentScreen instanceof GuiiCraftBrowser) {
         return ((GuiiCraftBrowser) this.mc.currentScreen).browser;
      } else {
         return backup != null ? backup.browser : null;
      }
   }

   public boolean handleQuery(IBrowser b, long queryId, String query, boolean persistent, IJSQueryCallback cb) {
      if (b == this.getBrowser() && query.equalsIgnoreCase("username")) {
         try {
            String name = this.mc.getSession().getUsername();
            cb.success(name);
         } catch (Throwable var8) {
            cb.failure(500, "Internal error.");
            var8.printStackTrace();
         }

         return true;
      } else {
         return false;
      }
   }

   public void cancelQuery(IBrowser b, long queryId) {
   }

   public void onAddressChange(IBrowser browser, String url) {
      if (this.mc.currentScreen instanceof GuiiCraftBrowser) {
         ((GuiiCraftBrowser) this.mc.currentScreen).onUrlChanged(browser, url);
      } else if (hasBackup()) {
         backup.onUrlChanged(browser, url);
      }

   }

   public void onTitleChange(IBrowser browser, String title) {
   }

   public void onTooltip(IBrowser browser, String text) {
   }

   public void onStatusMessage(IBrowser browser, String value) {
   }

   public boolean onConsoleMessage(IBrowser browser, String message, String source, int line) {
      return false;
   }
}
