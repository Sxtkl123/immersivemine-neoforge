package com.sxtkl.immersiveminer.mixin;

import com.sxtkl.immersiveminer.utils.MineUtils;
import com.sxtkl.immersiveminer.keybinding.KeyHandler;
import com.sxtkl.immersiveminer.mixinextension.BlockExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {

    @Shadow
    private GameType gameModeForPlayer;

    @Shadow
    @Final
    protected ServerPlayer player;

    @Shadow
    protected ServerLevel level;

    @Shadow
    public abstract boolean isCreative();

    @Shadow
    protected abstract boolean removeBlock(BlockPos pos, BlockState state, boolean canHarvest);

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    public void inject$destroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        // 这里暂时直接拦截了整个方法的执行，一旦玩家处于连锁状态，会直接劫持原有逻辑。
        // 这样做可能并不是很优雅，具体是否需要修改需要考量和其他模组是否会产生严重冲突。
        if (!KeyHandler.getInstance().isActivate(player)) return;

        cir.cancel();
        BlockState blockstate1 = this.level.getBlockState(pos);
        BlockPos originalPos = pos;
        pos = MineUtils.getSafeBlock(level, originalPos);
        var event = net.neoforged.neoforge.common.CommonHooks.fireBlockBreak(level, gameModeForPlayer, player, pos, blockstate1);
        if (event.isCanceled()) {
            cir.setReturnValue(false);
        } else {
            BlockEntity blockentity = this.level.getBlockEntity(pos);
            Block block = blockstate1.getBlock();
            if (block instanceof GameMasterBlock && !this.player.canUseGameMasterBlocks()) {
                this.level.sendBlockUpdated(pos, blockstate1, blockstate1, 3);
                cir.setReturnValue(false);
            } else if (this.player.blockActionRestricted(this.level, pos, this.gameModeForPlayer)) {
                cir.setReturnValue(false);
            } else {
                BlockState blockstate = block.playerWillDestroy(this.level, pos, blockstate1, this.player);

                if (this.isCreative()) {
                    removeBlock(pos, blockstate, false);
                    cir.setReturnValue(true);
                } else {
                    ItemStack itemstack = this.player.getMainHandItem();
                    ItemStack itemstack1 = itemstack.copy();
                    boolean flag1 = blockstate.canHarvestBlock(this.level, pos, this.player);
                    itemstack.mineBlock(this.level, blockstate, pos, this.player);
                    boolean flag = removeBlock(pos, blockstate, flag1);

                    if (flag1 && flag) {
                        ((BlockExtension) block).playerDestroy$ImmersiveMiner(this.level, this.player, pos, originalPos, blockstate, blockentity, itemstack1);
                    }

                    if (itemstack.isEmpty() && !itemstack1.isEmpty()) {
                        net.neoforged.neoforge.event.EventHooks.onPlayerDestroyItem(this.player, itemstack1, InteractionHand.MAIN_HAND);
                    }

                    cir.setReturnValue(true);
                }
            }
        }
    }

}
