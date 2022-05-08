package com.diamantino.electromatic.api.wireless;

import com.diamantino.electromatic.api.misc.Accessibility;

import java.util.UUID;

public interface IFrequency {

    public Accessibility getAccessibility();

    public UUID getOwner();

    public String getFrequencyName();

    public void notifyClients();

}