//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package tuhljin.automagy.common.lib.events;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import thaumcraft.api.blocks.BlocksTC;
import thaumcraft.api.casters.FocusEffect;
import thaumcraft.api.casters.ICasterTriggerManager;
import thaumcraft.api.casters.CasterTriggerRegistry;
import thaumcraft.common.items.casters.ItemFocus;
import thaumcraft.common.items.casters.foci.FocusEffectRift;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.blocks.ModBlocks;
import tuhljin.automagy.common.lib.ThaumcraftExtension;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.network.MessageParticles;

import javax.annotation.Nonnull;

public class AutomagyCasterTriggerManager implements ICasterTriggerManager {
    private static final int REACT_OBSIDIAN = 0;

    public AutomagyCasterTriggerManager() {
        CasterTriggerRegistry.registerWandBlockTrigger(this, 0, Blocks.OBSIDIAN.getDefaultState(), "automagy");
    }

    public boolean performTrigger(@Nonnull World world, ItemStack caster, EntityPlayer player, @Nonnull BlockPos pos, EnumFacing side, int event) {
        if (event == 0) {
            if (ThaumcraftExtension.isResearchComplete(player, "HUNGRYMAW") && this.transformObsidian(world, caster, player, pos)) {
                return false;
            }
        }
        return false;
    }

    public boolean transformObsidian(@Nonnull World world, ItemStack caster, EntityPlayer player, @Nonnull BlockPos pos) {
        boolean hasRift = false;
        for (FocusEffect effect : ItemFocus.getPackage(caster).getFocusEffects()) {
            if (effect instanceof FocusEffectRift) {
                hasRift = true;
                break;
            }
        }
        if (hasRift) {
            BlockPos pos2 = pos.offset(EnumFacing.DOWN);
            Block b = TjUtil.getBlock(world, pos2);
            if (b == BlocksTC.hungryChest) {
                if (!world.isRemote) {
                    MessageParticles.sendToClients((short)0, world, pos2);
                    world.setBlockState(pos2, ModBlocks.specialProcess.getDefaultState());
                } else {
                    int x = pos.getX();
                    int y = pos.getY();
                    int z = pos.getZ();

                    for(int i = 0; i < 10; ++i) {
                        Automagy.proxy.fxWisp3(x + 0.5F + world.rand.nextFloat() / 2.0F, y - 0.5F + world.rand.nextFloat() / 2.0F, z + 0.5F + world.rand.nextFloat() / 2.0F, x + 0.3F + world.rand.nextFloat() * 0.4F, y + 0.5F, z + 0.3F + world.rand.nextFloat() * 0.4F, 0.5F, 5, true, -0.025F);
                        Automagy.proxy.fxWisp3(x + 0.5F - world.rand.nextFloat() / 2.0F, y - 0.5F - world.rand.nextFloat() / 2.0F, z + 0.5F - world.rand.nextFloat() / 2.0F, x + 0.3F + world.rand.nextFloat() * 0.4F, y + 0.5F, z + 0.3F + world.rand.nextFloat() * 0.4F, 0.5F, 5, true, -0.025F);
                    }
                }

                return true;
            }
        }

        return false;
    }
}
