package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.network.MessageIncomeCalling;
import iCraft.core.network.NetworkHandler;
import java.io.IOException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiiCraftNumPad extends GuiiCraftBase {
    public static String callNumber = "";

    public GuiiCraftNumPad(String resource) {
        super(resource);
    }

    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.drawResizedString(callNumber, 154, 74, 4210752, 0.5F);
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
                callNumber = "";
            }

            if (callNumber.length() < 8) {
                if (xAxis >= 79 && xAxis <= 95 && yAxis >= 104 && yAxis <= 120) {
                    callNumber = callNumber + "0";
                }

                if (xAxis >= 61 && xAxis <= 77 && yAxis >= 86 && yAxis <= 102) {
                    callNumber = callNumber + "1";
                }

                if (xAxis >= 79 && xAxis <= 95 && yAxis >= 86 && yAxis <= 102) {
                    callNumber = callNumber + "2";
                }

                if (xAxis >= 98 && xAxis <= 114 && yAxis >= 86 && yAxis <= 102) {
                    callNumber = callNumber + "3";
                }

                if (xAxis >= 61 && xAxis <= 77 && yAxis >= 68 && yAxis <= 84) {
                    callNumber = callNumber + "4";
                }

                if (xAxis >= 79 && xAxis <= 95 && yAxis >= 68 && yAxis <= 84) {
                    callNumber = callNumber + "5";
                }

                if (xAxis >= 98 && xAxis <= 114 && yAxis >= 68 && yAxis <= 84) {
                    callNumber = callNumber + "6";
                }

                if (xAxis >= 61 && xAxis <= 77 && yAxis >= 50 && yAxis <= 66) {
                    callNumber = callNumber + "7";
                }

                if (xAxis >= 79 && xAxis <= 95 && yAxis >= 50 && yAxis <= 66) {
                    callNumber = callNumber + "8";
                }

                if (xAxis >= 98 && xAxis <= 114 && yAxis >= 50 && yAxis <= 66) {
                    callNumber = callNumber + "9";
                }
            }

            if (xAxis >= 89 && xAxis <= 114 && yAxis >= 124 && yAxis <= 134 && callNumber.length() != 0) {
                callNumber = callNumber.substring(0, callNumber.length() - 1);
            }

            if (xAxis >= 61 && xAxis <= 85 && yAxis >= 124 && yAxis <= 134 && callNumber.length() == 8) {
                NetworkHandler.sendToServer(new MessageIncomeCalling(super.mc.player.getHeldItemMainhand().getTagCompound().getInteger("number"), Integer.parseInt(callNumber)));
            }
        }
    }
}
