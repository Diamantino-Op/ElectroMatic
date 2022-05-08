package com.diamantino.electromatic.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public abstract class LocationCache<CachedType> {
    private final CachedType[] cachedValue;

    public LocationCache(Level world, BlockPos pos, Object... extraArgs) {

        if (world == null)
            throw new NullPointerException("World can't be null!");
        cachedValue = (CachedType[]) new Object[6];
        for (Direction d : Direction.values()) {
            cachedValue[d.ordinal()] = getNewValue(world, pos.relative(d), extraArgs);
        }
    }

    protected abstract CachedType getNewValue(Level world, BlockPos pos, Object... extraArgs);

    public CachedType getValue(Direction side) {
        return cachedValue[side.ordinal()];
    }
}