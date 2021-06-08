package com.awesome.cab.commons;

import com.awesome.cab.commons.stubs.FakeProvider;
import com.awesome.cab.commons.tracker.GpsHelper;
import com.awesome.cab.commons.tracker.GpsProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class GpsHelperTest {
    private static GpsProvider customerPoint;
    private static GpsHelper helper;
    private static List<GpsProvider> cabs;

    @BeforeAll
    public static void setUp() {
        helper = new GpsHelper();
        customerPoint = new FakeProvider(9.0, 7.0);

        cabs = new ArrayList<GpsProvider>() {{
            this.add(new FakeProvider(1L, 3.0, 2.0));
            this.add(new FakeProvider(2L, 7.0, -1.0));
            this.add(new FakeProvider(3L, 6.2, 0.3));
            this.add(new FakeProvider(23L, 226.2, 120.3));
            this.add(new FakeProvider(4L, 9.1, 8.0));
        }};

    }

    @Test
    public void testNFarthestPointStream() {
        Stream<GpsProvider> closestStream = helper.nFarthestPointStream(customerPoint, cabs, 3);
        List<GpsProvider> res = closestStream.collect(Collectors.toList());
        Assertions.assertEquals(3, res.size());
    }

    @Test
    public void testNClosestPointStream() {
        Stream<GpsProvider> closestStream = helper.nClosestPointStream(customerPoint, cabs, 3);
        List<GpsProvider> res = closestStream.collect(Collectors.toList());
        Assertions.assertEquals(3, res.size());
    }

    @Test
    public void testClosestPoint() {
        Optional<GpsProvider> provider = helper.calcClosestPoint(customerPoint, cabs);
        Assertions.assertTrue(provider.isPresent());
        Assertions.assertEquals(9.1, provider.get().getLatitude());
        Assertions.assertEquals(8.0, provider.get().getLongitude());
    }

    @Test
    public void testFurthestPoint() {
        Optional<GpsProvider> provider = helper.calcFarthestPoint(customerPoint, cabs);
        Assertions.assertTrue(provider.isPresent());
        Assertions.assertEquals(226.2, provider.get().getLatitude());
        Assertions.assertEquals(120.3, provider.get().getLongitude());
    }

    @Test
    public void testCalcDistance() {
        double distance = helper.calcDistance(customerPoint, cabs.get(1));
        Assertions.assertEquals(8.246211251235321, distance);
    }

}
