package iCraft.core.entity;

import com.mojang.authlib.GameProfile;
import iCraft.core.register.RegisterBlocks;

import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityPackingCase extends Entity {
   public ItemStack[] storedItems;
   private ItemStack itemStack;
   private boolean hasLanded;
   private GameProfile profile;

   public EntityPackingCase(World world, ItemStack[] storedItems, ItemStack itemStack, UUID id, String name) {
      this(world);
      this.storedItems = storedItems;
      this.itemStack = itemStack;
      this.hasLanded = false;
      this.profile = new GameProfile(id, name);
   }

   public EntityPackingCase(World world) {
      super(world);
   }

   protected void entityInit() {
   }

   protected void readEntityFromNBT(NBTTagCompound nbtTags) {
      NBTTagList tagList = nbtTags.getTagList("Items", 10);
      this.storedItems = new ItemStack[1];

      for (int tagCount = 0; tagCount < tagList.tagCount(); ++tagCount) {
         NBTTagCompound tagCompound = tagList.getCompoundTagAt(tagCount);
         int slotID = tagCompound.getByte("Slot") & 255;
         if (slotID < this.storedItems.length) {
            this.storedItems[slotID] = new ItemStack(tagCompound);
         }
      }

      this.hasLanded = nbtTags.getBoolean("hasLanded");
   }

   protected void writeEntityToNBT(NBTTagCompound nbtTags) {
      NBTTagList tagList = new NBTTagList();

      for (int slotCount = 0; slotCount < this.storedItems.length; ++slotCount) {
         if (this.storedItems[slotCount] != null) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setByte("Slot", (byte) slotCount);
            this.storedItems[slotCount].writeToNBT(tagCompound);
            tagList.appendTag(tagCompound);
         }
      }

      nbtTags.setTag("Items", tagList);
      nbtTags.setBoolean("hasLanded", this.hasLanded);
   }

   public void onUpdate() {
      if (!this.hasLanded) {
         if (super.onGround && !super.world.isRemote) {
            int len;
            int i;
            for (i = 0; i < 100; ++i) {
               len = MathHelper.floor(super.posX);
               i = MathHelper.floor(super.posY);
               int z = MathHelper.floor(super.posZ);
               IBlockState block = super.world.getBlockState(new BlockPos(len, i, z));
               if (block.getMaterial().isReplaceable()) {
                  if (this.placeCase(new BlockPos(len, i, z))) {
                     this.setDead();
                     return;
                  }

                  if (this.storedItems != null) {
                     ItemStack[] arr = this.storedItems;
                     len = arr.length;

                     for (i = 0; i < len; ++i) {
                        ItemStack stack = arr[i];
                        EntityItem e = new EntityItem(super.world, super.posX, super.posY, super.posZ, stack);
                        super.world.spawnEntity(e);
                     }

                     return;
                  }
               }
            }

            if (this.storedItems != null) {
               ItemStack[] arr = this.storedItems;
               len = arr.length;

               for (i = 0; i < len; ++i) {
                  ItemStack stack = arr[i];
                  EntityItem e = new EntityItem(super.world, super.posX, super.posY, super.posZ, stack);
                  super.world.spawnEntity(e);
               }
            }
         } else {
            super.motionY = -0.25D;
         }

         this.move(MoverType.SELF, 0.0D, this.motionY, 0.0D);
      }

   }

   private boolean placeCase(BlockPos pos) {
      if (RegisterBlocks.caseBlock == null) {
         return false;
      }

      super.world.setBlockState(pos, RegisterBlocks.caseBlock.getDefaultState(), 3);
      super.world.removeTileEntity(pos);

      if (this.profile == null || this.itemStack == null) {
         return false;
      }

      this.setDead();
      return true;
   }
}
