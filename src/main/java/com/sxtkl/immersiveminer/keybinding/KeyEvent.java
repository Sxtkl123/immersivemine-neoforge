package com.sxtkl.immersiveminer.keybinding;

import com.sxtkl.immersiveminer.Immersiveminer;
import com.sxtkl.immersiveminer.network.KeyData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = Immersiveminer.MODID)
public class KeyEvent {

    @SubscribeEvent
    public static void registerNetwork(RegisterPayloadHandlersEvent evt) {
        final PayloadRegistrar registrar = evt.registrar("1").executesOn(HandlerThread.NETWORK);
        registrar.playBidirectional(KeyData.TYPE, KeyData.STREAM_CODEC, (data, context) -> {
            if (data.activate()) {
                KeyHandler.getInstance().activate(context.player());
            } else {
                KeyHandler.getInstance().inactivate(context.player());
            }
        });
    }

    @SubscribeEvent
    public static void registerKey(RegisterKeyMappingsEvent evt) {
        evt.register(KeyBinding.VEIN_KEY);
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key evt) {
        if (evt.getKey() != KeyBinding.VEIN_KEY.getKey().getValue()) return;

        int action = evt.getAction();
        if (action == GLFW.GLFW_PRESS) {
            PacketDistributor.sendToServer(new KeyData(true));
            LocalKeyHandler.getInstance().setActivate(true);
        } else if (action == GLFW.GLFW_RELEASE) {
            PacketDistributor.sendToServer(new KeyData(false));
            LocalKeyHandler.getInstance().setActivate(false);
        }
    }

    @SubscribeEvent
    public static void onPlayerExit(PlayerEvent.PlayerLoggedOutEvent evt) {
        LocalKeyHandler.getInstance().setActivate(false);
        KeyHandler.getInstance().inactivate(evt.getEntity());
    }
}
