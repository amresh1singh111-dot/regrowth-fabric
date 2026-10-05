package com.mactso.regrowth;

import com.mactso.regrowth.commands.RegrowthCommands;
import com.mactso.regrowth.config.MyConfig;
import com.mactso.regrowth.utility.Utility;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class Main implements ModInitializer {

	public static final String MODID = "regrowth";

	@Override
	public void onInitialize() {
		MyConfig.init();
		CommandRegistrationCallback.EVENT
				.register((dispatcher, registryAccess, environment) -> RegrowthCommands.register(dispatcher));
		Utility.debugMsg(0, MODID + ": Registering Mod.");
	}
}
