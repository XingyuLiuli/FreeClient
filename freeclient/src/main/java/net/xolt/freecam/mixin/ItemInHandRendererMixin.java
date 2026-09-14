/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.xolt.freecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ItemInHandRenderer.class})
public class ItemInHandRendererMixin {
    private float tickDelta;

    @ModifyVariable(method={"renderHandsWithItems"}, at=@At(value="HEAD"), argsOnly=true)
    private LocalPlayer onRenderItem(LocalPlayer player) {
        if (Freecam.isEnabled()) {
            return Freecam.getFreeCamera();
        }
        return player;
    }

    @Inject(method={"renderHandsWithItems"}, at={@At(value="HEAD")})
    private void storeTickDelta(float tickDelta, PoseStack matrices, MultiBufferSource.BufferSource vertexConsumers, LocalPlayer player, int light, CallbackInfo ci) {
        this.tickDelta = tickDelta;
    }

    @ModifyVariable(method={"renderHandsWithItems"}, at=@At(value="HEAD"), argsOnly=true)
    private int onRenderItem2(int light) {
        if (Freecam.isEnabled()) {
            return Freecam.MC.getEntityRenderDispatcher().getPackedLightCoords((Entity)Freecam.getFreeCamera(), this.tickDelta);
        }
        return light;
    }
}

