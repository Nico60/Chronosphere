package com.nico60.tickmanipulatormod.client;

import com.nico60.tickmanipulatormod.TickManipulatorMod;
import com.nico60.tickmanipulatormod.client.renderer.PastSelfRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventBus {
    @SubscribeEvent
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(TickManipulatorMod.PAST_SELF.get(), PastSelfRenderer::new);
    }
}
