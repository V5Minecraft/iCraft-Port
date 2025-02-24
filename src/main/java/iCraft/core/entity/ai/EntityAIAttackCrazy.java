package iCraft.core.entity.ai;

import iCraft.core.entity.EntityPizzaDelivery;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityAIAttackCrazy extends EntityAIBase {
   private final EntityPizzaDelivery attacker;
   private int attackTick;
   private final double speedTowardsTarget;
   private final World theWorld;
   private Path entityPathEntity;
   private int ticker;
   private double field_151497_i;
   private double field_151495_j;
   private double field_151496_k;
   private int failedPathFindingPenalty;

   public EntityAIAttackCrazy(EntityPizzaDelivery entity, double speed) {
      this.attacker = entity;
      this.speedTowardsTarget = speed;
      this.theWorld = entity.world;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      EntityLivingBase entity = this.theWorld.getPlayerEntityByName(this.attacker.getPlayer());
      if (entity == null) {
         return false;
      } else if (!entity.isEntityAlive()) {
         return false;
      } else if (this.attacker.getAngry()) {
         if (--this.ticker <= 0) {
            this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving(entity);
            this.ticker = 4 + this.attacker.getRNG().nextInt(7);
            return this.entityPathEntity != null;
         } else {
            return true;
         }
      } else {
         return false;
      }
   }

   public boolean continueExecuting() {
      EntityLivingBase entity = this.theWorld.getPlayerEntityByName(this.attacker.getPlayer());
      return entity != null && entity.isEntityAlive() && !this.attacker.getNavigator().noPath() && this.attacker.getAngry();
   }

   public void startExecuting() {
      this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
      this.ticker = 0;
   }

   public void resetTask() {
      this.attacker.getNavigator().clearPath();
   }

   public void updateTask() {
      EntityLivingBase target = this.theWorld.getPlayerEntityByName(this.attacker.getPlayer());
      this.attacker.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
      double d0 = this.attacker.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
      double d1 = this.attacker.width * 2.0F * this.attacker.width * 2.0F + target.width;
      --this.ticker;
      if (this.attacker.getEntitySenses().canSee(target) && this.ticker <= 0 && (this.field_151497_i == 0.0D && this.field_151495_j == 0.0D && this.field_151496_k == 0.0D || target.getDistanceSq(this.field_151497_i, this.field_151495_j, this.field_151496_k) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
         this.field_151497_i = target.posX;
         this.field_151495_j = target.getEntityBoundingBox().minY;
         this.field_151496_k = target.posZ;
         this.ticker = this.failedPathFindingPenalty + 4 + this.attacker.getRNG().nextInt(7);
         if (this.attacker.getNavigator().getPath() != null) {
            PathPoint finalPathPoint = this.attacker.getNavigator().getPath().getFinalPathPoint();
            if (finalPathPoint != null && target.getDistanceSq(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1.0D) {
               this.failedPathFindingPenalty = 0;
            } else {
               this.failedPathFindingPenalty += 10;
            }
         } else {
            this.failedPathFindingPenalty += 10;
         }

         if (d0 > 1024.0D) {
            this.ticker += 10;
         } else if (d0 > 256.0D) {
            this.ticker += 5;
         }

         if (!this.attacker.getNavigator().tryMoveToEntityLiving(target, this.speedTowardsTarget)) {
            this.ticker += 15;
         }
      }

      this.attackTick = Math.max(this.attackTick - 1, 0);
      if (d0 <= d1 && this.attackTick <= 20) {
         this.attackTick = 20;
         if (!this.attacker.getHeldItemMainhand().isEmpty()) {
            this.attacker.swingArm(EnumHand.MAIN_HAND);
         }

         target.attackEntityFrom(DamageSource.CACTUS, 1.0F);
      }
   }
}
