package com.teamtea.fresh_food.core;

import net.minecraft.world.item.ItemStack;

public record ProcessResult(ItemStack stack, boolean changed) {
}
