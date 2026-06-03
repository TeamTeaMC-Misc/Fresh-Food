package com.teamtea.fresh_food;


import com.teamtea.fresh_food.core.RotRule;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber
public class ModRegistryInit {
    public static final ResourceKey<Registry<RotRule>> ROT_RULE =
            ResourceKey.createRegistryKey(
                    Identifier.fromNamespaceAndPath(FreshFood.MOD_ID, "rot_rule")
            );
    @SubscribeEvent
    public static void onNewRegistry(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                ROT_RULE,
                RotRule.CODEC,
                RotRule.CODEC
        );
    }
}
