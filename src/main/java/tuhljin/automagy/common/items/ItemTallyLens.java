package tuhljin.automagy.common.items;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import thaumcraft.common.lib.SoundsTC;
import tuhljin.automagy.common.blocks.BlockTallyBase;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.tiles.TileTallyBase;

public class ItemTallyLens extends ModVariantItem {
    public ItemTallyLens() {
        super(ImmutableMap.of(0, "clear", 1, "unbound", 2, "yield"));
    }

    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            int d = stack.getItemDamage();
            if (d == 1 || d == 2) {
                IBlockState state = world.getBlockState(pos);
                if (state.getBlock() == ModBlocks.tallyBox) {
                    BlockTallyBase blockT = (BlockTallyBase)state.getBlock();
                    TileTallyBase te = (TileTallyBase)world.getTileEntity(pos);
                    NBTTagCompound nbt = new NBTTagCompound();
                    te.writeServerNBT(nbt);
                    te.writeCommonNBT(nbt);
                    BlockTallyBase newBlock = d == 1 ? ModBlocks.tallyBlockWorld : ModBlocks.tallyBlockDrops;
                    IBlockState newState = newBlock.getDefaultState().withProperty(newBlock.FACING, state.getValue(blockT.FACING));
                    world.setBlockState(pos, newState);
                    TileTallyBase teNew = (TileTallyBase)world.getTileEntity(pos);
                    teNew.readServerNBT(nbt);
                    teNew.readCommonNBT(nbt);
                    teNew.markForUpdate();
                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }

                    world.playSound(player, pos, SoundsTC.upgrade, SoundCategory.BLOCKS, 0.5F, 1.0F);
                }
            }
        }

        return false;
    }
}
