package iCraft.client.render;

import iCraft.client.model.ModelPizzaDelivery;
import iCraft.core.utils.ICraftClientUtils;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPizzaDelivery extends RenderLiving {
   public RenderPizzaDelivery(RenderManager renderManager) {
      super(renderManager, new ModelPizzaDelivery(), 0.5F);
   }

   protected ResourceLocation getEntityTexture(Entity entity) {
      return ICraftClientUtils.getResource(ICraftClientUtils.ResourceType.RENDER, "pizzadelivery.png");
   }
}
