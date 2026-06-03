package com.teamtea.rotborn.core;

import net.minecraft.world.item.ItemStack;

public record ProcessResult(ItemStack stack, boolean changed) {
}
