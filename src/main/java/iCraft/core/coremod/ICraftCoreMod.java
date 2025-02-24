package iCraft.core.coremod;

import java.util.Map;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ICraftCoreMod implements IFMLLoadingPlugin {
   public static Logger log;

   public ICraftCoreMod() {
      log = LogManager.getLogger("iCraft-core");
   }

   public String[] getASMTransformerClass() {
      return null;
   }

   public String getModContainerClass() {
      return null;
   }

   public String getSetupClass() {
      return "iCraft.core.coremod.Setup";
   }

   public void injectData(Map<String, Object> data) {
   }

   public String getAccessTransformerClass() {
      return null;
   }
}
