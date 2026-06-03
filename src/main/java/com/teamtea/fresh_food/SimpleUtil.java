package com.teamtea.fresh_food;

import com.teamtea.fresh_food.core.RotData;
import com.teamtea.fresh_food.registry.ModDataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class SimpleUtil {
    public static int tryMergeRotStacks(Level level, ItemStack carried, ItemStack target) {
        if (carried.isEmpty() || target.isEmpty()) return 0;

        if (!carried.has(ModDataComponents.ROT_DATA)
                && !target.has(ModDataComponents.ROT_DATA)) {
            return 0;
        }

        if (!RotData.isSameExceptRotData(carried, target)) {
            return 0;
        }

        int max = target.getMaxStackSize();
        int canMove = Math.min(carried.getCount(), max - target.getCount());

        if (canMove <= 0) return 0;

        long now = CommonHook.getClockTime(level);

        RotData carriedRot = RotData.getOrCreate(level, carried, now);
        RotData targetRot = RotData.getOrCreate(level, target, now);

        if (carriedRot == null || targetRot == null) return 0;

        RotData merged = RotData.mergeRotData(
                targetRot,
                target.getCount(),
                carriedRot,
                canMove,
                now
        );

        target.grow(canMove);
        carried.shrink(canMove);
        target.set(ModDataComponents.ROT_DATA, merged);

        return canMove;
    }
}
