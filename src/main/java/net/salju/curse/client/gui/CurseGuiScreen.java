package net.salju.curse.client.gui;

import net.salju.curse.world.inventory.CurseGuiMenu;
import net.salju.curse.network.CurseGuiButtonMessage;
import net.salju.curse.init.CurseModConfig;
import net.salju.curse.CurseMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class CurseGuiScreen extends AbstractContainerScreen<CurseGuiMenu> {
	private final static HashMap<String, Object> guistate = CurseGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_yes;
	Button button_no;

	public CurseGuiScreen(CurseGuiMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 193;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("curse:textures/screens/curse_gui.png");

	@Override
	public void render(GuiGraphics ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics ms, float partialTicks, int gx, int gy) {
		ms.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	@Override
	protected void renderLabels(GuiGraphics ms, int mouseX, int mouseY) {
		ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_title"), 30, 8, 4210752, false);
		if ((CurseModConfig.WEAK.get() < 1.0))
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_enemy_damage"), 6, 20, 4210752, false);
		if (CurseModConfig.DEATH.get() > 1.0)
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_player_damage"), 6, 32, 4210752, false);
		if (CurseModConfig.KNOCK.get() > 1.0)
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_player_knockback"), 6, 44, 4210752, false);
		if (CurseModConfig.FIRE.get() == true)
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_fire"), 6, 56, 4210752, false);
		if (CurseModConfig.ANGRY.get() == true)
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_neutrals"), 6, 68, 4210752, false);
		if (CurseModConfig.SLEEP.get() == true)
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_sleep"), 6, 80, 4210752, false);
		if (CurseModConfig.EXP.get() > 1.0)
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_exp"), 6, 98, 4210752, false);
		if (CurseModConfig.DROPS.get() == true)
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_loot"), 6, 110, 4210752, false);
		if (CurseModConfig.ORE.get() == true)
			ms.drawString(this.font, Component.translatable("gui.curse.curse_gui.label_ores"), 6, 122, 4210752, false);
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
		button_yes = Button.builder(Component.translatable("gui.curse.curse_gui.button_yes"), e -> {
			if (true) {
				CurseMod.PACKET_HANDLER.sendToServer(new CurseGuiButtonMessage(0, x, y, z));
				CurseGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 102, this.topPos + 140, 40, 20).build();
		guistate.put("button:button_yes", button_yes);
		this.addRenderableWidget(button_yes);
		button_no = Button.builder(Component.translatable("gui.curse.curse_gui.button_no"), e -> {
			if (true) {
				CurseMod.PACKET_HANDLER.sendToServer(new CurseGuiButtonMessage(1, x, y, z));
				CurseGuiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}).bounds(this.leftPos + 144, this.topPos + 140, 35, 20).build();
		guistate.put("button:button_no", button_no);
		this.addRenderableWidget(button_no);
	}
}
