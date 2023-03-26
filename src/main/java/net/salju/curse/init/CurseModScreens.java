
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package net.salju.curse.init;

import net.salju.curse.client.gui.CurseGuiScreen;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class CurseModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(CurseModMenus.CURSE_GUI.get(), CurseGuiScreen::new);
		});
	}
}
