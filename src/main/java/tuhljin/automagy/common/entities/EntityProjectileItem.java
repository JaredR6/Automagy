package tuhljin.automagy.common.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class EntityProjectileItem extends EntityThrowable implements IEntityAdditionalSpawnData, IItemMetadata {
    public int metadata = 0;

    public EntityProjectileItem(World worldIn) {
        super(worldIn);
    }

    public EntityProjectileItem(World worldIn, EntityLivingBase throwerIn, int metadata) {
        super(worldIn, throwerIn);
        this.metadata = metadata;
        NBTTagCompound nbt = this.getEntityData();
        nbt.setInteger("metadata", metadata);
    }

    public EntityProjectileItem(World worldIn, double x, double y, double z, int metadata) {
        super(worldIn, x, y, z);
        this.metadata = metadata;
        NBTTagCompound nbt = this.getEntityData();
        nbt.setInteger("metadata", metadata);
    }

    public void writeSpawnData(ByteBuf buffer) {
        buffer.writeShort(this.metadata);
    }

    public void readSpawnData(ByteBuf additionalData) {
        this.metadata = additionalData.readShort();
    }

    public void readEntityFromNBT(NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
        NBTTagCompound nbt = this.getEntityData();
        this.metadata = nbt.getInteger("metadata");
    }

    public int getItemMetadata() {
        return this.metadata;
    }
}
