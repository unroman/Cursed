package net.salju.curse.command;

import org.checkerframework.checker.units.qual.s;
import net.salju.curse.procedures.CurseHelpersProcedure;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.RegisterCommandsEvent;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.Commands;

@Mod.EventBusSubscriber
public class CurseCommand {
	@SubscribeEvent
	public static void registerCommand(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("curse").requires(s -> s.hasPermission(4)).then(Commands.argument("curse", EntityArgument.players()).executes(arguments -> {
			Entity target = EntityArgument.getEntity(arguments, "curse");
			if (target instanceof Player player) {
				if (CurseHelpersProcedure.isCursed(player)) {
					CurseHelpersProcedure.setCursed(player, false);
					if (!player.level.isClientSide()) {
						player.displayClientMessage(Component.translatable("gui.curse.cure_message"), (true));
					}
				} else {
					CurseHelpersProcedure.setCursed(player, true);
					if (!player.level.isClientSide()) {
						player.displayClientMessage(Component.translatable("gui.curse.curse_message"), (true));
					}
				}
			}
			return 0;
		})));
	}
}