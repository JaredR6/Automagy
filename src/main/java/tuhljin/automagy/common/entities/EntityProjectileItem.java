package tuhljin.automagy.common.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nonnull;

public abstract class EntityProjectileItem extends EntityThrowable implements IEntityAdditionalSpawnData, IItemMetadata {
    public int metadata = 0;

    public EntityProjectileItem(@Nonnull World worldIn) {
        super(worldIn);
    }

    public EntityProjectileItem(@Nonnull World worldIn, @Nonnull EntityLivingBase throwerIn, int metadata) {
        super(worldIn, throwerIn);
        this.metadata = metadata;
        NBTTagCompound nbt = this.getEntityData();
        nbt.setInteger("metadata", metadata);
    }

    public EntityProjectileItem(@Nonnull World worldIn, double x, double y, double z, int metadata) {
        super(worldIn, x, y, z);
        this.metadata = metadata;
        NBTTagCompound nbt = this.getEntityData();
        nbt.setInteger("metadata", metadata);
    }

    public void writeSpawnData(@Nonnull ByteBuf buffer) {
        buffer.writeShort(this.metadata);
    }

    public void readSpawnData(@Nonnull ByteBuf additionalData) {
        this.metadata = additionalData.readShort();
    }

    public void readEntityFromNBT(@Nonnull NBTTagCompound tagCompound) {
        super.readEntityFromNBT(tagCompound);
        NBTTagCompound nbt = this.getEntityData();
        this.metadata = nbt.getInteger("metadata");
    }

    public int getItemMetadata() {
        return this.metadata;
    }
}
