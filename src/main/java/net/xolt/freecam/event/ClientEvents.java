/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.event;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.ConfigScreen;

public class ClientEvents {

    @Mod.EventBusSubscriber(modid="freeclient", value={Dist.CLIENT}, bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModBusEvents {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(Freecam.KEY_TOGGLE);
            event.register(Freecam.KEY_PLAYER_CONTROL);
            event.register(Freecam.KEY_TRIPOD_RESET);
            event.register(Freecam.KEY_CONFIG_GUI);
        }
    }

    @Mod.EventBusSubscriber(modid="freeclient", value={Dist.CLIENT})
    public static class ClientForgeEvents {
        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
            if (Freecam.KEY_TRIPOD_RESET.isDown()) {
                for (KeyMapping hotbarKey : Freecam.MC.options.keyHotbarSlots) {
                    while (hotbarKey.consumeClick()) {
                        Freecam.resetCamera(hotbarKey.getDefaultKey().getValue());
                        while (Freecam.KEY_TRIPOD_RESET.consumeClick()) {
                        }
                    }
                }
            }
            if (Freecam.KEY_TOGGLE.isDown()) {
                for (KeyMapping hotbarKey : Freecam.MC.options.keyHotbarSlots) {
                    while (hotbarKey.consumeClick()) {
                        Freecam.toggleTripod(hotbarKey.getDefaultKey().getValue());
                        while (Freecam.KEY_TOGGLE.consumeClick()) {
                        }
                    }
                }
            } else if (Freecam.KEY_TOGGLE.consumeClick()) {
                Freecam.toggle();
                while (Freecam.KEY_TOGGLE.consumeClick()) {
                }
            }
            while (Freecam.KEY_PLAYER_CONTROL.consumeClick()) {
                Freecam.switchControls();
            }
            while (Freecam.KEY_CONFIG_GUI.consumeClick()) {
                Freecam.MC.setScreen((Screen)new ConfigScreen(Freecam.MC.screen));
            }
        }
    }
}

