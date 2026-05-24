package dev.miami.streamer.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.miami.streamer.StreamerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.ClientAsset;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(PlayerInfo.class)
public class PlayerInfoMixin {

    @Shadow @Final private GameProfile profile;

    @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
    private void hideSkin(CallbackInfoReturnable<PlayerSkin> ci) {
        StreamerConfig cfg = StreamerConfig.getInstance();
        if (!cfg.isEnabled() || !cfg.isHideSkin()) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (!profile.id().equals(mc.player.getGameProfile().id())) return;

        PlayerSkin original = ci.getReturnValue();
        boolean slim = original.model() == PlayerModelType.SLIM;

        ClientAsset.Texture replacementBody = new ClientAsset.ResourceTexture(
                Identifier.fromNamespaceAndPath("streamer", "skins/" + (slim ? "alex" : "steve"))
        );

        ci.setReturnValue(new PlayerSkin(
                replacementBody,
                original.cape(),
                original.elytra(),
                slim ? PlayerModelType.SLIM : PlayerModelType.WIDE,
                original.secure()
        ));
    }
}