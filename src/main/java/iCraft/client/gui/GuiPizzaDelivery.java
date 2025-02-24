package iCraft.client.gui;

import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.inventory.container.ContainerPizzaDelivery;
import iCraft.core.register.RegisterItems;
import iCraft.core.utils.ICraftClientUtils;
import iCraft.core.utils.ICraftClientUtils.ResourceType;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPizzaDelivery extends GuiContainer {
   private final EntityPizzaDelivery delivery;

   public GuiPizzaDelivery(InventoryPlayer inventory, EntityPizzaDelivery delivery) {
      super(new ContainerPizzaDelivery(inventory, delivery));
      this.delivery = delivery;
   }

   @Override
   protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      fontRenderer.drawString(ICraftUtils.localize("container.inventory"), 8, ySize - 96 + 3, 0x404040);
      super.drawGuiContainerForegroundLayer(mouseX, mouseY);
   }

   @Override
   protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
      mc.renderEngine.bindTexture(ICraftClientUtils.getResource(ResourceType.GUI, "guipizzadelivery.png"));
      int guiWidth = (width - xSize) / 2;
      int guiHeight = (height - ySize) / 2;
      drawTexturedModalRect(guiWidth, guiHeight, 0, 0, xSize, ySize);
   }

   @Override
   public void drawScreen(int mouseX, int mouseY, float partialTick) {
      super.drawScreen(mouseX, mouseY, partialTick);

      int guiWidth = (width - xSize) / 2;
      int guiHeight = (height - ySize) / 2;
      GlStateManager.pushMatrix();
      RenderHelper.enableGUIStandardItemLighting();
      GlStateManager.disableLighting();
      GlStateManager.enableRescaleNormal();
      GlStateManager.enableColorMaterial();
      GlStateManager.enableLighting();
      itemRender.zLevel = 100.0F;

      ItemStack iron = new ItemStack(Items.IRON_INGOT, (delivery.getQuantity() * 2));
      ItemStack pizza = new ItemStack(RegisterItems.pizza, delivery.getQuantity());

      itemRender.renderItemAndEffectIntoGUI(iron, guiWidth + 62, guiHeight + 24);
      itemRender.renderItemOverlays(fontRenderer, iron, guiWidth + 62, guiHeight + 24);

      itemRender.renderItemAndEffectIntoGUI(pizza, guiWidth + 120, guiHeight + 24);
      itemRender.renderItemOverlays(fontRenderer, pizza, guiWidth + 120, guiHeight + 24);

      itemRender.zLevel = 0.0F;
      GlStateManager.disableLighting();
      if (isPointInRegion(62, 24, 16, 16, mouseX, mouseY))
         renderToolTip(iron, mouseX, mouseY);
      else if (isPointInRegion(120, 24, 16, 16, mouseX, mouseY))
         renderToolTip(pizza, mouseX, mouseY);

      GlStateManager.popMatrix();
      GlStateManager.enableLighting();
      GlStateManager.enableDepth();
      RenderHelper.enableStandardItemLighting();
   }
}
