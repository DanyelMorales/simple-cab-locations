/**
 * This interface aims to simplify distance calculation by implementing following formula:
 * https://www.mathsisfun.com/algebra/distance-2-points.html
 */
package com.awesome.cab.cab.tracker;

public interface GpsProvider {

    double getLongitude();

    double getLatitude();

    void setDistance(double value);

    double getDistance();

    // implementing default method as an easy way to resolve distance by using Pythagoras
    default Double calculateDistance(GpsProvider target) {
        double xA = this.getLatitude();
        double xB = target.getLatitude();
        double yA = this.getLongitude();
        double yB = target.getLongitude();
        double a2 = Math.pow(xA - xB, 2);
        double b2 = Math.pow(yA - yB, 2);
        double distance = Math.sqrt(a2 + b2);
        target.setDistance(distance);
        return distance;
    }
}
