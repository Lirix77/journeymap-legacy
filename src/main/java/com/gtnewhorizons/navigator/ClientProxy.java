package com.gtnewhorizons.navigator;

import com.gtnewhorizons.navigator.api.NavigatorApi;
import com.gtnewhorizons.navigator.api.model.layers.LayerManager;
import com.gtnewhorizons.navigator.impl.DirtyChunkLayerManager;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
        ClientRegistry.registerKeyBinding(NavigatorApi.ACTION_KEY);
        //NavigatorApi.registerLayerManager(DirtyChunkLayerManager.INSTANCE);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public static void onClientConnect(FMLNetworkEvent.ClientConnectedToServerEvent event) {
        NavigatorApi.layerManagers.forEach(LayerManager::clearFullCache);
    }
}
