package org.salondesdevs.superdungeonsdestroyers.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import org.salondesdevs.superdungeonsdestroyers.SuperDungeonsDestroyersClient;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("SuperDungeonsDestroyers v" + SuperDungeonsDestroyersClient.VERSION);
        new Lwjgl3Application(new SuperDungeonsDestroyersClient(), config);
    }
}
