package com.sxtkl.immersiveminer.mixin;

import com.sxtkl.immersiveminer.keybinding.LocalKeyHandler;
import com.sxtkl.immersiveminer.utils.MineUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GameMasterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MultiPlayerGameMode.class)
public abstract class MultiPlayerGameModeMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    private GameType localPlayerMode;

    @Inject(method = "destroyBlock", at = @At("HEAD"), cancellable = true)
    public void inject$destroyBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        // 这里暂时直接拦截了整个方法的执行，一旦玩家处于连锁状态，会直接劫持原有逻辑。
        // 这样做可能并不是很优雅，具体是否需要修改需要考量和其他模组是否会产生严重冲突。
        if (!LocalKeyHandler.getInstance().isActivate()) return;
        if (this.minecraft.player == null) return;
        if (this.minecraft.level == null) return;

        cir.cancel();
        BlockPos originalPos = pos;
        pos = MineUtils.getSafeBlock(this.minecraft.level, originalPos);
        if (this.minecraft.player.blockActionRestricted(this.minecraft.level, pos, this.localPlayerMode)) {
            cir.setReturnValue(false);
        } else {
            Level level = this.minecraft.level;
            BlockState blockstate = level.getBlockState(pos);
            if (!this.minecraft.player.getMainHandItem().getItem().canAttackBlock(blockstate, level, pos, this.minecraft.player)) {
                cir.setReturnValue(false);
            } else {
                Block block = blockstate.getBlock();
                if (block instanceof GameMasterBlock && !this.minecraft.player.canUseGameMasterBlocks()) {
                    cir.setReturnValue(false);
                } else if (blockstate.isAir()) {
                    cir.setReturnValue(false);
                } else {
                    BlockState removedBlockState =
                        block.playerWillDestroy(level, pos, blockstate, this.minecraft.player);
                    FluidState fluidstate = level.getFluidState(pos);
                    boolean flag = blockstate.onDestroyedByPlayer(level, pos, minecraft.player, false, fluidstate);
                    if (flag) {
                        block.destroy(level, pos, removedBlockState);
                    }

                    cir.setReturnValue(flag);
                }
            }
        }
    }

}
