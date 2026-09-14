/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import net.xolt.freecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Gui.class})
public class GuiMixin {
    @Inject(method={"getCameraPlayer"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetCameraPlayer(CallbackInfoReturnable<Player> cir) {
        if (Freecam.isEnabled()) {
            cir.setReturnValue(Freecam.MC.player);
        }
    }
}

