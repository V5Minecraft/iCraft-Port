package iCraft.core.utils;

import iCraft.core.ICraft;
import iCraft.core.item.ItemiCraft;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ICraftUtils {
   public static final HashMap<Integer, HashMap<ItemStack, ItemStack>> items = new HashMap();

   public static String localize(String s) {
      return I18n.translateToLocal(s);
   }

   public static void changeCalledStatus(ItemStack itemStack, int status, int status2, boolean isCalling) {
      if (itemStack != null && itemStack.getTagCompound() != null) {
         itemStack.getTagCompound().setInteger("called", status);
         if (status == 2) {
            itemStack.getTagCompound().setInteger("callCode", itemStack.getTagCompound().getInteger("number"));
         } else if (status == 0) {
            itemStack.getTagCompound().setInteger("callCode", 0);
            if (itemStack.getTagCompound().hasKey("isCalling")) {
               itemStack.getTagCompound().setBoolean("isCalling", false);
            }
         }

         Iterator i = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers().iterator();

         while (true) {
            EntityPlayerMP players;
            do {
               if (!i.hasNext()) {
                  return;
               }

               players = (EntityPlayerMP) i.next();
            } while (!players.getName().equals(isCalling ? itemStack.getTagCompound().getString("calledPlayer") : itemStack.getTagCompound().getString("callingPlayer")));

            List<NonNullList<ItemStack>> stacks = Arrays.asList(players.inventory.mainInventory);
            i = stacks.iterator();

            while (i.hasNext()) {
               ItemStack stack = (ItemStack) i.next();
               if (stack != null && stack.getItem() instanceof ItemiCraft) {
                  ItemiCraft iCraft = (ItemiCraft) stack.getItem();
                  if (stack.getTagCompound() != null && iCraft.getNumber(stack) == (isCalling ? itemStack.getTagCompound().getInteger("calledNumber") : itemStack.getTagCompound().getInteger("callingNumber"))) {
                     stack.getTagCompound().setInteger("called", status2);
                     if (status2 == 2) {
                        stack.getTagCompound().setInteger("callCode", itemStack.getTagCompound().getInteger("number"));
                     } else if (status2 == 0) {
                        stack.getTagCompound().setInteger("callCode", 0);
                        if (stack.getTagCompound().hasKey("isCalling")) {
                           stack.getTagCompound().setBoolean("isCalling", false);
                        }

                        return;
                     }

                     return;
                  }
               }
            }
         }
      }
   }

   public static void addBuyableItems(int index, ItemStack buyStack, ItemStack sellStack) {
      boolean repeated = false;
      for (Map.Entry<Integer, HashMap<ItemStack, ItemStack>> entry : items.entrySet()) {
         if (entry.getValue().containsKey(buyStack)) {
            repeated = true;
            break;
         }
      }

      if (buyStack != null && !repeated) {
         HashMap<ItemStack, ItemStack> map = new HashMap<>();
         map.put(buyStack, sellStack);
         items.put(index, map);
      }
   }

   public static void getNewestVersion() {
      try {
         URL url = new URL("https://dl.dropbox.com/s/8sczowv9foi0d8h/iCraft.txt");
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setRequestMethod("GET");
         BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
         ICraft.newestVersion = rd.readLine();
         rd.close();
      } catch (Exception var4) {
         var4.printStackTrace();
      }
   }

   public static List<IConfigElement> getConfigElements() {
      List<IConfigElement> list = new ArrayList();
      list.addAll((new ConfigElement(ICraft.configuration.getCategory("general"))).getChildElements());
      list.addAll((new ConfigElement(ICraft.configuration.getCategory("voice settings"))).getChildElements());
      return list;
   }
}
