package iCraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelPizzaDelivery extends ModelBase {
   public ModelRenderer deliveryHead = (new ModelRenderer(this)).setTextureSize(64, 64);
   public ModelRenderer deliveryBody;
   public ModelRenderer deliveryArms;
   public ModelRenderer rightDeliveryLeg;
   public ModelRenderer leftDeliveryLeg;
   public ModelRenderer deliveryNose;

   public ModelPizzaDelivery() {
      this.deliveryHead.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.deliveryHead.setTextureOffset(0, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, 0.0F);
      this.deliveryNose = (new ModelRenderer(this)).setTextureSize(64, 64);
      this.deliveryNose.setRotationPoint(0.0F, -2.0F, 0.0F);
      this.deliveryNose.setTextureOffset(24, 0).addBox(-1.0F, -1.0F, -6.0F, 2, 4, 2, 0.0F);
      this.deliveryHead.addChild(this.deliveryNose);
      this.deliveryBody = (new ModelRenderer(this)).setTextureSize(64, 64);
      this.deliveryBody.setRotationPoint(0.0F, 0.0F, 0.0F);
      this.deliveryBody.setTextureOffset(16, 20).addBox(-4.0F, 0.0F, -3.0F, 8, 12, 6, 0.0F);
      this.deliveryBody.setTextureOffset(0, 38).addBox(-4.0F, 0.0F, -3.0F, 8, 18, 6, 0.5F);
      this.deliveryArms = (new ModelRenderer(this)).setTextureSize(64, 64);
      this.deliveryArms.setRotationPoint(0.0F, 2.0F, 0.0F);
      this.deliveryArms.setTextureOffset(44, 22).addBox(-8.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
      this.deliveryArms.setTextureOffset(44, 22).addBox(4.0F, -2.0F, -2.0F, 4, 8, 4, 0.0F);
      this.deliveryArms.setTextureOffset(40, 38).addBox(-4.0F, 2.0F, -2.0F, 8, 4, 4, 0.0F);
      this.rightDeliveryLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(64, 64);
      this.rightDeliveryLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
      this.rightDeliveryLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
      this.leftDeliveryLeg = (new ModelRenderer(this, 0, 22)).setTextureSize(64, 64);
      this.leftDeliveryLeg.mirror = true;
      this.leftDeliveryLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
      this.leftDeliveryLeg.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4, 0.0F);
   }

   public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
      this.deliveryHead.render(f5);
      this.deliveryBody.render(f5);
      this.rightDeliveryLeg.render(f5);
      this.leftDeliveryLeg.render(f5);
      this.deliveryArms.render(f5);
   }

   public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
      this.deliveryHead.rotateAngleY = p_78087_4_ / 57.295776F;
      this.deliveryHead.rotateAngleX = p_78087_5_ / 57.295776F;
      this.deliveryArms.rotationPointY = 3.0F;
      this.deliveryArms.rotationPointZ = -1.0F;
      this.deliveryArms.rotateAngleX = -0.75F;
      this.rightDeliveryLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F) * 1.4F * p_78087_2_ * 0.5F;
      this.leftDeliveryLeg.rotateAngleX = MathHelper.cos(p_78087_1_ * 0.6662F + 3.1415927F) * 1.4F * p_78087_2_ * 0.5F;
      this.rightDeliveryLeg.rotateAngleY = 0.0F;
      this.leftDeliveryLeg.rotateAngleY = 0.0F;
   }
}
