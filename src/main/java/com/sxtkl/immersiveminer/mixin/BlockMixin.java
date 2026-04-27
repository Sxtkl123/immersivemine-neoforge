package com.sxtkl.immersiveminer.mixin;


import com.sxtkl.immersiveminer.mixinextension.BlockExtension;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.extensions.IBlockExtension;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Block.class)
public abstract class BlockMixin extends BlockBehaviour implements ItemLike, IBlockExtension,
    BlockExtension {

    public BlockMixin(Properties properties) {
        super(properties);
    }

    @Shadow
    private static void beginCapturingDrops() {}

    @Shadow
    public static void popResource(Level level, BlockPos pos, ItemStack stack) {}

    @Shadow
    public static List<ItemStack> getDrops(BlockState state, ServerLevel level, BlockPos pos, @javax.annotation.Nullable BlockEntity blockEntity, @javax.annotation.Nullable Entity entity, ItemStack tool) {
        return List.of();
    }

    @Shadow
    private static List<ItemEntity> stopCapturingDrops() {
        return List.of();
    }

    @Unique
    @Override
    public void playerDestroy$ImmersiveMiner(Level level, Player player, BlockPos pos,
                                             BlockPos dropPos, BlockState state,
                                             @Nullable BlockEntity blockEntity, ItemStack tool) {
        player.awardStat(Stats.BLOCK_MINED.get(Block.class.cast(this)));
        player.causeFoodExhaustion(0.005F);
        this.dropResources$ImmersiveMiner(state, level, pos, dropPos, blockEntity, player, tool);
    }

    @Unique
    @Override
    public void dropResources$ImmersiveMiner(BlockState state, Level level, BlockPos pos,
                                             BlockPos dropPos, @Nullable BlockEntity blockEntity,
                                             @Nullable Entity entity, ItemStack tool) {
        if (!(level instanceof ServerLevel)) return;
        beginCapturingDrops();
        getDrops(state, (ServerLevel)level, pos, blockEntity, entity, tool).forEach((p_49944_) -> popResource(level, dropPos, p_49944_));
        List<ItemEntity> captured = stopCapturingDrops();
        CommonHooks.handleBlockDrops((ServerLevel)level, pos, state, blockEntity, captured, entity, tool);
    }
}
