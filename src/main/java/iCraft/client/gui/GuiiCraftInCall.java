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
public class GuiiCraftInCall extends GuiiCraftBase {
    public GuiiCraftInCall(String resource) {
        super(resource);
    }

    public void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        if (this.isCalling()) {
            if (ICraftClientUtils.getClientPlayer(super.mc.world, true) != null) {
                GuiInventory.drawEntityOnScreen(88, 101, 26, 0.0F, 20.0F, ICraftClientUtils.getClientPlayer(super.mc.world, true));
            }
        } else if (ICraftClientUtils.getClientPlayer(super.mc.world, false) != null) {
            GuiInventory.drawEntityOnScreen(88, 101, 26, 0.0F, 20.0F, ICraftClientUtils.getClientPlayer(super.mc.world, false));
        }

        this.drawResizedString("Talking with " + (this.isCalling() ? ICraftClientUtils.getPlayerNumber(true) : ICraftClientUtils.getPlayerNumber(false)), 118, 218, 16777215, 0.5F);
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
            if (xAxis >= 51 && xAxis <= 67 && yAxis >= 38 && yAxis <= 54) {
                super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
                NetworkHandler.sendToServer(new MessageReceivedCall(0, this.isCalling()));
            }

            if (xAxis >= 72 && xAxis <= 103 && yAxis >= 117 && yAxis <= 127) {
                super.mc.player.openGui(ICraft.instance, 0, super.mc.world, 0, 0, 0);
                NetworkHandler.sendToServer(new MessageReceivedCall(0, this.isCalling()));
            }
        }

    }

    private boolean isCalling() {
        ItemStack itemStack = super.mc.player.getHeldItemMainhand();
        return itemStack != null && itemStack.getTagCompound() != null && itemStack.getTagCompound().hasKey("isCalling") && itemStack.getTagCompound().getBoolean("isCalling");
    }
}
