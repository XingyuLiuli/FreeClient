package com.thegameratort.fireoverlaycontroller.mixin;

import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.xolt.freecam.config.FreecamConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

/**
 * McFireOverlayController（Fabric/yarn）移植到 Forge 1.20.1（mojmap）：
 * <ul>
 *   <li>yarn InGameOverlayRenderer.renderFireOverlay(MatrixStack, VertexConsumerProvider)
 *       → mojmap ScreenEffectRenderer.renderFire(Minecraft, PoseStack)</li>
 *   <li>配置改由 freeclient 的 FreecamConfig 提供（fireOpacity 默认 0.5 / fireHeight 默认 0.4）</li>
 *   <li>注入点与原版一致：VertexConsumer.color(FFFF) 的 alpha（index 3）、
 *       PoseStack.translate(FFF) 的 y（index 1，原常量 -0.3f 改为 -1.0f + fireHeight）</li>
 * </ul>
 */
@Mixin(ScreenEffectRenderer.class)
public abstract class InGameOverlayRendererMixin {
	@ModifyArg(
		method = "renderFire(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;color(FFFF)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
		), index = 3
	)
	private static float renderFireOverlay_opacity(float alpha) {
		return FreecamConfig.FIRE_OPACITY.get().floatValue();
	}

	@ModifyArg(
		method = "renderFire(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V",
		at = @At(
			value = "INVOKE",
			target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"
		), index = 1
	)
	private static float renderFireOverlay_translate(float y) {
		return -1.0F + FreecamConfig.FIRE_HEIGHT.get().floatValue();
	}
}
