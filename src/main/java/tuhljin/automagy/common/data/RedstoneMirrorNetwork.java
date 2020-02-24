package tuhljin.automagy.common.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import tuhljin.automagy.common.Automagy;
import tuhljin.automagy.common.lib.TjUtil;
import tuhljin.automagy.common.tiles.TileRedcrystalMerc;

import javax.annotation.Nonnull;

public class RedstoneMirrorNetwork extends WorldSavedData {
    protected Map<BlockPos, Integer> highestPower = new HashMap<>();
    protected Map<BlockPos, HashMap<BlockPos, Integer>> powerLevels = new HashMap<>();
    private static final int NEEDS_POWER_RECALC = -1;
    private int dimensionID = -9909;

    public RedstoneMirrorNetwork(String dataMapName) {
        super(dataMapName);
    }

    public int getSignalGoingIn(BlockPos mirrorPos) {
        Integer strength = this.highestPower.get(mirrorPos);
        return strength == null ? 0 : (strength == -1 ? this.calculateSignal(mirrorPos) : strength);
    }

    public int getSignalGoingIn(int x, int y, int z) {
        return this.getSignalGoingIn(new BlockPos(x, y, z));
    }

    public void contributeToSignalInto(int x, int y, int z, TileEntity te, int strength) {
        this.dimensionID = te.getWorld().provider.getDimension();
        BlockPos mirrorPos = new BlockPos(x, y, z);
        Integer currentStrength = this.highestPower.get(mirrorPos);
        boolean newHigh;
        if (currentStrength != null && currentStrength >= strength) {
            newHigh = false;
        } else {
            this.highestPower.put(mirrorPos, strength);
            newHigh = true;
        }

        HashMap<BlockPos, Integer> submap = this.powerLevels.get(mirrorPos);
        if (submap == null) {
            submap = new HashMap<>();
            this.powerLevels.put(mirrorPos, submap);
            submap.put(new BlockPos(te.getPos()), strength);
        } else {
            BlockPos tePos = new BlockPos(te.getPos());
            Integer prev = null;
            if (!newHigh) {
                prev = (Integer)submap.get(tePos);
            }

            if (prev != null) {
                if (prev != strength) {
                    submap.put(tePos, strength);
                    if (prev.equals(this.highestPower.get(mirrorPos))) {
                        this.highestPower.put(mirrorPos, -1);
                    }
                }
            } else {
                submap.put(tePos, strength);
            }
        }

        this.markDirty();
    }

    private int calculateSignal(BlockPos mirrorPos) {
        int highest = 0;
        HashMap<BlockPos, Integer> map = this.powerLevels.get(mirrorPos);
        if (map != null) {
            Integer strength = null;
            for (Entry<BlockPos, Integer> entry : map.entrySet()) {
                strength = entry.getValue();
                highest = Math.max(highest, strength == null ? 0 : strength);
            }

            this.highestPower.put(mirrorPos, highest);
        }

        return highest;
    }

    public void removeSignal(int x, int y, int z) {
        BlockPos mirrorPos = new BlockPos(x, y, z);
        if (this.highestPower.containsKey(mirrorPos)) {
            this.highestPower.remove(mirrorPos);
            this.powerLevels.remove(mirrorPos);
            this.markDirty();
        }

    }

    public void removeContributor(int x, int y, int z, BlockPos tePos) {
        this.removeContributor(new BlockPos(x, y, z), tePos);
    }

    public void removeContributor(BlockPos mirrorPos, BlockPos tePos) {
        HashMap<BlockPos, Integer> submap = this.powerLevels.get(mirrorPos);
        if (submap != null) {
            Integer prevStrength = submap.get(tePos);
            if (prevStrength != null) {
                submap.remove(tePos);
                this.markDirty();
                if (prevStrength >= this.getSignalGoingIn(mirrorPos)) {
                    this.highestPower.put(mirrorPos, -1);
                }
            }
        }

    }

    public void removeContributor(int x, int y, int z, TileEntity te) {
        this.removeContributor(x, y, z, new BlockPos(te.getPos()));
    }

    public void cleanEntries(World world, int x, int y, int z) {
        this.cleanEntries(world, new BlockPos(x, y, z));
    }

    public void cleanEntries(World world, BlockPos mirrorPos) {
        HashMap<BlockPos, Integer> submap = this.powerLevels.get(mirrorPos);
        if (submap != null) {
            boolean anyInvalid = false;
            for (Entry<BlockPos, Integer> entry : submap.entrySet()) {
                BlockPos pos = entry.getKey();
                TileEntity te = world.getTileEntity(pos);
                boolean invalid = false;
                if (te == null) {
                    if (TjUtil.isChunkLoaded(world, mirrorPos)) {
                        invalid = true;
                    }
                } else if (!(te instanceof TileRedcrystalMerc)) {
                    invalid = true;
                } else {
                    TileRedcrystalMerc ter = (TileRedcrystalMerc)te;
                    if (ter.mirrorDim != world.provider.getDimension() || ter.mirrorX != mirrorPos.getX() || ter.mirrorY != mirrorPos.getY() || ter.mirrorZ != mirrorPos.getZ()) {
                        invalid = true;
                    }
                }

                if (invalid) {
                    this.removeContributor(mirrorPos, pos);
                    anyInvalid = true;
                }
            }

            if (anyInvalid) {
                this.highestPower.put(mirrorPos, -1);
            }
        }

    }

    public void cleanEntries() {
        WorldServer world = null;

        try {
            world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(this.dimensionID);
        } catch (Exception ignored) {
        }

        if (world != null) {
            for (Entry<BlockPos, HashMap<BlockPos, Integer>> entry : powerLevels.entrySet()) {
                this.cleanEntries(world, entry.getKey());
            }

        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.highestPower.clear();
        this.powerLevels.clear();
        int errors = 0;
        int suberrors = 0;
        NBTTagList mainList = nbttagcompound.getTagList("MirrorsRedstoneData", 10);
        int count = mainList.tagCount();

        for(int i = 0; i < count; ++i) {
            NBTTagCompound compound = mainList.getCompoundTagAt(i);
            int[] pos = compound.getIntArray("mirrorPos");
            if (pos.length != 3) {
                ++errors;
            } else {
                NBTTagList sublist = compound.getTagList("links", 10);
                int subcount = sublist.tagCount();
                if (subcount > 0) {
                    BlockPos mirrorPos = new BlockPos(pos[0], pos[1], pos[2]);
                    this.highestPower.put(mirrorPos, -1);
                    HashMap<BlockPos, Integer> submap = new HashMap<>();
                    this.powerLevels.put(mirrorPos, submap);

                    for(int j = 0; j < subcount; ++j) {
                        NBTTagCompound tag = sublist.getCompoundTagAt(j);
                        int[] pos2 = tag.getIntArray("pos");
                        if (pos2.length != 3) {
                            ++suberrors;
                        } else {
                            int strength = tag.getInteger("strength");
                            submap.put(new BlockPos(pos2[0], pos2[1], pos2[2]), strength);
                        }
                    }
                }
            }
        }

        this.dimensionID = nbttagcompound.getInteger("MirrorsRedstoneDimension");
        if (errors > 0 || suberrors > 0) {
            Automagy.logWarning("RedstoneMirrorNetwork: Malformed save data detected. Failed to load " + errors + " redstone strength entries and " + suberrors + " sub-entries.");
        }

    }

    @Nonnull
    @Override
    public NBTTagCompound writeToNBT(@Nonnull NBTTagCompound nbttagcompound) {
        NBTTagList mainList = new NBTTagList();

        for (Entry<BlockPos, HashMap<BlockPos, Integer>> entry : this.powerLevels.entrySet()) {
            BlockPos mirrorPos = entry.getKey();
            HashMap<BlockPos, Integer> submap = entry.getValue();
            if (submap.size() > 0) {
                NBTTagCompound compound = new NBTTagCompound();
                mainList.appendTag(compound);
                compound.setIntArray("mirrorPos", new int[]{mirrorPos.getX(), mirrorPos.getY(), mirrorPos.getZ()});
                NBTTagList sublist = new NBTTagList();
                compound.setTag("links", sublist);
                for (Entry<BlockPos, Integer> subentry : submap.entrySet()) {
                    Integer strength = subentry.getValue();
                    if (strength != null && strength > 0) {
                        BlockPos pos = subentry.getKey();
                        NBTTagCompound tag = new NBTTagCompound();
                        sublist.appendTag(tag);
                        tag.setIntArray("pos", new int[]{pos.getX(), pos.getY(), pos.getZ()});
                        tag.setInteger("strength", strength);
                    }
                }
            }
        }

        nbttagcompound.setTag("MirrorsRedstoneData", mainList);
        nbttagcompound.setInteger("MirrorsRedstoneDimension", this.dimensionID);
        return nbttagcompound;
    }

    public static RedstoneMirrorNetwork getData(int dimensionID) {
        World worldSave = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimensionID);

        RedstoneMirrorNetwork data = (RedstoneMirrorNetwork)worldSave.loadData(RedstoneMirrorNetwork.class, "AutomagyUniversalRedstone");
        if (data == null) {
            data = new RedstoneMirrorNetwork("AutomagyUniversalRedstone");
            data.dimensionID = dimensionID;
            worldSave.setData("AutomagyUniversalRedstone", data);
        }

        return data;
    }

    public static RedstoneMirrorNetwork getData(World world) {
        return getData(world.provider.getDimension());
    }
}
