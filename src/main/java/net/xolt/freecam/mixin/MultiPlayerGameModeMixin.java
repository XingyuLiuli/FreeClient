/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.mixin;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={MultiPlayerGameMode.class})
public class MultiPlayerGameModeMixin {
    @Inject(method={"useItemOn"}, at={@At(value="HEAD")}, cancellable=true)
    private void onInteractBlock(LocalPlayer player, InteractionHand hand, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (Freecam.isEnabled() && !Freecam.isPlayerControlEnabled() && !((Boolean)FreecamConfig.ALLOW_INTERACT.get()).booleanValue()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method={"interact"}, at={@At(value="HEAD")}, cancellable=true)
    private void onInteractEntity(Player player, Entity entity, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (entity.equals((Object)Freecam.MC.player) || Freecam.isEnabled() && !Freecam.isPlayerControlEnabled() && !((Boolean)FreecamConfig.ALLOW_INTERACT.get()).booleanValue()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method={"interactAt"}, at={@At(value="HEAD")}, cancellable=true)
    private void onInteractEntityAtLocation(Player player, Entity entity, EntityHitResult hitResult, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {
        if (entity.equals((Object)Freecam.MC.player) || Freecam.isEnabled() && !Freecam.isPlayerControlEnabled() && !((Boolean)FreecamConfig.ALLOW_INTERACT.get()).booleanValue()) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }

    @Inject(method={"attack"}, at={@At(value="HEAD")}, cancellable=true)
    private void onAttackEntity(Player player, Entity target, CallbackInfo ci) {
        if (target.equals((Object)Freecam.MC.player)) {
            ci.cancel();
        }
    }
}

