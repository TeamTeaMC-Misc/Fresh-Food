package com.teamtea.rotborn;

import com.teamtea.rotborn.api.FreshnessLevel;
import com.teamtea.rotborn.core.RotData;
import com.teamtea.rotborn.core.RotRule;
import com.teamtea.rotborn.registry.ModDataComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;

import java.lang.ref.WeakReference;
import java.util.Optional;

@EventBusSubscriber
public final class RottingEvents {

    // @SubscribeEvent
    // public static void onStackedOnOther(ItemStackedOnOtherEvent event) {
    //     if (event.getClickAction() != ClickAction.PRIMARY) return;
    //
    //     ItemStack carried = event.getCarriedItem();
    //     ItemStack target = event.getStackedOnItem();
    //
    //     if (carried.isEmpty() || target.isEmpty()) return;
    //
    //     if (!carried.has(ModDataComponents.ROT_DATA)
    //             && !target.has(ModDataComponents.ROT_DATA)) {
    //         return;
    //     }
    //
    //     if (!RotData.isSameExceptRotData(carried, target)) {
    //         return;
    //     }
    //
    //     int max = target.getMaxStackSize();
    //     int canMove = Math.min(carried.getCount(), max - target.getCount());
    //
    //     if (canMove <= 0) return;
    //
    //     Level level = event.getPlayer().level();
    //     long now = CommonHook.getClockTime(level);
    //
    //     RotData carriedRot = RotData.getOrCreate(level, carried, now);
    //     RotData targetRot = RotData.getOrCreate(level, target, now);
    //
    //     if (carriedRot == null || targetRot == null) return;
    //
    //     RotData merged = RotData.mergeRotData(
    //             targetRot,
    //             target.getCount(),
    //             carriedRot,
    //             canMove,
    //             now
    //     );
    //
    //     target.grow(canMove);
    //     carried.shrink(canMove);
    //
    //     target.set(ModDataComponents.ROT_DATA, merged);
    //
    //     event.setCanceled(true);
    // }
    @SubscribeEvent
    public static void onStackedOnOther(ItemStackedOnOtherEvent event) {
        if (event.getClickAction() != ClickAction.PRIMARY) return;

        int moved = SimpleUtil.tryMergeRotStacks(
                event.getPlayer().level(),
                event.getCarriedItem(),
                event.getStackedOnItem()
        );

        if (moved > 0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemTooltipEvent(ItemTooltipEvent event) {
        if (event.getItemStack().has(ModDataComponents.ROT_DATA)) {
            RotData rotData = event.getItemStack().get(ModDataComponents.ROT_DATA);
            Level level = event.getContext().level();
            if (level == null) return;

            long remainingTicks = rotData.startGameTime() + rotData.rotAfterTicks() - CommonHook.getClockTime(level);
            long remainingDays = Math.max(0, Mth.ceil(remainingTicks / 24000.0D));

            float freshness = Mth.clamp(
                    (float) remainingTicks / (float) rotData.rotAfterTicks(),
                    0.0F,
                    1.0F
            );

            Component line = FreshnessLevel.fromFreshness(freshness).tooltip(remainingDays);
            event.getToolTip().add(line);
        }
    }

    private static final int ROT_CHECK_INTERVAL = 20 * 5;

    @SubscribeEvent
    public static void onPlayerTick(EntityTickEvent.Post event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) {
            return;
        }

        Entity entity = event.getEntity();
        long gameTime = CommonHook.getClockTime(level);

        if ((gameTime + entity.getId()) % ROT_CHECK_INTERVAL != 0) {
            return;
        }

        if (entity instanceof Player) {
            return;
        }

        if (entity instanceof ItemEntity itemEntity) {
            RottingUtil.processItemEntity(level, itemEntity, gameTime);
            return;
        }


        boolean changed = false;

        ResourceHandler<ItemResource> handler = entity.getCapability(Capabilities.Item.ENTITY);
        if (handler != null && handler.size() > 0) {
            changed |= RottingUtil.processResourceHandler(level, handler, gameTime);
        } else if (entity instanceof Container container && entity.isAlive()) {
            changed |= RottingUtil.processContainer(level, container, gameTime);
        }

        if (changed) {
            entity.needsSync = true;
            // 一般 ResourceHandler 自己会写回；Container 实体则 processContainer 已 setChanged。
            // 实体没有通用 setChanged，必要时可以同步一次。
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        ServerLevel level = player.level();
        long gameTime = CommonHook.getClockTime(level);

        if (gameTime % ROT_CHECK_INTERVAL != 0) {
            return;
        }

        RottingUtil.processContainer(level, player.getInventory(), gameTime);

        // AbstractContainerMenu menu = player.containerMenu;
        // if (menu != null && menu != player.inventoryMenu) {
        //     processOpenMenu(level, menu, gameTime);
        // }
    }

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        if (!(event.getLevel() instanceof ServerLevel level)) {
            return;
        }

        long gameTime = CommonHook.getClockTime(level);

        if (gameTime % ROT_CHECK_INTERVAL != 0) {
            return;
        }

        RottingUtil.processLoadedContainers(level, gameTime);
    }

    public static WeakReference<Level> level = new WeakReference<>(null);

    @SubscribeEvent
    public static void onLevelTick(LevelEvent.Load event) {
        if (event.getLevel().isClientSide()) {
            level = new WeakReference<>((Level) event.getLevel());
        }
    }

    @SubscribeEvent
    public static void onItemPickup(ItemEntityPickupEvent.Post event) {
        Player player = event.getPlayer();
        ItemStack original = event.getOriginalStack();

        if (original.isEmpty()
            // || !original.has(ModDataComponents.ROT_DATA.get())
        ) {
            return;
        }

        Optional<RotRule.RotOutcome> rotRule = RotRule.getRotRule(player.level(), original);
        if (rotRule.isEmpty() && RotRule.RotOutcome.getFallbackRule(original) == null)
            return;

        Inventory inv = player.getInventory();

        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack target = inv.getItem(i);

            if (target.isEmpty() || !target.is(original.getItem())) {
                continue;
            }

            for (int j = i + 1; j < inv.getContainerSize(); j++) {
                ItemStack source = inv.getItem(j);

                if (source.isEmpty() || !source.is(original.getItem())) {
                    continue;
                }

                int moved = SimpleUtil.tryMergeRotStacks(player.level(), target, source);

                if (moved > 0 && target.isEmpty()) {
                    inv.setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }
}
