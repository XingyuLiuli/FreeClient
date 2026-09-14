/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.config;

import java.util.Arrays;
import java.util.Comparator;
import net.minecraft.util.Mth;
import net.minecraft.util.OptionEnum;
import net.minecraftforge.common.ForgeConfigSpec;

public class FreecamConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.EnumValue<FlightMode> FLIGHT_MODE;
    public static final ForgeConfigSpec.DoubleValue HORIZONTAL_SPEED;
    public static final ForgeConfigSpec.DoubleValue VERTICAL_SPEED;
    public static final ForgeConfigSpec.BooleanValue IGNORE_TRANSPARENT_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue IGNORE_OPENABLE_BLOCKS;
    public static final ForgeConfigSpec.BooleanValue IGNORE_ALL_COLLISION;
    public static final ForgeConfigSpec.BooleanValue ALWAYS_CHECK_COLLISION;
    public static final ForgeConfigSpec.EnumValue<Perspective> PERSPECTIVE;
    public static final ForgeConfigSpec.BooleanValue SHOW_PLAYER;
    public static final ForgeConfigSpec.BooleanValue SHOW_HAND;
    public static final ForgeConfigSpec.BooleanValue FULL_BRIGHTNESS;
    public static final ForgeConfigSpec.BooleanValue SHOW_SUBMERSION_FOG;
    public static final ForgeConfigSpec.BooleanValue DISABLE_ON_DAMAGE;
    public static final ForgeConfigSpec.BooleanValue FREEZE_PLAYER;
    public static final ForgeConfigSpec.BooleanValue ALLOW_INTERACT;
    public static final ForgeConfigSpec.EnumValue<InteractionMode> INTERACTION_MODE;
    public static final ForgeConfigSpec.BooleanValue NOTIFY_FREECAM;
    public static final ForgeConfigSpec.BooleanValue NOTIFY_TRIPOD;

    // McFireOverlayController 配置（合并进 freeclient 配置）
    public static final ForgeConfigSpec.DoubleValue FIRE_OPACITY;
    public static final ForgeConfigSpec.DoubleValue FIRE_HEIGHT;

    // CommandHistory 配置（合并进 freeclient 配置）
    public static final ForgeConfigSpec.IntValue HISTORY_LIMIT;
    public static final ForgeConfigSpec.BooleanValue ONLY_COMMAND;

    static {
        BUILDER.push("Freecam");
        FLIGHT_MODE = BUILDER.comment("The type of flight used by freecam.").defineEnum("Flight Mode", (Enum)FlightMode.DEFAULT);
        HORIZONTAL_SPEED = BUILDER.comment("The horizontal speed of freecam.").defineInRange("Horizontal Speed", 1.0, 0.0, 10.0);
        VERTICAL_SPEED = BUILDER.comment("The vertical speed of freecam.").defineInRange("Vertical Speed", 1.0, 0.0, 10.0);
        IGNORE_TRANSPARENT_BLOCKS = BUILDER.comment("Allows travelling through transparent blocks in freecam.").define("Ignore Transparent Blocks", true);
        IGNORE_OPENABLE_BLOCKS = BUILDER.comment("Allows travelling through doors/trapdoors/gates in freecam.").define("Ignore Openable Blocks", true);
        IGNORE_ALL_COLLISION = BUILDER.comment("Allows travelling through all blocks in freecam.").define("Ignore All Collision", true);
        ALWAYS_CHECK_COLLISION = BUILDER.comment("Whether 'Initial Perspective' should check for collision, even when using 'Ignore All Collision'.").define("Always Check Initial Collision", false);
        PERSPECTIVE = BUILDER.comment("The initial perspective of the camera.").defineEnum("Initial Perspective", (Enum)Perspective.INSIDE);
        SHOW_PLAYER = BUILDER.comment("Shows your player in its original position.").define("Show Player", true);
        SHOW_HAND = BUILDER.comment("Whether you can see your hand in freecam.").define("Show Hand", false);
        FULL_BRIGHTNESS = BUILDER.comment("Increases brightness while in freecam.").define("Full Brightness", false);
        SHOW_SUBMERSION_FOG = BUILDER.comment("Whether you see a fog overlay underwater, in lava, or powdered snow.").define("Show Submersion Fog", false);
        DISABLE_ON_DAMAGE = BUILDER.comment("Disables freecam when damage is received.").define("Disable on Damage", true);
        FREEZE_PLAYER = BUILDER.comment("Prevents player movement while freecam is active.\nWARNING: Multiplayer usage not advised.").define("Freeze Player", false);
        ALLOW_INTERACT = BUILDER.comment("Whether you can interact with blocks/entities in freecam.\nWARNING: Multiplayer usage not advised.").define("Allow Interaction", false);
        INTERACTION_MODE = BUILDER.comment("The source of block/entity interactions.").defineEnum("Interaction Mode", (Enum)InteractionMode.CAMERA);
        NOTIFY_FREECAM = BUILDER.comment("Notifies you when entering/exiting freecam.").define("Freecam Notifications", true);
        NOTIFY_TRIPOD = BUILDER.comment("Notifies you when entering/exiting tripod cameras.").define("Tripod Notifications", true);
        BUILDER.pop();

        // McFireOverlayController：屏幕火焰叠加的透明度与高度
        BUILDER.push("Fire Overlay");
        FIRE_OPACITY = BUILDER.comment("The opacity of the fire overlay.").defineInRange("Fire Overlay Opacity", 0.5, 0.0, 1.0);
        FIRE_HEIGHT = BUILDER.comment("The height of the fire overlay.").defineInRange("Fire Overlay Height", 0.4, 0.0, 1.0);
        BUILDER.pop();

        // CommandHistory：聊天历史的读取与记录范围
        BUILDER.push("Command History");
        HISTORY_LIMIT = BUILDER.comment("Command history limit.").defineInRange("History Limit", 50, 0, 500);
        ONLY_COMMAND = BUILDER.comment("Only the commands are recorded.").define("Only Command", false);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }

    public static enum FlightMode implements OptionEnum
    {
        CREATIVE(0, "text.freeclient.configScreen.option.movement.flightMode.creative"),
        DEFAULT(1, "text.freeclient.configScreen.option.movement.flightMode.default");

        private static final FlightMode[] BY_ID;
        private final int id;
        private final String key;

        private FlightMode(int id, String key) {
            this.id = id;
            this.key = key;
        }

        public int getId() {
            return this.id;
        }

        public String getKey() {
            return this.key;
        }

        public static FlightMode byId(int pId) {
            return BY_ID[Mth.positiveModulo((int)pId, (int)BY_ID.length)];
        }

        static {
            BY_ID = (FlightMode[])Arrays.stream(FlightMode.values()).sorted(Comparator.comparingInt(FlightMode::getId)).toArray(FlightMode[]::new);
        }
    }

    public static enum Perspective implements OptionEnum
    {
        FIRST_PERSON(0, "text.freeclient.configScreen.option.visual.perspective.firstPerson"),
        THIRD_PERSON(1, "text.freeclient.configScreen.option.visual.perspective.thirdPerson"),
        THIRD_PERSON_MIRROR(2, "text.freeclient.configScreen.option.visual.perspective.thirdPersonMirror"),
        INSIDE(3, "text.freeclient.configScreen.option.visual.perspective.inside");

        private static final Perspective[] BY_ID;
        private final int id;
        private final String key;

        private Perspective(int id, String name) {
            this.id = id;
            this.key = name;
        }

        public int getId() {
            return this.id;
        }

        public String getKey() {
            return this.key;
        }

        public static Perspective byId(int pId) {
            return BY_ID[Mth.positiveModulo((int)pId, (int)BY_ID.length)];
        }

        static {
            BY_ID = (Perspective[])Arrays.stream(Perspective.values()).sorted(Comparator.comparingInt(Perspective::getId)).toArray(Perspective[]::new);
        }
    }

    public static enum InteractionMode implements OptionEnum
    {
        CAMERA(0, "text.freeclient.configScreen.option.utility.interactionMode.camera"),
        PLAYER(1, "text.freeclient.configScreen.option.utility.interactionMode.player");

        private static final InteractionMode[] BY_ID;
        private final int id;
        private final String key;

        private InteractionMode(int id, String name) {
            this.id = id;
            this.key = name;
        }

        public int getId() {
            return this.id;
        }

        public String getKey() {
            return this.key;
        }

        public static InteractionMode byId(int pId) {
            return BY_ID[Mth.positiveModulo((int)pId, (int)BY_ID.length)];
        }

        static {
            BY_ID = (InteractionMode[])Arrays.stream(InteractionMode.values()).sorted(Comparator.comparingInt(InteractionMode::getId)).toArray(InteractionMode[]::new);
        }
    }
}

