package com.gtnewhorizons.navigator.api;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import journeymap.client.ui.fullscreen.Fullscreen;
import net.minecraft.client.settings.KeyBinding;

import org.lwjgl.input.Keyboard;

import com.gtnewhorizons.navigator.Navigator;
import com.gtnewhorizons.navigator.api.model.buttons.ButtonManager;
import com.gtnewhorizons.navigator.api.model.layers.InteractableLayerManager;
import com.gtnewhorizons.navigator.api.model.layers.LayerManager;
import com.gtnewhorizons.navigator.api.model.layers.LayerRenderer;
import com.gtnewhorizons.navigator.api.util.Util;

import journeymap.client.render.map.GridRenderer;

public final class NavigatorApi {

    public static final double CHUNK_WIDTH = 16;
    public static final KeyBinding ACTION_KEY = new KeyBinding(
        "navigator.key.action",
        Keyboard.KEY_DELETE,
        Navigator.MODNAME);

    public static final List<LayerManager> layerManagers = new ArrayList<>();

    /**
     * @param layerManager The {@link LayerManager} to register.
     */
    public static void registerLayerManager(LayerManager layerManager) {
        layerManagers.add(layerManager);
    }

    public static List<LayerRenderer> getActiveRenderer() {
        return layerManagers.stream()
            .filter(LayerManager::isLayerActive)
            .map(LayerManager::getLayerRenderer)
            .collect(Collectors.toList());
    }

    public static List<LayerRenderer> getActiveRendererByPriority() {
        List<LayerRenderer> list = getActiveRenderer();
        list.sort(Comparator.comparingInt(LayerRenderer::getRenderPriority));
        return list;
    }


    public static List<ButtonManager> getEnabledButtons() {
        return layerManagers.stream()
            .map(LayerManager::getButtonManager)
            .distinct()
            .collect(Collectors.toList());
    }

    public static List<ButtonManager> getDistinctButtons() {
        return getDistinctButtons(null);
    }

    public static List<ButtonManager> getDistinctButtons(ButtonManager toExclude) {
        return layerManagers.stream()
            .map(LayerManager::getButtonManager)
            .distinct()
            .filter(buttonManager -> !buttonManager.equals(toExclude))
            .collect(Collectors.toList());
    }

    public static List<InteractableLayerManager> getInteractableLayers() {
        return layerManagers.stream()
            .filter(layerManager -> layerManager instanceof InteractableLayerManager)
            .map(layerManager -> (InteractableLayerManager) layerManager)
            .collect(Collectors.toList());
    }

    public void openJourneyMapAt(@Nullable LayerManager layer, int blockX, int blockZ, int zoom) {
        if (!Util.isJourneyMapInstalled()) return;
        final GridRenderer gridRenderer = Fullscreen.getGridRenderer();

        if (layer != null) layer.activateLayer();
        if (zoom == -1) zoom = gridRenderer.getZoom();
        gridRenderer.center(gridRenderer.getMapType(), blockX, blockZ, zoom);
    }

    public void openJourneyMapAt(@Nullable LayerManager layer, int blockX, int blockZ) {
        this.openJourneyMapAt(layer, blockX, blockZ, -1);
    }
}
