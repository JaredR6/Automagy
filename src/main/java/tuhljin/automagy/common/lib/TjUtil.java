package tuhljin.automagy.common.lib;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import javax.annotation.Nullable;
import thaumcraft.common.tiles.crafting.TilePedestal;
import tuhljin.automagy.common.Automagy;

import javax.annotation.Nonnull;

public class TjUtil {

    public static void sendChatToPlayer(@Nonnull EntityPlayer player, String message) {
        message = I18n.format(message);
        sendRawChatToPlayer(player, message, TextFormatting.DARK_AQUA);
    }

    public static void sendFormattedChatToPlayer(@Nonnull EntityPlayer player, String message, Object... data) {
        message = I18n.format(message, data);
        sendRawChatToPlayer(player, message, TextFormatting.DARK_AQUA);
    }

    public static void sendRawChatToPlayer(@Nonnull EntityPlayer player, String message, @Nullable TextFormatting chatFormatting) {
        if (chatFormatting != null) {
            message = chatFormatting + message;
        }

        player.sendMessage(new TextComponentString(message));
    }

    public static boolean isPlayerOnline(String name) {
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();

        for (EntityPlayerMP player : players) {
            if (player.getName().equals(name))
                return true;
        }
        return false;
    }

    @Nonnull
    public static ArrayList<String> getMultiLineLocalizedString(String message) {
        message = I18n.format(message);
        String[] s = message.split("\\\\n");
        return new ArrayList<>(Arrays.asList(s));
     }

    @Nonnull
    public static Block getBlock(@Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos) {
        return blockAccess.getBlockState(pos).getBlock();
    }

    @Nullable
    public static EnumFacing getNextSideOnBlockFromDir(@Nonnull EnumFacing side, @Nonnull EnumFacing dir) {
        switch(side) {
            case UP:
            case DOWN:
                switch(dir) {
                    case UP:
                    case DOWN:
                        return null;
                    case NORTH:
                    case SOUTH:
                    case WEST:
                    case EAST:
                        return dir;
                    default:
                        return null;
                }
            case NORTH:
            case SOUTH:
                switch(dir) {
                    case UP:
                        return EnumFacing.UP;
                    case DOWN:
                        return EnumFacing.DOWN;
                    case NORTH:
                    case SOUTH:
                        return null;
                    case WEST:
                        return EnumFacing.WEST;
                    case EAST:
                        return EnumFacing.EAST;
                    default:
                        return null;
                }
            case WEST:
            case EAST:
                switch(dir) {
                    case UP:
                        return EnumFacing.UP;
                    case DOWN:
                        return EnumFacing.DOWN;
                    case NORTH:
                        return EnumFacing.NORTH;
                    case SOUTH:
                        return EnumFacing.SOUTH;
                    case WEST:
                    case EAST:
                        return null;
                }
        }

        return null;
    }

    @Nullable
    public static EnumFacing getSideFromEntityFacing(@Nonnull EntityLivingBase entity) {
        int l = MathHelper.floor((double)(entity.rotationYaw * 4.0F / 360.0F) + 2.5D) & 3;
        switch(l) {
            case 0:
                return EnumFacing.NORTH;
            case 1:
                return EnumFacing.EAST;
            case 2:
                return EnumFacing.SOUTH;
            case 3:
                return EnumFacing.WEST;
            default:
                return null;
        }
    }

    @Nullable
    public static String getBlockNameAt(@Nonnull World world, @Nonnull BlockPos pos) {
        String name = null;

        if (isChunkLoaded(world, pos)) {
            if (world.isAirBlock(pos))
                    return null;
            IBlockState state = world.getBlockState(pos);

            Block block = state.getBlock();
            Item blockItem = Item.getItemFromBlock(block);
            if (blockItem == Items.AIR) {
                name = block.getUnlocalizedName();
            } else {
                ItemStack blockStack = new ItemStack(blockItem, 1, block.getMetaFromState(state));
                name = blockItem.getUnlocalizedName(blockStack);
            }

            if (name.isEmpty()) {
                return null;
            }
                /*
                String temp = name + ".name";
                String name2 = I18n.format(temp);
                if (temp.equals(name2)) {
                    int meta = block.getMetaFromState(state);
                    temp = name + "." + meta + ".name";
                    name2 = I18n.format(temp);
                    if (temp.equals(name2)) {
                        if (name.substring(0, 5).equals("tile.")) {
                            name = name.substring(5);
                        }

                        name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    } else {
                        name = name2;
                    }
                } else {
                    name = name2;
                }

                 */
        }

        return name;
    }

    public static boolean isAcceptableSurfaceAtPos(@Nonnull World world, @Nonnull BlockPos pos, boolean allowGlowstone, boolean allowGlass, boolean allowPedestal) {
        if (world.isSideSolid(pos, EnumFacing.UP)) {
            return true;
        } else {
            if (allowGlowstone || allowGlass || allowPedestal) {
                Block block = TjUtil.getBlock(world, pos);
                if (allowGlowstone && block == Blocks.GLOWSTONE) {
                    return true;
                }

                if (allowGlass && block == Blocks.GLASS) {
                    return true;
                }

                if (allowPedestal) {
                    TileEntity tile = world.getTileEntity(pos);
                    return tile instanceof TilePedestal;
                }
            }

            return false;
        }
    }

    public static boolean isAcceptableSurfaceBelowPos(@Nonnull World world, @Nonnull BlockPos pos, boolean allowGlowstone, boolean allowGlass, boolean allowPedestal) {
        return isAcceptableSurfaceAtPos(world, pos.offset(EnumFacing.DOWN), allowGlowstone, allowGlass, allowPedestal);
    }

    @Nonnull
    public static NonNullList<ItemStack> getDropsFromBlock(@Nonnull World world, @Nonnull BlockPos pos, boolean includeSilkDrops, int fortune) {
        IBlockState state = world.getBlockState(pos);
        NonNullList<ItemStack> drops = NonNullList.create();
        state.getBlock().getDrops(drops, world, pos, state, fortune);
        if (includeSilkDrops) {
            ItemStack stack = getStackFromBlock(state.getBlock(), world, pos);
            if (!stack.isEmpty()) {
                boolean found = false;
                for (ItemStack drop : drops) {
                    if (areItemsEqualIgnoringSize(stack, drop)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    drops.add(stack);
                }
            }
        }

        return drops;
    }

    @Nonnull
    public static ItemStack getStackFromBlock(@Nonnull World world, @Nonnull BlockPos pos) {
        return getStackFromBlock(TjUtil.getBlock(world, pos), world, pos);
    }

    @Nonnull
    public static ItemStack getStackFromBlock(@Nonnull Block block, @Nonnull World world, @Nonnull BlockPos pos) {
        return new ItemStack(Item.getItemFromBlock(block), 1, block.getMetaFromState(world.getBlockState(pos)));
    }

    public static boolean isPrecipitationAt(@Nonnull World world, @Nonnull BlockPos pos) {
        if (!world.isRaining()) {
            return false;
        } else {
            Biome bg = world.getBiome(pos);
            return bg.getEnableSnow() || bg.canRain();
        }
    }

    public static boolean isEntityLookingDown(@Nonnull EntityLivingBase entity) {
        return entity.rotationPitch > 0.0F;
    }

    public static boolean areItemsEqualIgnoringSize(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
        return !stack1.isEmpty() && !stack2.isEmpty() && stack1.isItemEqual(stack2) && ItemStack.areItemStackTagsEqual(stack2, stack1);
    }

    public static boolean canItemsStack(@Nonnull ItemStack stack1, @Nonnull ItemStack stack2) {
        return (stack1.isEmpty() || stack2.isEmpty() || areItemsEqualIgnoringSize(stack1, stack2)) && stack1.isStackable();
    }

    @Nonnull
    public static NBTTagCompound writeLargeItemStackToNBT(@Nonnull ItemStack stack, @Nonnull NBTTagCompound nbt) {
        nbt.setShort("id", (short)Item.getIdFromItem(stack.getItem()));
        nbt.setInteger("Count", stack.getCount());
        nbt.setShort("Damage", (short)stack.getItemDamage());
        if (stack.hasTagCompound()) {
            nbt.setTag("tag", stack.getTagCompound());
        }

        return nbt;
    }

    @Nonnull
    public static ItemStack readLargeItemStackFromNBT(@Nonnull NBTTagCompound nbt) {
        Item item = Item.getItemById(nbt.getShort("id"));
        int damage = Math.max(nbt.getShort("Damage"), 0);
        ItemStack stack = new ItemStack(item, nbt.getInteger("Count"), damage);
        if (nbt.hasKey("tag", 10)) {
            stack.setTagCompound(nbt.getCompoundTag("tag"));
            item.updateItemStackNBT(stack.getTagCompound());
        }

        return stack;
    }

    public static boolean playerHasPartialStackedItem(@Nonnull EntityPlayer player, @Nonnull ItemStack stack) {
        if (!stack.isStackable()) {
            return false;
        } else {
            int max = stack.getMaxStackSize();
            if (max <= 1) {
                return false;
            } else {
                InventoryPlayer inv = player.inventory;

                for(int i = 0; i < inv.mainInventory.size(); ++i) {
                    ItemStack invStack = inv.mainInventory.get(i);
                    if (!invStack.isEmpty() && invStack.isItemEqual(stack) && invStack.getCount() < max) {
                        return true;
                    }
                }

                return false;
            }
        }
    }

    public static void consumePlayerItem(@Nonnull EntityPlayer player, int slot, ItemStack replace, boolean replaceOnlyIfNoContainer) {
        boolean replaced = false;
        boolean needUpdate = false;
        boolean containerTookSlot = false;
        InventoryPlayer inv = player.inventory;
        ItemStack stack = inv.getStackInSlot(slot);
        if (!stack.isEmpty()) {
            ItemStack container = ItemStack.EMPTY;
            if (stack.getItem().hasContainerItem(stack)) {
                container = stack.getItem().getContainerItem(stack);
                if (replaceOnlyIfNoContainer && !container.isEmpty()) {
                    replace = ItemStack.EMPTY;
                }
            }

            stack.shrink(1);
            if (stack.getCount() == 0) {
                if (container.isEmpty() && !playerHasPartialStackedItem(player, container)) {
                    inv.setInventorySlotContents(slot, container);
                    containerTookSlot = true;
                    needUpdate = true;
                }

                if (!containerTookSlot && !replace.isEmpty() && !playerHasPartialStackedItem(player, replace)) {
                    inv.setInventorySlotContents(slot, replace);
                    replaced = true;
                    needUpdate = true;
                }
            }

            if (!container.isEmpty() && !containerTookSlot) {
                if (!inv.addItemStackToInventory(container)) {
                    if (player instanceof EntityPlayerMP) {
                        player.dropItem(container, false);
                    }
                } else {
                    needUpdate = true;
                }
            }

            if (!replace.isEmpty() && !replaced) {
                if (!inv.addItemStackToInventory(replace)) {
                    if (player instanceof EntityPlayerMP) {
                        player.dropItem(replace, false);
                    }
                } else {
                    needUpdate = true;
                }
            }

            if (needUpdate && player instanceof EntityPlayerMP && !(player instanceof FakePlayer)) {
                ((EntityPlayerMP)player).sendContainerToPlayer(player.inventoryContainer);
            }
        }

    }

    public static void consumePlayerItem(@Nonnull EntityPlayer player, int slot, ItemStack replace) {
        consumePlayerItem(player, slot, replace, false);
    }

    public static void consumePlayerItem(@Nonnull EntityPlayer player, int slot) {
        consumePlayerItem(player, slot, (ItemStack)null, false);
    }

    public static void dropItemsIntoWorld(@Nullable IInventory inventory, @Nonnull World world, @Nonnull BlockPos pos, @Nullable Block block) {
        if (inventory != null) {
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            for(int i1 = 0; i1 < inventory.getSizeInventory(); ++i1) {
                ItemStack stack = inventory.getStackInSlot(i1);
                if (!stack.isEmpty()) {
                    dropItemIntoWorld(stack, world, x, y, z);
                }
            }

            if (block != null) {
                world.updateComparatorOutputLevel(pos, block);
            }
        }

    }

    @Nullable
    public static EntityItem dropItemIntoWorld(@Nonnull ItemStack stack, @Nonnull World world, double x, double y, double z) {
        Random rand = world.rand;
        float f = rand.nextFloat() * 0.8F + 0.1F;
        float f1 = rand.nextFloat() * 0.8F + 0.1F;
        EntityItem entityitem = null;

        for(float f2 = rand.nextFloat() * 0.8F + 0.1F; stack.getCount() > 0; world.spawnEntity(entityitem)) {
            int j1 = rand.nextInt(21) + 10;
            if (j1 > stack.getCount()) {
                j1 = stack.getCount();
            }

            stack.shrink(j1);
            entityitem = new EntityItem(world, x + (double)f, y + (double)f1, z + (double)f2, new ItemStack(stack.getItem(), j1, stack.getItemDamage()));
            float f3 = 0.05F;
            entityitem.motionX = rand.nextGaussian() * f3;
            entityitem.motionY = rand.nextGaussian() * f3 + 0.2F;
            entityitem.motionZ = rand.nextGaussian() * f3;
            if (stack.hasTagCompound()) {
                entityitem.getItem().setTagCompound(stack.getTagCompound().copy());
            }
        }

        return entityitem;
    }

    @Nonnull
    public static EntityItem dropItemIntoWorldSimple(@Nonnull ItemStack stack, @Nonnull World world, double x, double y, double z) {
        Random rand = world.rand;
        double f3 = 0.05D;
        double motionX = rand.nextGaussian() * f3;
        double motionY = rand.nextGaussian() * f3 + 0.2D;
        double motionZ = rand.nextGaussian() * f3;
        return dropItemIntoWorldWithMotion(stack, world, x, y, z, motionX, motionY, motionZ);
    }

    @Nonnull
    public static EntityItem dropItemIntoWorldWithMotion(@Nonnull ItemStack stack, @Nonnull World world, double x, double y, double z, double motionX, double motionY, double motionZ) {
        EntityItem entityitem = new EntityItem(world, x, y, z, stack);
        entityitem.motionX = motionX;
        entityitem.motionY = motionY;
        entityitem.motionZ = motionZ;
        world.spawnEntity(entityitem);
        return entityitem;
    }

    @Nonnull
    public static List<EntityItem> getItemsInArea(@Nonnull World world, @Nonnull AxisAlignedBB area) {
        return world.getEntitiesWithinAABB(EntityItem.class, area);
    }

    public static boolean canFullyExtract(@Nonnull IItemHandler inv, int slot) {
        ItemStack stack = inv.getStackInSlot(slot);
        if (stack.isEmpty()) {
            return false;
        } else {
            ItemStack extracted = inv.extractItem(slot, stack.getCount(), true);
            return !extracted.isEmpty() && extracted.getCount() >= stack.getCount() && ItemStack.areItemsEqual(stack, extracted);
        }
    }

    @Nonnull
    public static ItemStack addToInventory(ItemStack stack, @Nonnull IInventory inv, int firstSlot, int lastSlot) {
        stack = stack.copy();
        boolean reverse = firstSlot > lastSlot;
        int offSlot = reverse ? lastSlot - 1 : lastSlot + 1;
        int stacksTo = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
        int slot;
        ItemStack slottedStack;
        if (stacksTo > 1 && stack.isStackable()) {
            slot = firstSlot;

            while(slot != offSlot) {
                if (inv.isItemValidForSlot(slot, stack)) {
                    slottedStack = inv.getStackInSlot(slot);
                    if (!slottedStack.isEmpty() && canItemsStack(stack, slottedStack)) {
                        int newSize = stack.getCount() + slottedStack.getCount();
                        ItemStack newStack;
                        if (newSize <= stacksTo) {
                            newStack = slottedStack.copy();
                            newStack.setCount(newSize);
                            inv.setInventorySlotContents(slot, newStack);
                            return ItemStack.EMPTY;
                        }

                        if (slottedStack.getCount() < stacksTo) {
                            stack.setCount(newSize - stacksTo);
                            newStack = slottedStack.copy();
                            newStack.setCount(stacksTo);
                            inv.setInventorySlotContents(slot, newStack);
                        }
                    }
                }

                if (reverse) {
                    --slot;
                } else {
                    ++slot;
                }
            }
        }

        slot = firstSlot;

        while(slot != offSlot) {
            if (inv.isItemValidForSlot(slot, stack)) {
                slottedStack = inv.getStackInSlot(slot);
                if (slottedStack.isEmpty()) {
                    if (stack.getCount() <= stacksTo) {
                        inv.setInventorySlotContents(slot, stack.copy());
                        return ItemStack.EMPTY;
                    }

                    ItemStack newStack = stack.splitStack(stacksTo);
                    inv.setInventorySlotContents(slot, newStack);
                }
            }

            if (reverse) {
                --slot;
            } else {
                ++slot;
            }
        }

        return stack;
    }

    @Nonnull
    public static ItemStack addToInventory(ItemStack stack, @Nonnull IInventory inv) {
        return addToInventory(stack, inv, 0, inv.getSizeInventory() - 1);
    }

    public static boolean canFitInInventory(@Nonnull ItemStack stack, @Nonnull IInventory inv, int firstSlot, int lastSlot) {
        int toDropOff = stack.getCount();
        boolean stackable = stack.isStackable();
        int stacksTo = stackable ? Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()) : 1;

        for(int slot = firstSlot; slot <= lastSlot; ++slot) {
            if (inv.isItemValidForSlot(slot, stack)) {
                ItemStack slottedStack = inv.getStackInSlot(slot);
                int available = 0;
                if (slottedStack.isEmpty()) {
                    available = stacksTo;
                } else if (stackable && areItemsEqualIgnoringSize(stack, slottedStack)) {
                    available = stacksTo - slottedStack.getCount();
                }

                if (available > 0) {
                    toDropOff -= available;
                    if (toDropOff < 1) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Nonnull
    public static ItemStack addToInventory(@Nonnull ItemStack stack, IItemHandler handler, boolean simulate) {
        return ItemHandlerHelper.insertItem(handler, stack.copy(), simulate);
    }

    // TODO: Implement liquids
    @Nullable
    public static Fluid canDrainTank(@Nonnull IFluidHandler tank, EnumFacing dir, @Nullable Fluid fluid) {
        tank.getTankProperties();

        if (fluid != null) {
            FluidStack testDrain = tank.drain(Integer.MAX_VALUE, false);
            if (testDrain != null && testDrain.getFluid() == fluid) {
                return fluid;
            } else {
                return null;
            }
        } else {

            FluidStack testDrain = tank.drain(Integer.MAX_VALUE, false);
            return testDrain == null ? null : testDrain.getFluid();
        }
    }

    public static boolean isSourceBlock(@Nonnull World world, @Nonnull BlockPos blockPos) {
        IBlockState state = world.getBlockState(blockPos);
        if (state == Blocks.AIR.getDefaultState()) {
            return false;
        } else {
            Block block = state.getBlock();
            if (block instanceof BlockFluidClassic) {
                return ((BlockFluidClassic)block).isSourceBlock(world, blockPos);
            } else if (block instanceof BlockFluidBase) {
                BlockFluidBase var10001 = (BlockFluidBase)block;
                return state.getValue(BlockFluidBase.LEVEL) == 0;
            } else {
                return block.getMetaFromState(state) == 0;
            }
        }
    }

    public static float getDistanceBetweenPoints(int x1, int y1, int z1, int x2, int y2, int z2) {
        float f = (float)((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2));
        return MathHelper.sqrt(f);
    }

    public static double getDistanceBetweenPoints_squared(double x1, double y1, double z1, double x2, double y2, double z2) {
        return (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2);
    }

    public static double getDistanceBetweenPoints_double(double x1, double y1, double z1, double x2, double y2, double z2) {
        double f = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2) + (z1 - z2) * (z1 - z2);
        return (double)MathHelper.sqrt(f);
    }

    public static boolean isChunkLoaded(@Nonnull World world, @Nonnull BlockPos pos) {
        return world.getChunkFromBlockCoords(pos).isLoaded();
    }

    public static boolean isChunkLoaded(@Nonnull World world, int blockX, int blockZ) {
        return isChunkLoaded(world, new BlockPos(blockX, 0, blockZ));
    }

    @Nullable
    public static Pattern getSafePatternUsingAsteriskForWildcard(@Nonnull String str) {
        String s = str.replaceAll("\\*+", ".*");
        s = Pattern.quote(s);
        s = s.replace(".*", "\\E.*\\Q");
        s = s.replace("\\Q\\E", "");

        try {
            Pattern p = Pattern.compile(s);
            return p;
        } catch (Exception var4) {
            Automagy.logSevereError("Failed to create regex pattern. Name filter will not function properly. String was: " + str);
            return null;
        }
    }

    public static boolean areDoublesAlmostEqual_lowprec(double d1, double d2) {
        double epsilon = 1.0E-5D;
        return Math.abs(d1 - d2) < epsilon;
    }

    public static boolean willAdditionOverflowInt(int left, int right) {
        if (right < 0 && right != -2147483648) {
            return willSubtractionOverflowInt(left, -right);
        } else {
            return (~(left ^ right) & (left ^ left + right)) < 0;
        }
    }

    public static boolean willSubtractionOverflowInt(int left, int right) {
        if (right < 0) {
            return willAdditionOverflowInt(left, -right);
        } else {
            return ((left ^ right) & (left ^ left - right)) < 0;
        }
    }

    public static int overflowSafeAddInt(int a, int b) {
        return willAdditionOverflowInt(a, b) ? 2147483647 : a + b;
    }

    public static int overflowSafeSubtractInt(int a, int b) {
        return willSubtractionOverflowInt(a, b) ? -2147483648 : a - b;
    }
}
