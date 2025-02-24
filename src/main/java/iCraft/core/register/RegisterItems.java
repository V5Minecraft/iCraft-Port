package iCraft.core.register;

import iCraft.core.item.ItemPizza;
import iCraft.core.item.ItemiCraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegisterItems {
    public static Item iCraft = new ItemiCraft().setUnlocalizedName("icraft").setRegistryName("icraft");
    public static Item pizza = new ItemPizza().setUnlocalizedName("pizza").setRegistryName("pizza");

    public static void register() {
        setRegister(iCraft);
        setRegister(pizza);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender() {
        setRender(iCraft);
        setRender(pizza);
    }

    private static void setRegister(Item item) {
        ForgeRegistries.ITEMS.registerAll(item);
    }

    @SideOnly(Side.CLIENT)
    private static void setRender(Item item) {
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
