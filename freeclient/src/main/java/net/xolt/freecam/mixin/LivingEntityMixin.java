/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 *
 * 注意：在 Forge（modlauncher）生产环境下，mixin 0.8.5 对 @Shadow 成员名不做
 * refmap 重映射（RemapperChain 为空），shadow 声明必须直接使用运行时可见的
 * SRG 名，这与官方 freecam-forge-1.2.1+1.20 产物的字节码完全一致。
 *   getHealth() = m_21223_()F
 */
package net.xolt.freecam.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={LivingEntity.class})
public abstract class LivingEntityMixin {
    @Shadow
    public abstract float m_21223_();

    @Inject(method={"getFrictionInfluencedSpeed"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetMovementSpeed(CallbackInfoReturnable<Float> cir) {
        if (Freecam.isEnabled() && ((FreecamConfig.FlightMode)((Object)FreecamConfig.FLIGHT_MODE.get())).equals((Object)FreecamConfig.FlightMode.CREATIVE) && this.equals((Object)Freecam.getFreeCamera())) {
            cir.setReturnValue(Float.valueOf((float)((Double)FreecamConfig.HORIZONTAL_SPEED.get() / 10.0) * (float)(Freecam.getFreeCamera().isSprinting() ? 2 : 1)));
        }
    }

    @Inject(method={"setHealth"}, at={@At(value="HEAD")})
    private void onSetHealth(float health, CallbackInfo ci) {
        if (Freecam.isEnabled() && ((Boolean)FreecamConfig.DISABLE_ON_DAMAGE.get()).booleanValue() && this.equals(Freecam.MC.player) && !Freecam.MC.player.isCreative() && this.m_21223_() > health) {
            Freecam.setDisableNextTick(true);
        }
    }
}
