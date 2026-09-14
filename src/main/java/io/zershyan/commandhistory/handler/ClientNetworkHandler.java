package io.zershyan.commandhistory.handler;

import io.zershyan.commandhistory.CommandHistory;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.xolt.freecam.config.FreecamConfig;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * 合并进 freeclient 后由主类手动 {@code MinecraftForge.EVENT_BUS.register(ClientNetworkHandler.class)}
 * 注册（原 @Mod.EventBusSubscriber 注解移除）。
 */
public class ClientNetworkHandler {
    @SubscribeEvent
    public static void playerLogin(ClientPlayerNetworkEvent.LoggingIn event) {
        try {
            Path history = Paths.get("command_history.txt");
            if (!Files.exists(history)) Files.createFile(history);
            try(Stream<String> lines = Files.lines(history)) {
                List<String> strings = lines.toList();
                if(FreecamConfig.ONLY_COMMAND.get()) {
                    strings = strings.stream().filter(s -> s.startsWith("/")).toList();
                }
                if(strings.isEmpty()) return;
                Integer limit = FreecamConfig.HISTORY_LIMIT.get();
                List<String> result = new ArrayList<>(strings);
                int size = strings.size();
                if(size > limit) result = strings.subList(size - limit, size);
                Minecraft instance = Minecraft.getInstance();
                result.forEach(instance.gui.getChat()::addRecentChat);
            }
        } catch (Exception e) {
            CommandHistory.LOGGER.error(e.getMessage());
        }
    }

    @SubscribeEvent
    public static void playerLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        if(event.getPlayer() == null) return;
        try {
            Path history = Paths.get("command_history.txt");
            if (!Files.exists(history)) Files.createFile(history);
            Integer limit = FreecamConfig.HISTORY_LIMIT.get();
            Minecraft instance = Minecraft.getInstance();
            List<String> recentChat = instance.gui.getChat().getRecentChat();
            if(FreecamConfig.ONLY_COMMAND.get()) {
                recentChat = recentChat.stream().filter(s -> s.startsWith("/")).toList();
            }
            List<String> result = new ArrayList<>(recentChat);
            int size = recentChat.size();
            if(size > limit) result = recentChat.subList(size - limit, size);
            Files.write(history, result, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (Exception e) {
            CommandHistory.LOGGER.error(e.getMessage());
        }
    }
}
