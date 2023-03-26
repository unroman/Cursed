package net.salju.curse.client.gui;

import net.salju.curse.world.inventory.CurseGuiMenu;
import net.salju.curse.network.CurseGuiButtonMessage;
import net.salju.curse.CurseMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.Minecraft;

import java.util.HashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;

public class CurseGuiScreen extends AbstractContainerScreen<CurseGuiMenu> {
	private final static HashMap<String, Object> guistate = CurseGuiMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;

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
	public void render(PoseStack ms, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(ms);
		super.render(ms, mouseX, mouseY, partialTicks);
		this.renderTooltip(ms, mouseX, mouseY);
	}

	@Override
	protected void renderBg(PoseStack ms, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		RenderSystem.setShaderTexture(0, texture);
		this.blit(ms, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
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
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
		this.font.draw(poseStack, "Are you Cursed, my child?", 30, 8, -12829636);
		this.font.draw(poseStack, "-Double Damage from All Sources", 6, 20, -12829636);
		this.font.draw(poseStack, "-Double Knockback from All Sources", 6, 32, -12829636);
		this.font.draw(poseStack, "-Decreased Damage to Monsters", 6, 44, -12829636);
		this.font.draw(poseStack, "-Fire Lasts Forever, until Doused", 6, 56, -12829636);
		this.font.draw(poseStack, "-Neutral Mobs are Aggressive", 6, 68, -12829636);
		this.font.draw(poseStack, "-Cannot Sleep while Cursed", 6, 80, -12829636);
		this.font.draw(poseStack, "+Triple Experience from Monsters", 6, 98, -12829636);
		this.font.draw(poseStack, "+Extra Loot from Monsters", 6, 110, -12829636);
		this.font.draw(poseStack, "+Extra Loot from Ores", 6, 122, -12829636);
	}

	@Override
	public void onClose() {
		super.onClose();
		Minecraft.getInstance().keyboardHandler.setSendRepeatsToGui(false);
	}

	@Override
	public void init() {
		super.init();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		this.addRenderableWidget(new Button(this.leftPos + 102, this.topPos + 140, 40, 20, Component.literal("YES"), e -> {
			if (true) {
				CurseMod.PACKET_HANDLER.sendToServer(new CurseGuiButtonMessage(0, x, y, z));
				CurseGuiButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}));
		this.addRenderableWidget(new Button(this.leftPos + 144, this.topPos + 140, 35, 20, Component.literal("NO"), e -> {
			if (true) {
				CurseMod.PACKET_HANDLER.sendToServer(new CurseGuiButtonMessage(1, x, y, z));
				CurseGuiButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}));
	}
}