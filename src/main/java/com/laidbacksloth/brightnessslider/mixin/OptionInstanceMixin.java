package com.laidbacksloth.brightnessslider.mixin;

import com.laidbacksloth.brightnessslider.BetterBrightnessSlider;
import com.laidbacksloth.brightnessslider.BetterSlider;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.OptionInstance.ValueSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;
import java.util.function.Function;

@Mixin(OptionInstance.class)
public class OptionInstanceMixin {
    @Shadow
    @Final
    Component caption;

    @Shadow
    @Final
    @Mutable
    Function<Double, Component> toString;

    @Shadow
    @Final
    @Mutable
    private ValueSet<Double> values;

    @Shadow
    @Final
    @Mutable
    private Codec<Double> codec;

    @Shadow
    @Final
    @Mutable
    private Consumer<Double> onValueUpdate;

    @Inject(at = @At("RETURN"), method = "<init>*", remap = false)
    protected void init(CallbackInfo info) {
        if (this.caption.getContents() instanceof TranslatableContents translatableContents && translatableContents.getKey().equals("options.gamma")) {
            this.onValueUpdate = this::onValueUpdate;
            this.toString = this::toString;
            this.values = BetterSlider.INSTANCE;
            this.codec = this.values.codec();
        }
    }

    private Component toString(Double gamma) {
        return Component.translatable("options.gamma").append(": ").append(Component.literal(Math.round(gamma * 100) + "%"));
    }

    private void onValueUpdate(Double brightness) {
        brightness = Math.round(brightness / BetterBrightnessSlider.interval) * BetterBrightnessSlider.interval;
        Minecraft.getInstance().options.gamma().set(brightness);
    }
}
