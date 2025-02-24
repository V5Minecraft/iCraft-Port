package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftClientUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import net.minecraft.client.renderer.GlStateManager;

@SideOnly(Side.CLIENT)
public class GuiiCraftClock extends GuiiCraftBase {
   private float scroll;
   private boolean isDragging = false;
   private boolean canScroll;
   private int dragOffset = 0;
   private List<String> list = new ArrayList<>();

   public GuiiCraftClock(String resource) {
      super(resource);
   }

   public void initGui() {
      super.initGui();
      this.canScroll = this.list.size() > 5;
   }

   public void updateScreen() {
      super.updateScreen();
      this.canScroll = this.list.size() > 5;
   }

   public int getScroll() {
      return Math.max(Math.min((int) (this.scroll * 55.0F), 55), 0);
   }

   public int getIndex() {
      return this.list.size() <= 5 ? 0 : (int) ((float) this.list.size() * this.scroll - 5.0F / (float) this.list.size() * this.scroll);
   }

   protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
      super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
      this.drawTexturedModalRect(super.guiWidth + 113, super.guiHeight + 67 + this.getScroll(), this.canScroll ? 0 : 11, 180, 11, 15);
      int xAxis = mouseX - super.guiWidth;
      int yAxis = mouseY - super.guiHeight;

      for (int i = 0; i < 5; ++i) {
         if (this.getIndex() + i < this.list.size()) {
            int yStart = i * 14 + 67;
            boolean mouseOver = xAxis >= 52 && xAxis <= 111 && yAxis >= yStart && yAxis <= yStart + 14;
            this.drawTexturedModalRect(super.guiWidth + 52, super.guiHeight + yStart, mouseOver ? 0 : 60, 166, 60, 14);
         }
      }
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      for (int i = 0; i < 5; ++i) {
         if (this.getIndex() + i < this.list.size()) {
            int yStart = i * 28 + 66;
            this.drawResizedString(this.list.get(this.getIndex() + i), 110, yStart + 73, 16777215, 0.5F);
         }
      }

      GlStateManager.pushMatrix();
      GlStateManager.scale(1.0F, 1.0F, 1.0F);
      GlStateManager.disableLighting();
      GlStateManager.translate(super.guiWidth, super.guiHeight, 0.0F);
      super.fontRenderer.drawString(ICraftClientUtils.getTime(), 75, 46, 16777215);
      GlStateManager.enableLighting();
      GlStateManager.popMatrix();
      super.drawGuiContainerForegroundLayer(mouseX, mouseY);
   }

   protected void mouseClicked(int x, int y, int button) {
      try {
         super.mouseClicked(x, y, button);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      int xAxis = x - super.guiWidth;
      int yAxis = y - super.guiHeight;

      if (button == 0) {
         if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158) {
            super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
            ICraftClientUtils.hour24 = !ICraftClientUtils.hour24;
         }
         if (xAxis >= 113 && xAxis <= 123 && yAxis >= this.getScroll() + 67 && yAxis <= this.getScroll() + 67 + 15 && this.canScroll) {
            this.dragOffset = yAxis - (this.getScroll() + 67);
            this.isDragging = true;
         }
      }
   }

   protected void mouseClickMove(int mouseX, int mouseY, int button, long ticks) {
      super.mouseClickMove(mouseX, mouseY, button, ticks);
      int yAxis = mouseY - super.guiHeight;
      if (this.isDragging) {
         this.scroll = Math.min(Math.max((float) (yAxis - 67 - this.dragOffset) / 55.0F, 0.0F), 1.0F);
      }
   }

   protected void mouseReleased(int x, int y, int type) {
      super.mouseReleased(x, y, type);
      if (type == 0 && this.isDragging) {
         this.dragOffset = 0;
         this.isDragging = false;
      }
   }

   public void handleMouseInput() {
      try {
         super.handleMouseInput();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      int i = Mouse.getEventDWheel();
      if (i != 0 && this.canScroll) {
         this.scroll = Math.min(Math.max(this.scroll - i / 20.0F, 0.0F), 1.0F);
      }
   }
}
