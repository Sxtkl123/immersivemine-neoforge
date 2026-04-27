package com.sxtkl.immersiveminer;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Immersiveminer.MODID)
public class Immersiveminer {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "immersiveminer";

    public static ResourceLocation namespace(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

}
