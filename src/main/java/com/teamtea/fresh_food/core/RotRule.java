package com.teamtea.fresh_food.core;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.teamtea.fresh_food.ModRegistryInit;
import com.teamtea.fresh_food.registry.ModItems;
import com.teamtea.fresh_food.registry.RottingRegistry;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;

import java.util.Optional;
import java.util.function.Supplier;

public record RotRule(
        ItemPredicate inputs,
        RotOutcome outcome
) {
    public static final Codec<RotRule> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            ItemPredicate.CODEC.fieldOf("inputs").forGetter(RotRule::inputs),
            RotOutcome.MAP_CODEC.forGetter(RotRule::outcome)
    ).apply(ins, RotRule::new));

    public record RotOutcome(ItemStackTemplate result, boolean keepCount, int rotAfterTicks) {
        public static final MapCodec<RotOutcome> MAP_CODEC = RecordCodecBuilder.mapCodec(ins -> ins.group(
                ItemStackTemplate.MAP_CODEC.fieldOf("result").forGetter(RotOutcome::result),
                Codec.BOOL.optionalFieldOf("keep_count", true).forGetter(RotOutcome::keepCount),
                Codec.INT.fieldOf("rot_after_ticks").forGetter(RotOutcome::rotAfterTicks)
        ).apply(ins, RotOutcome::new));

        public static RotOutcome getFallbackRule(ItemStack stack) {
            if (stack.is(RotTags.NEVER_ROTS)) {
                return null;
            }

            if (!stack.has(DataComponents.FOOD) && !stack.is(RotTags.ALWAYS_ROTS)) {
                return null;
            }

            return DEFAULT_FOOD_OUTCOME.get();
        }

        public ItemStack createResult(ItemStack stack) {
            ItemStack itemStack = result.create();
            if (keepCount) {
                itemStack.setCount(stack.getCount());
            }
            return itemStack;
        }

        public static final Supplier<RotOutcome> DEFAULT_FOOD_OUTCOME =
                Suppliers.memoize(() -> new RotRule.RotOutcome(
                        new ItemStackTemplate(
                                ModItems.ROTTEN_FOOD.get().builtInRegistryHolder(),
                                1,
                                DataComponentPatch.EMPTY
                        ),
                        true,
                        RottingRegistry.DEFAULT_ROT_AFTER_TICKS
                ));
    }

    public static Optional<RotOutcome> getRotRule(Level level, ItemStack stack) {
        if (stack.is(RotTags.NEVER_ROTS)) return Optional.empty();

        Registry<RotRule> registry = level.registryAccess()
                .lookupOrThrow(ModRegistryInit.ROT_RULE);

        for (RotRule rule : registry) {
            if (rule.matches(stack)) {
                return Optional.of(rule.outcome);
            }
        }

        return Optional.empty();
    }

    public static RotOutcome fallback(ItemLike result, boolean keepCount, int rotAfterTicks) {
        return new RotOutcome(

                new ItemStackTemplate(
                        result.asItem().builtInRegistryHolder(),
                        1,
                        DataComponentPatch.EMPTY
                ),
                keepCount,
                rotAfterTicks
        );
    }

    private boolean matches(ItemStack stack) {
        return inputs.test(stack);
    }

    public ItemStack createResult(ItemStack stack) {
        return outcome.result.create();
    }
}
