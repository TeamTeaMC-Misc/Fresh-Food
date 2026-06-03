package com.teamtea.rotborn.registry;

import com.teamtea.rotborn.core.RotData;
import com.teamtea.rotborn.Rotborn;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModDataComponents {
    private ModDataComponents() {}

    public static final DeferredRegister.DataComponents DATA_COMPONENTS =
            DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Rotborn.MOD_ID);

    public static final Supplier<DataComponentType<RotData>> ROT_DATA =
            DATA_COMPONENTS.registerComponentType("rot_data", builder -> builder
                    .persistent(RotData.CODEC)
                    .networkSynchronized(RotData.STREAM_CODEC)
            );
}
