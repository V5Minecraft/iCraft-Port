package iCraft.core.utils;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.client.FMLClientHandler;
import org.tritonus.share.sampled.file.TAudioFileFormat;

public class ICraftClientUtils {
   public static boolean hour24 = true;
   public static String homePage = "mod://mcef/home.html";
   private static Minecraft mc = FMLClientHandler.instance().getClient();

   public static void checkUpdates(EntityPlayer player) {
      if (ICraft.newestVersion != null) {
         try {
            double version = Double.parseDouble("0.7");
            double latestVersion = Double.parseDouble(ICraft.newestVersion);
            if (latestVersion > version) {
               player.sendMessage(new TextComponentString(TextFormatting.AQUA + "----------------- " + TextFormatting.BLUE + "[" + TextFormatting.GOLD + "iCraft" + TextFormatting.BLUE + "]" + TextFormatting.AQUA + " -----------------"));
               player.sendMessage(new TextComponentString(TextFormatting.GREEN + ICraftUtils.localize("update.outdated") + " " + TextFormatting.GOLD + latestVersion + TextFormatting.GREEN + "."));
            }
         } catch (NumberFormatException e) {
            e.printStackTrace();
         }
      }

   }

   public static EntityLivingBase getClientPlayer(WorldClient clientWorld, boolean isCalling) {
      List<NonNullList<ItemStack>> itemStacks = Arrays.asList(mc.player.inventory.mainInventory);
      Iterator i = itemStacks.iterator();

      ItemStack itemStack;
      do {
         if (!i.hasNext()) {
            return null;
         }

         itemStack = (ItemStack)i.next();
      } while(itemStack == null || !(itemStack.getItem() instanceof ItemiCraft) || itemStack.getTagCompound() == null);

      return isCalling ? clientWorld.getPlayerEntityByName(itemStack.getTagCompound().getString("calledPlayer")) : clientWorld.getPlayerEntityByName(itemStack.getTagCompound().getString("callingPlayer"));
   }

   public static int getPlayerNumber(boolean isCalling) {
      List<NonNullList<ItemStack>> itemStacks = Arrays.asList(mc.player.inventory.mainInventory);
      Iterator i = itemStacks.iterator();

      ItemStack itemStack;
      do {
         if (!i.hasNext()) {
            return 0;
         }

         itemStack = (ItemStack)i.next();
      } while(itemStack == null || !(itemStack.getItem() instanceof ItemiCraft) || itemStack.getTagCompound() == null);

      return isCalling ? itemStack.getTagCompound().getInteger("calledNumber") : itemStack.getTagCompound().getInteger("callingNumber");
   }

   public static ResourceLocation getResource(ICraftClientUtils.ResourceType type, String name) {
      return new ResourceLocation("icraft", type.getPrefix() + name);
   }

   public static String getTime() {
      long time = (mc.world.getWorldTime() + 6000L) % (hour24 ? 24000L : 12000L);
      long hours = time / 1000L;
      long seconds = (long)((double)(time % 1000L) * 0.06D);
      return String.format("%02d", hours) + ":" + String.format("%02d", seconds);
   }

   public static String getAuthor(File file) {
      try {
         AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(file);
         if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = fileFormat.properties();
            String key = "author";
            return (String)properties.get(key);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return null;
   }

   public static enum ResourceType {
      GUI("gui"),
      SOUND("sounds"),
      TEXTURE_BLOCKS("textures/blocks"),
      TEXTURE_ITEMS("textures/items"),
      RENDER("render");

      private String prefix;

      private ResourceType(String s) {
         this.prefix = s;
      }

      public String getPrefix() {
         return this.prefix + "/";
      }
   }
}
