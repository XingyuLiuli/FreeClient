package link.infra.screenshotclipboard;

import link.infra.screenshotclipboard.common.MacOSCompat;
import link.infra.screenshotclipboard.common.ScreenshotToClipboard;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ScreenshotEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Forge 1.20.1 接入：监听 ScreenshotEvent，截图完成后自动复制到系统剪贴板。
 * <p>macOS 无法与 AWT 同时加载（GLFW 冲突），走 Objective-C 桥直接写 NSPasteboard；
 * 其余平台走 AWT 的 BufferedImage 复制。原 neoforge 版的 ScreenshotMixinMacOS
 * 功能被合并进此监听器，无需额外 mixin。</p>
 */
public final class ScreenshotToClipboardForgeClient {
	private ScreenshotToClipboardForgeClient() {
	}

	public static void init() {
		MinecraftForge.EVENT_BUS.addListener(ScreenshotToClipboardForgeClient::handleScreenshot);
		ScreenshotToClipboard.init();
	}

	public static void handleScreenshot(ScreenshotEvent event) {
		if (Minecraft.ON_OSX) {
			MacOSCompat.doCopyMacOS(event.getScreenshotFile().getAbsolutePath());
		} else {
			ScreenshotToClipboard.handleScreenshotAWT(event.getImage());
		}
	}
}
