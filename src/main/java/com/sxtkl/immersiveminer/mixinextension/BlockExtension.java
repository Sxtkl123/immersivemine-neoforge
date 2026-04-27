package com.sxtkl.immersiveminer.mixinextension;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockExtension {

    void playerDestroy$ImmersiveMiner(
        Level level,
        Player player,
        BlockPos pos,
        BlockPos dropPos,
        BlockState state,
        @Nullable BlockEntity blockEntity,
        ItemStack tool
    );

    void dropResources$ImmersiveMiner(
        BlockState state,
        Level level,
        BlockPos pos,
        BlockPos dropPos,
        @Nullable BlockEntity blockEntity,
        @Nullable Entity entity,
        ItemStack tool
    );

}
