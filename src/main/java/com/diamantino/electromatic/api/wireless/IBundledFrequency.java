package com.diamantino.electromatic.api.wireless;

public interface IBundledFrequency extends IFrequency {

    public byte[] getSignal();

    public void setSignal(byte[] signal);

}