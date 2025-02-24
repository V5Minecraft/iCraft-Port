package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageCraftBay;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftUtils;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class GuiiCraftShopping extends GuiiCraftBase {
   private int i = 0;
   private int qnt = 1;

   public GuiiCraftShopping(String resource) {
      super(resource);
   }

   public void initGui() {
      super.initGui();
      Keyboard.enableRepeatEvents(false);
      super.buttonList.clear();
      GuiButton left = new GuiButton(0, super.guiWidth + 52, super.guiHeight + 75, 15, 20, "<");
      GuiButton right = new GuiButton(1, super.guiWidth + 96, super.guiHeight + 75, 15, 20, ">");
      GuiButton up = new GuiButton(2, super.guiWidth + 125, super.guiHeight + 49, 10, 10, "\u25B2");
      GuiButton down = new GuiButton(3, super.guiWidth + 125, super.guiHeight + 58, 10, 10, "\u25BC");
      GuiButton buy = new GuiButton(4, super.guiWidth + 67, super.guiHeight + 75, 29, 20, "Buy");
      super.buttonList.add(left);
      super.buttonList.add(right);
      super.buttonList.add(up);
      super.buttonList.add(down);
      super.buttonList.add(buy);
   }

   public void updateScreen() {
      super.updateScreen();
      if (this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()).getCount() * this.qnt > 64 || this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()).getCount() * this.qnt > 64) {
         this.qnt = 1;
      }

   }

   private ItemStack getBuyStack(Collection<ItemStack> col) {
      return (ItemStack) col.toArray()[0];
   }

   private ItemStack getSellStack(Collection<ItemStack> col) {
      return (ItemStack) col.toArray()[0];
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
      ItemStack buyItem = new ItemStack(this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()).getItem(), this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()).getCount() * this.qnt, this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()).getItemDamage());
      ItemStack sellItem = new ItemStack(this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()).getItem(), this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()).getCount() * this.qnt, this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()).getItemDamage());
      super.itemRender.renderItemAndEffectIntoGUI(buyItem, super.guiWidth + 98, super.guiHeight + 51);
      super.itemRender.renderItemOverlays(super.fontRenderer, buyItem, super.guiWidth + 98, super.guiHeight + 51);
      super.itemRender.renderItemAndEffectIntoGUI(sellItem, super.guiWidth + 48, super.guiHeight + 51);
      super.itemRender.renderItemOverlays(super.fontRenderer, sellItem, super.guiWidth + 48, super.guiHeight + 51);
      super.itemRender.zLevel = 0.0F;
      GlStateManager.disableLighting();
      if (this.isMouseOver(98, 51, 16, 16, mouseX, mouseY)) {
         this.renderToolTip(this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()), mouseX, mouseY);
      }

      if (this.isMouseOver(48, 51, 16, 16, mouseX, mouseY)) {
         this.renderToolTip(this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()), mouseX, mouseY);
      }

      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.drawResizedString(ICraftClientUtils.getTime(), 148, 45, 16777215, 0.5F);
      super.drawGuiContainerForegroundLayer(mouseX, mouseY);
   }

   protected void actionPerformed(GuiButton guiButton) {
      if (guiButton.enabled) {
         switch (guiButton.id) {
            case 0:
               this.i = this.i - 1 >= 0 ? this.i - 1 : ICraftUtils.items.size() - 1;
               break;
            case 1:
               this.i = this.i + 1 < ICraftUtils.items.size() ? this.i + 1 : 0;
               break;
            case 2:
               this.qnt = this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()).getCount() * (this.qnt + 1) <= 64 && this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()).getCount() * (this.qnt + 1) <= 64 ? this.qnt + 1 : this.qnt;
               break;
            case 3:
               this.qnt = this.qnt - 1 > 0 ? this.qnt - 1 : this.qnt;
               break;
            case 4:
               if (ICraft.isIBayActive) {
                  NetworkHandler.sendToServer(new MessageCraftBay(this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()).getItem(), this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()).getCount() * this.qnt, this.getBuyStack(((HashMap) ICraftUtils.items.get(this.i)).keySet()).getItemDamage(), this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()).getItem(), this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()).getCount() * this.qnt, this.getSellStack(((HashMap) ICraftUtils.items.get(this.i)).values()).getItemDamage()));
                  super.mc.player.sendMessage(new TextComponentString(TextFormatting.BLUE + "[" + TextFormatting.GOLD + "iCraft" + TextFormatting.BLUE + "] " + TextFormatting.GREEN + ICraftUtils.localize("msg.iCraft.iBay")));
                  super.mc.player.closeScreen();
               }
         }
      }
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
         if (xAxis >= 143 && xAxis <= 158 && yAxis >= 51 && yAxis <= 66) {
            super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
         }
      }
   }
}
