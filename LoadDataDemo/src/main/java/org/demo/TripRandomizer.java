package org.demo;

import org.apache.commons.lang3.RandomUtils;

/**
 * Created by david on 2016-10-26.
 */
public class TripRandomizer {

    public static int randomDistance() {
        return RandomUtils.nextInt(20, 1230);
    }
}
