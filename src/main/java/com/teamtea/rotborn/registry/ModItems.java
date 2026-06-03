package com.teamtea.rotborn.registry;

import com.teamtea.rotborn.Rotborn;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {
    private ModItems() {
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Rotborn.MOD_ID);

    public static final DeferredItem<Item> ROTTEN_FOOD = ITEMS.registerItem(
            "rotten_food",
            Item::new,
            () -> new Item.Properties()
                    .stacksTo(64)
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.1F)
                            .build())
    );

    // public static final DeferredItem<Item> SPOILED_APPLE = ITEMS.registerItem(
    //         "spoiled_apple",
    //         Item::new,
    //         () -> new Item.Properties()
    //                 .stacksTo(64)
    //                 .food(new FoodProperties.Builder()
    //                         .nutrition(1)
    //                         .saturationModifier(0.1F)
    //                         .build())
    // );
    //
    // public static final DeferredItem<Item> MOLDY_BREAD = ITEMS.registerItem(
    //         "moldy_bread",
    //         Item::new,
    //         () -> new Item.Properties()
    //                 .stacksTo(64)
    //                 .food(new FoodProperties.Builder()
    //                         .nutrition(2)
    //                         .saturationModifier(0.1F)
    //                         .build())
    // );
    //
    // public static final DeferredItem<Item> SPOILED_MEAT = ITEMS.registerItem(
    //         "spoiled_meat",
    //         Item::new,
    //         () -> new Item.Properties()
    //                 .stacksTo(64)
    //                 .food(new FoodProperties.Builder()
    //                         .nutrition(2)
    //                         .saturationModifier(0.1F)
    //                         .build())
    // );

    public static final DeferredItem<Item> SOUR_STEW = ITEMS.registerItem(
            "sour_stew",
            Item::new,
            () -> new Item.Properties()
                    .stacksTo(1)
                    .food(new FoodProperties.Builder()
                            .nutrition(3)
                            .saturationModifier(0.2F)
                            .build())
    );

    // public static final DeferredItem<Item> MOLDY_CARROT = ITEMS.registerItem(
    //         "moldy_carrot",
    //         Item::new,
    //         () -> new Item.Properties()
    //                 .stacksTo(64)
    //                 .food(new FoodProperties.Builder()
    //                         .nutrition(1)
    //                         .saturationModifier(0.1F)
    //                         .build())
    // );
    //
    // public static final DeferredItem<Item> SPOILED_EGG = ITEMS.registerItem(
    //         "spoiled_egg",
    //         Item::new,
    //         () -> new Item.Properties()
    //                 .stacksTo(16)
    //                 .food(new FoodProperties.Builder()
    //                         .nutrition(1)
    //                         .saturationModifier(0.05F)
    //                         .build())
    // );
    //
    // public static final DeferredItem<Item> FERMENTED_MUSHROOM = ITEMS.registerItem(
    //         "fermented_mushroom",
    //         Item::new,
    //         () -> new Item.Properties()
    //                 .stacksTo(64)
    //                 .food(new FoodProperties.Builder()
    //                         .nutrition(2)
    //                         .saturationModifier(0.1F)
    //                         .build())
    // );

    public static final DeferredItem<Item> SOUR_MILK = ITEMS.registerItem(
            "sour_milk",
            Item::new,
            () -> new Item.Properties()
                    .stacksTo(64)
                    // .craftRemainder(Items.BUCKET)
                    .food(new FoodProperties.Builder()
                            .nutrition(1)
                            .saturationModifier(0.05F)
                            .build())
    );
}
