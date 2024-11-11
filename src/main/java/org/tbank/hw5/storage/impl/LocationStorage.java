package org.tbank.hw5.storage.impl;

import org.springframework.stereotype.Repository;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.storage.DataObserver;
import org.tbank.hw5.storage.LocalStorage;

import java.util.List;

@Repository
public class LocationStorage extends LocalStorage<String, Location> implements DataObserver<Location> {
    @Override
    public void update(List<Location> elements) {
        for (Location location : elements) {
            save(location.getSlug(), location);
        }
    }
}
