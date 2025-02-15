package com.nico60.tickmanipulatormod.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockHistoryManager {
    private static final Map<BlockPos, BlockState> blockHistory = new HashMap<>();

    public static void saveBlockState(ServerLevel level, BlockPos pos) {
        blockHistory.put(pos, level.getBlockState(pos));
    }

    public static void restoreBlocks(ServerLevel level) {
        for (Map.Entry<BlockPos, BlockState> entry : blockHistory.entrySet()) {
            level.setBlock(entry.getKey(), entry.getValue(), 3);
        }
        blockHistory.clear();
    }
}
