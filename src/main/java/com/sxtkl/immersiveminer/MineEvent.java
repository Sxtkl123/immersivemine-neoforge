package com.sxtkl.immersiveminer;

import com.sxtkl.immersiveminer.keybinding.KeyHandler;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockEvent;

@EventBusSubscriber(modid = Immersiveminer.MODID)
public class MineEvent {

    private static final Set<UUID> PROCESSING_PLAYERS = ConcurrentHashMap.newKeySet();

    @SubscribeEvent
    public static void onMineBlock(BlockEvent.BreakEvent evt) {
        Player player = evt.getPlayer();
        if (!KeyHandler.getInstance().isActivate(player)) return;

        UUID pid = player.getUUID();
        if (PROCESSING_PLAYERS.contains(pid)) return;
        BlockPos target = MineUtils.getSafeBlock(player.level(), evt.getPos());
        if (target.equals(evt.getPos())) return;

        evt.setCanceled(true);
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        PROCESSING_PLAYERS.add(pid);
        try {
            serverPlayer.gameMode.destroyBlock(target);
        } finally {
            PROCESSING_PLAYERS.remove(pid);
        }
    }

}
