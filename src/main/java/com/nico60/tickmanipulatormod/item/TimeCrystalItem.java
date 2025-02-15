package com.nico60.tickmanipulatormod.item;

import com.mojang.authlib.GameProfile;
import com.nico60.tickmanipulatormod.TickManipulatorMod;
import com.nico60.tickmanipulatormod.entity.PastSelfEntity;
import com.nico60.tickmanipulatormod.util.MovementState;
import com.nico60.tickmanipulatormod.util.PastSelfCapability;
import com.nico60.tickmanipulatormod.util.TimeRecorder;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TimeCrystalItem extends Item {
    public TimeCrystalItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            ServerPlayer player = (ServerPlayer) context.getPlayer();
            if (player != null) {
                Deque<MovementState> recordedMovements = TimeRecorder.getRecordedMovements(player);
                if (!recordedMovements.isEmpty()) {
                    GameProfile profile = player.getGameProfile();
                    PastSelfEntity pastSelf = new PastSelfEntity(TickManipulatorMod.PAST_SELF.get(), level, profile);
                    pastSelf.setRecordedPath(new LinkedList<>(recordedMovements));
                    level.addFreshEntity(pastSelf);
                    PastSelfCapability.setEntity(pastSelf);
                    player.displayClientMessage(Component.translatable("message.past_self_activated"), false);
                }
            }
        }
        return InteractionResult.SUCCESS;
    }
}
