package com.sxtkl.immersiveminer.linkage.jade;

import com.sxtkl.immersiveminer.utils.MineUtils;
import com.sxtkl.immersiveminer.keybinding.LocalKeyHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;

public enum UltimineComponentProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor,
                              IPluginConfig config) {
        if (!LocalKeyHandler.getInstance().isActivate()) return;
        Level level = accessor.getLevel();
        BlockPos pos = accessor.getPosition();
        int cnt = MineUtils.getUltimineSize(level, pos);
        if (cnt != -1) tooltip.add(Component.translatable("jade.immersiveminer.size", cnt));
        else tooltip.add(Component.translatable("jade.immersiveminer.too_large"));
    }

    @Override
    public ResourceLocation getUid() {
        return UltiminePlugin.ULTIMINE;
    }

}
