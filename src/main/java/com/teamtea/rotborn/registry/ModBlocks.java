package com.teamtea.rotborn.registry;

import com.teamtea.rotborn.Rotborn;
import com.teamtea.rotborn.block.IceBoxBlock;
import com.teamtea.rotborn.block.RottingBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    private ModBlocks() {
    }

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Rotborn.MOD_ID);
    public static final DeferredRegister.Items BLOCK_ITEMS = DeferredRegister.createItems(Rotborn.MOD_ID);

    // public static final DeferredBlock<RottingBlock> ROTTING_HAY_BALE = BLOCKS.registerBlock("rotting_hay_bale",
    //         (p) -> new RottingBlock(p
    //                 .mapColor(MapColor.COLOR_YELLOW)
    //                 .strength(0.5F)
    //                 .sound(SoundType.GRASS)
    //                 .randomTicks()));
    //
    // public static final DeferredBlock<RottingBlock> ROTTING_MELON = BLOCKS.registerBlock("rotting_melon",
    //         (p) -> new RottingBlock(p
    //                 .randomTicks()
    //                 .strength(0.5F)),
    //         () -> BlockBehaviour.Properties.ofFullCopy(Blocks.MELON));
    //
    // public static final DeferredBlock<RottingBlock> ROTTING_PUMPKIN = BLOCKS.registerBlock("rotting_pumpkin",
    //         (p) -> new RottingBlock(p
    //                 .randomTicks()
    //                 .strength(0.5F)),
    //         () -> BlockBehaviour.Properties.ofFullCopy(Blocks.PUMPKIN));

    public static final DeferredBlock<RottingBlock> ROTTING_CROPS = BLOCKS.registerBlock("rotting_crops",
            (p) -> new RottingBlock(p
                    .mapColor(MapColor.COLOR_BROWN)
                    .instabreak()
                    .sound(SoundType.CROP)
                    .randomTicks()));

    public static final DeferredBlock<IceBoxBlock> ICE_BOX = BLOCKS.registerBlock("ice_box",
            (p) -> new IceBoxBlock(p
                    .mapColor(MapColor.ICE)
                    .noOcclusion()
                    .strength(2.0F, 3.0F)
                    .sound(SoundType.WOOD)));

    // public static final DeferredItem<BlockItem> ROTTING_HAY_BALE_ITEM = BLOCK_ITEMS.registerItem(
    //         "rotting_hay_bale",
    //         properties -> new BlockItem(ROTTING_HAY_BALE.get(), properties),
    //         Item.Properties::new
    // );
    //
    // public static final DeferredItem<BlockItem> ROTTING_MELON_ITEM = BLOCK_ITEMS.registerItem(
    //         "rotting_melon",
    //         properties -> new BlockItem(ROTTING_MELON.get(), properties),
    //         Item.Properties::new
    // );
    //
    // public static final DeferredItem<BlockItem> ROTTING_PUMPKIN_ITEM = BLOCK_ITEMS.registerItem(
    //         "rotting_pumpkin",
    //         properties -> new BlockItem(ROTTING_PUMPKIN.get(), properties),
    //         Item.Properties::new
    // );

    public static final DeferredItem<BlockItem> ROTTING_CROPS_ITEM = BLOCK_ITEMS.registerItem(
            "rotting_crops",
            properties -> new BlockItem(ROTTING_CROPS.get(), properties),
            Item.Properties::new
    );

    public static final DeferredItem<BlockItem> ICE_BOX_ITEM = BLOCK_ITEMS.registerItem(
            "ice_box",
            properties -> new BlockItem(ICE_BOX.get(), properties.useBlockDescriptionPrefix()),
            Item.Properties::new
    );

}
