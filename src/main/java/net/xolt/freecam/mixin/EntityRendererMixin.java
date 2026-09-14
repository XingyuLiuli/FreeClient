/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.mixin.EntityRenderDispatcherAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={EntityRenderer.class})
public class EntityRendererMixin {
    @Inject(method={"renderNameTag"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderLabel(Entity entity, Component text, PoseStack matrices, MultiBufferSource vertexConsumers, int light, CallbackInfo ci) {
        if (Freecam.isEnabled() && !((EntityRenderDispatcherAccessor)Freecam.MC.getEntityRenderDispatcher()).getShouldRenderShadow()) {
            ci.cancel();
        }
    }
}

