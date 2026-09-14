/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import net.minecraft.client.renderer.LightTexture;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={LightTexture.class})
public class LightTextureMixin {
    @ModifyArg(method={"updateLightTexture"}, at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/platform/NativeImage;setPixelRGBA(III)V"), index=2)
    private int onSetColor(int color) {
        if (Freecam.isEnabled() && ((Boolean)FreecamConfig.FULL_BRIGHTNESS.get()).booleanValue()) {
            return -1;
        }
        return color;
    }
}

