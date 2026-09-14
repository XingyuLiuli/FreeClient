/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.util;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.KeyboardInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import net.xolt.freecam.Freecam;
import net.xolt.freecam.config.FreecamConfig;
import net.xolt.freecam.util.FreecamPosition;
import net.xolt.freecam.util.Motion;

public class FreeCamera
extends LocalPlayer {
    private static final ClientPacketListener CONNECTION = new ClientPacketListener(Freecam.MC, Freecam.MC.screen, Freecam.MC.getConnection().getConnection(), Freecam.MC.getCurrentServer(), new GameProfile(UUID.randomUUID(), "FreeCamera"), Freecam.MC.getTelemetryManager().createWorldSessionManager(false, null, null)){

        public void send(Packet<?> packet) {
        }
    };

    public FreeCamera(int id) {
        this(id, FreecamPosition.getSwimmingPosition((Entity)Freecam.MC.player));
    }

    public FreeCamera(int id, FreecamPosition position) {
        super(Freecam.MC, Freecam.MC.level, CONNECTION, Freecam.MC.player.getStats(), Freecam.MC.player.getRecipeBook(), false, false);
        this.setId(id);
        this.applyPosition(position);
        this.getAbilities().flying = true;
        this.input = new KeyboardInput(Freecam.MC.options);
    }

    public void applyPosition(FreecamPosition position) {
        super.setPose(position.pose);
        this.moveTo(position.x, position.y, position.z, position.yaw, position.pitch);
        this.xBob = this.getXRot();
        this.yBob = this.getYRot();
        this.xBobO = this.getXRot();
        this.yBobO = this.getYRot();
    }

    public void applyPerspective(FreecamConfig.Perspective perspective, boolean checkCollision) {
        FreecamPosition position = new FreecamPosition((Entity)this);
        switch (perspective) {
            case INSIDE: {
                break;
            }
            case FIRST_PERSON: {
                this.moveForwardUntilCollision(position, 0.4, checkCollision);
                break;
            }
            case THIRD_PERSON_MIRROR: {
                position.mirrorRotation();
            }
            case THIRD_PERSON: {
                this.moveForwardUntilCollision(position, -4.0, checkCollision);
            }
        }
    }

    private boolean moveForwardUntilCollision(FreecamPosition position, double distance, boolean checkCollision) {
        if (!checkCollision) {
            position.moveForward(distance);
            this.applyPosition(position);
            return true;
        }
        return this.moveForwardUntilCollision(position, distance);
    }

    private boolean moveForwardUntilCollision(FreecamPosition position, double maxDistance) {
        boolean negative = maxDistance < 0.0;
        maxDistance = negative ? -1.0 * maxDistance : maxDistance;
        double increment = 0.1;
        for (double distance = 0.0; distance < maxDistance; distance += increment) {
            FreecamPosition oldPosition = new FreecamPosition((Entity)this);
            position.moveForward(negative ? -1.0 * increment : increment);
            this.applyPosition(position);
            if (this.canEnterPose(this.getPose())) continue;
            this.applyPosition(oldPosition);
            return distance > 0.0;
        }
        return true;
    }

    public void spawn() {
        if (this.clientLevel != null) {
            this.clientLevel.addPlayer(this.getId(), (AbstractClientPlayer)this);
        }
    }

    public void despawn() {
        if (this.clientLevel != null && this.clientLevel.getEntity(this.getId()) != null) {
            this.clientLevel.removeEntity(this.getId(), Entity.RemovalReason.DISCARDED);
        }
    }

    protected void checkFallDamage(double p_20990_, boolean p_20991_, BlockState p_20992_, BlockPos p_20993_) {
    }

    public float getAttackAnim(float tickDelta) {
        return Freecam.MC.player.getAttackAnim(tickDelta);
    }

    public int getUseItemRemainingTicks() {
        return Freecam.MC.player.getUseItemRemainingTicks();
    }

    public boolean isUsingItem() {
        return Freecam.MC.player.isUsingItem();
    }

    public boolean onClimbable() {
        return false;
    }

    public boolean isInWater() {
        return false;
    }

    public MobEffectInstance getEffect(MobEffect effect) {
        return Freecam.MC.player.getEffect(effect);
    }

    public PushReaction getPistonPushReaction() {
        return (Boolean)FreecamConfig.IGNORE_ALL_COLLISION.get() != false ? PushReaction.IGNORE : PushReaction.NORMAL;
    }

    public boolean canCollideWith(Entity other) {
        return false;
    }

    public void setPose(Pose pose) {
        super.setPose(Pose.SWIMMING);
    }

    public boolean isMovingSlowly() {
        return false;
    }

    protected boolean updateIsUnderwater() {
        this.wasUnderwater = this.isEyeInFluidType((FluidType)ForgeMod.WATER_TYPE.get());
        return this.wasUnderwater;
    }

    protected void doWaterSplashEffect() {
    }

    public void aiStep() {
        if (((FreecamConfig.FlightMode)((Object)FreecamConfig.FLIGHT_MODE.get())).equals((Object)FreecamConfig.FlightMode.DEFAULT)) {
            this.getAbilities().setFlyingSpeed(0.0f);
            Motion.doMotion(this, (Double)FreecamConfig.HORIZONTAL_SPEED.get(), (Double)FreecamConfig.VERTICAL_SPEED.get());
        } else {
            this.getAbilities().setFlyingSpeed((float)((Double)FreecamConfig.VERTICAL_SPEED.get() / 10.0));
        }
        super.aiStep();
        this.getAbilities().flying = true;
        this.setOnGround(false);
    }
}

