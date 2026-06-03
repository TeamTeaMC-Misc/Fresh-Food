package com.teamtea.rotborn.api;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public enum FreshnessLevel {
    FRESH(0.5F, "fresh_days", ChatFormatting.GREEN, 0x884A8F2A),
    STALE(0.2F, "stale_days", ChatFormatting.YELLOW, 0xAA9A9226),
    SPOILING(0.1F, "spoiling_days", ChatFormatting.RED, 0xCC9B5D1A),
    SPOILED(0.0F, "spoiled", ChatFormatting.DARK_RED, 0xDD5A1A16);

    private final float minFreshness;
    private final String tooltipKey;
    private final ChatFormatting textColor;
    private final int backgroundColor;

    FreshnessLevel(float minFreshness, String tooltipKey,
                   ChatFormatting textColor, int backgroundColor) {
        this.minFreshness = minFreshness;
        this.tooltipKey = tooltipKey;
        this.textColor = textColor;
        this.backgroundColor = backgroundColor;
    }

    public ChatFormatting textColor() {
        return textColor;
    }

    public int backgroundColor() {
        return backgroundColor;
    }

    public Component tooltip(long remainingDays) {
        String key = "tooltip.rotborn.freshness." + tooltipKey;
        if (this == SPOILED) {
            return Component.translatable(key).withStyle(textColor);
        }
        return Component.translatable(key, remainingDays).withStyle(textColor);
    }

    public static FreshnessLevel fromFreshness(float freshness) {
        float value = Mth.clamp(freshness, 0.0F, 1.0F);
        for (FreshnessLevel level : values()) {
            if (value > level.minFreshness) {
                return level;
            }
        }
        return SPOILED;
    }
}