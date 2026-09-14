package com.laidbacksloth.brightnessslider;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.client.OptionInstance.SliderableValueSet;

import java.util.Optional;

public enum BetterSlider implements SliderableValueSet<Double> {
    INSTANCE;

    @Override
    public double toSliderValue(Double double_) {
        return (double_ - BetterBrightnessSlider.min) / (BetterBrightnessSlider.max - BetterBrightnessSlider.min);
    }

    @Override
    public Double fromSliderValue(double double_) {
        return double_ * (BetterBrightnessSlider.max - BetterBrightnessSlider.min) + BetterBrightnessSlider.min;
    }

    @Override
    public Optional<Double> validateValue(Double double_) {
        return double_ >= BetterBrightnessSlider.min && double_ <= BetterBrightnessSlider.max ? Optional.of(double_) : Optional.empty();
    }

    @Override
    public Codec<Double> codec() {
        return Codec.either(Codec.doubleRange(BetterBrightnessSlider.min, BetterBrightnessSlider.max), Codec.BOOL).xmap(either -> either.map(value -> value, value -> value ? 1.0 : 0.0), Either::left);
    }
}
