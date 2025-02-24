package iCraft.core;

import iCraft.client.mp3.MP3Player;
import iCraft.client.voice.VoiceClient;
import iCraft.core.entity.EntityPackingCase;
import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.network.DescriptionHandler;
import iCraft.core.network.NetworkHandler;
import iCraft.core.utils.ICraftUtils;
import iCraft.core.voice.VoiceManager;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = "icraft", name = "iCraft", version = "0.7", guiFactory = "iCraft.client.gui.GuiFactoryICraft", dependencies = "after:mcef", acceptedMinecraftVersions = "[1.12.2]")
public class ICraft {
   public static Logger logger = LogManager.getLogger("iCraft");
   @SidedProxy(clientSide = "iCraft.client.ClientProxy", serverSide = "iCraft.core.CommonProxy")
   public static CommonProxy proxy;
   @Instance("icraft")
   public static ICraft instance;
   public static Configuration configuration;
   public static String newestVersion;
   public static VoiceManager voiceManager;
   public static VoiceClient voiceClient;
   public static MP3Player mp3Player;
   public static File mp3Folder;
   public static List<File> musics = new ArrayList();
   public static List<String> musicNames = new ArrayList();
   public static int currentMusicId = 0;
   public static boolean isVoiceEnabled = true;
   public static int VOICE_PORT = 2550;
   public static boolean isIBayActive = true;
   public static String[] buyableItems = new String[0];

   public void addEntities() {
      EntityRegistry.registerModEntity(new ResourceLocation("icraft", "falling_packing_case"), EntityPackingCase.class, "FallingPackingCase", 0, instance, 150, 5, true, 0x000000, 0xFFFFFF);
      EntityRegistry.registerModEntity(new ResourceLocation("icraft", "pizza_delivery"), EntityPizzaDelivery.class, "PizzaDelivery", 1, instance, 150, 5, true, 0x000000, 0xFFFFFF);
   }

   @EventHandler
   public void onServerStart(FMLServerStartingEvent event) {
      if (isVoiceEnabled) {
         voiceManager.start();
      }
   }

   @EventHandler
   public void serverStopping(FMLServerStoppingEvent event) {
      if (isVoiceEnabled) {
         voiceManager.stop();
      }
   }

   @EventHandler
   public void preInit(FMLPreInitializationEvent event) {
      proxy.preInit(event);
      File config = event.getSuggestedConfigurationFile();
      mp3Folder = new File(proxy.getMinecraftDir(), "mods/iCraft/mp3");
      configuration = new Configuration(config);
      mp3Folder.mkdirs();
      FMLCommonHandler.instance().bus().register(new ICraftEventHandler());
      this.addEntities();
      proxy.loadConfiguration();
      FMLCommonHandler.instance().bus().register(new CommonPlayerTracker());
      proxy.registerUtilities();
      NetworkHandler.init();
      DescriptionHandler.init();
      logger.info("Carrier has arrived.");
   }

   @EventHandler
   public void init(FMLInitializationEvent event) {
      proxy.init(event);
      NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
      ICraftUtils.getNewestVersion();
      if (isVoiceEnabled) {
         voiceManager = new VoiceManager();
      }
      if (Loader.isModLoaded("mcef")) {
         proxy.registerNetHandler();
      }
      proxy.registerRenders();
      logger.info("Prismatic core online.");
   }

   @EventHandler
   public void postInit(FMLPostInitializationEvent event) {
      logger.info("Ready to roll out!");
   }
}
