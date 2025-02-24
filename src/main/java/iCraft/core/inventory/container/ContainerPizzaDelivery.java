package iCraft.core.inventory.container;

import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.inventory.slot.SlotDelivery;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerPizzaDelivery extends ContainerBase {
   private final EntityPizzaDelivery delivery;

   public ContainerPizzaDelivery(InventoryPlayer inventory, EntityPizzaDelivery delivery) {
      this.delivery = delivery;
      this.delivery.openInventory(inventory.player);
      this.addSlotToContainer(new Slot(this.delivery, 0, 62, 53));
      this.addSlotToContainer(new SlotDelivery(this.delivery, 1, 120, 53));
      this.addPlayerSlots(inventory, 8, 84);
   }

   public boolean canInteractWith(EntityPlayer player) {
      return true;
   }

   public void onCraftMatrixChanged(IInventory inventory) {
      this.delivery.resetSlotContents();
      super.onCraftMatrixChanged(inventory);
   }

   @Override
   public ItemStack transferStackInSlot(EntityPlayer player, int slotID) {
      ItemStack stack = ItemStack.EMPTY;
      Slot currentSlot = this.inventorySlots.get(slotID);

      if (currentSlot != null && currentSlot.getHasStack()) {
         ItemStack slotStack = currentSlot.getStack();
         stack = slotStack.copy();

         if (slotID == 1) {
            if (!this.mergeItemStack(slotStack, 2, 38, true)) {
               return ItemStack.EMPTY;
            }

            currentSlot.onSlotChange(slotStack, stack);
         } else if (slotID != 0) {
            if (slotID >= 2 && slotID < 29) {
               if (!this.mergeItemStack(slotStack, 29, 38, false)) {
                  return ItemStack.EMPTY;
               }
            } else if (slotID >= 29 && slotID < 38 && !this.mergeItemStack(slotStack, 2, 29, false)) {
               return ItemStack.EMPTY;
            }
         } else if (!this.mergeItemStack(slotStack, 2, 38, false)) {
            return ItemStack.EMPTY;
         }

         if (slotStack.isEmpty()) {
            currentSlot.putStack(ItemStack.EMPTY);
         } else {
            currentSlot.onSlotChanged();
         }

         if (slotStack.getCount() == stack.getCount()) {
            return ItemStack.EMPTY;
         }

         currentSlot.onTake(player, slotStack);
      }

      return stack;
   }

   public void onContainerClosed(EntityPlayer player) {
      super.onContainerClosed(player);
      this.delivery.closeInventory(player);
   }
}
