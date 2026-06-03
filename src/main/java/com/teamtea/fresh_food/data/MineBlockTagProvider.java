package com.teamtea.fresh_food.data;


import com.teamtea.fresh_food.FreshFood;
import com.teamtea.fresh_food.core.RotTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.IntrinsicHolderTagsProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.concurrent.CompletableFuture;


public final class MineBlockTagProvider extends IntrinsicHolderTagsProvider<BlockEntityType<?>> {

    public MineBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> providerCompletableFuture) {
        // super(packOutput, providerCompletableFuture, Rotborn.MOD_ID);
        super(packOutput, Registries.BLOCK_ENTITY_TYPE, providerCompletableFuture, block -> block.builtInRegistryHolder().key(), FreshFood.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(RotTags.TICKING_CONTAINER)
                .add(BlockEntityType.FURNACE)
                .add(BlockEntityType.BLAST_FURNACE)
                .add(BlockEntityType.SMOKER)
                .add(BlockEntityType.CAMPFIRE);
        tag(RotTags.TICKING_ENTITY);
    }


    public Identifier srl(String croptopia, String name) {
        return Identifier.fromNamespaceAndPath(croptopia, name);
    }

}
