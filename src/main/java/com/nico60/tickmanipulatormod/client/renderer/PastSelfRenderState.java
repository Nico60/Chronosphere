package com.nico60.tickmanipulatormod.client.renderer;

import com.nico60.tickmanipulatormod.entity.PastSelfEntity;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.ResourceLocation;

public class PastSelfRenderState extends HumanoidRenderState {
    private PastSelfEntity pastEntity;

    public PastSelfRenderState(PastSelfEntity entity) {
        pastEntity = entity;
    }

    public void setEntity(PastSelfEntity entity) {
        pastEntity = entity;
    }

    public PastSelfEntity getEntity() {
        return pastEntity;
    }

    public ResourceLocation getTexture() {
        return pastEntity != null ? pastEntity.getSkinTexture() : ResourceLocation.fromNamespaceAndPath("minecraft", "textures/entity/player/slim/steve.png");
    }
}
