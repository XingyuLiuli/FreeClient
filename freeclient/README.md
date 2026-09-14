# freeclient（自由视角整合模组）

将 **freecam（自由视角）**、**BetterBrightnessSlider（扩展亮度滑块）**、**ScreenshotToClipboard（截图自动复制）**、**CommandHistory（聊天历史）**、**McFireOverlayController（火焰叠加控制）** 五个模组合并为一个 Forge 1.20.1 客户端模组：**freeclient**。

## 产物

- 构建产物：`build/libs/freeclient-1.2.0.jar`（直接放入 1.20.1 Forge 的 mods 文件夹）
- Mod id：`freeclient`，版本 `1.2.0`
- 兼容：Minecraft 1.20.1、Forge 47.4.0+、Java 17

## 功能一览

| 功能 | 来源 | 说明 |
| --- | --- | --- |
| 自由视角 / 三脚架相机 | freecam | 原有功能不变 |
| 扩展亮度滑块 | BetterBrightnessSlider | -100% ~ 1200%、步进 5%、负伽马生效；Embeddium 可选适配 |
| 截图自动复制 | ScreenshotToClipboard | 截图完成后自动复制到系统剪贴板（macOS 走 NSPasteboard） |
| 聊天历史 | CommandHistory | 登录读回 / 登出写入 `command_history.txt`（配置文件可调） |
| 火焰叠加控制 | McFireOverlayController | 着火时屏幕火焰的透明度与高度（配置并入 freeclient.toml） |

## 配置文件（命名随模组改为 freeclient）

- `freeclient.toml`：全部配置合并为一段文件，含三个小节：
  - **Freecam**：自由视角各项
  - **Fire Overlay**：火焰叠加（`Fire Overlay Opacity` 默认 0.5、`Fire Overlay Height` 默认 0.4）
  - **Command History**：聊天历史（`History Limit` 默认 50、`Only Command` 默认 false）

配置界面：游戏内按 freeclient 的配置快捷键（默认未绑定，可在按键设置中绑定）打开，视觉选项页含火焰叠加两项滑块。

## 移植说明

### ScreenshotToClipboard（arch/yarn → Forge mojmap）
- `MinecraftClient.IS_SYSTEM_MAC` → `Minecraft.ON_OSX`；`NativeImage` → `com.mojang.blaze3d.platform.NativeImage`
- `NativeImagePointerAccessor`：`@Accessor("pointer")` → `@Accessor("pixels")`（mojmap 1.20.1 字段同为 long 指针）
- 原 neoforge 的 `ScreenshotMixinMacOS` 合并进 `ScreenshotToClipboardForgeClient` 的 `ScreenshotEvent` 监听（mac 分支直接取 `getScreenshotFile()`），无需额外 mixin
- `java-objc-bridge`（macOS 支持）已随 jar 打包，保持单 jar

### CommandHistory（Forge 原样）
- 去掉 `@Mod`，事件挂载移入 freeclient 主类（`MinecraftForge.EVENT_BUS.register(ClientNetworkHandler.class)`）
- 配置（`historyLimit` / `onlyCommand`）从独立 toml 合并进 `FreecamConfig`，随 freeclient.toml 统一管理

### McFireOverlayController（Fabric → Forge）
- 删除 AutoConfig / ModMenu 依赖，配置并入 `FreecamConfig`（默认值按用户要求改为 0.5 / 0.4）
- mixin 重写为 mojmap 1.20.1：`InGameOverlayRenderer.renderFireOverlay(MatrixStack, VertexConsumerProvider)`
  → `ScreenEffectRenderer.renderFire(Minecraft, PoseStack)`
- 注入点与原版一致：`VertexConsumer.color(FFFF)` 的 alpha（index 3）、`PoseStack.translate(FFF)` 的 y（index 1，原常量 -0.3f 改为 -1.0f + height）

## Mixin 配置

| 配置 | 内容 | refmap |
| --- | --- | --- |
| freeclient.mixins.json | freecam 原有 18 个 mixin | freeclient.refmap.json（静态） |
| freeclient-brightness.mixins.json | 亮度滑块 3 个 mixin | freeclient-extra.refmap.json（共享） |
| freeclient-screenshot.mixins.json | AWTHackMixin、NativeImagePointerAccessor | freeclient-extra.refmap.json（共享） |
| freeclient-fire.mixins.json | InGameOverlayRendererMixin | freeclient-extra.refmap.json（共享） |

注：MixinGradle 的 `add` 只产出最后一个 refmap，因此后三个配置共享 AP 生成的 combined refmap（多余条目运行时无害）。

## 构建方法

环境：JDK 17（Temurin 17.0.13）、Gradle 8.1.1。

```bash
cd freecam-brightness
export JAVA_HOME=/home/user/tools/jdk-17.0.13+11
export PATH=$JAVA_HOME/bin:/home/user/tools/gradle-8.1.1/bin:$PATH
gradle build --no-daemon --console=plain --max-workers=2
```

产物：`build/libs/freeclient-1.2.0.jar`

注意：若修改过 mixin 配置文件名/refmap 名后重构建，需 `gradle build --rerun-tasks`（不要 `clean`，clean 会删除 MCP 映射产物导致 AP 报 "Mixin has no targets"，重建耗时更长）。

## 依赖与许可

- **freeclient 整体以 GNU Affero General Public License v3.0（AGPL-3.0）开源发布**，详见本仓库 `LICENSE`。
- 第三方组件版权与许可声明见 `NOTICE` 文件（本仓库根目录，同时打包进 jar 的 `META-INF/NOTICE`）：
  - freecam（hashalite）— MIT
  - BetterBrightnessSlider（LaidBackSloth）— MIT
  - ScreenshotToClipboard（comp500）— MIT
  - CommandHistory（zershyan）— AGPL-3.0-or-later
  - McFireOverlayController（TheGameratorT）— LGPL-3.0（已修改，修改源码见本仓库）
  - java-objc-bridge（ca.weblite，macOS 剪贴板支持）— Apache-2.0
- Embeddium `[0.3,)`：CLIENT 侧可选依赖（亮度滑块控件适配，未安装时静默跳过），不打包、不随本项目分发。
- 源码链接：本仓库即为完整源码（含构建脚本与全部资源）。

## 测试建议

1. 放入 1.20.1 Forge 整合包 mods 目录，确认模组列表出现 "FreeClient"。
2. 截图后粘贴到画图/聊天框，确认已自动复制。
3. 输入几条指令后退出服务器/重进，确认聊天历史恢复。
4. 着火时确认火焰叠加透明度/高度受配置控制（改 `freeclient.toml` 或配置界面）。
5. 自由视角与扩展亮度滑块回归测试；装上 Embeddium 后再测一次亮度滑块。
