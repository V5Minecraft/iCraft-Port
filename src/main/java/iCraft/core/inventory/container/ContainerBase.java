package iCraft.core.inventory.container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;

public abstract class ContainerBase extends Container {
   protected void addPlayerSlots(InventoryPlayer inventory, int x, int y) {
      int i;
      for (i = 0; i < 3; ++i) {
         for (int j = 0; j < 9; ++j) {
            this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, x + j * 18, y + i * 18));
         }
      }

      for (i = 0; i < 9; ++i) {
         this.addSlotToContainer(new Slot(inventory, i, x + i * 18, y + 58));
      }
   }
}
