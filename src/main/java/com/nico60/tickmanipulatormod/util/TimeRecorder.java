package com.nico60.tickmanipulatormod.util;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.*;

public class TimeRecorder {
    private static final int RECORD_DURATION = 200; // Number of ticks (20 = 1 second)
    private static final Map<UUID, Deque<MovementState>> playerMovements = new HashMap<>();

    public static void recordPlayerMovement(ServerPlayer player) {
        playerMovements.putIfAbsent(player.getUUID(), new LinkedList<>());
        Deque<MovementState> movements = playerMovements.get(player.getUUID());
        if (movements.size() >= RECORD_DURATION) {
            movements.pollFirst();
        }
        movements.addLast(new MovementState(player.position(), player.getYRot(), player.getXRot()));
    }

    public static Deque<MovementState> getRecordedMovements(Player player) {
        return playerMovements.getOrDefault(player.getUUID(), new LinkedList<>());
    }
}
