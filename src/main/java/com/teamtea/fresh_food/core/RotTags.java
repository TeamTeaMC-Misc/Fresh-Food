package com.teamtea.fresh_food.core;

import com.teamtea.fresh_food.FreshFood;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

public final class RotTags {
    private RotTags() {}

    public static final TagKey<Item> NEVER_ROTS = itemTag("never_rots");
    public static final TagKey<Item> ALWAYS_ROTS = itemTag("always_rots");

    // public static final TagKey<Item> ROTS_TO_SPOILED_EGG = itemTag("rot_result/spoiled_egg");
    // public static final TagKey<Item> ROTS_TO_SPOILED_MEAT = itemTag("rot_result/spoiled_meat");
    // public static final TagKey<Item> ROTS_TO_SPOILED_FRUIT = itemTag("rot_result/spoiled_fruit");
    // public static final TagKey<Item> ROTS_TO_MOLDY_VEGETABLE = itemTag("rot_result/moldy_vegetable");
    // public static final TagKey<Item> ROTS_TO_MOLDY_BREAD = itemTag("rot_result/moldy_bread");
    public static final TagKey<Item> ROTS_TO_SOUR_STEW = itemTag("rot_result/sour_stew");
    // public static final TagKey<Item> ROTS_TO_FERMENTED_MUSHROOM = itemTag("rot_result/fermented_mushroom");
    public static final TagKey<Item> ROTS_TO_SOUR_MILK = itemTag("rot_result/sour_milk");

    public static final TagKey<BlockEntityType<?>> TICKING_ENTITY = blockTag("ticking_entity");
    public static final TagKey<BlockEntityType<?>> TICKING_CONTAINER = blockTag("ticking_container");

    private static TagKey<Item> itemTag(String path) {
        return TagKey.create(Registries.ITEM, FreshFood.rl(path));
    }

    private static TagKey<BlockEntityType<?>> blockTag(String path) {
        return TagKey.create(Registries.BLOCK_ENTITY_TYPE, FreshFood.rl(path));
    }
}
