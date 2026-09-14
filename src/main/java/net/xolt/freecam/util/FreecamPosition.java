/*
 * Restored source (decompiled and re-mapped to Mojang official mappings).
 */
package net.xolt.freecam.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.ChunkPos;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class FreecamPosition {
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;
    public Pose pose;
    private final Quaternionf rotation = new Quaternionf(0.0f, 0.0f, 0.0f, 1.0f);
    private final Vector3f verticalPlane = new Vector3f(0.0f, 1.0f, 0.0f);
    private final Vector3f diagonalPlane = new Vector3f(1.0f, 0.0f, 0.0f);
    private final Vector3f horizontalPlane = new Vector3f(0.0f, 0.0f, 1.0f);

    public FreecamPosition(Entity entity) {
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
        this.pose = entity.getPose();
        this.setRotation(entity.getYRot(), entity.getXRot());
    }

    public void setRotation(float yaw, float pitch) {
        this.pitch = pitch;
        this.yaw = yaw;
        this.rotation.rotationYXZ(-yaw * ((float)Math.PI / 180), pitch * ((float)Math.PI / 180), 0.0f);
        this.horizontalPlane.set(0.0f, 0.0f, 1.0f).rotate((Quaternionfc)this.rotation);
        this.verticalPlane.set(0.0f, 1.0f, 0.0f).rotate((Quaternionfc)this.rotation);
        this.diagonalPlane.set(1.0f, 0.0f, 0.0f).rotate((Quaternionfc)this.rotation);
    }

    public void mirrorRotation() {
        this.setRotation(this.yaw + 180.0f, -this.pitch);
    }

    public void moveForward(double distance) {
        this.move(distance, 0.0, 0.0);
    }

    public void move(double fwd, double up, double right) {
        this.x += (double)this.horizontalPlane.x() * fwd + (double)this.verticalPlane.x() * up + (double)this.diagonalPlane.x() * right;
        this.y += (double)this.horizontalPlane.y() * fwd + (double)this.verticalPlane.y() * up + (double)this.diagonalPlane.y() * right;
        this.z += (double)this.horizontalPlane.z() * fwd + (double)this.verticalPlane.z() * up + (double)this.diagonalPlane.z() * right;
    }

    public static FreecamPosition getSwimmingPosition(Entity entity) {
        FreecamPosition position = new FreecamPosition(entity);
        if (position.pose != Pose.SWIMMING) {
            position.y += (double)(entity.getEyeHeight(position.pose) - entity.getEyeHeight(Pose.SWIMMING));
            position.pose = Pose.SWIMMING;
        }
        return position;
    }

    public ChunkPos getChunkPos() {
        return new ChunkPos((int)(this.x / 16.0), (int)(this.z / 16.0));
    }
}

