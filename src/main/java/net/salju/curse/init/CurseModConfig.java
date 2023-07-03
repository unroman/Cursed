package net.salju.curse.init;

import net.minecraftforge.common.ForgeConfigSpec;

public class CurseModConfig {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;

	public static final ForgeConfigSpec.DoubleValue DEATH;
	public static final ForgeConfigSpec.DoubleValue WEAK;
	public static final ForgeConfigSpec.DoubleValue KNOCK;
	public static final ForgeConfigSpec.BooleanValue FIRE;
	public static final ForgeConfigSpec.BooleanValue ANGRY;
	public static final ForgeConfigSpec.BooleanValue SLEEP;

	public static final ForgeConfigSpec.DoubleValue EXP;
	public static final ForgeConfigSpec.BooleanValue DROPS;
	public static final ForgeConfigSpec.BooleanValue ORE;
	public static final ForgeConfigSpec.BooleanValue LOOT;
	
	static {
		BUILDER.push("Curses");
		DEATH = BUILDER.comment("How much damage the player takes from everything.").defineInRange("Player Damage", 2.0, 1.0, 76.0);
		KNOCK = BUILDER.comment("How much knockback the player takes from everything.").defineInRange("Player Knockback", 2.0, 1.0, 76.0);
		WEAK = BUILDER.comment("How much damage the player does against everything.").defineInRange("Hard Difficulty Damage", 0.5, 0.0, 1.0);
		FIRE = BUILDER.comment("Should fire last forever on the player until doused manually?").define("Player Burns Burns Burns", true);
		ANGRY = BUILDER.comment("Should neutral mobs be hostile to the player?").define("Player Hostile Neutrals", true);
		SLEEP = BUILDER.comment("Should the player be unable go to sleep?").define("Player Sleepless", true);
		BUILDER.pop();
		BUILDER.push("Benefits");
		EXP = BUILDER.comment("How much experience the player gains from defeating enemies.").defineInRange("Player Experience", 3.0, 1.0, 76.0);
		DROPS = BUILDER.comment("Should the player get extra drops from defeating enemies?").define("Player Extra Drops", true);
		ORE = BUILDER.comment("Should the player have extra ore drops?").define("Player Extra Ore Drops", true);
		LOOT = BUILDER.comment("Should the player have an extra level of Looting?").define("Player Looting +1", true);
		BUILDER.pop();
		CONFIG = BUILDER.build();
	}
}