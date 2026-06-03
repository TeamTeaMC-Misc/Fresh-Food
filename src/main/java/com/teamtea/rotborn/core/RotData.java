package com.teamtea.rotborn.core;

import com.teamtea.rotborn.registry.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamtea.rotborn.registry.RottingRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * Immutable value stored on ItemStack through a DataComponentType.
 *
 * @param startGameTime game time when this stack started rotting
 * @param rotAfterTicks duration before the stack becomes spoiled
 */
public record RotData(long startGameTime, int rotAfterTicks) {
    public static final Codec<RotData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.LONG.fieldOf("start_game_time").forGetter(RotData::startGameTime),
            Codec.INT.fieldOf("rot_after_ticks").forGetter(RotData::rotAfterTicks)
    ).apply(instance, RotData::new));

    public static final StreamCodec<ByteBuf, RotData> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_LONG, RotData::startGameTime,
            ByteBufCodecs.VAR_INT, RotData::rotAfterTicks,
            RotData::new
    );

    public boolean expired(long currentGameTime) {
        return currentGameTime - this.startGameTime >= this.rotAfterTicks;
    }

    public static RotData mergeRotData(
            RotData a, int countA,
            RotData b, int countB,
            long now
    ) {
        int total = countA + countB;

        int rotAfterTicks = Math.min(a.rotAfterTicks(), b.rotAfterTicks());

        float progressA = Mth.clamp(
                (float) (now - a.startGameTime()) / (float) a.rotAfterTicks(),
                0.0F,
                1.0F
        );

        float progressB = Mth.clamp(
                (float) (now - b.startGameTime()) / (float) b.rotAfterTicks(),
                0.0F,
                1.0F
        );

        float mergedProgress = (progressA * countA + progressB * countB) / total;

        long newStartGameTime = now - (long) (mergedProgress * rotAfterTicks);

        return new RotData(newStartGameTime, rotAfterTicks);
    }

    // public static boolean isSameExceptRotData(ItemStack a, ItemStack b) {
    //     if (!a.is(b.getItem())) {
    //         return false;
    //     }
    //
    //     DataComponentPatch patchA = a.getComponentsPatch();
    //     DataComponentPatch patchB = b.getComponentsPatch();
    //
    //     DataComponentPatch filteredA = removeComponent(patchA, ModDataComponents.ROT_DATA.get());
    //     DataComponentPatch filteredB = removeComponent(patchB, ModDataComponents.ROT_DATA.get());
    //
    //     return filteredA.equals(filteredB);
    // }

    public static boolean isSameExceptRotData(ItemStack a, ItemStack b) {
        if (!a.is(b.getItem())) {
            return false;
        }

        ItemStack copyA = a.copy();
        ItemStack copyB = b.copy();

        copyA.remove(ModDataComponents.ROT_DATA.get());
        copyB.remove(ModDataComponents.ROT_DATA.get());

        copyA.setCount(1);
        copyB.setCount(1);

        return ItemStack.matches(copyA, copyB);
    }

    public static RotData getOrCreate(Level level, ItemStack stack, long now) {
        RotData rotData = stack.get(ModDataComponents.ROT_DATA);

        if (rotData != null) {
            return rotData;
        }

        RotRule.RotOutcome rule = RotRule.getRotRule(level, stack)
                .orElseGet(() -> RotRule.RotOutcome.getFallbackRule(stack));

        if (rule == null) {
            return null;
        }

        RotData created = new RotData(
                now,
                rule.rotAfterTicks()
        );

        stack.set(ModDataComponents.ROT_DATA, created);

        return created;
    }
}
