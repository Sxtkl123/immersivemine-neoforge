package com.sxtkl.immersiveminer.keybinding;


import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class LocalKeyHandler {

    private static LocalKeyHandler INSTANCE;

    private boolean activate;

    private LocalKeyHandler() {
        this.activate = false;
    }

    public static LocalKeyHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LocalKeyHandler();
        }
        return INSTANCE;
    }

    public boolean isActivate() {
        return activate;
    }

    public void setActivate(boolean activate) {
        this.activate = activate;
    }
}
