package com.nico60.tickmanipulatormod.entity;

import com.mojang.authlib.GameProfile;
import com.nico60.tickmanipulatormod.TickManipulatorMod;
import com.nico60.tickmanipulatormod.util.MovementState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PastSelfEntity extends Mob {
    private Deque<MovementState> recordedPath;
    private int pathIndex = 0;
    private ResourceLocation skinTexture;
    private UUID playerUUID;

    public PastSelfEntity(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    public PastSelfEntity(EntityType<? extends Mob> entityType, Level level, GameProfile playerProfile) {
        super(entityType, level);
        this.setNoAi(false);
        this.setSilent(true);
        this.setInvisible(false);
        this.setInvulnerable(true);
        ServerPlayer player = this.getPlayer();
        if (player != null) {
            this.playerUUID = player.getUUID();
            this.copyAttributesFromPlayer(player);
            this.setPos(player.getX(), player.getY(), player.getZ());
        }
        this.skinTexture = getPlayerSkin(playerProfile);
    }

    public ServerPlayer getPlayer() {
        if (this.playerUUID == null || !(this.level() instanceof ServerLevel serverLevel)) {
            return null;
        }
        return (ServerPlayer) serverLevel.getPlayerByUUID(this.playerUUID);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.playerUUID != null) {
            tag.putUUID("PlayerUUID", this.playerUUID);
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("PlayerUUID")) {
            this.playerUUID = tag.getUUID("PlayerUUID");
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        return LivingEntity.createLivingAttributes()
                .add(Attributes.FOLLOW_RANGE, 64.0);
    }

    public void copyAttributesFromPlayer(ServerPlayer player) {
        for (Attribute attribute : ForgeRegistries.ATTRIBUTES.getValues()) {
            Optional<Holder<Attribute>> attributeHolder = ForgeRegistries.ATTRIBUTES.getHolder(attribute);
            if (attributeHolder.isPresent()) {
                AttributeInstance playerAttr = player.getAttribute(attributeHolder.get());
                if (playerAttr != null) {
                    AttributeInstance thisAttr = this.getAttribute(attributeHolder.get());
                    if (thisAttr != null) {
                        thisAttr.setBaseValue(playerAttr.getBaseValue());
                        for (AttributeModifier modifier : playerAttr.getModifiers()) {
                            thisAttr.addTransientModifier(modifier);
                        }
                    }
                }
            }
        }
    }

    @Override
    public @NotNull Iterable<ItemStack> getArmorSlots() {
        if (getPlayer() != null) {
            return getPlayer().getArmorSlots();
        }
        return Collections.emptyList();
    }

    @Override
    public @NotNull ItemStack getItemBySlot(@NotNull EquipmentSlot slot) {
        if (getPlayer() != null) {
            return getPlayer().getItemBySlot(slot);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(@NotNull EquipmentSlot slot, @NotNull ItemStack stack) {
        if (getPlayer() != null) {
            getPlayer().setItemSlot(slot, stack);
        }
    }

    @Override
    public @NotNull HumanoidArm getMainArm() {
        if (getPlayer() != null) {
            return getPlayer().getMainArm();
        }
        return HumanoidArm.RIGHT;
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(TickManipulatorMod.PAST_SELF.get(), PastSelfEntity.createAttributes().build());
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            if (recordedPath != null && pathIndex < recordedPath.size()) {
                MovementState nextState = recordedPath.peek();
                assert nextState != null;
                BlockPos blockPosBelow = new BlockPos((int) nextState.getPosition().x,
                        (int) nextState.getPosition().y - 1, (int) nextState.getPosition().z);
                BlockState blockState = this.level().getBlockState(blockPosBelow);
                double yOffset = 0.0;
                if (blockState.getBlock() instanceof SnowLayerBlock) {
                    yOffset = 0.5;
                }
                this.setPos(nextState.getPosition().add(0, yOffset, 0));
                this.setRot(nextState.getYRot(), nextState.getXRot());
                pathIndex++;
                recordedPath.poll();
            } else {
                this.level().broadcastEntityEvent(this, (byte) 60);
                this.discard();
            }
        } else {
            for (int i = 0; i < 3; i++) {
                this.level().addParticle(ParticleTypes.SOUL,
                        this.getX(), this.getY() + 0.5, this.getZ(), 0, 0.02, 0);
            }
        }
    }

    public void setRecordedPath(Deque<MovementState> path) {
        this.recordedPath = path;
    }

    private ResourceLocation getPlayerSkin(GameProfile profile) {
        Minecraft mc = Minecraft.getInstance();
        SkinManager skinManager = mc.getSkinManager();
        PlayerSkin playerSkin = skinManager.getInsecureSkin(profile);
        return playerSkin.texture();
    }

    public ResourceLocation getSkinTexture() {
        return this.skinTexture;
    }
}
