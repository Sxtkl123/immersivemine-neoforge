package com.sxtkl.immersiveminer.keybinding;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.OnlyIn;


public class KeyHandler {

    private static KeyHandler INSTANCE;

    private final Set<UUID> activatePlayers;

    private KeyHandler() {
        this.activatePlayers = ConcurrentHashMap.newKeySet();
    }

    public void activate(Player player) {
        this.activate(player.getUUID());
    }

    public void activate(UUID uuid) {
        this.activatePlayers.add(uuid);
    }

    public void inactivate(Player player) {
        this.inactivate(player.getUUID());
    }

    public void inactivate(UUID uuid) {
        this.activatePlayers.remove(uuid);
    }

    public boolean isActivate(Player player) {
        return this.isActivate(player.getUUID());
    }

    public boolean isActivate(UUID uuid) {
        return this.activatePlayers.contains(uuid);
    }

    public static KeyHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new KeyHandler();
        }
        return INSTANCE;
    }
}
