/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam;

import com.mojang.blaze3d.platform.InputConstants;
import io.zershyan.commandhistory.handler.ClientNetworkHandler;
import java.util.HashMap;
import java.util.function.BiFunction;
import link.infra.screenshotclipboard.ScreenshotToClipboardForgeClient;
import net.minecraft.client.CameraType;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.IConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.xolt.freecam.config.ConfigScreen;
import net.xolt.freecam.config.FreecamConfig;
import net.xolt.freecam.util.FreeCamera;
import net.xolt.freecam.util.FreecamPosition;

/**
 * 自由视角主类（合并工程：freecam + BetterBrightnessSlider + ScreenshotToClipboard
 * + CommandHistory + McFireOverlayController，统一以 freeclient 身份发布）。
 */
@Mod(value="freeclient")
public class Freecam {
    public static final String MOD_ID = "freeclient";
    public static final Minecraft MC = Minecraft.getInstance();
    public static final KeyMapping KEY_TOGGLE = new KeyMapping("key.freeclient.toggle", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, 293, "category.freeclient.freeclient");
    public static final KeyMapping KEY_PLAYER_CONTROL = new KeyMapping("key.freeclient.playerControl", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, -1, "category.freeclient.freeclient");
    public static final KeyMapping KEY_TRIPOD_RESET = new KeyMapping("key.freeclient.tripodReset", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, -1, "category.freeclient.freeclient");
    public static final KeyMapping KEY_CONFIG_GUI = new KeyMapping("key.freeclient.configGui", (IKeyConflictContext)KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, -1, "category.freeclient.freeclient");
    private static boolean freecamEnabled = false;
    private static boolean tripodEnabled = false;
    private static boolean playerControlEnabled = false;
    private static boolean disableNextTick = false;
    private static Integer activeTripod = null;
    private static FreeCamera freeCamera;
    private static HashMap<Integer, FreecamPosition> overworld_tripods;
    private static HashMap<Integer, FreecamPosition> nether_tripods;
    private static HashMap<Integer, FreecamPosition> end_tripods;
    private static CameraType rememberedF5;

    public Freecam() {
        // 全部配置统一在 freeclient.toml（freecam + 火焰叠加 + 聊天历史）
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, (IConfigSpec)FreecamConfig.SPEC, "freeclient.toml");
        ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((BiFunction)new BiFunction<Minecraft, Screen, Screen>(){

            @Override
            public Screen apply(Minecraft minecraft, Screen screen) {
                return new ConfigScreen(screen);
            }
        }));
        // ScreenshotToClipboard：监听截图事件并复制到剪贴板
        ScreenshotToClipboardForgeClient.init();
        // CommandHistory：登录读回 / 登出写入聊天历史
        MinecraftForge.EVENT_BUS.register(ClientNetworkHandler.class);
    }

    public static void toggle() {
        if (tripodEnabled) {
            Freecam.toggleTripod(activeTripod);
            return;
        }
        if (freecamEnabled) {
            Freecam.onDisableFreecam();
        } else {
            Freecam.onEnableFreecam();
        }
        boolean bl = freecamEnabled = !freecamEnabled;
        if (!freecamEnabled) {
            Freecam.onDisabled();
        }
    }

    public static void toggleTripod(Integer keyCode) {
        if (keyCode == null) {
            return;
        }
        if (tripodEnabled) {
            if (activeTripod.equals(keyCode)) {
                Freecam.onDisableTripod();
                tripodEnabled = false;
            } else {
                Freecam.onDisableTripod();
                Freecam.onEnableTripod(keyCode);
            }
        } else {
            if (freecamEnabled) {
                Freecam.toggle();
            }
            Freecam.onEnableTripod(keyCode);
            tripodEnabled = true;
        }
        if (!tripodEnabled) {
            Freecam.onDisabled();
        }
    }

    public static void switchControls() {
        if (!Freecam.isEnabled()) {
            return;
        }
        if (playerControlEnabled) {
            Freecam.freeCamera.input = new KeyboardInput(Freecam.MC.options);
        } else {
            Freecam.MC.player.input = new KeyboardInput(Freecam.MC.options);
            Freecam.freeCamera.input = new Input();
        }
        playerControlEnabled = !playerControlEnabled;
    }

    private static void onEnableTripod(int keyCode) {
        Freecam.onEnable();
        FreecamPosition position = Freecam.getTripodsForDimension().get(keyCode);
        boolean chunkLoaded = false;
        if (position != null) {
            ChunkPos chunkPos = position.getChunkPos();
            chunkLoaded = Freecam.MC.level.getChunkSource().hasChunk(chunkPos.x, chunkPos.z);
        }
        if (!chunkLoaded) {
            Freecam.resetCamera(keyCode);
            position = null;
        }
        freeCamera = position == null ? new FreeCamera(-420 - keyCode % 48) : new FreeCamera(-420 - keyCode % 48, position);
        freeCamera.spawn();
        MC.setCameraEntity((Entity)freeCamera);
        activeTripod = keyCode;
        if (((Boolean)FreecamConfig.NOTIFY_TRIPOD.get()).booleanValue()) {
            Freecam.MC.player.displayClientMessage((Component)Component.translatable((String)"msg.freeclient.openTripod").append("" + activeTripod % 48), true);
        }
    }

    private static void onDisableTripod() {
        Freecam.getTripodsForDimension().put(activeTripod, new FreecamPosition((Entity)freeCamera));
        Freecam.onDisable();
        if (Freecam.MC.player != null && ((Boolean)FreecamConfig.NOTIFY_TRIPOD.get()).booleanValue()) {
            Freecam.MC.player.displayClientMessage((Component)Component.translatable((String)"msg.freeclient.closeTripod").append("" + activeTripod % 48), true);
        }
        activeTripod = null;
    }

    private static void onEnableFreecam() {
        Freecam.onEnable();
        freeCamera = new FreeCamera(-420);
        freeCamera.applyPerspective((FreecamConfig.Perspective)((Object)FreecamConfig.PERSPECTIVE.get()), (Boolean)FreecamConfig.ALWAYS_CHECK_COLLISION.get() != false || (Boolean)FreecamConfig.IGNORE_ALL_COLLISION.get() == false);
        freeCamera.spawn();
        MC.setCameraEntity((Entity)freeCamera);
        if (((Boolean)FreecamConfig.NOTIFY_FREECAM.get()).booleanValue()) {
            Freecam.MC.player.displayClientMessage((Component)Component.translatable((String)"msg.freeclient.enable"), true);
        }
    }

    private static void onDisableFreecam() {
        Freecam.onDisable();
        if (Freecam.MC.player != null && ((Boolean)FreecamConfig.NOTIFY_FREECAM.get()).booleanValue()) {
            Freecam.MC.player.displayClientMessage((Component)Component.translatable((String)"msg.freeclient.disable"), true);
        }
    }

    private static void onEnable() {
        Freecam.MC.smartCull = false;
        Freecam.MC.gameRenderer.setRenderHand(((Boolean)FreecamConfig.SHOW_HAND.get()).booleanValue());
        rememberedF5 = Freecam.MC.options.getCameraType();
        if (Freecam.MC.gameRenderer.getMainCamera().isDetached()) {
            Freecam.MC.options.setCameraType(CameraType.FIRST_PERSON);
        }
    }

    private static void onDisable() {
        Freecam.MC.smartCull = true;
        Freecam.MC.gameRenderer.setRenderHand(true);
        MC.setCameraEntity((Entity)Freecam.MC.player);
        playerControlEnabled = false;
        freeCamera.despawn();
        Freecam.freeCamera.input = new Input();
        freeCamera = null;
        if (Freecam.MC.player != null) {
            Freecam.MC.player.input = new KeyboardInput(Freecam.MC.options);
        }
    }

    private static void onDisabled() {
        if (rememberedF5 != null) {
            Freecam.MC.options.setCameraType(rememberedF5);
        }
    }

    public static void resetCamera(int keyCode) {
        if (tripodEnabled && activeTripod != null && activeTripod == keyCode && freeCamera != null) {
            freeCamera.copyPosition((Entity)Freecam.MC.player);
        } else {
            Freecam.getTripodsForDimension().put(keyCode, null);
        }
        if (((Boolean)FreecamConfig.NOTIFY_TRIPOD.get()).booleanValue()) {
            Freecam.MC.player.displayClientMessage((Component)Component.translatable((String)"msg.freeclient.tripodReset").append("" + keyCode % 48), true);
        }
    }

    public static void clearTripods() {
        overworld_tripods = new HashMap();
        nether_tripods = new HashMap();
        end_tripods = new HashMap();
    }

    public static FreeCamera getFreeCamera() {
        return freeCamera;
    }

    public static HashMap<Integer, FreecamPosition> getTripodsForDimension() {
        return switch (Freecam.MC.level.dimensionTypeId().location().getPath()) {
            case "the_nether" -> nether_tripods;
            case "the_end" -> end_tripods;
            default -> overworld_tripods;
        };
    }

    public static boolean disableNextTick() {
        return disableNextTick;
    }

    public static void setDisableNextTick(boolean damage) {
        disableNextTick = damage;
    }

    public static boolean isEnabled() {
        return freecamEnabled || tripodEnabled;
    }

    public static boolean isPlayerControlEnabled() {
        return playerControlEnabled;
    }

    static {
        overworld_tripods = new HashMap();
        nether_tripods = new HashMap();
        end_tripods = new HashMap();
        rememberedF5 = null;
    }
}

