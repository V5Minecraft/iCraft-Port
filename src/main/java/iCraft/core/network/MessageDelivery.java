package iCraft.core.network;

import iCraft.core.entity.EntityPizzaDelivery;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class MessageDelivery extends MessageBase<MessageDelivery> {
   private int qnt;

   public MessageDelivery() {
   }

   public MessageDelivery(int qnt) {
      this.qnt = qnt;
   }

   public void fromBytes(ByteBuf buf) {
      this.qnt = buf.readInt();
   }

   public void toBytes(ByteBuf buf) {
      buf.writeInt(this.qnt);
   }

   public void handleClientSide(MessageDelivery message, EntityPlayer player) {
   }

   public void handleServerSide(MessageDelivery message, EntityPlayer player) {
      World world = player.world;
      EntityPizzaDelivery pizza = new EntityPizzaDelivery(world, player.posX + 40.0D, player.posY, player.posZ + 40.0D);
      pizza.setPlayer(player.getName());
      pizza.setQuantity(message.qnt);
      world.spawnEntity(pizza);
   }
}
