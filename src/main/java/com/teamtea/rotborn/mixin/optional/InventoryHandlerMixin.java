package com.teamtea.rotborn.mixin.optional;

import com.google.common.collect.Multiset;
import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.teamtea.rotborn.CommonHook;
import com.teamtea.rotborn.core.RotData;
import com.teamtea.rotborn.registry.ModDataComponents;
import cpw.mods.inventorysorter.InventoryHandler;
import cpw.mods.inventorysorter.ItemStackHolder;
import me.fallenbreath.conditionalmixin.api.annotation.Condition;
import me.fallenbreath.conditionalmixin.api.annotation.Restriction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InventoryHandler.class)
@Restriction(require = @Condition("inventorysorter"))
public abstract class InventoryHandlerMixin {
    // @ModifyExpressionValue(
    //         method = "findStackWithItem",
    //         at = @At(
    //                 value = "INVOKE",
    //                 target = "Lnet/minecraft/world/item/ItemStack;isSameItemSameComponents(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;)Z"
    //         )
    // )
    // private boolean rotborn$allowFindStackIgnoringRotData(
    //         boolean original,
    //         @Local(name = "sis") ItemStack sis,
    //         @Local(argsOnly = true) ItemStack is
    // ) {
    //     return original || RotData.isSameExceptRotData(sis, is);
    // }
    @ModifyExpressionValue(
            method = "getInventoryContent",
            at = @At(value = "INVOKE", target = "Lcom/google/common/collect/TreeMultiset;create(Ljava/util/Comparator;)Lcom/google/common/collect/TreeMultiset;"),
            remap = false
    )
    private <E> TreeMultiset<E> rotborn$mergeRotData(
            TreeMultiset<E> original, @Share("st$player") LocalRef<Player> playerLocalRef
    ) {
        return original;
    }

    @WrapOperation(
            method = "getInventoryContent",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/Slot;mayPickup(Lnet/minecraft/world/entity/player/Player;)Z"),
            remap = false
    )
    private boolean rotborn$mergeRotData(
            Slot instance, Player player, Operation<Boolean> original, @Share("st$player") LocalRef<Player> playerLocalRef
    ) {
        playerLocalRef.set(player);
        return original.call(instance, player);
    }

    @ModifyReturnValue(
            method = "getInventoryContent",
            at = @At("RETURN"),
            remap = false
    )
    private Multiset<ItemStackHolder> rotborn$mergeRotData(
            Multiset<ItemStackHolder> original, @Share("st$player") LocalRef<Player> playerLocalRef
    ) {
        if (original == null || original.isEmpty()) return original;

        Player player = playerLocalRef.get();
        Level level = player == null ? CommonHook.rotborn$getLevel() : player.level();

        long now = CommonHook.getClockTime(level);
        SortedMultiset<ItemStackHolder> result =
                TreeMultiset.create(new InventoryHandler.ItemStackComparator());

        for (Multiset.Entry<ItemStackHolder> entry : original.entrySet()) {
            ItemStack stack = entry.getElement().is().copy();
            int addCount = entry.getCount();

            ItemStackHolder same = null;
            for (ItemStackHolder existing : result.elementSet()) {
                if (RotData.isSameExceptRotData(existing.is(), stack)) {
                    same = existing;
                    break;
                }
            }

            if (same == null) {
                result.add(new ItemStackHolder(stack), addCount);
            } else {
                int oldCount = result.count(same);

                RotData merged = RotData.mergeRotData(
                        RotData.getOrCreate(level, same.is(), now), oldCount,
                        RotData.getOrCreate(level, stack, now), addCount,
                        now
                );

                if (merged != null) {
                    same.is().set(ModDataComponents.ROT_DATA.get(), merged);
                } else {
                    same.is().remove(ModDataComponents.ROT_DATA.get());
                }

                result.add(same, addCount);
            }
        }

        return result;
    }
}