package com.nico60.tickmanipulatormod.handler;

import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.nico60.tickmanipulatormod.entity.PastSelfEntity.registerAttributes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        registerAttributes(event);
    }
}
