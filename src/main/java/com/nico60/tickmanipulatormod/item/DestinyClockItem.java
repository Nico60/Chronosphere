package com.nico60.tickmanipulatormod.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DestinyClockItem extends Item {

    private static double tickSpeedMultiplier = 1.0;
    private static int previousTickSpeed = 20;

    public DestinyClockItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            Player player = context.getPlayer();
            assert player != null;
            ServerLevel serverLevel = (ServerLevel) level;
            GameRules gameRules = serverLevel.getGameRules();
            int currentTickSpeed = gameRules.getInt(GameRules.RULE_RANDOMTICKING);

            if (getTickSpeedMultiplier() == 1.0) {
                previousTickSpeed = currentTickSpeed;
                setTickSpeedMultiplier(0.5);
                serverLevel.getServer().getCommands().performPrefixedCommand(serverLevel.getServer().createCommandSourceStack(), "gamerule randomTickSpeed 2");
                player.displayClientMessage(Component.translatable("message.time_slow_down"), false);
            } else if (getTickSpeedMultiplier() == 0.5) {
                setTickSpeedMultiplier(2.0);
                serverLevel.getServer().getCommands().performPrefixedCommand(serverLevel.getServer().createCommandSourceStack(), "gamerule randomTickSpeed 100");
                player.displayClientMessage(Component.translatable("message.time_is_speeding_up"), false);
            } else {
                setTickSpeedMultiplier(1.0);
                serverLevel.getServer().getCommands().performPrefixedCommand(serverLevel.getServer().createCommandSourceStack(), "gamerule randomTickSpeed " + previousTickSpeed);
                player.displayClientMessage(Component.translatable("message.time_has_returned_to_normal"), false);
            }
        }
        return InteractionResult.SUCCESS;
    }

    public static void setTickSpeedMultiplier(double multiplier) {
        tickSpeedMultiplier = Math.max(0.1, Math.min(multiplier, 10.0));
    }

    public static double getTickSpeedMultiplier() {
        return tickSpeedMultiplier;
    }
}
