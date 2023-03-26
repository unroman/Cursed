package net.salju.curse.network;

import net.salju.curse.world.inventory.CurseGuiMenu;
import net.salju.curse.procedures.CurseHelpersProcedure;
import net.salju.curse.CurseMod;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import java.util.function.Supplier;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CurseGuiButtonMessage {
	private final int buttonID, x, y, z;

	public CurseGuiButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public CurseGuiButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(CurseGuiButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(CurseGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Player entity = context.getSender();
			int buttonID = message.buttonID;
			int x = message.x;
			int y = message.y;
			int z = message.z;
			handleButtonAction(entity, buttonID, x, y, z);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(Player player, int buttonID, int x, int y, int z) {
		Level world = player.level;
		HashMap guistate = CurseGuiMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {
			CurseHelpersProcedure.setCursed(player, true);
			if (!player.level.isClientSide()) {
				player.displayClientMessage(Component.literal("You have been cursed"), (true));
			}
			player.closeContainer();
		}
		if (buttonID == 1) {
			player.closeContainer();
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		CurseMod.addNetworkMessage(CurseGuiButtonMessage.class, CurseGuiButtonMessage::buffer, CurseGuiButtonMessage::new, CurseGuiButtonMessage::handler);
	}
}