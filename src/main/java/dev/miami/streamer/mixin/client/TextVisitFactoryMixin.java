package dev.miami.streamer.mixin.client;

import dev.miami.streamer.StreamerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TextVisitFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(TextVisitFactory.class)
public class TextVisitFactoryMixin {
	@Unique private static final MinecraftClient mc = MinecraftClient.getInstance();

	@ModifyArg(
			method = "visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/text/TextVisitFactory;visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z",
					ordinal = 0
			),
			index = 0
	)
	private static String yucky(String string) {
		if (mc == null || mc.getSession() == null) return string;
		StreamerConfig config = StreamerConfig.getInstance();
		if (!config.isEnabled()) return string;
		string = string.replace(mc.getSession().getUsername(), config.getSelfName());
		if (config.isHideOthers() && mc.getNetworkHandler() != null) {
			for (var entry : mc.getNetworkHandler().getPlayerList()) {
				String name = entry.getProfile().name();
				if (!name.equals(mc.getSession().getUsername())) {
					string = string.replace(name, config.getOthersName());
				}
			}
		}

		return string;
	}
}