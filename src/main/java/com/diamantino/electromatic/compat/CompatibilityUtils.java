package com.diamantino.electromatic.compat;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

import java.util.*;

public class CompatibilityUtils {

    private static Map<String, CompatModule> modules = new HashMap<String, CompatModule>();

    private CompatibilityUtils() {

    }

    public static void registerModule(String modid, Class<? extends CompatModule> module, Class<? extends CompatModule> alternate) {

        registerModule(modid, module.getName(), alternate == null ? null : alternate.getName());
    }

    public static void registerModule(String modid, String module, String alternate) {

        if (modid == null || modid.trim().isEmpty())
            return;
        if (module == null)
            return;
        if (modules.containsKey(module))
            return;

        if ( ModList.get().isLoaded(modid)) {
            try {
                Class<?> c = Class.forName(module);
                if (!CompatModule.class.isAssignableFrom(c))
                    return;
                modules.put(modid, (CompatModule) c.newInstance());
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (alternate == null || alternate.trim().isEmpty())
            return;

        try {
            Class<?> c2 = Class.forName(alternate);
            if (!CompatModule.class.isAssignableFrom(c2))
                return;
            modules.put(modid, (CompatModule) c2.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<CompatModule> getLoadedModules() {

        return Collections.unmodifiableList(new ArrayList<CompatModule>(modules.values()));
    }

    public static CompatModule getModule(String modid) {

        try {
            return modules.get(modid);
        } catch (Exception ex) {
        }

        return null;
    }

    public static void init(FMLCommonSetupEvent ev) {
        for (CompatModule m : getLoadedModules())
            m.init(ev);
    }

    public static void postInit(FMLLoadCompleteEvent ev) {

        for (CompatModule m : getLoadedModules())
            m.postInit(ev);
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerRenders() {

        for (CompatModule m : getLoadedModules())
            m.registerRenders();
    }

    public static boolean isScrewdriver(ItemStack item) {

        for (CompatModule m : getLoadedModules())
            if (m.isScrewdriver(item))
                return true;
        return false;
    }

    /**
     * Register your modules here
     */
    static {
        //registerModule(Dependencies.COMPUTER_CRAFT, CompatModuleCC.class, null);
        //registerModule(Dependencies.MCMP, CompatModuleMCMP.class, null);
        //registerModule(Dependencies.WAILA, CompatModuleWaila.class, null);
        //registerModule(Dependencies.IC2, CompatModuleIC2.class, null);
        //registerModule(Dependencies.HC, CompatModuleHydCraft.class, null);
    }

}