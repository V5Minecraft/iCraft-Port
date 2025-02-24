package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageReceivedCall;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftClientUtils;
import java.io.IOException;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiiCraftIncomingCall extends GuiiCraftBase {
   public GuiiCraftIncomingCall(String resource) {
      super(resource);
   }

   protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
      super.mc.renderEngine.bindTexture(ICraftClientUtils.getResource(ICraftClientUtils.ResourceType.GUI, this.isCalling() ? "guiicraftincall.png" : "guiicraftincomingcall.png"));
      super.guiWidth = (super.width - super.xSize) / 2;
      super.guiHeight = (super.height - super.ySize) / 2;
      this.drawTexturedModalRect(super.guiWidth, super.guiHeight, 0, 0, super.xSize, super.ySize);
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      if (this.isCalling()) {
         if (ICraftClientUtils.getClientPlayer(super.mc.world, true) != null) {
            GuiInventory.drawEntityOnScreen(88, 101, 26, 0.0F, 20.0F, ICraftClientUtils.getClientPlayer(super.mc.world, true));
         }

         this.drawResizedString("Calling to " + ICraftClientUtils.getPlayerNumber(true), 118, 218, 16777215, 0.5F);
      } else {
         if (ICraftClientUtils.getClientPlayer(super.mc.world, false) != null) {
            GuiInventory.drawEntityOnScreen(88, 101, 26, 0.0F, 20.0F, ICraftClientUtils.getClientPlayer(super.mc.world, false));
         }

         this.drawResizedString(ICraftClientUtils.getPlayerNumber(false) + " is calling you", 118, 218, 16777215, 0.5F);
      }

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
         if (this.isCalling()) {
            if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158) {
               super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
               NetworkHandler.sendToServer(new MessageReceivedCall(0, true));
            }

            if (xAxis >= 72 && xAxis <= 103 && yAxis >= 117 && yAxis <= 127) {
               super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
               NetworkHandler.sendToServer(new MessageReceivedCall(0, true));
            }
         } else {
            if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158) {
               super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
               NetworkHandler.sendToServer(new MessageReceivedCall(0, false));
            }

            if (xAxis >= 53 && xAxis <= 84 && yAxis >= 121 && yAxis <= 131) {
               super.mc.player.openGui(ICraft.instance, 7, super.mc.world, 0, 0, 0);
               NetworkHandler.sendToServer(new MessageReceivedCall(7, false));
            }

            if (xAxis >= 90 && xAxis <= 122 && yAxis >= 121 && yAxis <= 131) {
               super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
               NetworkHandler.sendToServer(new MessageReceivedCall(0, false));
            }
         }
      }

   }

   private boolean isCalling() {
      ItemStack itemStack = super.mc.player.getHeldItemMainhand();
      return itemStack != null && itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("isCalling") && itemStack.getTagCompound().getBoolean("isCalling");
   }
}
