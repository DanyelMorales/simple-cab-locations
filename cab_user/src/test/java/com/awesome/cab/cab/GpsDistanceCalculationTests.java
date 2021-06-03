package com.awesome.cab.cab;

import com.awesome.cab.cab.stubs.FakeProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GpsDistanceCalculationTests {

    @Test
    public void testCalculateDistance() {
        FakeProvider customerPoint = new FakeProvider(9.0, 7.0);
        FakeProvider cab = new FakeProvider(3.0, 2.0);
        Assertions.assertEquals(7.810249675906654, customerPoint.calculateDistance(cab));

        customerPoint = new FakeProvider(-3.0, 5.0);
        cab = new FakeProvider(7.0, -1.0);
        Assertions.assertEquals(11.661903789690601, customerPoint.calculateDistance(cab));

        customerPoint = new FakeProvider(2.6, 1.0);
        cab = new FakeProvider(6.2, 0.3);
        Assertions.assertEquals(3.66742416417845, customerPoint.calculateDistance(cab));
    }


}
