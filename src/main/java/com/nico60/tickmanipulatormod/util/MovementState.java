package com.nico60.tickmanipulatormod.util;

import net.minecraft.world.phys.Vec3;

public class MovementState {
    private final Vec3 position;
    private final float yRot;
    private final float xRot;

    public MovementState(Vec3 position, float yRot, float xRot) {
        this.position = position;
        this.yRot = yRot;
        this.xRot = xRot;
    }

    public Vec3 getPosition() {
        return position;
    }

    public float getYRot() {
        return yRot;
    }

    public float getXRot() {
        return xRot;
    }
}
