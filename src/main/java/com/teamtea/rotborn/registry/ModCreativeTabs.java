package com.teamtea.rotborn.registry;

import com.teamtea.rotborn.Rotborn;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {
    private ModCreativeTabs() {
    }

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Rotborn.MOD_ID);

    public static final Supplier<CreativeModeTab> ROTBORN_TAB =
            CREATIVE_TABS.register("rotborn", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.rotborn"))
                    .icon(() -> ModItems.ROTTEN_FOOD.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.ROTTEN_FOOD.get());
                        // output.accept(ModItems.SPOILED_APPLE.get());
                        // output.accept(ModItems.MOLDY_BREAD.get());
                        // output.accept(ModItems.SPOILED_MEAT.get());
                        output.accept(ModItems.SOUR_STEW.get());
                        // output.accept(ModItems.MOLDY_CARROT.get());
                        // output.accept(ModItems.SPOILED_EGG.get());
                        // output.accept(ModItems.FERMENTED_MUSHROOM.get());
                        output.accept(ModItems.SOUR_MILK.get());
                        output.accept(ModBlocks.ICE_BOX.get());
                    })
                    .build());
}
