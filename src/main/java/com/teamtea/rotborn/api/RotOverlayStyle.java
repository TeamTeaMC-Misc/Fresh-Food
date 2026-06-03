package com.teamtea.rotborn.api;

public enum RotOverlayStyle {
    FULL_FILL,
    SIDE_BAR,
    BOTTOM_BAR;

    public boolean isAfter() {
        return this != FULL_FILL;
    }
}