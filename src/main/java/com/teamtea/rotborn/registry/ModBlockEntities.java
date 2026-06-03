package com.teamtea.rotborn.registry;

import com.teamtea.rotborn.Rotborn;
import com.teamtea.rotborn.block.IceBoxBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModBlockEntities {
    private ModBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Rotborn.MOD_ID);

    public static final Supplier<BlockEntityType<IceBoxBlockEntity>> ICE_BOX_ENTITY =
            BLOCK_ENTITIES.register("ice_box",
                    () -> new BlockEntityType<>(IceBoxBlockEntity::new, ModBlocks.ICE_BOX.get()));
}
