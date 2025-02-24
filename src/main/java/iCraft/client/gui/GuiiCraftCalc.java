package iCraft.client.gui;

import iCraft.core.ICraft;
import java.io.IOException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiiCraftCalc extends GuiiCraftBase {
   private String exNum1 = "";
   private String exNum2 = "";
   private static int operation = 0;
   public boolean equalsPressed = false;

   public GuiiCraftCalc(String resource) {
      super(resource);
   }

   public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
      this.drawResizedString(this.getExResult().endsWith(".0") ? this.getExResult().substring(0, this.getExResult().indexOf(".")) : this.getExResult(), 146, 78, 4210752, 0.5F);
      this.drawTime();
      super.drawGuiContainerForegroundLayer(mouseX, mouseY);
   }

   public String getResult() {
      float num1 = Float.parseFloat(this.exNum1);
      float num2 = Float.parseFloat(this.exNum2);
      float result;
      switch (operation) {
         case 1:
            result = num1 + num2;
            return Float.toString(result);
         case 2:
            result = num1 - num2;
            return Float.toString(result);
         case 3:
            result = num1 / num2;
            return Float.toString(result);
         case 4:
            result = num1 * num2;
            return Float.toString(result);
         default:
            return null;
      }
   }

   public String op(int operation) {
      switch (operation) {
         case 1:
            return "+";
         case 2:
            return "-";
         case 3:
            return "/";
         case 4:
            return "*";
         default:
            return null;
      }
   }

   public String getExResult() {
      return !this.exNum1.equals("") ? (operation != 0 ? (!this.exNum2.equals("") ? (!this.equalsPressed ? this.exNum1 + this.op(operation) + this.exNum2 : this.getResult()) : this.exNum1 + this.op(operation)) : this.exNum1) : "";
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
            operation = 0;
            this.exNum1 = "";
            this.exNum2 = "";
            this.equalsPressed = false;
            super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
         }

         if (this.exNum1.length() + this.exNum2.length() < 16) {
            if (xAxis >= 52 && xAxis <= 86 && yAxis >= 120 && yAxis <= 136) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "0";
               } else {
                  this.exNum2 = this.exNum2 + "0";
               }
            }

            if (xAxis >= 52 && xAxis <= 68 && yAxis >= 102 && yAxis <= 118) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "1";
               } else {
                  this.exNum2 = this.exNum2 + "1";
               }
            }

            if (xAxis >= 70 && xAxis <= 86 && yAxis >= 102 && yAxis <= 118) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "2";
               } else {
                  this.exNum2 = this.exNum2 + "2";
               }
            }

            if (xAxis >= 89 && xAxis <= 105 && yAxis >= 102 && yAxis <= 118) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "3";
               } else {
                  this.exNum2 = this.exNum2 + "3";
               }
            }

            if (xAxis >= 52 && xAxis <= 68 && yAxis >= 84 && yAxis <= 100) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "4";
               } else {
                  this.exNum2 = this.exNum2 + "4";
               }
            }

            if (xAxis >= 70 && xAxis <= 86 && yAxis >= 84 && yAxis <= 100) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "5";
               } else {
                  this.exNum2 = this.exNum2 + "5";
               }
            }

            if (xAxis >= 89 && xAxis <= 105 && yAxis >= 84 && yAxis <= 100) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "6";
               } else {
                  this.exNum2 = this.exNum2 + "6";
               }
            }

            if (xAxis >= 52 && xAxis <= 68 && yAxis >= 66 && yAxis <= 82) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "7";
               } else {
                  this.exNum2 = this.exNum2 + "7";
               }
            }

            if (xAxis >= 70 && xAxis <= 86 && yAxis >= 66 && yAxis <= 82) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "8";
               } else {
                  this.exNum2 = this.exNum2 + "8";
               }
            }

            if (xAxis >= 89 && xAxis <= 105 && yAxis >= 66 && yAxis <= 82) {
               if (operation == 0) {
                  this.exNum1 = this.exNum1 + "9";
               } else {
                  this.exNum2 = this.exNum2 + "9";
               }
            }

            if (xAxis >= 89 && xAxis <= 105 && yAxis >= 120 && yAxis <= 136) {
               if (operation == 0) {
                  if (this.exNum1.length() > 0 && !this.exNum1.contains(".")) {
                     this.exNum1 = this.exNum1 + ".";
                  }
               } else if (this.exNum2.length() > 0 && !this.exNum2.contains(".")) {
                  this.exNum2 = this.exNum2 + ".";
               }
            }
         }

         if (xAxis >= 107 && xAxis <= 123 && yAxis >= 84 && yAxis <= 100) {
            operation = 1;
         }

         if (xAxis >= 107 && xAxis <= 123 && yAxis >= 66 && yAxis <= 82) {
            operation = 2;
         }

         if (xAxis >= 89 && xAxis <= 105 && yAxis >= 48 && yAxis <= 64) {
            operation = 3;
         }

         if (xAxis >= 107 && xAxis <= 123 && yAxis >= 48 && yAxis <= 64) {
            operation = 4;
         }

         if (xAxis >= 70 && xAxis <= 86 && yAxis >= 48 && yAxis <= 64) {
            if (operation == 0 && !this.exNum1.contains("-")) {
               this.exNum1 = (new StringBuffer(this.exNum1)).insert(0, "-").toString();
            } else if (operation != 0 && !this.exNum2.contains("-")) {
               this.exNum2 = (new StringBuffer(this.exNum2)).insert(0, "-").toString();
            }
         }

         if (xAxis >= 107 && xAxis <= 123 && yAxis >= 102 && yAxis <= 136 && !this.exNum2.equals("")) {
            this.equalsPressed = true;
         }

         if (xAxis >= 52 && xAxis <= 68 && yAxis >= 48 && yAxis <= 64) {
            operation = 0;
            this.exNum1 = "";
            this.exNum2 = "";
            this.equalsPressed = false;
         }
      }
   }
}
