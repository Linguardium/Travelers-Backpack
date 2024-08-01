package com.tiviacz.travelersbackpack.capability.entity;

import com.tiviacz.travelersbackpack.TravelersBackpack;
import com.tiviacz.travelersbackpack.capability.CapabilityUtils;
import com.tiviacz.travelersbackpack.network.ClientboundSyncCapabilityPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.network.PacketDistributor;

public class TravelersBackpackEntityWearable implements IEntityTravelersBackpack
{
    private ItemStack wearable = new ItemStack(Items.AIR, 0);
    private final LivingEntity livingEntity;

    public TravelersBackpackEntityWearable(final LivingEntity livingEntity)
    {
        this.livingEntity = livingEntity;
    }

    @Override
    public boolean hasWearable()
    {
        return !this.wearable.isEmpty();
    }

    @Override
    public ItemStack getWearable()
    {
        return this.wearable;
    }

    @Override
    public void setWearable(ItemStack stack)
    {
        this.wearable = stack;
    }

    @Override
    public void removeWearable()
    {
        this.wearable = new ItemStack(Items.AIR, 0);
    }

    @Override
    public void synchronise()
    {
        if(livingEntity != null && !livingEntity.level().isClientSide)
        {
            CapabilityUtils.getEntityCapability(livingEntity).ifPresent(cap -> TravelersBackpack.NETWORK.send(new ClientboundSyncCapabilityPacket(livingEntity.getId(), false, this.wearable), PacketDistributor.TRACKING_ENTITY.with(livingEntity)));
        }
    }

    @Override
    public CompoundTag saveTag()
    {
        CompoundTag compound = new CompoundTag();

        if(hasWearable())
        {
            ItemStack wearable = getWearable();
            wearable.save(livingEntity.registryAccess(), compound);
        }
        return compound;
    }

    @Override
    public void loadTag(CompoundTag compoundTag)
    {
        ItemStack wearable = ItemStack.parseOptional(livingEntity.registryAccess(), compoundTag);
        setWearable(wearable);
    }
}
