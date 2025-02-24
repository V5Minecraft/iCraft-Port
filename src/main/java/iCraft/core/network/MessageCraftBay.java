package iCraft.core.network;

import iCraft.core.entity.EntityPackingCase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageCraftBay extends MessageBase<MessageCraftBay> {
   private Item buyItem;
   private int buyQnt;
   private int buyMeta;
   private Item sellItem;
   private int sellQnt;
   private int sellMeta;
   private ItemStack buyStack;
   private ItemStack sellStack;

   public MessageCraftBay() {
   }

   public MessageCraftBay(Item buyItem, int buyQnt, int buyMeta, Item sellItem, int sellQnt, int sellMeta) {
      this.buyItem = buyItem;
      this.buyQnt = buyQnt;
      this.buyMeta = buyMeta;
      this.sellItem = sellItem;
      this.sellQnt = sellQnt;
      this.sellMeta = sellMeta;
   }

   public void fromBytes(ByteBuf buf) {
      this.buyStack = ByteBufUtils.readItemStack(buf);
      this.sellStack = ByteBufUtils.readItemStack(buf);
   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeItemStack(buf, new ItemStack(this.buyItem, this.buyQnt, this.buyMeta));
      ByteBufUtils.writeItemStack(buf, new ItemStack(this.sellItem, this.sellQnt, this.sellMeta));
   }

   public void handleClientSide(MessageCraftBay message, EntityPlayer player) {
   }

   public void handleServerSide(MessageCraftBay message, EntityPlayer player) {
      World world = player.getEntityWorld();
      ItemStack[] stack = new ItemStack[]{message.buyStack};
      Entity packingCase = new EntityPackingCase(world, stack, message.sellStack, player.getGameProfile().getId(), player.getGameProfile().getName());
      packingCase.setPosition(player.posX, player.posY + 75.0D <= 256.0D ? player.posY + 75.0D : 75.0D, player.posZ);
      world.spawnEntity(packingCase);
   }
}
