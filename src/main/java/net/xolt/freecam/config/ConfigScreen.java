/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.config;

import com.mojang.serialization.Codec;
import java.util.Arrays;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.xolt.freecam.config.FreecamConfig;

public class ConfigScreen
extends Screen {
    private static final int TITLE_TOP_OFFSET = 8;
    private static final int OPTIONS_LIST_TOP_OFFSET = 24;
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BUTTON_SPACING = 5;
    private static final int DONE_BUTTON_WIDTH = 200;
    private static final int DONE_BUTTON_BOTTOM_OFFSET = 6;
    protected final Screen previous;

    public ConfigScreen(Screen previous) {
        this(previous, (Component)Component.translatable((String)"text.freeclient.configScreen.title"));
    }

    public ConfigScreen(Screen previous, Component title) {
        super(title);
        this.previous = previous;
    }

    protected void init() {
        int verticalOffset = 29;
        this.addRenderableWidget(Button.builder((Component)Component.translatable((String)"text.freeclient.configScreen.option.movement"), button -> this.minecraft.setScreen((Screen)new MovementOptionsScreen(this))).bounds((this.width - 150) / 2, verticalOffset, 150, 20).tooltip(Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.movement.@Tooltip"))).build());
        this.addRenderableWidget(Button.builder((Component)Component.translatable((String)"text.freeclient.configScreen.option.collision"), button -> this.minecraft.setScreen((Screen)new CollisionOptionsScreen(this))).bounds((this.width - 150) / 2, verticalOffset += 25, 150, 20).tooltip(Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.collision.@Tooltip"))).build());
        this.addRenderableWidget(Button.builder((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual"), button -> this.minecraft.setScreen((Screen)new VisualOptionsScreen(this))).bounds((this.width - 150) / 2, verticalOffset += 25, 150, 20).tooltip(Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual.@Tooltip"))).build());
        this.addRenderableWidget(Button.builder((Component)Component.translatable((String)"text.freeclient.configScreen.option.utility"), button -> this.minecraft.setScreen((Screen)new UtilityOptionsScreen(this))).bounds((this.width - 150) / 2, verticalOffset += 25, 150, 20).tooltip(Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.utility.@Tooltip"))).build());
        this.addRenderableWidget(Button.builder((Component)Component.translatable((String)"text.freeclient.configScreen.option.notification"), button -> this.minecraft.setScreen((Screen)new NotificationOptionsScreen(this))).bounds((this.width - 150) / 2, verticalOffset += 25, 150, 20).tooltip(Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.notification.@Tooltip"))).build());
        this.addRenderableWidget(Button.builder((Component)CommonComponents.GUI_DONE, button -> this.onClose()).bounds((this.width - 200) / 2, this.height - 20 - 6, 200, 20).build());
    }

    public void onClose() {
        this.minecraft.setScreen(this.previous);
    }

    public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(guiGraphics);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 0xFFFFFF);
        super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
    }

    private static class NotificationOptionsScreen
    extends OptionsListScreen {
        public NotificationOptionsScreen(Screen previous) {
            super(previous, (Component)Component.translatable((String)"text.freeclient.configScreen.option.notification"));
        }

        @Override
        protected void init() {
            super.init();
            OptionInstance<Boolean> notifyFreecam = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.notification.notifyFreecam", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.notification.notifyFreecam.@Tooltip")), (boolean)((Boolean)FreecamConfig.NOTIFY_FREECAM.get()), value -> FreecamConfig.NOTIFY_FREECAM.set(value));
            this.optionsList.addBig(notifyFreecam);
            OptionInstance<Boolean> notifyTripod = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.notification.notifyTripod", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.notification.notifyTripod.@Tooltip")), (boolean)((Boolean)FreecamConfig.NOTIFY_TRIPOD.get()), value -> FreecamConfig.NOTIFY_TRIPOD.set(value));
            this.optionsList.addBig(notifyTripod);
            this.addWidget(this.optionsList);
        }
    }

    private static class UtilityOptionsScreen
    extends OptionsListScreen {
        public UtilityOptionsScreen(Screen previous) {
            super(previous, (Component)Component.translatable((String)"text.freeclient.configScreen.option.utility"));
        }

        @Override
        protected void init() {
            super.init();
            OptionInstance<Boolean> disableOnDamage = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.utility.disableOnDamage", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.utility.disableOnDamage.@Tooltip")), (boolean)((Boolean)FreecamConfig.DISABLE_ON_DAMAGE.get()), value -> FreecamConfig.DISABLE_ON_DAMAGE.set(value));
            this.optionsList.addBig(disableOnDamage);
            OptionInstance<Boolean> freezePlayer = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.utility.freezePlayer", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.utility.freezePlayer.@Tooltip")), (boolean)((Boolean)FreecamConfig.FREEZE_PLAYER.get()), value -> FreecamConfig.FREEZE_PLAYER.set(value));
            this.optionsList.addBig(freezePlayer);
            OptionInstance<Boolean> allowInteract = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.utility.allowInteract", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.utility.allowInteract.@Tooltip")), (boolean)((Boolean)FreecamConfig.ALLOW_INTERACT.get()), value -> FreecamConfig.ALLOW_INTERACT.set(value));
            this.optionsList.addBig(allowInteract);
            OptionInstance<FreecamConfig.InteractionMode> interactionMode = new OptionInstance<>("text.freeclient.configScreen.option.utility.interactionMode", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.utility.interactionMode.@Tooltip")), (unused, option) -> Component.translatable((String)option.getKey()), new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.InteractionMode.values()), Codec.INT.xmap(FreecamConfig.InteractionMode::byId, FreecamConfig.InteractionMode::getId)), (FreecamConfig.InteractionMode)FreecamConfig.INTERACTION_MODE.get(), newValue -> FreecamConfig.INTERACTION_MODE.set(newValue));
            this.optionsList.addBig(interactionMode);
            this.addWidget(this.optionsList);
        }
    }

    private static class VisualOptionsScreen
    extends OptionsListScreen {
        public VisualOptionsScreen(Screen previous) {
            super(previous, (Component)Component.translatable((String)"text.freeclient.configScreen.option.visual"));
        }

        @Override
        protected void init() {
            super.init();
            OptionInstance<FreecamConfig.Perspective> perspective = new OptionInstance<>("text.freeclient.configScreen.option.visual.perspective", value2 -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual.perspective.@Tooltip")), (unused, option) -> Component.translatable((String)option.getKey()), new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.Perspective.values()), Codec.INT.xmap(FreecamConfig.Perspective::byId, FreecamConfig.Perspective::getId)), (FreecamConfig.Perspective)FreecamConfig.PERSPECTIVE.get(), newValue -> FreecamConfig.PERSPECTIVE.set(newValue));
            this.optionsList.addBig(perspective);
            OptionInstance<Boolean> showPlayer = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.visual.showPlayer", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual.showPlayer.@Tooltip")), (boolean)((Boolean)FreecamConfig.SHOW_PLAYER.get()), value -> FreecamConfig.SHOW_PLAYER.set(value));
            this.optionsList.addBig(showPlayer);
            OptionInstance<Boolean> showHand = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.visual.showHand", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual.showHand.@Tooltip")), (boolean)((Boolean)FreecamConfig.SHOW_HAND.get()), value -> FreecamConfig.SHOW_HAND.set(value));
            this.optionsList.addBig(showHand);
            OptionInstance<Boolean> fullBright = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.visual.fullBright", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual.fullBright.@Tooltip")), (boolean)((Boolean)FreecamConfig.FULL_BRIGHTNESS.get()), value -> FreecamConfig.FULL_BRIGHTNESS.set(value));
            this.optionsList.addBig(fullBright);
            OptionInstance<Boolean> showSubmersion = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.visual.showSubmersion", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual.showSubmersion.@Tooltip")), (boolean)((Boolean)FreecamConfig.SHOW_SUBMERSION_FOG.get()), value -> FreecamConfig.SHOW_SUBMERSION_FOG.set(value));
            this.optionsList.addBig(showSubmersion);
            OptionInstance<Double> fireOpacity = new OptionInstance<>("text.freeclient.configScreen.option.visual.fireOpacity", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual.fireOpacity.@Tooltip")), (unused, option) -> Component.translatable((String)"text.freeclient.configScreen.option.visual.fireOpacity").append(": " + FreecamConfig.FIRE_OPACITY.get()), OptionInstance.UnitDouble.INSTANCE, (Double)FreecamConfig.FIRE_OPACITY.get(), value -> {
                FreecamConfig.FIRE_OPACITY.set((double)Math.round(value * 100.0) / 100.0);
            });
            this.optionsList.addBig(fireOpacity);
            OptionInstance<Double> fireHeight = new OptionInstance<>("text.freeclient.configScreen.option.visual.fireHeight", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.visual.fireHeight.@Tooltip")), (unused, option) -> Component.translatable((String)"text.freeclient.configScreen.option.visual.fireHeight").append(": " + FreecamConfig.FIRE_HEIGHT.get()), OptionInstance.UnitDouble.INSTANCE, (Double)FreecamConfig.FIRE_HEIGHT.get(), value -> {
                FreecamConfig.FIRE_HEIGHT.set((double)Math.round(value * 100.0) / 100.0);
            });
            this.optionsList.addBig(fireHeight);
            this.addWidget(this.optionsList);
        }
    }

    private static class CollisionOptionsScreen
    extends OptionsListScreen {
        public CollisionOptionsScreen(Screen previous) {
            super(previous, (Component)Component.translatable((String)"text.freeclient.configScreen.option.collision"));
        }

        @Override
        protected void init() {
            super.init();
            OptionInstance<Boolean> ignoreTransparent = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.collision.ignoreTransparent", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.collision.ignoreTransparent.@Tooltip")), (boolean)((Boolean)FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.get()), value -> FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.set(value));
            this.optionsList.addBig(ignoreTransparent);
            OptionInstance<Boolean> ignoreOpenable = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.collision.ignoreOpenable", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.collision.ignoreOpenable.@Tooltip")), (boolean)((Boolean)FreecamConfig.IGNORE_OPENABLE_BLOCKS.get()), value -> FreecamConfig.IGNORE_OPENABLE_BLOCKS.set(value));
            this.optionsList.addBig(ignoreOpenable);
            OptionInstance<Boolean> ignoreAll = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.collision.ignoreAll", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.collision.ignoreAll.@Tooltip")), (boolean)((Boolean)FreecamConfig.IGNORE_ALL_COLLISION.get()), value -> FreecamConfig.IGNORE_ALL_COLLISION.set(value));
            this.optionsList.addBig(ignoreAll);
            OptionInstance<Boolean> alwaysCheck = OptionInstance.createBoolean((String)"text.freeclient.configScreen.option.collision.alwaysCheck", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.collision.alwaysCheck.@Tooltip")), (boolean)((Boolean)FreecamConfig.ALWAYS_CHECK_COLLISION.get()), value -> FreecamConfig.ALWAYS_CHECK_COLLISION.set(value));
            this.optionsList.addBig(alwaysCheck);
            this.addWidget(this.optionsList);
        }
    }

    private static class MovementOptionsScreen
    extends OptionsListScreen {
        public MovementOptionsScreen(Screen previous) {
            super(previous, (Component)Component.translatable((String)"text.freeclient.configScreen.option.movement"));
        }

        @Override
        protected void init() {
            super.init();
            OptionInstance<FreecamConfig.FlightMode> flightMode = new OptionInstance<>("text.freeclient.configScreen.option.movement.flightMode", value2 -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.movement.flightMode.@Tooltip")), (unused, option) -> Component.translatable((String)option.getKey()), new OptionInstance.Enum<>(Arrays.asList(FreecamConfig.FlightMode.values()), Codec.INT.xmap(FreecamConfig.FlightMode::byId, FreecamConfig.FlightMode::getId)), (FreecamConfig.FlightMode)FreecamConfig.FLIGHT_MODE.get(), newValue -> FreecamConfig.FLIGHT_MODE.set(newValue));
            this.optionsList.addBig(flightMode);
            OptionInstance<Double> horizontalSpeed = new OptionInstance<>("text.freeclient.configScreen.option.movement.horizontalSpeed", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.movement.horizontalSpeed.@Tooltip")), (unused, option) -> Component.translatable((String)"text.freeclient.configScreen.option.movement.horizontalSpeed").append(": " + FreecamConfig.HORIZONTAL_SPEED.get()), OptionInstance.UnitDouble.INSTANCE, (Double)FreecamConfig.HORIZONTAL_SPEED.get() / 10.0, value -> {
                if (Math.abs(value - (Double)FreecamConfig.HORIZONTAL_SPEED.get() / 10.0) >= 0.01) {
                    FreecamConfig.HORIZONTAL_SPEED.set((double)Math.round(value * 100.0) / 10.0);
                }
            });
            this.optionsList.addBig(horizontalSpeed);
            OptionInstance<Double> verticalSpeed = new OptionInstance<>("text.freeclient.configScreen.option.movement.verticalSpeed", value -> Tooltip.create((Component)Component.translatable((String)"text.freeclient.configScreen.option.movement.verticalSpeed.@Tooltip")), (unused, option) -> Component.translatable((String)"text.freeclient.configScreen.option.movement.verticalSpeed").append(": " + FreecamConfig.VERTICAL_SPEED.get()), OptionInstance.UnitDouble.INSTANCE, (Double)FreecamConfig.VERTICAL_SPEED.get() / 10.0, value -> {
                if (Math.abs(value - (Double)FreecamConfig.VERTICAL_SPEED.get() / 10.0) >= 0.01) {
                    FreecamConfig.VERTICAL_SPEED.set((double)Math.round(value * 100.0) / 10.0);
                }
            });
            this.optionsList.addBig(verticalSpeed);
            this.addWidget(this.optionsList);
        }
    }

    private static class OptionsListScreen
    extends Screen {
        protected final Screen previous;
        protected OptionsList optionsList;

        protected OptionsListScreen(Screen previous, Component title) {
            super(title);
            this.previous = previous;
        }

        protected void init() {
            this.optionsList = new OptionsList(this.minecraft, this.width, this.height, 24, this.height - 32, 25);
            this.addRenderableWidget(Button.builder((Component)CommonComponents.GUI_DONE, button -> this.onClose()).bounds((this.width - 200) / 2, this.height - 20 - 6, 200, 20).build());
        }

        public void onClose() {
            this.minecraft.setScreen(this.previous);
        }

        public void render(GuiGraphics guiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
            this.renderBackground(guiGraphics);
            this.optionsList.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
            guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, 8, 0xFFFFFF);
            super.render(guiGraphics, pMouseX, pMouseY, pPartialTick);
        }
    }
}

