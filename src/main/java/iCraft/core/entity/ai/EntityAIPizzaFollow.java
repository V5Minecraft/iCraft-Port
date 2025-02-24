package iCraft.core.entity.ai;

import iCraft.core.entity.EntityPizzaDelivery;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIPizzaFollow extends EntityAIBase {
   private EntityPizzaDelivery theEntity;
   private EntityPlayer player;
   private World theWorld;
   private float moveSpeed;
   private PathNavigate thePathfinder;
   private int ticker;
   private float minDist;
   private boolean avoidWater;

   public EntityAIPizzaFollow(EntityPizzaDelivery theEntity, float speed, float min) {
      this.theEntity = theEntity;
      this.theWorld = theEntity.world;
      this.moveSpeed = speed;
      this.thePathfinder = theEntity.getNavigator();
      this.minDist = min;
      this.setMutexBits(3);
   }

   public boolean shouldExecute() {
      EntityPlayer follow = this.theWorld.getPlayerEntityByName(this.theEntity.getPlayer());
      if (follow == null) {
         return false;
      } else if (this.theEntity.world.provider.getDimension() != follow.world.provider.getDimension()) {
         return false;
      } else if (this.theEntity.getDistanceSq(follow) < (double) (this.minDist * this.minDist)) {
         return false;
      } else if (this.theEntity.getAngry()) {
         return false;
      } else {
         this.player = follow;
         return true;
      }
   }

   public boolean shouldContinueExecuting() {
      return !this.thePathfinder.noPath() && !this.theEntity.getAngry() && this.player.world.provider.getDimension() == this.theEntity.world.provider.getDimension();
   }

   public void startExecuting() {
      this.ticker = 0;
      this.avoidWater = ((PathNavigateGround) this.theEntity.getNavigator()).getCanSwim();
      ((PathNavigateGround) this.theEntity.getNavigator()).setCanSwim(false);
   }

   public void resetTask() {
      this.player = null;
      this.thePathfinder.clearPath();
      ((PathNavigateGround) this.theEntity.getNavigator()).setCanSwim(this.avoidWater);
   }

   public void updateTask() {
      this.theEntity.getLookHelper().setLookPositionWithEntity(this.player, 6.0F, (float) this.theEntity.getVerticalFaceSpeed() / 10);

      if (--this.ticker <= 0) {
         this.ticker = 10;

         if (!this.thePathfinder.tryMoveToEntityLiving(this.player, this.moveSpeed) && this.theEntity.getDistanceSq(this.player) >= 144.0D) {
            int x = MathHelper.floor(this.player.posX) - 2;
            int y = MathHelper.floor(this.player.posZ) - 2;
            int z = MathHelper.floor(this.player.getEntityBoundingBox().minY);

            for (int l = 0; l <= 4; ++l) {
               for (int i1 = 0; i1 <= 4; ++i1) {
                  BlockPos blockPos = new BlockPos(x + l, z, y + i1);
                  BlockPos belowBlockPos = blockPos.down();
                  BlockPos aboveBlockPos = blockPos.up();

                  IBlockState belowBlockState = this.theWorld.getBlockState(belowBlockPos);
                  IBlockState blockState = this.theWorld.getBlockState(blockPos);
                  IBlockState aboveBlockState = this.theWorld.getBlockState(aboveBlockPos);

                  if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) &&
                          belowBlockState.getBlock().isTopSolid(belowBlockState) &&
                          !blockState.getBlock().isNormalCube(blockState) &&
                          !aboveBlockState.getBlock().isNormalCube(aboveBlockState)) {

                     this.thePathfinder.clearPath();
                     this.theEntity.setPosition(this.player.posX, this.player.posY, this.player.posZ);
                     return;
                  }
               }
            }
         }
      }
   }
}
