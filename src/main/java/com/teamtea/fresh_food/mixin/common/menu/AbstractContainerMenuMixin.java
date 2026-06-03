package com.teamtea.fresh_food.mixin.common.menu;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.fresh_food.CommonHook;
import com.teamtea.fresh_food.SimpleUtil;
import com.teamtea.fresh_food.core.RotData;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {


    @ModifyExpressionValue(
            method = "moveItemStackTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private boolean rotborn$allowMergeIgnoringRotData(
            boolean original,
            @Local(argsOnly = true) ItemStack source,
            @Local(name = "target") ItemStack target
    ) {
        return original || RotData.isSameExceptRotData(source, target);
    }


    @Inject(
            method = "moveItemStackTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;setCount(I)V",
                    ordinal = 0
            )
    )
    private void rotborn$mergeRotDataBeforeFullMerge(
            ItemStack source,
            int startSlot,
            int endSlot,
            boolean backwards,
            CallbackInfoReturnable<Boolean> cir,
            @Local(name = "target") ItemStack target
    ) {
        SimpleUtil.tryMergeRotStacks(CommonHook.rotborn$getLevel((AbstractContainerMenu)(Object)this), source, target);
    }


    @Inject(
            method = "moveItemStackTo",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;shrink(I)V"
            )
    )
    private void rotborn$mergeRotDataBeforePartialMerge(
            ItemStack source,
            int startSlot,
            int endSlot,
            boolean backwards,
            CallbackInfoReturnable<Boolean> cir,
            @Local(name = "target") ItemStack target
    ) {
        int movedCount = target.getMaxStackSize() - target.getCount();
        SimpleUtil.tryMergeRotStacks(CommonHook.rotborn$getLevel((AbstractContainerMenu)(Object)this), source.copyWithCount(movedCount), target);
    }
}