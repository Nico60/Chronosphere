package com.nico60.tickmanipulatormod.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.GameRules;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class RewindingHourglassItem extends Item {
    public RewindingHourglassItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!level.isClientSide) {
            Player player = context.getPlayer();
            ServerLevel serverLevel = (ServerLevel) level;
            GameRules gameRules = serverLevel.getGameRules();
            int currentTime = (int) serverLevel.getDayTime();

            assert player != null;
            if (player.isCrouching()) {
                serverLevel.setDayTime(currentTime - 1000);
                player.displayClientMessage(Component.translatable( "message.time_rewind"), false);
            } else {
                serverLevel.setDayTime(currentTime + 1000);
                player.displayClientMessage(Component.translatable( "message.time_forward"), false);
            }

            GameRules.IntegerValue tickSpeedRule = gameRules.getRule(GameRules.RULE_RANDOMTICKING);

            int newTickSpeed = tickSpeedRule.get() + (player.isCrouching() ? -1 : 1);
            tickSpeedRule.set(newTickSpeed, serverLevel.getServer());

            player.displayClientMessage(Component.translatable("message.ticks_speed", newTickSpeed), false);
        }
        return InteractionResult.SUCCESS;
    }
}
