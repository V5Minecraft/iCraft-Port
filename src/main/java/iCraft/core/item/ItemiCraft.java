package iCraft.core.item;

import iCraft.core.ICraft;
import iCraft.core.utils.ICraftUtils;
import java.util.List;
import java.util.Random;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemiCraft extends ItemBase {
   public Random rand = new Random();

   public ItemiCraft() {
      this.setMaxStackSize(1);
   }

   public void addInformation(ItemStack itemStack, @Nullable World world, List<String> list, ITooltipFlag flag) {
      super.addInformation(itemStack, world, list, flag);
      list.add(ICraftUtils.localize("tooltip.number") + ": " + this.getNumber(itemStack));
   }

   public void onUpdate(ItemStack itemStack, World world, Entity entity, int i, boolean flag) {
      if (itemStack != null && !world.isRemote && this.getNumber(itemStack) == 0) {
         this.setNumber(itemStack);
      }

   }

   public void setNumber(ItemStack itemStack) {
      if (itemStack.getTagCompound() == null) {
         itemStack.setTagCompound(new NBTTagCompound());
      }

      int number = 10000000 + this.rand.nextInt(90000000);
      itemStack.getTagCompound().setInteger("number", number);
   }

   public int getNumber(ItemStack itemStack) {
      return itemStack.getTagCompound() == null ? 0 : itemStack.getTagCompound().getInteger("number");
   }

   @Override
   public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer entityPlayer, EnumHand hand) {
      ItemStack itemStack = entityPlayer.getHeldItem(hand);

      if (itemStack.getTagCompound() != null) {
         if (itemStack.getTagCompound().hasKey("called") && itemStack.getTagCompound().getInteger("called") != 0) {
            int called = itemStack.getTagCompound().getInteger("called");

            if (called == 1) {
               entityPlayer.openGui(ICraft.instance, 6, world, 0, 0, 0);
            } else if (called == 2) {
               entityPlayer.openGui(ICraft.instance, 7, world, 0, 0, 0);
            }
         } else {
            entityPlayer.openGui(ICraft.instance, 0, world, 0, 0, 0);
         }
      }

      return new ActionResult<>(EnumActionResult.SUCCESS, itemStack);
   }

   public EnumRarity getRarity(ItemStack itemStack) {
      return EnumRarity.EPIC;
   }
}
