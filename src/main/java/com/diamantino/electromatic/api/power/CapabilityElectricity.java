package com.diamantino.electromatic.api.power;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

/**
 * @author DiamantinoOp
 */
public class CapabilityElectricity {

    public static Capability<IPowerBase> ELECTRICITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IPowerBase.class);
    }

    public static Tag writeNBT(Capability<IPowerBase> capability, IPowerBase instance, Direction direction) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("electricity", instance.getEnergy());
        return nbt;
    }

    public static void readNBT(Capability<IPowerBase> capability, IPowerBase instance, Direction side, Tag nbt) {
        CompoundTag tags = (CompoundTag) nbt;
        double energy = tags.getDouble("electricity");
        instance.addEnergy(-(instance.getEnergy() - energy), false);
    }

}