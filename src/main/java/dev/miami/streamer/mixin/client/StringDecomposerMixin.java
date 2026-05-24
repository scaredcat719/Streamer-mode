package dev.miami.streamer.mixin.client;

import dev.miami.streamer.StreamerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.util.StringDecomposer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(StringDecomposer.class)
public class StringDecomposerMixin {
    @ModifyArg(
            method = "iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/StringDecomposer;iterateFormatted(Ljava/lang/String;ILnet/minecraft/network/chat/Style;Lnet/minecraft/network/chat/Style;Lnet/minecraft/util/FormattedCharSink;)Z",
                    ordinal = 0
            ),
            index = 0
    )
    private static String yucky(String string) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.getUser() == null) return string;
        StreamerConfig config = StreamerConfig.getInstance();
        if (!config.isEnabled()) return string;
        string = string.replace(mc.getUser().getName(), config.getSelfName());
        if (config.isHideOthers() && mc.getConnection() != null) {
            for (PlayerInfo entry : mc.getConnection().getOnlinePlayers()) {
                String name = entry.getProfile().name();
                if (!name.equals(mc.getUser().getName())) {
                    string = string.replace(name, config.getOthersName());
                }
            }
        }

        return string;
    }
}