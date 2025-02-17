package com.nico60.tickmanipulatormod.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TeleportBeaconItem extends Item {
    public TeleportBeaconItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            if (context.getPlayer() instanceof ServerPlayer player) {
                BlockPos pos = context.getClickedPos();
                player.getPersistentData().putLong("TeleportBeaconPos", pos.asLong());
                player.displayClientMessage(Component.translatable("message.teleport_beacon_placed",
                        pos.getX(), + pos.getY(), + pos.getZ()), true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            long posLong = player.getPersistentData().getLong("TeleportBeaconPos");
            if (posLong != 0) {
                BlockPos pos = BlockPos.of(posLong);
                player.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
                player.getPersistentData().remove("TeleportBeaconPos");
                player.displayClientMessage(Component.translatable("message.teleport_to_the_beacon"), true);
            } else {
                player.displayClientMessage(Component.translatable("message.teleport_no_beacon_defined"), true);
            }
        }
        return InteractionResult.SUCCESS;
    }
}
