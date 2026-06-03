package com.teamtea.fresh_food.registry;

import com.teamtea.fresh_food.FreshFood;
import com.teamtea.fresh_food.ModRegistryInit;
import com.teamtea.fresh_food.core.RotRule;
import com.teamtea.fresh_food.core.RotTags;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

public class RottingRegistry {
    public static final ResourceKey<RotRule> EGG = createKey("egg");
    public static final ResourceKey<RotRule> MEAT = createKey("meat");
    public static final ResourceKey<RotRule> FRUIT = createKey("fruit");
    public static final ResourceKey<RotRule> VEGETABLE = createKey("vegetable");
    public static final ResourceKey<RotRule> BREAD = createKey("bread");
    public static final ResourceKey<RotRule> STEW = createKey("stew");
    public static final ResourceKey<RotRule> MUSHROOM = createKey("mushroom");
    public static final ResourceKey<RotRule> MILK = createKey("milk");

    public static final int DEFAULT_ROT_AFTER_TICKS = 20 * 60 * 20 * 7; // 20 min

    private static ResourceKey<RotRule> createKey(String name) {
        return ResourceKey.create(ModRegistryInit.ROT_RULE, FreshFood.rl(name));
    }

    private static ResourceKey<Block> createBlockKey(Identifier Identifier) {
        return ResourceKey.create(Registries.BLOCK, Identifier);
    }

    private static ItemPredicate itemPredicate(HolderGetter<Item> items, TagKey<Item> tag) {
        return new ItemPredicate(
                Optional.of(items.getOrThrow(tag)),
                MinMaxBounds.Ints.ANY,
                DataComponentMatchers.ANY
        );
    }

    private static void register(
            BootstrapContext<RotRule> context,
            ResourceKey<RotRule> key,
            ItemPredicate input,
            ItemLike result,
            boolean keepCount,
            int rotAfterTicks
    ) {
        context.register(
                key,
                new RotRule(
                        input,
                        new RotRule.RotOutcome(template(result),
                                keepCount,
                                rotAfterTicks)
                )
        );
    }

    private static ItemStackTemplate template(ItemLike item) {
        return new ItemStackTemplate(
                item.asItem().builtInRegistryHolder(),
                1,
                DataComponentPatch.EMPTY
        );
    }

    public static void bootstrap(BootstrapContext<RotRule> context) {
        HolderGetter<Item> items = context.lookup(Registries.ITEM);

        // register(
        //         context,
        //         EGG,
        //         itemPredicate(items, RotTags.ROTS_TO_SPOILED_EGG),
        //         (ModItems.SPOILED_EGG.get()),
        //         true,
        //         DEFAULT_ROT_AFTER_TICKS
        // );

        // register(
        //         context,
        //         MEAT,
        //         itemPredicate(items, RotTags.ROTS_TO_SPOILED_MEAT),
        //         (ModItems.SPOILED_MEAT.get()),
        //         true,
        //         DEFAULT_ROT_AFTER_TICKS
        // );

        // register(
        //         context,
        //         FRUIT,
        //         itemPredicate(items, RotTags.ROTS_TO_SPOILED_FRUIT),
        //         (ModItems.SPOILED_APPLE.get()),
        //         true,
        //         DEFAULT_ROT_AFTER_TICKS
        // );

        // register(
        //         context,
        //         VEGETABLE,
        //         itemPredicate(items, RotTags.ROTS_TO_MOLDY_VEGETABLE),
        //         (ModItems.MOLDY_CARROT.get()),
        //         true,
        //         DEFAULT_ROT_AFTER_TICKS
        // );

        // register(
        //         context,
        //         BREAD,
        //         itemPredicate(items, RotTags.ROTS_TO_MOLDY_BREAD),
        //         (ModItems.MOLDY_BREAD.get()),
        //         true,
        //         DEFAULT_ROT_AFTER_TICKS
        // );

        register(
                context,
                STEW,
                itemPredicate(items, RotTags.ROTS_TO_SOUR_STEW),
                (ModItems.SOUR_STEW.get()),
                false,
                DEFAULT_ROT_AFTER_TICKS
        );

        // register(
        //         context,
        //         MUSHROOM,
        //         itemPredicate(items, RotTags.ROTS_TO_FERMENTED_MUSHROOM),
        //         (ModItems.FERMENTED_MUSHROOM.get()),
        //         true,
        //         DEFAULT_ROT_AFTER_TICKS
        // );

        register(
                context,
                MILK,
                itemPredicate(items, RotTags.ROTS_TO_SOUR_MILK),
                ModItems.SOUR_MILK.get(),
                false,
                DEFAULT_ROT_AFTER_TICKS
        );
    }
}
