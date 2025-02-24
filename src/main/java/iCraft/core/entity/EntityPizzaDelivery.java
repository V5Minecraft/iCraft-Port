package iCraft.core.entity;

import iCraft.core.ICraft;
import iCraft.core.entity.ai.EntityAIAttackCrazy;
import iCraft.core.entity.ai.EntityAIDelivery;
import iCraft.core.entity.ai.EntityAIPizzaFollow;
import iCraft.core.register.RegisterItems;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class EntityPizzaDelivery extends EntityCreature implements IInventory {
   public ItemStack[] inventory;
   public static final DataParameter<String> CUSTOM_NAME = EntityDataManager.createKey(EntityPizzaDelivery.class, DataSerializers.STRING);
   public static final DataParameter<Integer> SOME_INTEGER = EntityDataManager.createKey(EntityPizzaDelivery.class, DataSerializers.VARINT);
   public static final DataParameter<Integer> ANOTHER_INTEGER = EntityDataManager.createKey(EntityPizzaDelivery.class, DataSerializers.VARINT);
   public static final DataParameter<Byte> SOME_BYTE = EntityDataManager.createKey(EntityPizzaDelivery.class, DataSerializers.BYTE);
   public static final DataParameter<Byte> ANOTHER_BYTE = EntityDataManager.createKey(EntityPizzaDelivery.class, DataSerializers.BYTE);

   public EntityPizzaDelivery(World world) {
      super(world);
      this.inventory = new ItemStack[2];
      this.setSize(0.6F, 1.8F);
      ((PathNavigateGround) this.getNavigator()).setBreakDoors(true);
      ((PathNavigateGround) this.getNavigator()).setCanSwim(false);
      super.tasks.addTask(0, new EntityAISwimming(this));
      super.tasks.addTask(1, new EntityAIMoveIndoors(this));
      super.tasks.addTask(2, new EntityAIRestrictOpenDoor(this));
      super.tasks.addTask(3, new EntityAIOpenDoor(this, true));
      super.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 0.6D));
      super.tasks.addTask(5, new EntityAIPizzaFollow(this, 1.0F, 4.0F));
      super.tasks.addTask(5, new EntityAIDelivery(this, 4.0F));
      super.tasks.addTask(5, new EntityAIAttackCrazy(this, 1.0D));
      this.setAlwaysRenderNameTag(true);
      this.setNoAI(false);
   }

   public EntityPizzaDelivery(World world, double x, double y, double z) {
      this(world);
      this.setPosition(x, y, z);
      super.prevPosX = x;
      super.prevPosY = y;
      super.prevPosZ = z;
   }

   protected void entityInit() {
      super.entityInit();
      this.dataManager.register(CUSTOM_NAME, "");
      this.dataManager.register(SOME_INTEGER, 1);
      this.dataManager.register(ANOTHER_INTEGER, 300);
      this.dataManager.register(SOME_BYTE, (byte) 0);
      this.dataManager.register(ANOTHER_BYTE, (byte) 0);
   }

   protected void applyEntityAttributes() {
      super.applyEntityAttributes();
      this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(10.0D);
      this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
   }

   protected boolean canDespawn() {
      return false;
   }

   protected String getLivingSound() {
      return "mob.villager.idle";
   }

   @Override
   protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
      return SoundEvents.ENTITY_VILLAGER_HURT;
   }

   @Override
   protected SoundEvent getDeathSound() {
      return SoundEvents.ENTITY_VILLAGER_DEATH;
   }

   protected void addRandomDrop() {
      this.dropItem(RegisterItems.pizza, 1);
   }

   @Override
   public boolean processInteract(EntityPlayer player, EnumHand hand) {
      if (!player.isSneaking() && !this.getAngry()) {
         player.openGui(ICraft.instance, 11, super.world, this.getEntityId(), 0, 0);
      }

      return true;
   }

   public void readEntityFromNBT(NBTTagCompound nbtTags) {
      super.readEntityFromNBT(nbtTags);
      this.setPlayer(nbtTags.getString("playerId"));
      this.setQuantity(nbtTags.getInteger("quantity"));
      this.setPatience(nbtTags.getInteger("patience"));
      this.setTrade(nbtTags.getBoolean("trade"));
      this.setAngry(nbtTags.getBoolean("angry"));
      NBTTagList tagList = nbtTags.getTagList("Items", 10);
      this.inventory = new ItemStack[2];

      for (int tagCount = 0; tagCount < tagList.tagCount(); ++tagCount) {
         NBTTagCompound tagCompound = tagList.getCompoundTagAt(tagCount);
         int slotID = tagCompound.getByte("Slot") & 255;
         if (slotID < this.inventory.length) {
            this.inventory[slotID] = new ItemStack(tagCompound);
         }
      }

   }

   public void writeEntityToNBT(NBTTagCompound nbtTags) {
      super.writeEntityToNBT(nbtTags);
      nbtTags.setString("playerId", this.getPlayer());
      nbtTags.setInteger("quantity", this.getQuantity());
      nbtTags.setInteger("patience", this.getPatience());
      nbtTags.setBoolean("trade", this.getTrade());
      nbtTags.setBoolean("angry", this.getAngry());
      NBTTagList tagList = new NBTTagList();

      for (int slotCount = 0; slotCount < this.inventory.length; ++slotCount) {
         if (this.inventory[slotCount] != null) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setByte("Slot", (byte) slotCount);
            this.inventory[slotCount].writeToNBT(tagCompound);
            tagList.appendTag(tagCompound);
         }
      }

      nbtTags.setTag("Items", tagList);
   }

   public String getPlayer() {
      return this.dataManager.get(CUSTOM_NAME);
   }

   public void setPlayer(String player) {
      this.dataManager.set(CUSTOM_NAME, player);
   }

   public int getQuantity() {
      return this.dataManager.get(SOME_INTEGER);
   }

   public void setQuantity(int newQuantity) {
      this.dataManager.set(SOME_INTEGER, newQuantity);
   }

   public int getPatience() {
      return this.dataManager.get(ANOTHER_INTEGER);
   }

   public void setPatience(int i) {
      this.dataManager.set(ANOTHER_INTEGER, i);
   }

   public boolean getTrade() {
      return this.dataManager.get(SOME_BYTE) == 1;
   }

   public void setTrade(boolean status) {
      this.dataManager.set(SOME_BYTE, (byte) (status ? 1 : 0));
   }

   public boolean getAngry() {
      return this.dataManager.get(ANOTHER_BYTE) == 1;
   }

   public void setAngry(boolean angry) {
      this.dataManager.set(ANOTHER_BYTE, (byte) (angry ? 1 : 0));
   }

   public int getSizeInventory() {
      return this.inventory.length;
   }

   @Override
   public boolean isEmpty() {
      return false;
   }

   public ItemStack getStackInSlot(int slotID) {
      return slotID >= 0 && slotID < this.inventory.length ? this.inventory[slotID] : ItemStack.EMPTY;
   }

   public ItemStack decrStackSize(int slotID, int amount) {
      if (!this.inventory[slotID].isEmpty()) {
         ItemStack itemstack;
         if (slotID == 1) {
            itemstack = this.inventory[slotID];
            this.inventory[slotID] = ItemStack.EMPTY;
            return itemstack;
         } else if (this.inventory[slotID].getCount() <= amount) {
            itemstack = this.inventory[slotID];
            this.inventory[slotID] = ItemStack.EMPTY;
            if (this.inventoryResetNeededOnSlotChange(slotID)) {
               this.resetSlotContents();
            }

            return itemstack;
         } else {
            itemstack = this.inventory[slotID].splitStack(amount);
            if (this.inventory[slotID].getCount() == 0) {
               this.inventory[slotID] = null;
            }

            if (this.inventoryResetNeededOnSlotChange(slotID)) {
               this.resetSlotContents();
            }

            return itemstack;
         }
      } else {
         return null;
      }
   }

   private boolean inventoryResetNeededOnSlotChange(int i) {
      return i == 0;
   }

   public ItemStack removeStackFromSlot(int slotID) {
      if (!this.inventory[slotID].isEmpty()) {
         ItemStack itemstack = this.inventory[slotID];
         this.inventory[slotID] = ItemStack.EMPTY;
         return itemstack;
      } else {
         return null;
      }
   }

   public void setInventorySlotContents(int slotID, ItemStack itemStack) {
      this.inventory[slotID] = itemStack;
      if (!this.inventory[slotID].isEmpty() && this.inventory[slotID].getCount() > this.getInventoryStackLimit()) {
         itemStack.setCount(this.getInventoryStackLimit());
      }

      if (this.inventoryResetNeededOnSlotChange(slotID)) {
         this.resetSlotContents();
      }
   }

   public String getName() {
      return "Pizza Deliverer";
   }

   public boolean hasCustomName() {
      return true;
   }

   public int getInventoryStackLimit() {
      return this.getQuantity() * 2;
   }

   public void markDirty() {
      this.resetSlotContents();
   }

   public void resetSlotContents() {
      ItemStack toSell = this.inventory[0];
      if (toSell == null) {
         this.setInventorySlotContents(1, (ItemStack) null);
      } else if (toSell.getItem() == Items.IRON_INGOT && toSell.getCount() >= this.getQuantity() * 2 && !this.getTrade()) {
         this.setInventorySlotContents(1, new ItemStack(RegisterItems.pizza, this.getQuantity()));
      }

   }

   @Override
   public boolean isUsableByPlayer(EntityPlayer player) {
      return true;
   }

   public void openInventory(EntityPlayer player) {
   }

   public void closeInventory(EntityPlayer player) {
   }

   public boolean isItemValidForSlot(int slotID, ItemStack itemStack) {
      return true;
   }

   public int getField(int id) {
      return 0;
   }

   public void setField(int id, int value) {
   }

   public int getFieldCount() {
      return 0;
   }

   public void clear() {
   }
}
