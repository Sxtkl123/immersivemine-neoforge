package com.sxtkl.immersiveminer.keybinding;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {

    public static final KeyMapping VEIN_KEY = new KeyMapping(
        "key.immersiveminer.vein",
        InputConstants.Type.KEYSYM,
        GLFW.GLFW_KEY_GRAVE_ACCENT,
        KeyMapping.CATEGORY_MISC
    );

}
