package link.infra.screenshotclipboard.common.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * mojmap 1.20.1 的 NativeImage 以 {@code long pixels} 字段保存原生指针（yarn 中名为 pointer），
 * 仅改字段名即可复用原逻辑。
 */
@Mixin(NativeImage.class)
public interface NativeImagePointerAccessor {
	@Accessor("pixels")
	long getPixels();
}
