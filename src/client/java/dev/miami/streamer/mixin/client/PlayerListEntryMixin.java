package dev.miami.streamer.mixin.client;

import com.mojang.authlib.GameProfile;
import dev.miami.streamer.StreamerConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.entity.player.PlayerSkinType;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerListEntry.class)
public class PlayerListEntryMixin {

    @Shadow @Final private GameProfile profile;

    @Inject(method = "getSkinTextures", at = @At("RETURN"), cancellable = true)
    private void hideSkin(CallbackInfoReturnable<SkinTextures> ci) {
        StreamerConfig cfg = StreamerConfig.getInstance();
        if (!cfg.isEnabled() || !cfg.isHideSkin()) return;

        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        if (!profile.id().equals(mc.player.getGameProfile().id())) return;

        SkinTextures original = ci.getReturnValue();
        boolean slim = original.model() == PlayerSkinType.SLIM;

        AssetInfo.TextureAssetInfo replacementBody = new AssetInfo.TextureAssetInfo(
                Identifier.of("streamer", "skins/" + (slim ? "alex" : "steve"))
        );

        ci.setReturnValue(new SkinTextures(
                replacementBody,
                original.cape(),
                original.elytra(),
                slim ? PlayerSkinType.SLIM : PlayerSkinType.WIDE,
                original.secure()
        ));
    }
}