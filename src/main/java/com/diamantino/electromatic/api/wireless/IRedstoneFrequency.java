package com.diamantino.electromatic.api.wireless;

public interface IRedstoneFrequency extends IFrequency {

    public byte getSignal();

    public void setSignal(byte signal);

}