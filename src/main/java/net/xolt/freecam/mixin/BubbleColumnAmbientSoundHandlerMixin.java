/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 *
 * 注意：@Shadow 字段在 Forge 生产环境不经 refmap 重映射，必须写运行时 SRG 名
 * （与官方 freecam-forge-1.2.1+1.20 产物字节码一致）：player = f_119662_
 */
package net.xolt.freecam.mixin;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.BubbleColumnAmbientSoundHandler;
import net.xolt.freecam.util.FreeCamera;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={BubbleColumnAmbientSoundHandler.class})
public class BubbleColumnAmbientSoundHandlerMixin {
    @Shadow
    @Final
    private LocalPlayer f_119662_;

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTick(CallbackInfo ci) {
        if (this.f_119662_ instanceof FreeCamera) {
            ci.cancel();
        }
    }
}
