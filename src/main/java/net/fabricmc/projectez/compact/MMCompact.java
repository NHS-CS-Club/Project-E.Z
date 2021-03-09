package net.fabricmc.projectez.compact;

import net.fabricmc.projectez.SimpleConfig;
import net.fabricmc.projectez.SimpleZoom;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.sargunvohra.mcmods.autoconfig1.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

import java.util.Optional;
import java.util.function.Supplier;

public class MMCompact implements ModMenuApi {

    @Override
    public String getModId() {
        return SimpleZoom.MOD_ID;
    }


    @Override
    public Optional<Supplier<Screen>> getConfigScreen(Screen screen) {
        return Optional.of(AutoConfig.getConfigScreen(SimpleConfig.class, screen));
    }

}
