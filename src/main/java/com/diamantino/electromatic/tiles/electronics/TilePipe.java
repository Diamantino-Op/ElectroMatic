package com.diamantino.electromatic.tiles.electronics;

import com.diamantino.electromatic.registration.EMBlockEntityTypes;
import com.diamantino.electromatic.tiles.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TilePipe extends TileBase {
    public TilePipe(BlockPos pos, BlockState state) {
        super(EMBlockEntityTypes.pipe.get(), pos, state);
    }
}