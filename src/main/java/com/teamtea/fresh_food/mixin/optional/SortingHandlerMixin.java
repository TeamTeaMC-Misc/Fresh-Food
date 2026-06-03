package com.teamtea.fresh_food.mixin.optional;

import cpw.mods.inventorysorter.SortingHandler;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

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