package org.popcraft.bolt;

import org.popcraft.bolt.data.Store;
import org.popcraft.bolt.util.BoltPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Bolt {
    private final AccessRegistry accessRegistry = new AccessRegistry();
    private final Map<UUID, BoltPlayer> players = new HashMap<>();
    private Store store;

    public Bolt(final Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public BoltPlayer getBoltPlayer(final UUID uuid) {
        return players.computeIfAbsent(uuid, x -> new BoltPlayer(uuid));
    }

    public void removeBoltPlayer(final UUID uuid) {
        players.remove(uuid);
    }

    public AccessRegistry getAccessRegistry() {
        return accessRegistry;
    }
}
