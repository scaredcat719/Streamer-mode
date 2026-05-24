package dev.miami.streamer.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.miami.streamer.StreamerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {

    @Inject(method = "extractLines", at = @At("HEAD"))
    private void filterDebugText(GuiGraphicsExtractor graphics, List<String> lines, boolean alignLeft, CallbackInfo ci, @Local(name = "lines", argsOnly = true) LocalRef<List<String>> linesRef) {
        if (StreamerConfig.getInstance().isEnabled() && StreamerConfig.getInstance().isHideCoords()) {
            List<String> filtered;
            filtered = lines.stream()
                    .filter(line -> !line.startsWith("XYZ:")
                            && !line.startsWith("Block:")
                            && !line.startsWith("Chunk:")
                            && !line.startsWith("Facing:")
                            && !line.startsWith("Looking At:")
                            && !line.startsWith("Chunks")
                            && !line.contains("Targeted Block")
                            && !line.contains("Targeted Fluid")
                            && !line.contains("Hit result"))
                    .collect(Collectors.toList());
            linesRef.set(filtered);
        }
    }
}
