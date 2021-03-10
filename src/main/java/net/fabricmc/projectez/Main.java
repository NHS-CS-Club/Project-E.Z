package net.fabricmc.projectez;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.projectez.mods.*;
import net.fabricmc.projectez.util.ArrayListSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Set;

public class Main implements ModInitializer {

	public static final String MOD_ID = "projectez";
	public static final String MOD_NAME = "Project E.Z.";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	public static final Set<Mod> mods = new ArrayListSet<>();

	@Override
	public void onInitialize() {
		LOGGER.info("INIT "+MOD_NAME);

		mods.add(new LightLevelDisplayMod());
		mods.add(new PotionHUDMod());
		mods.add(new FullBrightMod());
		mods.add(new SimpleZoomMod());

		for (Mod mod : mods) mod.init();
		for (Mod mod : mods) mod.setEnabled(true);
	}
}
