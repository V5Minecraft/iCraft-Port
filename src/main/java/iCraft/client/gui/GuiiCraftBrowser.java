package iCraft.client.gui;

import iCraft.client.InternetHandler;
import iCraft.core.utils.ICraftClientUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.montoyo.mcef.api.API;
import net.montoyo.mcef.api.IBrowser;
import net.montoyo.mcef.api.MCEFApi;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.GlStateManager;

@SideOnly(Side.CLIENT)
public class GuiiCraftBrowser extends GuiScreen {
   public IBrowser browser = null;
   private GuiButton back = null;
   private GuiButton fwd = null;
   private GuiButton go = null;
   private GuiButton min = null;
   private GuiNewTextField url = null;

   public void initGui() {
      if (this.browser == null) {
         API api = MCEFApi.getAPI();
         if (api == null) {
            return;
         }

         this.browser = api.createBrowser(ICraftClientUtils.homePage, false);
      }

      if (this.browser != null) {
         this.browser.resize(super.mc.displayWidth, super.mc.displayHeight - this.scaleY(40));
      }

      Keyboard.enableRepeatEvents(true);
      super.buttonList.clear();
      if (this.url == null) {
         super.buttonList.add(this.back = new GuiButton(0, 0, 20, 20, 20, "<"));
         super.buttonList.add(this.fwd = new GuiButton(1, 20, 20, 20, 20, ">"));
         super.buttonList.add(this.go = new GuiButton(2, super.width - 40, 20, 20, 20, "Go"));
         super.buttonList.add(this.min = new GuiButton(3, super.width - 20, 20, 20, 20, "_"));
         this.url = new GuiNewTextField(0, super.fontRenderer, 45, 26, super.width - 92, 20);
         this.url.setMaxStringLength(65535);
         this.url.setEnableBackgroundDrawing(false);
         this.url.setText(ICraftClientUtils.homePage);
      } else {
         super.buttonList.add(this.back);
         super.buttonList.add(this.fwd);
         super.buttonList.add(this.go);
         super.buttonList.add(this.min);
         this.go.x = super.width - 40;
         this.min.x = super.width - 20;
         String old = this.url.getText();
         this.url = new GuiNewTextField(0, super.fontRenderer, 45, 26, super.width - 92, 20);
         this.url.setMaxStringLength(65535);
         this.url.setEnableBackgroundDrawing(false);
         this.url.setText(old);
      }
   }

   public int scaleY(int y) {
      double sy = (double) y / (double) super.height * (double) super.mc.displayHeight;
      return (int) sy;
   }

   public void loadURL(String url) {
      if (url.contains(".")) {
         this.browser.loadURL(url);
      } else {
         this.browser.loadURL("https://www.google.com/search?q=" + url);
      }
   }

   public void updateScreen() {
      if (!this.url.isFocused() && !this.browser.getURL().equals(this.url.getText())) {
         this.url.setText(this.browser.getURL());
      }
   }

   public void drawScreen(int mouseX, int mouseY, float partialTick) {
      drawRect(0, 0, super.width, 21, -16777216);
      drawRect(0, 21, super.width, 40, -16750849);
      drawRect(0, 22, super.width, 39, -1);
      this.drawString(super.fontRenderer, ICraftClientUtils.getTime(), super.width / 2, 6, 16777215);
      this.url.drawTextBox();
      super.drawScreen(mouseX, mouseY, partialTick);
      if (this.browser != null) {
         GlStateManager.disableDepth();
         this.browser.draw(0.0D, (double) super.height, (double) super.width, 40.0D);
         GlStateManager.enableDepth();
      }
   }

   public void onGuiClosed() {
      if (!InternetHandler.hasBackup() && this.browser != null) {
         this.browser.close();
      }
      Keyboard.enableRepeatEvents(false);
   }

   public void handleInput() {
      boolean pressed;
      int num;
      while (Keyboard.next()) {
         if (Keyboard.getEventKey() == 1) {
            super.mc.displayGuiScreen((GuiScreen) null);
            return;
         }

         pressed = this.url.isFocused();
         char key = Keyboard.getEventCharacter();
         num = Keyboard.getEventKey();
         if (this.browser != null && !pressed) {
            if (key != '.' && key != ';' && key != ',') {
               if (pressed) {
                  this.browser.injectKeyTyped(key, 0);
               } else {
                  this.browser.injectKeyTyped(key, 0);
               }
            }

            if (key != 0) {
               this.browser.injectKeyTyped(key, 0);
            }
         }

         if (!pressed && pressed && num == 28) {
            this.actionPerformed(this.go);
         } else {
            this.url.textboxKeyTyped(key, num);
         }
      }

      while (Mouse.next()) {
         int btn = Mouse.getEventButton();
         pressed = Mouse.getEventButtonState();
         int sx = Mouse.getEventX();
         num = Mouse.getEventY();
         int x;
         if (this.browser != null) {
            x = super.mc.displayHeight - num - this.scaleY(40);
            if (btn == -1) {
               this.browser.injectMouseMove(sx, x, 0, x < 0);
            } else {
               this.browser.injectMouseButton(sx, x, 0, btn + 1, pressed, 1);
            }
         }

         if (pressed) {
            x = sx * super.width / super.mc.displayWidth;
            int y = super.height - num * super.height / super.mc.displayHeight - 1;

            try {
               this.mouseClicked(x, y, btn);
            } catch (Exception var8) {
               var8.printStackTrace();
            }

            this.url.mouseClicked(x, y, btn);
         }
      }
   }

   public void onUrlChanged(IBrowser b, String nurl) {
      if (b == this.browser && this.url != null) {
         this.url.setText(nurl);
      }
   }

   protected void actionPerformed(GuiButton src) {
      if (this.browser != null) {
         if (src.id == 0) {
            this.browser.goBack();
         } else if (src.id == 1) {
            this.browser.goForward();
         } else if (src.id == 2) {
            this.loadURL(this.url.getText());
         } else if (src.id == 3) {
            InternetHandler.setBackup(this);
            super.mc.displayGuiScreen((GuiScreen) null);
         }
      }
   }

   public boolean doesGuiPauseGame() {
      return false;
   }
}
