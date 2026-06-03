package com.teamtea.fresh_food.mixin.optional;

import cpw.mods.inventorysorter.InventoryHandler;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryHandler.ItemStackComparator.class)
@Restriction(require = @Condition("inventorysorter"))
public abstract class InventoryHandlerInternalMixin {

    // @ModifyExpressionValue(
    //         method = "compare(Lcpw/mods/inventorysorter/ItemStackHolder;Lcpw/mods/inventorysorter/ItemStackHolder;)I",
    //         at = @At(
    //                 value = "INVOKE",
    //                 target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
    //         )
    // )
    // private boolean rotborn$compareIgnoringRotData(
    //         boolean original,
    //         @Local(argsOnly = true, ordinal = 0) ItemStackHolder o1,
    //         @Local(argsOnly = true, ordinal = 1) ItemStackHolder o2
    // ) {
    //     return original || RotData.isSameExceptRotData(o1.is(), o2.is());
    // }
}