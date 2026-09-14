/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 *
 * 注意：@Shadow 字段在 Forge 生产环境不经 refmap 重映射，必须写运行时 SRG 名
 * （与官方 freecam-forge-1.2.1+1.20 产物字节码一致）：
 *   entity = f_90551_   eyeHeightOld = f_90563_   eyeHeight = f_90562_
 */
package net.xolt.freecam.mixin;

import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FogType;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import net.xolt.freecam.util.FreeCamera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={Camera.class})
public class CameraMixin {
    @Shadow
    private Entity f_90551_;
    @Shadow
    private float f_90563_;
    @Shadow
    private float f_90562_;

    @Inject(method={"setup"}, at={@At(value="HEAD")})
    public void onUpdate(BlockGetter area, Entity newFocusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo ci) {
        if (newFocusedEntity == null || this.f_90551_ == null || newFocusedEntity.equals((Object)this.f_90551_)) {
            return;
        }
        if (newFocusedEntity instanceof FreeCamera || this.f_90551_ instanceof FreeCamera) {
            this.f_90563_ = this.f_90562_ = newFocusedEntity.getEyeHeight();
        }
    }

    @Inject(method={"getFluidInCamera"}, at={@At(value="HEAD")}, cancellable=true)
    public void onGetSubmersionType(CallbackInfoReturnable<FogType> cir) {
        if (Freecam.isEnabled() && !((Boolean)FreecamConfig.SHOW_SUBMERSION_FOG.get()).booleanValue()) {
            cir.setReturnValue(FogType.NONE);
        }
    }
}
