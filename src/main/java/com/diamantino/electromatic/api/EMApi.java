package com.diamantino.electromatic.api;

import com.diamantino.electromatic.api.pipe.IVacuumPipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fml.ModList;

/**
 * This is then main hub where you can interface with ElectroMatic as a modder. Note that the 'instance' in this class will be filled in ElectroMatic's
 * preInit. This means this class is save to use from the init phase.
 *
 * @author DiamantinoOp
 */
public class EMApi {

    private static IEMApi instance;

    public static IEMApi getInstance() {

        return instance;
    }

    public static interface IEMApi {

        //public IVacuumPipe getVacuumPipe(BlockEntity te);

        //public AlloyFurnaceRegistry getAlloyFurnaceRegistry();

        /**
         * Should be called by an Block#onBlockAdded that implements ISilkyRemovable. It will get the BlockEntity and load the tag "tileData" stored in
         * the supplied itemstack.
         *
         * @param world
         * @param pos
         * @param stack
         */
        public void loadSilkySettings(Level world, BlockPos pos, ItemStack stack);

    }

    /**
     * For internal use only, don't call it.
     *
     * @param inst
     */
    public static void init(IEMApi inst) {

        if (instance == null && ModList.get().isLoaded("electromatic")) {
            instance = inst;
        } else {
            throw new IllegalStateException("This method should be called from ElectroMatic only!");
        }
    }
}