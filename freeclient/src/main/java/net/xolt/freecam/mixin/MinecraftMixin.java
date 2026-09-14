/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Minecraft.class})
public class MinecraftMixin {
    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void onTick(CallbackInfo ci) {
        if (Freecam.isEnabled()) {
            if (Freecam.MC.player != null && Freecam.MC.player.input instanceof KeyboardInput && !Freecam.isPlayerControlEnabled()) {
                Input input = new Input();
                input.shiftKeyDown = Freecam.MC.player.input.shiftKeyDown;
                Freecam.MC.player.input = input;
            }
            Freecam.MC.gameRenderer.setRenderHand(((Boolean)FreecamConfig.SHOW_HAND.get()).booleanValue());
            if (Freecam.disableNextTick()) {
                Freecam.toggle();
                Freecam.setDisableNextTick(false);
            }
        }
    }

    @Inject(method={"startAttack"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDoAttack(CallbackInfoReturnable<Boolean> cir) {
        if (Freecam.isEnabled() && !Freecam.isPlayerControlEnabled() && !((Boolean)FreecamConfig.ALLOW_INTERACT.get()).booleanValue()) {
            cir.cancel();
        }
    }

    @Inject(method={"pickBlock"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDoItemPick(CallbackInfo ci) {
        if (Freecam.isEnabled() && !Freecam.isPlayerControlEnabled() && !((Boolean)FreecamConfig.ALLOW_INTERACT.get()).booleanValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"continueAttack"}, at={@At(value="HEAD")}, cancellable=true)
    private void onHandleBlockBreaking(CallbackInfo ci) {
        if (Freecam.isEnabled() && !Freecam.isPlayerControlEnabled() && !((Boolean)FreecamConfig.ALLOW_INTERACT.get()).booleanValue()) {
            ci.cancel();
        }
    }

    @Inject(method={"handleKeybinds"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/KeyMapping;consumeClick()Z", ordinal=2)}, cancellable=true)
    private void onHandleInputEvents(CallbackInfo ci) {
        if (Freecam.KEY_TOGGLE.isDown() || Freecam.KEY_TRIPOD_RESET.isDown()) {
            ci.cancel();
        }
    }
}

