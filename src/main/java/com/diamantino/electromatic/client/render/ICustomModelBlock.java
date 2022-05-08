package com.diamantino.electromatic.client.render;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author DiamantinoOp
 */
public interface ICustomModelBlock {

    @OnlyIn(Dist.CLIENT)
    void initModel();

}