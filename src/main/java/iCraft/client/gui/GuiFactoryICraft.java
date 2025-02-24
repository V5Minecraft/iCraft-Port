package iCraft.client.gui;

import iCraft.core.utils.ICraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;

import java.util.Set;

public class GuiFactoryICraft implements IModGuiFactory {
   @Override
   public void initialize(Minecraft minecraftInstance) {
   }

   @Override
   public boolean hasConfigGui() {
      return true;
   }

   @Override
   public GuiScreen createConfigGui(GuiScreen parentScreen) {
      return new ConfigGUI(parentScreen);
   }

   @Override
   public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
      return null;
   }

   public static class ConfigGUI extends GuiConfig {
      public ConfigGUI(GuiScreen parent) {
         super(parent, ICraftUtils.getConfigElements(), "iCraft", false, true, ICraftUtils.localize("iCraft.configgui.title"));
      }
   }
}
