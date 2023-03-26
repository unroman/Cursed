package net.salju.curse.procedures;

import net.salju.curse.network.CurseModVariables;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.LivingEntity;

public class CurseHelpersProcedure {
	public static boolean isCursed(LivingEntity target) {
		return ((target.getCapability(CurseModVariables.PLAYER_VARIABLES_CAPABILITY, null).orElse(new CurseModVariables.PlayerVariables())).Cursed);
	}

	public static void setCursed(Player target, boolean curse) {
		target.getCapability(CurseModVariables.PLAYER_VARIABLES_CAPABILITY, null).ifPresent(capability -> {
			capability.Cursed = curse;
			capability.syncPlayerVariables(target);
		});
	}
}