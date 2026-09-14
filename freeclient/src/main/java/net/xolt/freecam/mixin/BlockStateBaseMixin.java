/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 *
 * 注意：@Shadow 方法在 Forge 生产环境不经 refmap 重映射，必须写运行时 SRG 名
 * （与官方 freecam-forge-1.2.1+1.20 产物字节码一致）：getBlock() = m_60734_()
 */
package net.xolt.freecam.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.CollisionWhitelist;
import net.xolt.freecam.config.FreecamConfig;
import net.xolt.freecam.util.FreeCamera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BlockBehaviour.BlockStateBase.class})
public abstract class BlockStateBaseMixin {
    @Shadow
    public abstract Block m_60734_();

    @Inject(method={"getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetCollisionShape(BlockGetter world, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        if (context instanceof EntityCollisionContext && ((EntityCollisionContext)context).getEntity() instanceof FreeCamera) {
            if ((!((Boolean)FreecamConfig.ALWAYS_CHECK_COLLISION.get()).booleanValue() || Freecam.isEnabled()) && ((Boolean)FreecamConfig.IGNORE_ALL_COLLISION.get()).booleanValue()) {
                cir.setReturnValue(Shapes.empty());
            }
            if (((Boolean)FreecamConfig.IGNORE_TRANSPARENT_BLOCKS.get()).booleanValue() && CollisionWhitelist.isTransparent(this.m_60734_())) {
                cir.setReturnValue(Shapes.empty());
            }
            if (((Boolean)FreecamConfig.IGNORE_OPENABLE_BLOCKS.get()).booleanValue() && CollisionWhitelist.isOpenable(this.m_60734_())) {
                cir.setReturnValue(Shapes.empty());
            }
        }
    }
}
