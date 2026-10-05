package com.mactso.regrowth.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.mactso.regrowth.Main;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

// Plain properties file: config/regrowth.properties (created with defaults on first run).
public class MyConfig {

	public static boolean CANCEL_EVENT = true;
	public static boolean CONTINUE_EVENT = false;

	public static boolean tagsInitialized = false;

	// blocks walls can be built on, separated by semicolons.
	private static final String DEFAULT_WALL_FOUNDATIONS = "minecraft:grass_block;minecraft:sand;minecraft:red_sand;"
			+ "minecraft:netherrack;minecraft:sandstone;minecraft:podzol;minecraft:dirt;minecraft:stone;"
			+ "minecraft:coarse_dirt";

	// mod:mob,type(eat,cut,grow,both,tall,villagerflags),Seconds;
	private static final String DEFAULT_REGROWTH_MOBS = "minecraft:cow,both,300.0;" + "minecraft:horse,eat,180.0;"
			+ "minecraft:donkey,eat,180.0;" + "minecraft:sheep,eat,120.0;" + "minecraft:pig,reforest,450.0;"
			+ "minecraft:bee,grow,500.0;" + "minecraft:chicken,grow,320.0;" + "minecraft:villager,chrwvt,2.0;"
			+ "minecraft:creeper,tall,90.0;" + "minecraft:zombie,stumble, 30.0;" + "minecraft:bat,stumble, 30.0;"
			+ "minecraft:skeleton,mushroom, 40.0;" + "minecraft:tropical_fish,coral, 15.0;"
			+ "minecraft:squid,coral, 15.0;";

	// biome to get biome category, wall size, wall block type
	private static final String DEFAULT_BIOME_WALL_DATA = "Regrowth:default,40,minecraft:cobblestone_wall,minecraft:oak_fence;"
			+ "minecraft:plains,40,minecraft:cobblestone_wall,minecraft:oak_fence;"
			+ "minecraft:desert,40,minecraft:sandstone_wall,minecraft:birch_fence;"
			+ "minecraft:extreme_hills,40,minecraft:cobblestone_wall,minecraft:spruce_fence;"
			+ "minecraft:taiga,40,minecraft:mossy_cobblestone_wall,minecraft:spruce_fence;"
			+ "minecraft:savanna,40,minecraft:stone_brick_wall,minecraft:acacia_fence;"
			+ "minecraft:icy,40,minecraft:diorite_wall,minecraft:spruce_fence;"
			+ "minecraft:the_end,40,minecraft:end_stone_brick_wall,minecraft:birch_fence;"
			+ "minecraft:beach,40,minecraft:sandstone_wall,minecraft:oak_fence;"
			+ "minecraft:forest,40,minecraft:mossy_stone_brick_wall,minecraft:oak_fence;"
			+ "minecraft:mesa,40,minecraft:red_sandstone_wall,minecraft:oak_fence;"
			+ "minecraft:jungle,40,minecraft:granite_wall,minecraft:jungle_fence;"
			+ "minecraft:river,40,minecraft:mossy_cobblestone_wall,minecraft:oak_fence;"
			+ "minecraft:nether,40,minecraft:blackstone_wall,minecraft:nether_brick_fence;"
			+ "Regrowth:minimum,32,regrowth:minimum_wall_size,regrowth:fence_placeholder";

	private static int debugLevel;
	public static double eatingHealsOdds = 0.99;
	public static Block playerWallControlBlock = Blocks.COBBLESTONE_WALL;
	public static Block torchBlock = Blocks.TORCH;

	public static String[] defaultRegrowthMobs;
	public static String defaultRegrowthMobs6464 = DEFAULT_REGROWTH_MOBS;
	public static String[] defaultWallFoundationsArray = DEFAULT_WALL_FOUNDATIONS.split(";");
	public static String[] defaultWallBiomeData;
	public static String defaultWallBiomeData6464 = DEFAULT_BIOME_WALL_DATA;

	private static int torchLightLevel = 3;

	private static int mushroomDensity = 7;
	private static int mushroomXDensity = 6;
	private static int mushroomZDensity = 6;
	private static double mushroomMinTemp = 0.2;
	private static double mushroomMaxTemp = 1.2;

	private static Path configFile;
	private static String playerWallControlBlockString = "minecraft:cobblestone_wall";
	private static String torchBlockString = "minecraft:torch";

	public static int getaDebugLevel() {
		return debugLevel;
	}

	public static int getDebugLevel() {
		return debugLevel;
	}

	public static void setaDebugLevel(int debugLevel) {
		MyConfig.debugLevel = debugLevel;
	}

	public static void setDebugLevel(int debugLevel) {
		MyConfig.debugLevel = debugLevel;
	}

	public static double getEatingHealsOdds() {
		return eatingHealsOdds;
	}

	public static void setEatingHeals(double aEatingHeals) {
		MyConfig.eatingHealsOdds = aEatingHeals;
	}

	public static Block getPlayerWallControlBlock() {
		return playerWallControlBlock;
	}

	public static void setPlayerWallControlBlock(Block playerWallControlBlock) {
		MyConfig.playerWallControlBlock = playerWallControlBlock;
	}

	public static Block getTorchBlock() {
		return torchBlock;
	}

	public static void setTorchBlock(Block torchBlock) {
		MyConfig.torchBlock = torchBlock;
	}

	public static int getMushroomDensity() {
		return MyConfig.mushroomDensity;
	}

	public static int getMushroomXDensity() {
		return MyConfig.mushroomXDensity;
	}

	public static int getMushroomZDensity() {
		return MyConfig.mushroomZDensity;
	}

	public static double getMushroomMinTemp() {
		return MyConfig.mushroomMinTemp;
	}

	public static double getMushroomMaxTemp() {
		return MyConfig.mushroomMaxTemp;
	}

	public static int getTorchLightLevel() {
		return torchLightLevel;
	}

	// call once from the mod initializer.
	public static void init() {
		configFile = FabricLoader.getInstance().getConfigDir().resolve("regrowth.properties");
		Properties p = new Properties();
		if (Files.exists(configFile)) {
			try (var reader = Files.newBufferedReader(configFile)) {
				p.load(reader);
			} catch (IOException e) {
				System.out.println("Regrowth Warn: could not read " + configFile + " : " + e.getMessage());
			}
		}
		bakeConfig(p);
		RegrowthEntitiesManager.regrowthMobInit();
		WallFoundationDataManager.wallFoundationsInit();
		writeConfig();
	}

	private static int getInt(Properties p, String key, int def, int min, int max) {
		try {
			int v = Integer.parseInt(p.getProperty(key, "" + def).trim());
			return Math.max(min, Math.min(max, v));
		} catch (NumberFormatException e) {
			return def;
		}
	}

	private static double getDouble(Properties p, String key, double def, double min, double max) {
		try {
			double v = Double.parseDouble(p.getProperty(key, "" + def).trim());
			return Math.max(min, Math.min(max, v));
		} catch (NumberFormatException e) {
			return def;
		}
	}

	private static Block lookupBlock(String id) {
		try {
			return BuiltInRegistries.BLOCK.getOptional(Identifier.parse(id.trim())).orElse(Blocks.AIR);
		} catch (Exception e) {
			System.out.println("Regrowth Debug:  Block Illegal Config (uPper CaSe?): " + id);
			return Blocks.AIR;
		}
	}

	public static void bakeConfig(Properties p) {
		debugLevel = getInt(p, "debugLevel", 0, 0, 2);
		eatingHealsOdds = getDouble(p, "eatingHeals", 0.99, 0.0, 1.0);
		torchLightLevel = getInt(p, "torchLightLevel", 3, 0, 10);
		mushroomDensity = getInt(p, "mushroomDensity", 7, 3, 21);
		mushroomXDensity = getInt(p, "mushroomXDensity", 6, 3, 11);
		mushroomZDensity = getInt(p, "mushroomZDensity", 6, 3, 11);
		mushroomMinTemp = getDouble(p, "mushroomMinTemp", 0.2, -2.0, 2.0);
		mushroomMaxTemp = getDouble(p, "mushroomMaxTemp", 1.2, -2.0, 2.0);

		defaultRegrowthMobs6464 = p.getProperty("regrowthMobs", DEFAULT_REGROWTH_MOBS);
		defaultWallBiomeData6464 = p.getProperty("biomeWallData", DEFAULT_BIOME_WALL_DATA);

		List<String> foundations = new ArrayList<>();
		for (String s : p.getProperty("wallFoundations", DEFAULT_WALL_FOUNDATIONS).split(";")) {
			if (!s.trim().isEmpty())
				foundations.add(s.trim());
		}
		defaultWallFoundationsArray = foundations.toArray(new String[0]);

		playerWallControlBlockString = p.getProperty("playerWallControlBlockString", "minecraft:cobblestone_wall");
		torchBlockString = p.getProperty("torchBlockString", "minecraft:torch");
		playerWallControlBlock = lookupBlock(playerWallControlBlockString);
		torchBlock = lookupBlock(torchBlockString);
		if (playerWallControlBlock == Blocks.AIR) {
			System.out.println("Regrowth Warn:  Player Wall Control Block is : " + playerWallControlBlockString);
		}
		if (debugLevel > 0) {
			System.out.println("Regrowth Debug Level: " + debugLevel);
		}
	}

	private static void writeConfig() {
		StringBuilder sb = new StringBuilder();
		sb.append("# Regrowth (").append(Main.MODID).append(") configuration.  Delete a line to restore its default.\n");
		sb.append("# Debug Level: 0 = Off, 1 = Log, 2 = Chat+Log  (0-2)\n");
		sb.append("debugLevel=").append(debugLevel).append("\n");
		sb.append("# Eating Heals: 0-No, 1-yes  (0.0-1.0)\n");
		sb.append("eatingHeals=").append(eatingHealsOdds).append("\n");
		sb.append("# Villagers will only place torches on blocks this dark or darker.  (0-10)\n");
		sb.append("torchLightLevel=").append(torchLightLevel).append("\n");
		sb.append("# Mushroom density - 3 dense to 11 sparse to 21 very sparse  (3-21)\n");
		sb.append("mushroomDensity=").append(mushroomDensity).append("\n");
		sb.append("# Mushroom X axis density - 3 dense to 11 sparse\n");
		sb.append("mushroomXDensity=").append(mushroomXDensity).append("\n");
		sb.append("# Mushroom Z axis density - 3 dense to 11 sparse\n");
		sb.append("mushroomZDensity=").append(mushroomZDensity).append("\n");
		sb.append("# Mushroom minimum / maximum biome temperature  (-2.0 to 2.0)\n");
		sb.append("mushroomMinTemp=").append(mushroomMinTemp).append("\n");
		sb.append("mushroomMaxTemp=").append(mushroomMaxTemp).append("\n");
		sb.append("# When this block is over a bell, villagers build walls.  If it is 'minecraft:air' players can't turn off wall building.\n");
		sb.append("playerWallControlBlockString=").append(playerWallControlBlockString).append("\n");
		sb.append("# The torch the villagers place.  It can be a modded torch.\n");
		sb.append("torchBlockString=").append(torchBlockString).append("\n");
		sb.append("# Blocks villagers can build walls on, separated by semicolons.\n");
		sb.append("wallFoundations=").append(String.join(";", defaultWallFoundationsArray)).append("\n");
		sb.append("# mod:mob,action(eat,cut,grow,both,tall,mushroom,stumble,reforest,coral,villager flags),seconds;\n");
		sb.append("regrowthMobs=").append(defaultRegrowthMobs6464).append("\n");
		sb.append("# biome,wall diameter,wall block,fence block;\n");
		sb.append("biomeWallData=").append(defaultWallBiomeData6464).append("\n");
		try {
			Files.createDirectories(configFile.getParent());
			Files.writeString(configFile, sb.toString());
		} catch (IOException e) {
			System.out.println("Regrowth Warn: could not write " + configFile + " : " + e.getMessage());
		}
	}
}
