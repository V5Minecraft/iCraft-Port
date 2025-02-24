package iCraft.core.entity.ai;

import iCraft.core.entity.EntityPizzaDelivery;
import iCraft.core.utils.ICraftUtils;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class EntityAIDelivery extends EntityAIBase {
   private EntityPizzaDelivery theEntity;
   private float minDist;
   private World theWorld;
   private EntityPlayer player;

   public EntityAIDelivery(EntityPizzaDelivery theEntity, float min) {
      this.theEntity = theEntity;
      this.minDist = min;
      this.theWorld = theEntity.world;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      EntityPlayer player = this.theWorld.getPlayerEntityByName(this.theEntity.getPlayer());
      if (player == null) {
         return false;
      } else if (this.theEntity.world.provider.getDimension() != player.world.provider.getDimension()) {
         return false;
      } else if (this.theEntity.getDistanceSq(player) > (double)(this.minDist * this.minDist)) {
         return false;
      } else if (this.theEntity.getAngry()) {
         return false;
      } else {
         this.player = player;
         return true;
      }
   }

   public boolean shouldContinueExecuting() {
      return !this.theEntity.getAngry() && this.theEntity.getDistanceSq(this.player) <= (double)(this.minDist * this.minDist) && this.player.world.provider.getDimension() == this.theEntity.world.provider.getDimension();
   }

   public void resetTask() {
      this.player = null;
   }

   public void updateTask() {
      if (!this.theEntity.getTrade()) {
         if (this.theEntity.getPatience() > 0) {
            this.theEntity.setPatience(this.theEntity.getPatience() - 1);
         } else {
            if (this.theEntity.getDistanceSq(this.player) <= 100.0D) {
               this.player.closeScreen();
               this.player.sendMessage(new TextComponentString(TextFormatting.AQUA + ICraftUtils.localize("msg.delivery") + ": " + TextFormatting.RED + ICraftUtils.localize("msg.delivery.rage")));
            }

            this.theEntity.setAngry(true);
         }
      } else {
         this.player.sendMessage(new TextComponentString(TextFormatting.AQUA + ICraftUtils.localize("msg.delivery") + ": " + TextFormatting.GREEN + ICraftUtils.localize("msg.delivery.allOk")));
         this.theEntity.setDead();
      }

   }
}
