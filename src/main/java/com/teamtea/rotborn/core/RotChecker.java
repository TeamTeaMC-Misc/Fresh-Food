package com.teamtea.rotborn.core;

import com.teamtea.rotborn.registry.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class RotChecker {
    private static boolean canRot(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        }
        if (stack.is(RotTags.NEVER_ROTS)) {
            return false;
        }
        if (stack.has(DataComponents.FOOD)) {
            return true;
        }
        if (stack.is(Items.EGG)) {
            return true;
        }
        return false;
    }

    public static boolean isMeat(Item item) {
        return item == Items.BEEF
                || item == Items.COOKED_BEEF
                || item == Items.PORKCHOP
                || item == Items.COOKED_PORKCHOP
                || item == Items.CHICKEN
                || item == Items.COOKED_CHICKEN
                || item == Items.MUTTON
                || item == Items.COOKED_MUTTON
                || item == Items.RABBIT
                || item == Items.COOKED_RABBIT
                || item == Items.COD
                || item == Items.COOKED_COD
                || item == Items.SALMON
                || item == Items.COOKED_SALMON
                || item == Items.TROPICAL_FISH
                || item == Items.PUFFERFISH;
    }

    public static boolean isFruit(Item item) {
        return item == Items.APPLE
                || item == Items.GOLDEN_APPLE
                || item == Items.ENCHANTED_GOLDEN_APPLE
                || item == Items.MELON_SLICE
                || item == Items.SWEET_BERRIES
                || item == Items.GLOW_BERRIES
                || item == Items.CHORUS_FRUIT;
    }

    public static boolean isVegetable(Item item) {
        return item == Items.CARROT
                || item == Items.GOLDEN_CARROT
                || item == Items.POTATO
                || item == Items.BAKED_POTATO
                || item == Items.BEETROOT
                || item == Items.DRIED_KELP;
    }

    public static boolean isBread(Item item) {
        return item == Items.BREAD
                || item == Items.COOKIE
                || item == Items.CAKE
                || item == Items.PUMPKIN_PIE;
    }

    public static boolean isStew(Item item) {
        return item == Items.MUSHROOM_STEW
                || item == Items.RABBIT_STEW
                || item == Items.BEETROOT_SOUP
                || item == Items.SUSPICIOUS_STEW;
    }

    public static boolean isMushroom(Item item) {
        return item == Items.BROWN_MUSHROOM
                || item == Items.RED_MUSHROOM;
    }
}
