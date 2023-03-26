package net.salju.curse.procedures;

import net.salju.curse.world.inventory.CurseGuiMenu;

import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.living.LootingLevelEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.TickEvent;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.SpellcasterIllager;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.Difficulty;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.tags.TagKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.Registry;
import net.minecraft.core.BlockPos;

import io.netty.buffer.Unpooled;

@Mod.EventBusSubscriber
public class CurseEventsProcedure {
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onHurt(LivingHurtEvent event) {
		if (event != null && event.getEntity() != null) {
			Entity source = event.getSource().getEntity();
			Entity target = event.getEntity();
			float damage = event.getAmount();
			if (target instanceof Player player && CurseHelpersProcedure.isCursed(player)) {
				event.setAmount((float) (damage * 2));
			} else if (source instanceof Player player && CurseHelpersProcedure.isCursed(player)) {
				if (target instanceof Monster) {
					LevelAccessor world = source.getLevel();
					if (world.getDifficulty() == Difficulty.NORMAL) {
						event.setAmount((float) (damage * 0.75));
					} else if (world.getDifficulty() == Difficulty.HARD) {
						event.setAmount((float) (damage * 0.5));
					} else {
						event.setAmount((float) (damage * 0.9));
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onKnockKnock(LivingKnockBackEvent event) {
		if (event != null && event.getEntity() != null) {
			Entity target = event.getEntity();
			float damage = event.getStrength();
			if (target instanceof Player player && CurseHelpersProcedure.isCursed(player)) {
				event.setStrength(damage * 2);
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDropXp(LivingExperienceDropEvent event) {
		if (event != null && event.getEntity() != null && event.getAttackingPlayer() != null) {
			Player player = event.getAttackingPlayer();
			Entity target = event.getEntity();
			int xp = event.getDroppedExperience();
			if (CurseHelpersProcedure.isCursed(player)) {
				if (target instanceof Monster) {
					event.setDroppedExperience(xp * 3);
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		if (event != null && event.getEntity() != null && event.getSource().getEntity() != null) {
			Entity target = event.getEntity();
			Entity source = event.getSource().getEntity();
			LevelAccessor world = target.getLevel();
			double x = target.getX();
			double y = target.getY();
			double z = target.getZ();
			int loot = event.getLootingLevel();
			if (source instanceof Player player && CurseHelpersProcedure.isCursed(player)) {
				if (world instanceof Level lvl && (Math.random() >= 0.85)) {
					if (target instanceof ZombifiedPiglin || target instanceof AbstractPiglin) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (0 + loot), (2 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.GOLD_INGOT));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof Drowned || target instanceof Guardian) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (0 + loot), (2 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.LAPIS_LAZULI));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof Husk) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (0 + loot), (2 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.SAND));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof Zombie) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (0 + loot), (2 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.SLIME_BALL));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof Witch) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (0 + loot), (2 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.PHANTOM_MEMBRANE));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof SpellcasterIllager) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 0, 1)); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.DIAMOND));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof AbstractIllager) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (0 + loot), (2 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.EMERALD));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof WitherSkeleton && (Math.random() >= (0.9 - (0.05 * loot)))) {
						ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.NETHERITE_SCRAP));
						item.setPickUpDelay(10);
						lvl.addFreshEntity(item);
					} else if (target instanceof EnderMan) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 0, (1 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.ENDER_EYE));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof Skeleton) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (1 + loot), (5 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.ARROW));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof Creeper) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (1 + loot), (5 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.GUNPOWDER));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof MagmaCube || target instanceof Vex) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), (0 + loot), (2 + loot))); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.GLOWSTONE_DUST));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof Blaze) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 0, 2)); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.BLAZE_ROD));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					} else if (target instanceof Spider) {
						for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 0, 2)); index0++) {
							ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.FERMENTED_SPIDER_EYE));
							item.setPickUpDelay(10);
							lvl.addFreshEntity(item);
						}
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			Player player = event.player;
			LevelAccessor world = event.player.level;
			if (CurseHelpersProcedure.isCursed(player)) {
				if (!world.isClientSide() && !player.isCreative() && !player.isSpectator()) {
					if (player.getRemainingFireTicks() == 1) {
						player.setSecondsOnFire(120);
					}
					for (Mob angry : player.level.getEntitiesOfClass(Mob.class, player.getBoundingBox().inflate(28.0D))) {
						if (angry instanceof IronGolem golem && golem.isPlayerCreated())
							continue;
						if (angry.getType().is(TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation("curse:no_angry"))))
							continue;
						if (!(angry instanceof Animal) && (angry.getTarget() == null)) {
							if (player.hasLineOfSight(angry) || (player.distanceTo(angry) <= 4.0D)) {
								if ((angry instanceof NeutralMob) || (angry instanceof AbstractPiglin)) {
									angry.setTarget(player);
								}
							}
						}
					}
				}
				if (player.isSleeping() && player.getSleepTimer() >= 95) {
					player.stopSleeping();
					player.displayClientMessage(Component.literal("You cannot sleep as a cursed player"), (true));
				}
			}
		}
	}

	@SubscribeEvent
	public static void onLooter(LootingLevelEvent event) {
		if (event != null && event.getDamageSource().getEntity() != null) {
			Entity source = event.getDamageSource().getEntity();
			int loot = event.getLootingLevel();
			if (source instanceof Player player && CurseHelpersProcedure.isCursed(player)) {
				event.setLootingLevel(loot + 1);
			}
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		ServerPlayer player = (ServerPlayer) event.getEntity();
		LevelAccessor world = player.level;
		double x = player.getX();
		double y = player.getY();
		double z = player.getZ();
		if (!(player.level instanceof ServerLevel ? player.getAdvancements().getOrStartProgress(player.server.getAdvancements().getAdvancement(new ResourceLocation("minecraft:story/root"))).isDone() : false)) {
			if (!CurseHelpersProcedure.isCursed(player)) {
				BlockPos pos = new BlockPos(x, y, z);
				NetworkHooks.openScreen((ServerPlayer) player, new MenuProvider() {
					@Override
					public Component getDisplayName() {
						return Component.literal("CurseGui");
					}

					@Override
					public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
						return new CurseGuiMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos));
					}
				}, pos);
			}
		}
	}

	@SubscribeEvent
	public static void onBlockBreak(BlockEvent.BreakEvent event) {
		if (event != null && event.getState() != null && event.getPlayer() != null) {
			Player player = event.getPlayer();
			if (CurseHelpersProcedure.isCursed(player) && !player.isCreative()) {
				LevelAccessor world = event.getLevel();
				double x = event.getPos().getX();
				double y = event.getPos().getY();
				double z = event.getPos().getZ();
				BlockState block = event.getState();
				InteractionHand handy = player.swingingArm;
				ItemStack tool = player.getItemInHand(handy);
				int fort = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
				if (!(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, tool) != 0)) {
					if (world instanceof Level lvl && (Math.random() >= 0.65 - (0.05 * fort))) {
						if (block.getBlock() == Blocks.COAL_ORE || block.getBlock() == Blocks.DEEPSLATE_COAL_ORE) {
							for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
								ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.COAL));
								item.setPickUpDelay(10);
								lvl.addFreshEntity(item);
							}
						} else if (block.getBlock() == Blocks.DIAMOND_ORE || block.getBlock() == Blocks.DEEPSLATE_DIAMOND_ORE) {
							for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 0, (1 + fort))); index0++) {
								ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.DIAMOND));
								item.setPickUpDelay(10);
								lvl.addFreshEntity(item);
							}
						} else if (block.getBlock() == Blocks.LAPIS_ORE || block.getBlock() == Blocks.DEEPSLATE_LAPIS_ORE) {
							for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
								ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.LAPIS_LAZULI));
								item.setPickUpDelay(10);
								lvl.addFreshEntity(item);
							}
						} else if (block.getBlock() == Blocks.REDSTONE_ORE || block.getBlock() == Blocks.DEEPSLATE_REDSTONE_ORE) {
							for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
								ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.REDSTONE));
								item.setPickUpDelay(10);
								lvl.addFreshEntity(item);
							}
						} else if (block.getBlock() == Blocks.IRON_ORE || block.getBlock() == Blocks.DEEPSLATE_IRON_ORE) {
							if (tool.isEnchanted()) {
								for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
									ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.IRON_INGOT));
									item.setPickUpDelay(10);
									lvl.addFreshEntity(item);
								}
							} else {
								for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
									ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.RAW_IRON));
									item.setPickUpDelay(10);
									lvl.addFreshEntity(item);
								}
							}
						} else if (block.getBlock() == Blocks.GOLD_ORE || block.getBlock() == Blocks.DEEPSLATE_GOLD_ORE) {
							if (tool.isEnchanted()) {
								for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
									ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.GOLD_INGOT));
									item.setPickUpDelay(10);
									lvl.addFreshEntity(item);
								}
							} else {
								for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
									ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.RAW_GOLD));
									item.setPickUpDelay(10);
									lvl.addFreshEntity(item);
								}
							}
						} else if (block.getBlock() == Blocks.COPPER_ORE || block.getBlock() == Blocks.DEEPSLATE_COPPER_ORE) {
							if (tool.isEnchanted()) {
								for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
									ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.COPPER_INGOT));
									item.setPickUpDelay(10);
									lvl.addFreshEntity(item);
								}
							} else {
								for (int index0 = 0; index0 < (int) (Mth.nextInt(RandomSource.create(), 1, 2)); index0++) {
									ItemEntity item = new ItemEntity(lvl, x, y, z, new ItemStack(Items.RAW_COPPER));
									item.setPickUpDelay(10);
									lvl.addFreshEntity(item);
								}
							}
						}
					}
				}
			}
		}
	}
}