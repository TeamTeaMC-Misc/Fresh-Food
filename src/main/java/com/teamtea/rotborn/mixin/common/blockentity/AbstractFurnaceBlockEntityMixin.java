package com.teamtea.rotborn.mixin.common.blockentity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.teamtea.rotborn.CommonHook;
import com.teamtea.rotborn.SimpleUtil;
import com.teamtea.rotborn.core.RotData;
import com.teamtea.rotborn.registry.ModDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class AbstractFurnaceBlockEntityMixin {


    @ModifyExpressionValue(
            method = "canBurn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
            )
    )
    private static boolean rotborn$allowBurnResultMerge(
            boolean original,
            @Local(ordinal = 0, argsOnly = true) ItemStack resultItemStack,
            @Local(argsOnly = true) ItemStack burnResult
    ) {
        return original || RotData.isSameExceptRotData(burnResult, resultItemStack);
    }

    @Unique
    private static final ThreadLocal<Level> rotborn$ROTBORN_LEVEL = new ThreadLocal<>();

    @WrapOperation(
            method = "serverTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/entity/AbstractFurnaceBlockEntity;burn(Lnet/minecraft/core/NonNullList;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)V"
            )
    )
    private static void rotborn$wrapBurn(
            NonNullList<ItemStack> items, ItemStack inputItemStack, ItemStack result, Operation<Void> original, @Local(argsOnly = true) AbstractFurnaceBlockEntity entity
    ) {

        // RotData rot = inputItemStack.get(ModDataComponents.ROT_DATA.get());
        // if (rot != null && result.get(ModDataComponents.ROT_DATA.get()) == null) {
        //     result = result.copy();
        //     result.set(ModDataComponents.ROT_DATA.get(), rot);
        // }

        rotborn$ROTBORN_LEVEL.set(entity.getLevel());

        try {
            original.call(items, inputItemStack, result);
        } finally {
            rotborn$ROTBORN_LEVEL.remove();
        }
    }

    @WrapOperation(
            method = "burn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/item/ItemStack;grow(I)V"
            )
    )
    private static void rotborn$mergeBurnResultRotData(
            ItemStack instance, int amount, Operation<Void> original, @Local(name = "result") ItemStack result
    ) {
        Level level = rotborn$ROTBORN_LEVEL.get();
        int i = SimpleUtil.tryMergeRotStacks(level != null ? level : CommonHook.rotborn$getLevel(), result, instance);
        if (i < 1) {
            original.call(instance, amount);
        }
    }
}