package com.teamtea.rotborn.data.model.model;


import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.core.Holder;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.NonNull;

import java.util.stream.Stream;

public class ES2ModelProvider extends ModelProvider {


    public ES2ModelProvider(PackOutput output, String modId) {
        super(output, modId);
    }

    @Override
    protected @NonNull Stream<? extends Holder<Item>> getKnownItems() {
        // 似乎不写最好
        return Stream.of();
    }

    @Override
    protected @NonNull Stream<? extends Holder<Block>> getKnownBlocks() {
        return Stream.of();
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, @NonNull ItemModelGenerators itemModels) {
        // ESBlockModelGenerators.builder().models(blockModels).build().run();
        new ESItemModelGenerators(itemModels).run();
    }
    //
    // @Override
    // public CompletableFuture<?> run(CachedOutput cache) {
    //     ModelProvider.ItemInfoCollector itemModels = new ModelProvider.ItemInfoCollector(this::getKnownItems);
    //     ModelProvider.BlockStateGeneratorCollector blockStateGenerators = new ModelProvider.BlockStateGeneratorCollector(this::getKnownBlocks);
    //     ModelProvider.SimpleModelCollector simpleModels = new ModelProvider.SimpleModelCollector();
    //     registerModels(new BlockModelGenerators(blockStateGenerators, itemModels, simpleModels), new ItemModelGenerators(itemModels, simpleModels));
    //     blockStateGenerators.validate();
    //     itemModels.finalizeAndValidate();
    //     return CompletableFuture.allOf(
    //             blockStateGenerators.save(cache, this.blockStatePathProvider),
    //             simpleModels.save(cache, this.modelPathProvider),
    //             itemModels.save(cache, this.itemInfoPathProvider)
    //     );
    // }
}
