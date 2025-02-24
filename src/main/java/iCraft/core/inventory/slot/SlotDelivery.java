package iCraft.core.inventory.slot;

import iCraft.core.entity.EntityPizzaDelivery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotDelivery extends Slot {
   private EntityPizzaDelivery delivery;

   public SlotDelivery(IInventory inventory, int index, int x, int y) {
      super(inventory, index, x, y);
      this.delivery = (EntityPizzaDelivery) inventory;
   }

   public boolean isItemValid(ItemStack itemStack) {
      return false;
   }

   @Override
   public ItemStack onTake(EntityPlayer player, ItemStack itemStack) {
      ItemStack toSell = this.delivery.getStackInSlot(0);
      if (this.canBuy(toSell)) {
         this.delivery.setTrade(true);
         this.delivery.setAngry(false);
         if (toSell != null && toSell.getCount() <= 0) {
            toSell = null;
         }

         this.delivery.setInventorySlotContents(0, toSell);
      }

      return toSell;
   }

   private boolean canBuy(ItemStack itemStack) {
      if (itemStack != null && itemStack.getItem() == Items.IRON_INGOT) {
         itemStack.setCount(itemStack.getCount() - this.delivery.getQuantity() * 2);
         return true;
      } else {
         return false;
      }
   }
}
