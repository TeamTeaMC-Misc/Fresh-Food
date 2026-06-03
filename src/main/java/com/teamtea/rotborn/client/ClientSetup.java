package com.teamtea.rotborn.client;


import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;

@EventBusSubscriber
public class ClientSetup {

    @SubscribeEvent
    public static void onRegisterItemDecorationsEvent(RegisterItemDecorationsEvent event) {
        // BuiltInRegistries.ITEM.forEach(item -> {
        //     if (canRot(item.getDefaultInstance())) {
        //         event.register(item, RotItemDecorator.DECORATOR);
        //     }
        // });
        // event.register(Items.APPLE, RotItemDecorator.DECORATOR);
    }

    public static boolean canRot(ItemStack stack) {
        return stack.has(DataComponents.FOOD);
    }
}
