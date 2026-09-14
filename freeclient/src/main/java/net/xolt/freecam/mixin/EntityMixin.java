/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import net.minecraft.world.entity.Entity;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Entity.class})
public class EntityMixin {
    @Inject(method={"turn"}, at={@At(value="HEAD")}, cancellable=true)
    private void onChangeLookDirection(double x, double y, CallbackInfo ci) {
        if (Freecam.isEnabled() && this.equals(Freecam.MC.player) && !Freecam.isPlayerControlEnabled()) {
            Freecam.getFreeCamera().turn(x, y);
            ci.cancel();
        }
    }

    @Inject(method={"push(Lnet/minecraft/world/entity/Entity;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPushAwayFrom(Entity entity, CallbackInfo ci) {
        if (Freecam.isEnabled() && (entity.equals((Object)Freecam.getFreeCamera()) || this.equals((Object)Freecam.getFreeCamera()))) {
            ci.cancel();
        }
    }

    @Inject(method={"setDeltaMovement"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetVelocity(CallbackInfo ci) {
        if (Freecam.isEnabled() && ((Boolean)FreecamConfig.FREEZE_PLAYER.get()).booleanValue() && !Freecam.isPlayerControlEnabled() && this.equals(Freecam.MC.player)) {
            ci.cancel();
        }
    }

    @Inject(method={"moveRelative"}, at={@At(value="HEAD")}, cancellable=true)
    private void onUpdateVelocity(CallbackInfo ci) {
        if (Freecam.isEnabled() && ((Boolean)FreecamConfig.FREEZE_PLAYER.get()).booleanValue() && !Freecam.isPlayerControlEnabled() && this.equals(Freecam.MC.player)) {
            ci.cancel();
        }
    }

    @Inject(method={"setPos"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetPosition(CallbackInfo ci) {
        if (Freecam.isEnabled() && ((Boolean)FreecamConfig.FREEZE_PLAYER.get()).booleanValue() && !Freecam.isPlayerControlEnabled() && this.equals(Freecam.MC.player)) {
            ci.cancel();
        }
    }

    @Inject(method={"setPos"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetPos(CallbackInfo ci) {
        if (Freecam.isEnabled() && ((Boolean)FreecamConfig.FREEZE_PLAYER.get()).booleanValue() && !Freecam.isPlayerControlEnabled() && this.equals(Freecam.MC.player)) {
            ci.cancel();
        }
    }

    @Inject(method={"setPosRaw"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetPosRaw(CallbackInfo ci) {
        if (Freecam.isEnabled() && ((Boolean)FreecamConfig.FREEZE_PLAYER.get()).booleanValue() && !Freecam.isPlayerControlEnabled() && this.equals(Freecam.MC.player)) {
            ci.cancel();
        }
    }
}

