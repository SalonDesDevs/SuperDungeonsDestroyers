package org.salondesdevs.superdungeonsdestroyers.systems.common.ui.screens;

import com.badlogic.gdx.utils.I18NBundle;
import net.wytrem.ecs.Service;
import org.salondesdevs.superdungeonsdestroyers.systems.common.Assets;

import javax.inject.Inject;

public class I18NService extends Service {
    @Inject
    Assets assets;

    @Override
    public void initialize() {
    }

    public String get(String key) {
        return assets.i18n.get(key);
    }

    public String format(String key, Object... args) {
        return assets.i18n.format(key, args);
    }
}
