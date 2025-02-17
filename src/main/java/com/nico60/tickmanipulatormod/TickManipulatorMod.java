package com.nico60.tickmanipulatormod;

import com.mojang.logging.LogUtils;
import com.nico60.tickmanipulatormod.item.BlockRestorerItem;
import com.nico60.tickmanipulatormod.item.DestinyClockItem;
import com.nico60.tickmanipulatormod.item.RewindingHourglassItem;
import com.nico60.tickmanipulatormod.item.TeleportBeaconItem;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.commands.TickCommand;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(TickManipulatorMod.MOD_ID)
public class TickManipulatorMod {
    public static final String MOD_ID = "tickmanipulatormod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> MOD_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> BLOCK_RESTORER = ITEMS.register("block_restorer",
            () -> new BlockRestorerItem(new Item.Properties().stacksTo(1).setId(ITEMS.key("block_restorer"))));
    public static final RegistryObject<Item> DESTINY_CLOCK = ITEMS.register("destiny_clock",
            () -> new DestinyClockItem(new Item.Properties().stacksTo(1).setId(ITEMS.key("destiny_clock"))));
    public static final RegistryObject<Item> REWINDING_HOURGLASS = ITEMS.register("rewinding_hourglass",
            () -> new RewindingHourglassItem(new Item.Properties().stacksTo(1).setId(ITEMS.key("rewinding_hourglass"))));
    public static final RegistryObject<Item> TELEPORT_BEACON = ITEMS.register("teleport_beacon",
            () -> new TeleportBeaconItem(new Item.Properties().stacksTo(1).setId(ITEMS.key("teleport_beacon"))));

    public static final RegistryObject<CreativeModeTab> MOD_TAB = MOD_TABS.register("mod_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(DESTINY_CLOCK.get()))
                    .title(Component.translatable("itemGroup.tickmanipulatormod"))
                    .displayItems((parameters, output) -> {
                        output.accept(TELEPORT_BEACON.get());
                        output.accept(DESTINY_CLOCK.get());
                        output.accept(REWINDING_HOURGLASS.get());
                        output.accept(BLOCK_RESTORER.get());
                    }).build());

    public TickManipulatorMod(@NotNull FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MOD_TABS.register(modEventBus);
        ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
        if (Config.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
        Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    public void addCreative(@NotNull BuildCreativeModeTabContentsEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(@NotNull ServerStartingEvent event) {
        TickCommand.register(event.getServer().getCommands().getDispatcher());
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
