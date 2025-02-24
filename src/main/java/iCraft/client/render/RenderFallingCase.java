package iCraft.client.render;

import iCraft.client.model.ModelFallingCase;
import iCraft.core.entity.EntityPackingCase;
import iCraft.core.utils.ICraftClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderFallingCase extends Render<EntityPackingCase> {
   private final Minecraft mc = Minecraft.getMinecraft();
   private final ModelFallingCase model = new ModelFallingCase();
   private static final ResourceLocation TEXTURE = ICraftClientUtils.getResource(ICraftClientUtils.ResourceType.RENDER, "packingcase.png");

   public RenderFallingCase(RenderManager renderManager) {
      super(renderManager);
   }

   @Override
   protected ResourceLocation getEntityTexture(EntityPackingCase entity) {
      return TEXTURE;
   }

   @Override
   public void doRender(EntityPackingCase entity, double x, double y, double z, float entityYaw, float partialTicks) {
      if (!entity.isDead) {
         renderCase(x, y, z);
      }
   }

   private void renderCase(double x, double y, double z) {
      GlStateManager.pushMatrix();
      GlStateManager.translate(x, y, z);
      GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
      GlStateManager.translate(0.0F, 0.9F, 0.0F);
      mc.renderEngine.bindTexture(TEXTURE);
      model.render(0.0625F);
      GlStateManager.popMatrix();
   }
}
