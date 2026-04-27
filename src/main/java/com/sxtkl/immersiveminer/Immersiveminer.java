package com.sxtkl.immersiveminer;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(Immersiveminer.MODID)
public class Immersiveminer {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "immersiveminer";

    public static ResourceLocation namespace(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

}
