package com.nico60.tickmanipulatormod.util;

import com.nico60.tickmanipulatormod.entity.PastSelfEntity;

public class PastSelfCapability {
    private static PastSelfEntity pastSelfEntity;

    public static void setEntity(PastSelfEntity entity) {
        pastSelfEntity = entity;
    }

    public static PastSelfEntity getEntity() {
        return pastSelfEntity;
    }
}
