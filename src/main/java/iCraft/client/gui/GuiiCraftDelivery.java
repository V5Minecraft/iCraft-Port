package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageDelivery;
import iCraft.core.network.NetworkHandler;
import iCraft.core.register.RegisterItems;
import iCraft.core.utils.ICraftUtils;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiiCraftDelivery extends GuiiCraftBase {
   private int qnt = 1;
   private GuiButton qntMore;
   private GuiButton qntLess;
   private GuiButton buy;

   public GuiiCraftDelivery(String resource) {
      super(resource);
   }

   public void initGui() {
      super.initGui();
      Keyboard.enableRepeatEvents(false);
      super.buttonList.clear();
      this.qntMore = new GuiButton(0, super.guiWidth + 60, super.guiHeight + 80, 15, 20, "\u25B2");
      this.qntLess = new GuiButton(1, super.guiWidth + 60, super.guiHeight + 100, 15, 20, "\u25BC");
      this.buy = new GuiButton(2, super.guiWidth + 85, super.guiHeight + 90, 30, 20, "Buy");
      super.buttonList.add(this.qntMore);
      super.buttonList.add(this.qntLess);
      super.buttonList.add(this.buy);
   }

   protected void actionPerformed(GuiButton guiButton) {
      if (guiButton.enabled) {
         switch (guiButton.id) {
            case 0:
               this.qnt = this.qnt + 1 <= 32 ? this.qnt + 1 : this.qnt;
               break;
            case 1:
               this.qnt = this.qnt - 1 >= 1 ? this.qnt - 1 : this.qnt;
               break;
            case 2:
               NetworkHandler.sendToServer(new MessageDelivery(this.qnt));
               super.mc.player.closeScreen();
               super.mc.player.sendMessage(new TextComponentString(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "iCraft" + TextFormatting.BLUE + "] " + ICraftUtils.localize("msg.iCraft.delivery")));
         }
      }
   }

   public void drawScreen(int mouseX, int mouseY, float partialTick) {
      super.drawScreen(mouseX, mouseY, partialTick);
      GlStateManager.pushMatrix();
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableColorMaterial();
      GlStateManager.enableLighting();
      super.itemRender.zLevel = 100.0F;
      ItemStack iron = new ItemStack(Items.IRON_INGOT, this.qnt * 2);
      ItemStack pizza = new ItemStack(RegisterItems.pizza, this.qnt);
      super.itemRender.renderItemAndEffectIntoGUI(iron, super.guiWidth + 53, super.guiHeight + 45);
      super.itemRender.renderItemOverlays(super.fontRenderer, iron, super.guiWidth + 53, super.guiHeight + 45);
      super.itemRender.renderItemAndEffectIntoGUI(pizza, super.guiWidth + 107, super.guiHeight + 45);
      super.itemRender.renderItemOverlays(super.fontRenderer, pizza, super.guiWidth + 107, super.guiHeight + 45);
      super.itemRender.zLevel = 0.0F;
      GlStateManager.disableLighting();
      if (this.isMouseOver(53, 45, 16, 16, mouseX, mouseY)) {
         this.renderToolTip(iron, mouseX, mouseY);
      } else if (this.isMouseOver(107, 45, 16, 16, mouseX, mouseY)) {
         this.renderToolTip(pizza, mouseX, mouseY);
      }

      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.drawTime();
      super.drawGuiContainerForegroundLayer(mouseX, mouseY);
   }

   protected void mouseClicked(int x, int y, int button) {
      try {
         super.mouseClicked(x, y, button);
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      if (button == 0) {
         int xAxis = x - super.guiWidth;
         int yAxis = y - super.guiHeight;
         if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158) {
            super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
         }
      }
   }
}
