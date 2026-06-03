package com.teamtea.rotborn;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jspecify.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

@EventBusSubscriber
public class CommonHook {

    public static Map<Player, WeakReference<AbstractContainerMenu>> client = new HashMap<>();
    public static Map<Player, WeakReference<AbstractContainerMenu>> server = new HashMap<>();

    public static @Nullable Level rotborn$getLevel(AbstractContainerMenu menu) {
        for (Map.Entry<Player, WeakReference<AbstractContainerMenu>> entry : server.entrySet()) {
            if (entry.getValue().get() == menu)
                return entry.getKey().level();
        }
        for (Map.Entry<Player, WeakReference<AbstractContainerMenu>> entry : client.entrySet()) {
            if (entry.getValue().get() == menu)
                return entry.getKey().level();
        }
        return rotborn$getLevel();
    }

    public static @Nullable Level rotborn$getLevel() {
        Level overworld = ServerLifecycleHooks.getCurrentServer() == null ?
                RottingEvents.level.get() : ServerLifecycleHooks.getCurrentServer().overworld();
        return overworld;
    }

    public static long getClockTime(Level level) {
        return level.getDefaultClockTime();
    }

    @SubscribeEvent
    public static void checkMenu(PlayerTickEvent.Pre event) {
        Player entity = event.getEntity();
        Map<Player, WeakReference<AbstractContainerMenu>> use = entity instanceof ServerPlayer ?
                server : client;
        use.put(entity, new WeakReference<>(entity.containerMenu));
    }

    // public static void checkMenu(ServerTickEvent.Post event) {
    //     HashSet<ServerPlayer> serverPlayers = new HashSet<>(event.getServer().getPlayerList().getPlayers());
    // }

    @SubscribeEvent
    public static void checkMenu(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof Player player) {
            Map<Player, WeakReference<AbstractContainerMenu>> use = !event.getLevel().isClientSide() ?
                    server : client;
            use.remove(player);
        }
    }
}
