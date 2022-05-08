package com.diamantino.electromatic.api.wire.electricity;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

/**
 * @author DiamantinoOp
 */
public class CapabilityElectricDevice {

    public static Capability<IElectricDevice> ELECTRICITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event){
        event.register(IElectricDevice.class);
    }

}