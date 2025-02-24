package iCraft.client.gui;

import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;

import java.io.IOException;

public class GuiiCraftBase extends GuiScreen {
   private final String resource;
   protected int xSize = 176;
   protected int ySize = 166;
   protected int guiWidth;
   protected int guiHeight;

   public GuiiCraftBase(String resource) {
      this.resource = resource;
   }

   @Override
   public void initGui() {
      guiWidth = (width - xSize) / 2;
      guiHeight = (height - ySize) / 2;
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTick) {
      drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);

      GlStateManager.disableRescaleNormal();
      RenderHelper.disableStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.disableDepth();

      super.drawScreen(mouseX, mouseY, partialTick);

      RenderHelper.enableGUIStandardItemLighting();

      GlStateManager.pushMatrix();
      GlStateManager.translate(guiWidth, guiHeight, 0.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableRescaleNormal();

      OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

      GlStateManager.disableLighting();
      drawGuiContainerForegroundLayer(mouseX, mouseY);
      GlStateManager.enableLighting();

      GlStateManager.popMatrix();

      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
   }

   protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
      mc.renderEngine.bindTexture(ICraftClientUtils.getResource(ResourceType.GUI, resource + ".png"));
      drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
   }

   protected void drawResizedString(String toRender, int posX, int posY, int color, float size) {
      GlStateManager.pushMatrix();
      GlStateManager.scale(size, size, size);
      fontRenderer.drawString(toRender, posX, posY, color);
      GlStateManager.popMatrix();
   }

   protected void drawTime() {
      drawResizedString(ICraftClientUtils.getTime(), 164, 58, 0xffffff, 0.5F);
   }

   protected boolean isMouseOver(int startX, int startY, int width, int height, int mouseX, int mouseY) {
      int w = guiWidth;
      int h = guiHeight;
      mouseX -= w;
      mouseY -= h;
      return mouseX >= startX - 1 && mouseX < startX + width + 1 && mouseY >= startY - 1 && mouseX < startY + height + 1;
   }

   @Override
   protected void keyTyped(char ch, int keyCode) {
      try {
         super.keyTyped(ch, keyCode);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      if (keyCode == mc.gameSettings.keyBindInventory.getKeyCode()) {
         mc.displayGuiScreen(null);
      }
   }

   @Override
   public boolean doesGuiPauseGame() {
      return false;
   }
}
