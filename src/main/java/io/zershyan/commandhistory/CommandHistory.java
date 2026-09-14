package io.zershyan.commandhistory;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

/**
 * CommandHistory 合并进 freeclient 后不再作为独立 @Mod，
 * 仅保留日志与命名常量；配置注册与事件挂载由 freeclient 主类完成。
 */
public final class CommandHistory {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "commandhistory";

    private CommandHistory() {
    }
}
