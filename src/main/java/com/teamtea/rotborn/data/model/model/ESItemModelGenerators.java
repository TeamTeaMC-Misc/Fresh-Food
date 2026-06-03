package com.teamtea.rotborn.data.model.model;

import com.teamtea.rotborn.registry.ModItems;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jspecify.annotations.NonNull;


public class ESItemModelGenerators {
    private final ItemModelGenerators models;

    public ESItemModelGenerators(@NonNull ItemModelGenerators itemModels) {
        this.models = itemModels;
    }

    public ItemModelGenerators getModels() {
        return models;
    }


    public void run() {
        for (DeferredHolder<Item, ? extends Item> entry : ModItems.ITEMS.getEntries()) {
            addSimple(entry.get());
        }
    }

    public void addSimple(Item item) {
        models.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    // public void addSimple(ItemLike item, String texture) {
    //     models.itemModelOutput.accept(item.asItem(),
    //             ItemModelUtils.plainModel(createFlatItemModel(item.asItem(), new Material(Rotborn.rl("item/%s".formatted(texture))), ModelTemplates.FLAT_ITEM)));
    // }


}