/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.entity.Entity;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={GameRenderer.class})
public class GameRendererMixin {
    @Inject(method={"shouldRenderBlockOutline"}, at={@At(value="HEAD")}, cancellable=true)
    private void onShouldRenderBlockOutline(CallbackInfoReturnable<Boolean> cir) {
        if (Freecam.isEnabled() && !Freecam.isPlayerControlEnabled() && !((Boolean)FreecamConfig.ALLOW_INTERACT.get()).booleanValue()) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method={"pick"}, at=@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;"))
    private Entity onUpdateTargetedEntity(Entity entity) {
        if (Freecam.isEnabled() && (Freecam.isPlayerControlEnabled() || ((FreecamConfig.InteractionMode)((Object)FreecamConfig.INTERACTION_MODE.get())).equals((Object)FreecamConfig.InteractionMode.PLAYER))) {
            return Freecam.MC.player;
        }
        return entity;
    }
}

