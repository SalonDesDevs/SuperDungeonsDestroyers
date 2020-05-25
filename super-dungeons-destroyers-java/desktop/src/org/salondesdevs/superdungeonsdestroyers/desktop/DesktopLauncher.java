package org.salondesdevs.superdungeonsdestroyers.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.sf.cglib.core.Local;
import org.salondesdevs.superdungeonsdestroyers.SuperDungeonsDestroyersClient;
import org.salondesdevs.superdungeonsdestroyers.library.utils.Version;

import java.util.Locale;

public class DesktopLauncher {
    public static void main(String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("SuperDungeonsDestroyers v" + Version.VERSION);
        new Lwjgl3Application(new SuperDungeonsDestroyersClient(), config);
    }
}
