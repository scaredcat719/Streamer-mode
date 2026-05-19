package dev.miami.streamer.mixin.client;

import dev.miami.streamer.StreamerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.DebugHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
@Mixin(DebugHud.class)
public class DebugHudMixin {

    @Redirect(
            method = "render",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/DebugHud;drawText(Lnet/minecraft/client/gui/DrawContext;Ljava/util/List;Z)V")
    )
    private void filterDebugText(DebugHud instance, DrawContext context, List<String> text, boolean left) {
        if (!StreamerConfig.getInstance().isEnabled() || !StreamerConfig.getInstance().isHideCoords()) {
            drawText(context, text, left);
            return;
        }

        List<String> filtered;
        if (left) {
            filtered = text.stream()
                    .filter(line -> !line.startsWith("XYZ:")
                            && !line.startsWith("Block:")
                            && !line.startsWith("Chunk:")
                            && !line.startsWith("Facing:")
                            && !line.startsWith("Looking At:")
                            && !line.startsWith("Chunks"))
                    .collect(Collectors.toList());
        } else {
            filtered = text.stream()
                    .filter(line -> !line.contains("Targeted Block")
                            && !line.contains("Targeted Fluid")
                            && !line.contains("Hit result"))
                    .collect(Collectors.toList());
        }

        drawText(context, filtered, left);
    }

    @Shadow private void drawText(DrawContext context, List<String> text, boolean left) {}
}