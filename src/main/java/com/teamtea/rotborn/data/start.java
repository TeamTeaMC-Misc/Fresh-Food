package com.teamtea.rotborn.data;

import com.teamtea.rotborn.data.model.model.ES2ModelProvider;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static com.teamtea.rotborn.Rotborn.MOD_ID;


public class start {

    public static void dataGen(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        var packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        if (event instanceof GatherDataEvent.Server) {
            generator.addProvider(true, new MineBlockTagProvider(packOutput, lookupProvider));
            generator.addProvider(true, new MineItemTagProvider(packOutput, lookupProvider));
            generator.addProvider(true, new DatapackRegistryGenerator(packOutput, lookupProvider));
        }
        if (event instanceof GatherDataEvent.Client) {
            event.addProvider(new ES2ModelProvider(packOutput, MOD_ID));
        }

    }
}
