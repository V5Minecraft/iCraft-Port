package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageContacts;
import iCraft.core.network.MessageIncomeCalling;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;

@SideOnly(Side.CLIENT)
public class GuiiCraftContacts extends GuiiCraftBase {
   private boolean isInEditMode = false;
   private float scroll;
   private boolean isDragging = false;
   private boolean canScroll;
   private int dragOffset = 0;
   private List<Integer> list = new ArrayList();
   private GuiTextField textField;

   public GuiiCraftContacts(String resource) {
      super(resource);
   }

   public void initGui() {
      super.initGui();
      this.textField = new GuiTextField(0, super.fontRenderer, super.guiWidth + 52, super.guiHeight + 54, 72, 10);
      this.textField.setMaxStringLength(8);
      this.textField.setFocused(false);
      this.list = super.mc.player.getHeldItemMainhand().getTagCompound() != null ? this.readNBT(super.mc.player.getHeldItemMainhand().getTagCompound()) : this.list;
      this.canScroll = this.list.size() > 5;
   }

   public void updateScreen() {
      super.updateScreen();
      this.textField.updateCursorCounter();
      this.list = super.mc.player.getHeldItemMainhand().getTagCompound() != null ? this.readNBT(super.mc.player.getHeldItemMainhand().getTagCompound()) : this.list;
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
      this.drawTexturedModalRect(super.guiWidth + 114, super.guiHeight + 36, 190, this.isInEditMode ? 51 : 42, 9, 9);
      int xAxis = mouseX - super.guiWidth;
      int yAxis = mouseY - super.guiHeight;

      for (int i = 0; i < 5; ++i) {
         if (this.getIndex() + i < this.list.size()) {
            int yStart = i * 14 + 67;
            boolean mouseOver = xAxis >= 52 && xAxis <= 111 && yAxis >= yStart && yAxis <= yStart + 14;
            this.drawTexturedModalRect(super.guiWidth + 52, super.guiHeight + yStart, mouseOver ? 0 : 60, 166, 60, 14);
         }
      }

      this.textField.drawTextBox();
      if (this.isMouseOver(114, 36, 9, 9, mouseX, mouseY)) {
         List<String> tooltip = new ArrayList();
         tooltip.add(ICraftUtils.localize("iCraft.gui.contactListMode"));
         this.drawHoveringText(tooltip, mouseX, mouseY, super.fontRenderer);
      }

   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      super.fontRenderer.drawString(ICraftUtils.localize("iCraft.gui.Contacts"), 58, 40, 16777215);

      for (int i = 0; i < 5; ++i) {
         if (this.getIndex() + i < this.list.size()) {
            int yStart = i * 28 + 69;
            this.drawResizedString(String.valueOf(this.list.get(this.getIndex() + i)), 110, yStart + 73, 16777215, 0.5F);
         }
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
      this.textField.mouseClicked(x, y, button);
      if (button == 0) {
         int xAxis = x - super.guiWidth;
         int yAxis = y - super.guiHeight;
         if (xAxis >= 80 && xAxis <= 95 && yAxis >= 143 && yAxis <= 158) {
            super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
         }

         if (xAxis >= 114 && xAxis <= 122 && yAxis >= 36 && yAxis <= 44) {
            this.isInEditMode = !this.isInEditMode;
         }

         if (xAxis >= 113 && xAxis <= 123 && yAxis >= this.getScroll() + 67 && yAxis <= this.getScroll() + 67 + 15 && this.canScroll) {
            this.dragOffset = yAxis - (this.getScroll() + 67);
            this.isDragging = true;
         }

         for (int i = 0; i < 5; ++i) {
            if (this.getIndex() + i < this.list.size()) {
               int yStart = i * 14 + 67;
               if (xAxis >= 52 && xAxis <= 111 && yAxis >= yStart && yAxis <= yStart + 14) {
                  if (!this.isInEditMode) {
                     NetworkHandler.sendToServer(new MessageIncomeCalling(super.mc.player.getHeldItemMainhand().getTagCompound().getInteger("number"), (Integer) this.list.get(this.getIndex() + i)));
                  } else {
                     NetworkHandler.sendToServer(new MessageContacts(1, (Integer) this.list.get(this.getIndex() + i)));
                  }
               }
            }
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
         if (i > 0) {
            i = 1;
         }

         if (i < 0) {
            i = -1;
         }

         this.scroll = Math.min(Math.max((float) ((double) this.scroll - (double) i / 20.0D), 0.0F), 1.0F);
      }

   }

   protected void keyTyped(char ch, int keyCode) {
      this.textField.textboxKeyTyped(ch, keyCode);
      if (this.textField.isFocused() && keyCode != 1) {
         if (keyCode == 28) {
            if (this.textField.getText().matches("[0-9]+") && this.textField.getText().length() == 8 && !this.list.contains(Integer.parseInt(this.textField.getText()))) {
               NetworkHandler.sendToServer(new MessageContacts(0, Integer.parseInt(this.textField.getText())));
            }

            this.textField.setText("");
         }
      } else {
         super.keyTyped(ch, keyCode);
      }

   }

   private List<Integer> readNBT(NBTTagCompound nbtTags) {
      NBTTagList tagList = nbtTags.getTagList("contacts", 10);
      List<Integer> contacts = new ArrayList();

      for (int i = 0; i < tagList.tagCount(); ++i) {
         NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
         int j = tagCompound.getInteger("number" + i);
         contacts.add(i, j);
      }

      return contacts;
   }
}
