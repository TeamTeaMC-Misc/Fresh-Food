package com.teamtea.fresh_food.data;


import com.teamtea.fresh_food.FreshFood;
import com.teamtea.fresh_food.ModRegistryInit;
import com.teamtea.fresh_food.registry.RottingRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class DatapackRegistryGenerator extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder REGISTRY_SET_BUILDER = new RegistrySetBuilder()
            .add(ModRegistryInit.ROT_RULE, RottingRegistry::bootstrap)
            ;


    public DatapackRegistryGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, REGISTRY_SET_BUILDER, Set.of(FreshFood.MOD_ID));
    }


}