package iCraft.core.coremod;

import java.io.File;
import java.util.Map;
import net.minecraftforge.fml.relauncher.FMLInjectionData;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

public class Setup implements IFMLCallHook {
   private File mcDir;

   public Void call() {
      Library.init(this.mcDir, (String) FMLInjectionData.data()[4]);
      return null;
   }

   public void injectData(Map<String, Object> data) {
      this.mcDir = (File) data.get("mcLocation");
   }
}
