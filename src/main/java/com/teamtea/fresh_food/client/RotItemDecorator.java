package com.teamtea.fresh_food.client;

import com.teamtea.fresh_food.CommonHook;
import com.teamtea.fresh_food.api.FreshnessLevel;
import com.teamtea.fresh_food.api.RotOverlayStyle;
import com.teamtea.fresh_food.core.RotData;
import com.teamtea.fresh_food.registry.ModDataComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.IItemDecorator;
import org.jspecify.annotations.NonNull;

public class RotItemDecorator implements IItemDecorator {

    public static final RotItemDecorator DECORATOR = new RotItemDecorator();

    public RotOverlayStyle rotOverlayStyle = RotOverlayStyle.FULL_FILL;

    @Override
    public boolean render(
            @NonNull GuiGraphicsExtractor graphics,
            @NonNull Font font,
            ItemStack stack,
            int x,
            int y
    ) {
        RotData rot = stack.get(ModDataComponents.ROT_DATA.get());
        if (rot == null) {
            return false;
        }

        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) {
            return false;
        }

        long now = CommonHook.getClockTime(mc.level);
        float rotProgress = Mth.clamp(
                (float) (now - rot.startGameTime()) / (float) rot.rotAfterTicks(),
                0.0F,
                1.0F
        );

        float freshness = 1.0F - rotProgress;
        FreshnessLevel level = FreshnessLevel.fromFreshness(freshness);
        int color = level.backgroundColor();

        switch (rotOverlayStyle) {
            case FULL_FILL -> renderFullFill(graphics, x, y, freshness, color);
            case SIDE_BAR -> renderSideBar(graphics, x, y, freshness, color);
            case BOTTOM_BAR -> renderBottomBar(graphics, x, y, freshness, color);
        }


//         long now = mc.level.getDefaultClockTime();
//
//         float rotProgress = Mth.clamp(
//                 (float) (now - rot.startGameTime()) / (float) rot.rotAfterTicks(),
//                 0.0F,
//                 1.0F
//         );
//
//         // // 1.0 = fresh, 0.0 = rotten
//         // float freshness = 1.0F - rotProgress;
//         //
//         // int width = (int) (13.0F * freshness);
//         //
//         // int color = freshness > 0.5F
//         //         ? 0xFF55FF55
//         //         : freshness > 0.2F
//         //         ? 0xFFFFFF55
//         //         : 0xFFFF5555;
//         //
//         // // background
//         // graphics.fill(RenderPipelines.GUI, x + 2, y + 13, x + 15, y + 15, 0xFF000000);
//         //
//         // // freshness bar
//         // if (width > 0) {
//         //     graphics.fill(RenderPipelines.GUI, x + 2, y + 13, x + 2 + width, y + 14, color);
//         // }
//
// //         // 1.0 = fresh, 0.0 = rotten
// //         float freshness = 1.0F - rotProgress;
// //
// //         int height = (int) (16.0F * freshness);
// //
// //         int color = freshness > 0.5F
// //                 ? 0xFF55FF55
// //                 : freshness > 0.2F
// //                 ? 0xFFFFFF55
// //                 : 0xFFFF5555;
// //
// // // 左侧背景
// //         graphics.fill(RenderPipelines.GUI, x, y, x + 2, y + 16, 0xAA000000);
// //
// // // 从下往上填充
// //         if (height > 0) {
// //             graphics.fill(
// //                     RenderPipelines.GUI,
// //                     x,
// //                     y + 16 - height,
// //                     x + 2,
// //                     y + 16,
// //                     color
// //             );
// //         }
//
//
//
// // 腐烂覆盖高度：0~16
//
//          rotProgress = Mth.clamp(
//                 (float) (now - rot.startGameTime()) / (float) rot.rotAfterTicks(),
//                 0.0F,
//                 1.0F
//         );
//
// // 1.0 = fresh, 0.0 = rotten
//         float freshness = 1.0F - rotProgress;
//
//         int height = (int) (16.0F * freshness);
//
//         int color =  FreshnessLevel.fromFreshness(freshness).backgroundColor();
//
//                 // rotProgress < 0.20F
//                 //         ? 0x884A8F2A // Fresh - 深绿色
//                 //         : rotProgress < 0.50F
//                 //         ? 0xAA9A9226 // Stale - 黄绿色
//                 //         : rotProgress < 0.80F
//                 //         ? 0xCC9B5D1A // Spoiled - 棕黄色
//                 //         : 0xDD5A1A16; // Rotten - 暗腐红
//
// // 可选：淡淡的背景框
// //         graphics.fill(RenderPipelines.GUI, x, y, x + 16, y + 16, 0x22000000);
//
// // 从上往下覆盖
//         if (height > 0) {
//             graphics.fill(
//                     RenderPipelines.GUI,
//                     x,
//                     y + 16 - height,
//                     x + 16,
//                     y + 16,
//                     color
//             );
//         }
        return true;
    }


    private static void renderBottomBar(GuiGraphicsExtractor graphics, int x, int y,
                                        float freshness, int color) {
        int width = Mth.clamp((int) (13.0F * freshness), 0, 13);
        graphics.fill(RenderPipelines.GUI, x + 2, y + 13, x + 15, y + 15, 0xFF000000);

        if (width > 0) {
            graphics.fill(RenderPipelines.GUI, x + 2, y + 13, x + 2 + width, y + 14, color);
        }
    }

    private static void renderSideBar(GuiGraphicsExtractor graphics, int x, int y,
                                      float freshness, int color) {
        int height = Mth.clamp((int) (16.0F * freshness), 0, 16);
        graphics.fill(RenderPipelines.GUI, x, y, x + 2, y + 16, 0xAA000000);

        if (height > 0) {
            graphics.fill(RenderPipelines.GUI, x, y + 16 - height, x + 2, y + 16, color);
        }
    }

    private static void renderFullFill(GuiGraphicsExtractor graphics, int x, int y,
                                       float freshness, int color) {
        int height = Mth.clamp((int) (16.0F * freshness), 0, 16);
        if (height > 0) {
            graphics.fill(RenderPipelines.GUI, x, y + 16 - height, x + 16, y + 16, color);
        }
    }
}