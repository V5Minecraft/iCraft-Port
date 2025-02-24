package iCraft.client;

import com.google.common.io.Files;
import iCraft.client.gui.GuiPizzaDelivery;
import iCraft.client.gui.GuiiCraft;
import iCraft.client.gui.GuiiCraftBlacklist;
import iCraft.client.gui.GuiiCraftCalc;
import iCraft.client.gui.GuiiCraftClock;
import iCraft.client.gui.GuiiCraftContacts;
import iCraft.client.gui.GuiiCraftDelivery;
import iCraft.client.gui.GuiiCraftInCall;
import iCraft.client.gui.GuiiCraftIncomingCall;
import iCraft.client.gui.GuiiCraftMP3Player;
import iCraft.client.gui.GuiiCraftNumPad;
import iCraft.client.gui.GuiiCraftSettings;
import iCraft.client.gui.GuiiCraftShopping;
import iCraft.client.mp3.MP3Player;
import iCraft.client.render.RenderFallingCase;
import iCraft.client.render.RenderPizzaDelivery;
import iCraft.core.CommonProxy;
import iCraft.core.ICraft;
import iCraft.core.entity.EntityPackingCase;
import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.register.RegisterBlocks;
import iCraft.core.register.RegisterItems;
import iCraft.core.utils.ICraftClientUtils;
import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ClientProxy extends CommonProxy {
   public void loadConfiguration() {
      super.loadConfiguration();
      ICraftClientUtils.homePage = ICraft.configuration.get("general", "Web Home Page", "mod://mcef/home.html").getString();
      if (ICraft.configuration.hasChanged()) {
         ICraft.configuration.save();
      }

   }

   public void onConfigSync() {
      super.onConfigSync();
      if (ICraft.isVoiceEnabled && ICraft.voiceClient != null) {
         ICraft.voiceClient.start();
      }

      if (ICraft.mp3Folder.listFiles().length != 0) {
         File[] arr = ICraft.mp3Folder.listFiles();
         int len = arr.length;

         for (int i = 0; i < len; ++i) {
            File file = arr[i];
            if (!file.isDirectory() && Files.getFileExtension(file.getAbsolutePath()).equals("mp3")) {
               ICraft.musics.add(file);
               String patchedName = file.getName().replaceAll(".mp3", "");
               ICraft.musicNames.add(patchedName);
            }
         }
      }

      ICraft.mp3Player = new MP3Player();
   }

   public EntityPlayer getClientPlayer() {
      return Minecraft.getMinecraft().player;
   }

   public void registerUtilities() {
      FMLCommonHandler.instance().bus().register(new ICraftClientEventHandler());
      FMLCommonHandler.instance().bus().register(new ClientTickHandler());
      new ICraftKeyHandler();
   }

   public void registerNetHandler() {
      new InternetHandler();
   }

   public void registerRenders() {
      ItemModelMesher itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

      itemModelMesher.register(RegisterItems.iCraft, 0, new ModelResourceLocation("icraft:icraft", "inventory"));
      itemModelMesher.register(RegisterItems.pizza, 0, new ModelResourceLocation("icraft:pizza", "inventory"));
      itemModelMesher.register(Item.getItemFromBlock(RegisterBlocks.caseBlock), 0, new ModelResourceLocation("icraft:caseblock", "inventory"));

      RenderingRegistry.registerEntityRenderingHandler(EntityPackingCase.class, new RenderFallingCase(Minecraft.getMinecraft().getRenderManager()));
      RenderingRegistry.registerEntityRenderingHandler(EntityPizzaDelivery.class, new RenderPizzaDelivery(Minecraft.getMinecraft().getRenderManager()));
      ICraft.logger.info("Render registrations complete.");
   }

   public GuiScreen getClientGui(int ID, EntityPlayer player, World world, int x, int y, int z) {
      switch (ID) {
         case 0:
            return new GuiiCraft("GuiiCraft");
         case 1:
            return new GuiiCraftCalc("GuiiCraftCalc");
         case 2:
         case 8:
         default:
            return null;
         case 3:
            return new GuiiCraftClock("GuiiCraftClock");
         case 4:
            return new GuiiCraftSettings("GuiiCraftSettings");
         case 5:
            return new GuiiCraftNumPad("GuiiCraftNumPad");
         case 6:
            return new GuiiCraftIncomingCall("whatever");
         case 7:
            return new GuiiCraftInCall("GuiiCraftInCall");
         case 9:
            return new GuiiCraftShopping("GuiiCraftShopping");
         case 10:
            return new GuiiCraftMP3Player("GuiiCraftMP3Player");
         case 11:
            EntityPizzaDelivery pizza = (EntityPizzaDelivery) world.getEntityByID(x);
            if (pizza != null) {
               return new GuiPizzaDelivery(player.inventory, pizza);
            }
         case 12:
            return new GuiiCraftDelivery("GuiiCraftDelivery");
         case 13:
            return new GuiiCraftBlacklist("GuiiCraftClock");
         case 14:
            return new GuiiCraftContacts("GuiiCraftContacts");
      }
   }

   public File getMinecraftDir() {
      return Minecraft.getMinecraft().mcDataDir;
   }

   public void stopPhoneRingSound() {
      Minecraft.getMinecraft().getSoundHandler().stopSound((ISound) ClientTickHandler.phoneRing);
   }

   public void preInit(FMLPreInitializationEvent event) {
      super.preInit(event);
   }

   public void init(FMLInitializationEvent event) {
      super.init(event);
      RegisterBlocks.registerRender();

      GameRegistry.addShapedRecipe(
              new net.minecraft.util.ResourceLocation("icraft", "icraft_recipe"),
              new net.minecraft.util.ResourceLocation("icraft"),
              new ItemStack(RegisterItems.iCraft),
              "III",
              "IDI",
              "III",
              'I', Items.IRON_INGOT,
              'D', Blocks.GLASS_PANE
      );
   }
}
