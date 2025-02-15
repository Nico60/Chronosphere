package com.nico60.tickmanipulatormod.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.nico60.tickmanipulatormod.entity.PastSelfEntity;
import com.nico60.tickmanipulatormod.util.PastSelfCapability;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class PastSelfRenderer extends HumanoidMobRenderer<PastSelfEntity, PastSelfRenderState, HumanoidModel<PastSelfRenderState>> {
    private final HumanoidModel<PastSelfRenderState> model;
    private final PastSelfEntity pastEntity = PastSelfCapability.getEntity();

    public PastSelfRenderer(EntityRendererProvider.Context context) {
        super(context, new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER)), 0.5F);
        this.model = new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull PastSelfRenderState state) {
        return state.getTexture();
    }

    @Override
    protected boolean shouldRenderLayers(@NotNull PastSelfRenderState state) {
        return true;
    }

    @Override
    public void render(@NotNull PastSelfRenderState state, @NotNull PoseStack poseStack, @NotNull MultiBufferSource buffer, int packedLight) {
        if (pastEntity instanceof PastSelfEntity) {
            state.setEntity(pastEntity);
            ResourceLocation skin = this.getTextureLocation(state);
            VertexConsumer vertexConsumer = buffer.getBuffer(this.model.renderType(skin));
            this.model.setupAnim(state);
            this.model.renderToBuffer(poseStack, vertexConsumer, packedLight, getOverlayCoords(state, 0.0F));
        }
        super.render(state, poseStack, buffer, packedLight);
    }

    @Override
    public @NotNull PastSelfRenderState createRenderState() {
        return new PastSelfRenderState(null);
    }
}
