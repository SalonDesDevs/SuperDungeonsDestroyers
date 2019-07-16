package org.salondesdevs.superdungeonsdestroyers.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import org.salondesdevs.superdungeonsdestroyers.SuperDungeonsDestroyers;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new SuperDungeonsDestroyers(), config);
    }
}
