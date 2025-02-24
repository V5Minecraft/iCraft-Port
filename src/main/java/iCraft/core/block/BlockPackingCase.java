package iCraft.core.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockPackingCase extends BlockHorizontal {
   public static final PropertyDirection FACING = BlockHorizontal.FACING;

   public BlockPackingCase() {
      super(Material.WOOD);
      this.setBlockUnbreakable();
      this.setCreativeTab(CreativeTabs.MISC);
      this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
   }

   @Override
   public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
      EnumFacing direction = placer.getHorizontalFacing().rotateY();
      return this.getDefaultState().withProperty(FACING, direction);
   }

   @Override
   protected BlockStateContainer createBlockState() {
      return new BlockStateContainer(this, FACING);
   }

   @Override
   public IBlockState getStateFromMeta(int meta) {
      EnumFacing facing = EnumFacing.getHorizontal(meta);
      return this.getDefaultState().withProperty(FACING, facing);
   }

   @Override
   public int getMetaFromState(IBlockState state) {
      return state.getValue(FACING).getHorizontalIndex();
   }

   @Override
   public boolean isOpaqueCube(IBlockState state) {
      return false;
   }

   @Override
   public boolean isFullCube(IBlockState state) {
      return false;
   }
}
