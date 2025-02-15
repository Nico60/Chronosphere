package com.nico60.tickmanipulatormod.handler;

import com.nico60.tickmanipulatormod.TickManipulatorMod;
import com.nico60.tickmanipulatormod.util.TimeRecorder;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TickManipulatorMod.MOD_ID)
public class PlayerEventHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player == null) {
            return;
        }
        if (!player.level().isClientSide) {
            TimeRecorder.recordPlayerMovement((ServerPlayer) player);
        }
    }
}
