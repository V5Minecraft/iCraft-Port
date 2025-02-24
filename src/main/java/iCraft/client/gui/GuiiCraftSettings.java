package iCraft.client.gui;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftUtils;
import java.io.IOException;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiiCraftSettings extends GuiiCraftBase {
    public GuiiCraftSettings(String resource) {
        super(resource);
    }

    protected void drawGuiContainerBackgroundLayer(float partialTick, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(partialTick, mouseX, mouseY);
        int xAxis = mouseX - super.guiWidth;
        int yAxis = mouseY - super.guiHeight;
        boolean mouseOver = xAxis >= 51 && xAxis <= 124 && yAxis >= 54 && yAxis <= 67;
        this.drawTexturedModalRect(super.guiWidth + 51, super.guiHeight + 54, mouseOver ? 0 : 74, 166, 74, 14);
        boolean mouseOver1 = xAxis >= 51 && xAxis <= 124 && yAxis >= 68 && yAxis <= 84;
        this.drawTexturedModalRect(super.guiWidth + 51, super.guiHeight + 68, mouseOver1 ? 0 : 74, 166, 74, 14);
    }

    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        super.fontRenderer.drawString(ICraftUtils.localize("iCraft.gui.Settings"), 58, 40, 16777215);
        super.fontRenderer.drawString(ICraftUtils.localize("iCraft.gui.Blacklist"), 58, 57, 16777215);
        super.fontRenderer.drawString(ICraftUtils.localize("iCraft.gui.Contacts"), 58, 71, 16777215);
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

            if (xAxis >= 51 && xAxis <= 124 && yAxis >= 54 && yAxis <= 67) {
                super.mc.player.openGui(ICraft.instance, 13, super.mc.world, 0, 0, 0);
            }

            if (xAxis >= 51 && xAxis <= 124 && yAxis >= 68 && yAxis <= 84) {
                super.mc.player.openGui(ICraft.instance, 14, super.mc.world, 0, 0, 0);
            }
        }
    }
}
