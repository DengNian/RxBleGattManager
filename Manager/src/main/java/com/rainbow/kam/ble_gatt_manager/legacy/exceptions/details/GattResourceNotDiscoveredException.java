package com.rainbow.kam.ble_gatt_manager.legacy.exceptions.details;

import com.rainbow.kam.ble_gatt_manager.legacy.exceptions.GattException;

/**
 * Created by Kang Young Won on 2016-05-12.
 */
public class GattResourceNotDiscoveredException extends GattException {

    public GattResourceNotDiscoveredException(String detailMessage) {
        super(detailMessage);
    }
}
