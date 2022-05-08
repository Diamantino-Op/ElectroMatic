package com.diamantino.electromatic.api.wireless;

import com.diamantino.electromatic.api.misc.Accessibility;
import net.minecraft.world.entity.player.Player;

import java.util.List;
import java.util.UUID;

public interface IWirelessManager {

    public List<IFrequency> getFrequencies();

    public List<IRedstoneFrequency> getRedstoneFrequencies();

    public List<IBundledFrequency> getBundledFrequencies();

    public List<IRedstoneFrequency> getAvailableRedstoneFrequencies(Player player);

    public List<IBundledFrequency> getAvailableBundledFrequencies(Player player);

    public IRedstoneFrequency registerRedstoneFrequency(Player owner, String frequency, Accessibility accessibility);

    public IBundledFrequency registerBundledFrequency(Player owner, String frequency, Accessibility accessibility);

    public IFrequency registerFrequency(Player owner, String frequency, Accessibility accessibility, boolean isBundled);

    public IFrequency registerFrequency(IFrequency frequency);

    public void unregisterFrequency(IFrequency frequency);

    public IFrequency getFrequency(Accessibility accessibility, String frequency, UUID owner);

    public void registerWirelessDevice(IWirelessDevice device);

    public void unregisterWirelessDevice(IWirelessDevice device);

}