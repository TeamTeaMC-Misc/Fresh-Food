package com.teamtea.rotborn.mixin.client;

import com.teamtea.rotborn.client.RotItemDecorator;
import com.teamtea.rotborn.registry.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiGraphicsExtractor.class)
public abstract class GuiGraphicsMixin {

    // @Inject(
    //         method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
    //         at = @At("TAIL")
    // )
    // private void better_food_overlay(LivingEntity owner, Level level, ItemStack itemStack, int x, int y, int seed, CallbackInfo ci) {
    //     // if (!FoodExpireClient.shouldRender(stack)) return;
    //     //
    //     // GuiGraphics g = (GuiGraphics) (Object) this;
    //     //
    //     // float progress = FoodExpireClient.getExpireProgress(stack); // 0~1
    //     //
    //     // int color;
    //     // if (progress < 0.33f) {
    //     //     color = 0x6600FF00; // green
    //     // } else if (progress < 0.66f) {
    //     //     color = 0x66FFFF00; // yellow
    //     // } else {
    //     //     color = 0x66FF0000; // red
    //     // }
    //     //
    //     // // 左下角画一条 1~13px 的腐烂进度条
    //     // int width = Math.max(1, (int) (13 * progress));
    //     //
    //     // g.fill(x + 2, y + 13, x + 2 + width, y + 15, color);
    // }

    @Inject(
            method = "itemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
            at = @At(value = "INVOKE",
                    shift = At.Shift.AFTER,
                    target = "Lnet/neoforged/neoforge/client/ItemDecoratorHandler;render(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;II)V")
    )
    private void rot$afterNeoForgeDecorators(
            Font font, ItemStack stack, int x, int y, String countText, CallbackInfo ci
    ) {
        if (RotItemDecorator.DECORATOR.rotOverlayStyle.isAfter()) {
            RotItemDecorator.DECORATOR.render((GuiGraphicsExtractor) (Object) this, font, stack, x, y);
        }
    }

    @Inject(
            method = "item(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;III)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/state/gui/GuiRenderState;addItem(Lnet/minecraft/client/renderer/state/gui/GuiItemRenderState;)V"
            )
    )
    private void yourmod$renderRotBackground(
            @Nullable LivingEntity owner,
            @Nullable Level level,
            ItemStack stack,
            int x,
            int y,
            int seed,
            CallbackInfo ci
    ) {
        if (!RotItemDecorator.DECORATOR.rotOverlayStyle.isAfter())
            RotItemDecorator.DECORATOR.render((GuiGraphicsExtractor) (Object) this, Minecraft.getInstance().font, stack, x, y);
    }


}