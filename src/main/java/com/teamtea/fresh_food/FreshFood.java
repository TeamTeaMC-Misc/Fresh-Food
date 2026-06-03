package com.teamtea.fresh_food;

import com.teamtea.fresh_food.data.start;
import com.teamtea.fresh_food.registry.*;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

@Mod(FreshFood.MOD_ID)
public class FreshFood {
    public static final String MOD_ID = "fresh_food";

    public FreshFood(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlocks.BLOCK_ITEMS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModDataComponents.DATA_COMPONENTS.register(modEventBus);
        ModCreativeTabs.CREATIVE_TABS.register(modEventBus);

        modEventBus.addListener(this::registerCapabilities);


        modEventBus.addListener(this::gatherData);
        modEventBus.addListener(this::gatherData2);
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.Item.BLOCK,
                ModBlockEntities.ICE_BOX_ENTITY.get(),
                (iceBox, side) -> VanillaContainerWrapper.of(iceBox)
        );
    }

    public void gatherData(final GatherDataEvent.Client event) {
        start.dataGen(event);
    }

    public void gatherData2(final GatherDataEvent.Server event) {
        start.dataGen(event);
    }

    public static Identifier rl(String id) {
        return Identifier.fromNamespaceAndPath(MOD_ID, id);
    }
}
