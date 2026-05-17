package com.gtnewhorizons.navigator.api.model.layers;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.gtnewhorizons.navigator.api.model.buttons.ButtonManager;
import com.gtnewhorizons.navigator.api.model.locations.ILocationProvider;
import com.gtnewhorizons.navigator.api.model.locations.IWaypointAndLocationProvider;
import com.gtnewhorizons.navigator.api.model.waypoints.Waypoint;
import com.gtnewhorizons.navigator.api.model.waypoints.WaypointManager;

public abstract class InteractableLayerManager extends LayerManager {

    protected final WaypointManager waypointManagers;

    protected Waypoint activeWaypoint = null;

    public InteractableLayerManager(ButtonManager buttonManager) {
        super(buttonManager);
        waypointManagers = addWaypointManager(this);
    }

    /**
     * @param manager This layer manager
     * @return The {@link LayerRenderer} implementation for the mod or null if none
     */
    protected abstract @Nullable LayerRenderer addLayerRenderer(InteractableLayerManager manager);

    /**
     * @param manager This layer manager
     * @return The {@link WaypointManager} implementation for the mod or null if none
     */
    protected @Nullable WaypointManager addWaypointManager(InteractableLayerManager manager) {
        return null;
    }

    /**
     * Update the information contained in the {@link IWaypointAndLocationProvider}
     * <p>
     * If this information is updated outside of this method {@link #forceRefresh()} should be called
     *
     * @param location The location to update
     */
    public void updateElement(IWaypointAndLocationProvider location) {}

    @Nullable
    @Override
    protected final LayerRenderer addLayerRenderer(LayerManager manager) {
        return addLayerRenderer(this);
    }

    public void setActiveWaypoint(Waypoint waypoint) {
        activeWaypoint = waypoint;
        getVisibleLocations().forEach(element -> element.onWaypointUpdated(waypoint));
        waypointManagers.updateActiveWaypoint(waypoint);
    }

    public void clearActiveWaypoint() {
        activeWaypoint = null;
        getVisibleLocations().forEach(IWaypointAndLocationProvider::onWaypointCleared);
        waypointManagers.clearActiveWaypoint();
    }

    public boolean hasActiveWaypoint() {
        return activeWaypoint != null;
    }

    public @Nullable WaypointManager getWaypointManager() {
        return waypointManagers;
    }

    @Override
    public final void updateElement(ILocationProvider location) {
        if (location instanceof IWaypointAndLocationProvider waypointLoc) {
            if (hasActiveWaypoint()) {
                waypointLoc.onWaypointUpdated(activeWaypoint);
            }
            updateElement(waypointLoc);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<IWaypointAndLocationProvider> getVisibleLocations() {
        return (Collection<IWaypointAndLocationProvider>) super.getVisibleLocations();
    }
}
