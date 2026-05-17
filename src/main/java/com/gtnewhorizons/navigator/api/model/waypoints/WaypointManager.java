package com.gtnewhorizons.navigator.api.model.waypoints;

import com.gtnewhorizons.navigator.api.model.layers.InteractableLayerManager;

public abstract class WaypointManager {

    protected final InteractableLayerManager manager;

    public WaypointManager(InteractableLayerManager layerManager) {
        this.manager = layerManager;
    }

    public abstract void clearActiveWaypoint();

    public abstract void updateActiveWaypoint(Waypoint waypoint);

    public abstract boolean hasWaypoint();
}
