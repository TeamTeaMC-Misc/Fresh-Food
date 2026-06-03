package com.teamtea.rotborn.data;


import com.teamtea.rotborn.Rotborn;
import com.teamtea.rotborn.core.RotTags;
import com.teamtea.rotborn.registry.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;


public final class MineItemTagProvider extends ItemTagsProvider {

    public MineItemTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture) {
        super(packOutput, providerCompletableFuture, Rotborn.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(RotTags.ALWAYS_ROTS)
                .addTags(Tags.Items.EGGS, Tags.Items.FOODS_EDIBLE_WHEN_PLACED
                        // ,Tags.Items.ANIMAL_FOODS
                );

        tag(RotTags.NEVER_ROTS)
                .add(
                        Items.ROTTEN_FLESH,
                        Items.SPIDER_EYE,
                        Items.FERMENTED_SPIDER_EYE,
                        Items.HONEY_BOTTLE,
                        ModItems.ROTTEN_FOOD.get(),
                        // ModItems.SPOILED_APPLE.get(),
                        // ModItems.MOLDY_BREAD.get(),
                        // ModItems.SPOILED_MEAT.get(),
                        ModItems.SOUR_STEW.get(),
                        // ModItems.MOLDY_CARROT.get(),
                        // ModItems.SPOILED_EGG.get(),
                        // ModItems.FERMENTED_MUSHROOM.get(),
                        ModItems.SOUR_MILK.get()
                )
                .addTags(Tags.Items.FOODS_GOLDEN);

        // tag(RotTags.ROTS_TO_SPOILED_EGG)
        //         .addTag(Tags.Items.EGGS);
        //
        // tag(RotTags.ROTS_TO_SPOILED_MEAT)
        //         .addTag(Tags.Items.FOODS_RAW_MEAT)
        //         .addTag(Tags.Items.FOODS_COOKED_MEAT)
        //         .addTag(Tags.Items.FOODS_RAW_FISH)
        //         .addTag(Tags.Items.FOODS_COOKED_FISH)
        //         .addTag(ItemTags.MEAT)
        //         .addTag(ItemTags.FISHES);

        // tag(RotTags.ROTS_TO_SPOILED_FRUIT)
        //         .addTag(Tags.Items.FOODS_FRUIT)
        //         .addTag(Tags.Items.FOODS_BERRY);

        // tag(RotTags.ROTS_TO_MOLDY_VEGETABLE)
        //         .addTag(Tags.Items.FOODS_VEGETABLE)
        //         .add(
        //                 Items.CARROT,
        //                 Items.GOLDEN_CARROT,
        //                 Items.POTATO,
        //                 Items.BAKED_POTATO,
        //                 Items.BEETROOT,
        //                 Items.DRIED_KELP
        //         );

        // tag(RotTags.ROTS_TO_MOLDY_BREAD)
        //         .addTag(Tags.Items.FOODS_BREAD)
        //         .addTag(Tags.Items.FOODS_COOKIE)
        //         .addTag(Tags.Items.FOODS_PIE)
        //         .add(Items.CAKE);

        tag(RotTags.ROTS_TO_SOUR_STEW)
                .addTag(Tags.Items.FOODS_SOUP)
                .add(
                        Items.MUSHROOM_STEW,
                        Items.RABBIT_STEW,
                        Items.BEETROOT_SOUP,
                        Items.SUSPICIOUS_STEW
                );

        // tag(RotTags.ROTS_TO_FERMENTED_MUSHROOM)
        //         .addTag(Tags.Items.MUSHROOMS)
        //         .add(
        //                 Items.BROWN_MUSHROOM,
        //                 Items.RED_MUSHROOM
        //         );

        tag(RotTags.ROTS_TO_SOUR_MILK)
                .addTag(Tags.Items.BUCKETS_MILK)
                .addTag(Tags.Items.DRINKS_MILK);
    }


    public Identifier srl(String croptopia, String name) {
        return Identifier.fromNamespaceAndPath(croptopia, name);
    }

}
