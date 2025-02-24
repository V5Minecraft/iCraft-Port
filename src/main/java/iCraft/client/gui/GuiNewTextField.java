package iCraft.client.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiNewTextField extends GuiTextField {
   private final FontRenderer fontRendererObj;
   private String text;
   private int cursorCounter;
   private boolean isEnabled;
   private int lineScrollOffset;
   private int enabledColor;
   private int disabledColor;

   public GuiNewTextField(int componentId, FontRenderer fontRenderer, int startX, int startY, int sizeX, int sizeY) {
      super(componentId, fontRenderer, startX, startY, sizeX, sizeY);
      this.fontRendererObj = fontRenderer;
   }

   private void initializeVars() {
      Class textField = this.getClass().getSuperclass();

      try {
         this.text = this.getText();
         Field cursorC = GuiTextField.class.getDeclaredField("cursorCounter");
         cursorC.setAccessible(true);
         this.cursorCounter = (Integer) cursorC.get(textField);
         Field isE = GuiTextField.class.getDeclaredField("isEnabled");
         isE.setAccessible(true);
         this.isEnabled = (Boolean) isE.get(textField);
         Field lineScroll = GuiTextField.class.getDeclaredField("lineScrollOffset");
         lineScroll.setAccessible(true);
         this.lineScrollOffset = (Integer) lineScroll.get(textField);
         Field enabledCl = GuiTextField.class.getDeclaredField("enabledColor");
         enabledCl.setAccessible(true);
         this.enabledColor = (Integer) enabledCl.get(textField);
         Field disabledCl = GuiTextField.class.getDeclaredField("disabledColor");
         disabledCl.setAccessible(true);
         this.disabledColor = (Integer) disabledCl.get(textField);
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }

   public void drawTextBox() {
      this.initializeVars();
      if (this.getVisible()) {
         if (this.getEnableBackgroundDrawing()) {
            drawRect(super.x - 1, super.y - 1, super.x + super.width + 1, super.y + super.height + 1, -6250336);
            drawRect(super.x, super.y, super.x + super.width, super.y + super.height, -16777216);
         }

         int i = this.isEnabled ? this.enabledColor : this.disabledColor;
         int j = this.getCursorPosition() - this.lineScrollOffset;
         int k = this.getSelectionEnd() - this.lineScrollOffset;
         String s = this.fontRendererObj.trimStringToWidth(this.text.substring(this.lineScrollOffset), this.getWidth());
         boolean flag = j >= 0 && j <= s.length();
         boolean flag1 = this.isFocused() && this.cursorCounter / 6 % 2 == 0 && flag;
         int l = this.getEnableBackgroundDrawing() ? super.x + 4 : super.x;
         int i1 = this.getEnableBackgroundDrawing() ? super.y + (super.height - 8) / 2 : super.y;
         int j1 = l;
         if (k > s.length()) {
            k = s.length();
         }

         if (s.length() > 0) {
            String s1 = flag ? s.substring(0, j) : s;
            j1 = this.fontRendererObj.drawString(s1, l, i1, i);
         }

         boolean flag2 = this.getCursorPosition() < this.text.length() || this.text.length() >= this.getMaxStringLength();
         int k1 = j1;
         if (!flag) {
            k1 = j > 0 ? l + super.width : l;
         } else if (flag2) {
            k1 = j1 - 1;
            --j1;
         }

         if (s.length() > 0 && flag && j < s.length()) {
            this.fontRendererObj.drawString(s.substring(j), j1, i1, i);
         }

         if (flag1) {
            if (flag2) {
               Gui.drawRect(k1, i1 - 1, k1 + 1, i1 + 1 + this.fontRendererObj.FONT_HEIGHT, -3092272);
            } else {
               this.fontRendererObj.drawString("_", k1, i1, i);
            }
         }

         if (k != j) {
            int l1 = l + this.fontRendererObj.getStringWidth(s.substring(0, k));
            Class textField = this.getClass().getSuperclass();

            try {
               Method drawCursor = GuiTextField.class.getDeclaredMethod("drawCursorVertical", Integer.class, Integer.class, Integer.class, Integer.class);
               drawCursor.setAccessible(true);
               drawCursor.invoke(textField, k1, i1 - 1, l1 - 1, i1 + 1 + this.fontRendererObj.FONT_HEIGHT);
            } catch (Exception var15) {
               var15.printStackTrace();
            }
         }
      }
   }
}
