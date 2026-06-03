package com.teamtea.rotborn.mixin.optional;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.rotborn.core.RotData;
import cpw.mods.inventorysorter.InventoryHandler;
import cpw.mods.inventorysorter.ItemStackHolder;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

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