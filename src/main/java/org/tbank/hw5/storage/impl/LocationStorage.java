package org.tbank.hw5.storage.impl;

import org.springframework.stereotype.Repository;
import org.tbank.hw5.model.Location;
import org.tbank.hw5.storage.LocalStorage;

@Repository
public class LocationStorage extends LocalStorage<String, Location> {
}
