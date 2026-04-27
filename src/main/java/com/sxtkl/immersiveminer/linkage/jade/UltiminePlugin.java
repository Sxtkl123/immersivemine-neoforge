package com.sxtkl.immersiveminer.linkage.jade;

import com.sxtkl.immersiveminer.Immersiveminer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class UltiminePlugin implements IWailaPlugin {

    public static final ResourceLocation ULTIMINE = Immersiveminer.namespace("ultimine");

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(UltimineComponentProvider.INSTANCE, Block.class);
    }
}
