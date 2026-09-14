/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import net.minecraft.network.Connection;
import net.xolt.freecam.Freecam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={Connection.class})
public class ConnectionMixin {
    @Inject(method={"handleDisconnection"}, at={@At(value="HEAD")})
    private void onHandleDisconnection(CallbackInfo ci) {
        if (Freecam.isEnabled()) {
            Freecam.toggle();
        }
        Freecam.clearTripods();
    }
}

