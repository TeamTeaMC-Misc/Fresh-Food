package com.teamtea.rotborn.mixin.optional;

import com.google.common.collect.Multiset;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.rotborn.RottingEvents;
import com.teamtea.rotborn.SimpleUtil;
import cpw.mods.inventorysorter.ItemStackHolder;
import cpw.mods.inventorysorter.SortingHandler;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Restriction(require = @Condition("inventorysorter"))
@Mixin(SortingHandler.class)
public abstract class SortingHandlerMixin {
    // @ModifyExpressionValue(
    //         method = "compactInventory",
    //         at = @At(
    //                 value = "INVOKE",
    //                 target = "Ljava/lang/Math;min(II)I"))
    // private int rotborn$mergeRotDataForSortedTarget(int original,
    //                                                 @Local(name = "stackHolder") Multiset.Entry<ItemStackHolder> stackHolder,
    //                                                 @Local(argsOnly = true, ordinal = 1) ItemStackHolder o2) {
    //     if (original > 0) {
    //         SimpleUtil.tryMergeRotStacks(rotborn$getLevel(), result, instance);
    //     }
    //     return original;
    // }
    //
    // @Unique
    // private static @Nullable Level rotborn$getLevel() {
    //     Level overworld = ServerLifecycleHooks.getCurrentServer() == null ?
    //             RottingEvents.level.get() : ServerLifecycleHooks.getCurrentServer().overworld();
    //     return overworld;
    // }
}