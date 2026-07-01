package com.community.marketplace.common;

import java.math.BigDecimal;

public class GeoUtils {

    private static final double EARTH_RADIUS_KM = 6371.0;

    public static double calculateDistanceKm(BigDecimal lat1, BigDecimal lon1, BigDecimal lat2, BigDecimal lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return -1;
        }

        double dLat = Math.toRadians(lat2.doubleValue() - lat1.doubleValue());
        double dLon = Math.toRadians(lon2.doubleValue() - lon1.doubleValue());

        double rLat1 = Math.toRadians(lat1.doubleValue());
        double rLat2 = Math.toRadians(lat2.doubleValue());

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.sin(dLon / 2) * Math.sin(dLon / 2)
                * Math.cos(rLat1) * Math.cos(rLat2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }

    public static String formatDistance(double distanceKm) {
        if (distanceKm < 0) {
            return "距离未知";
        }
        if (distanceKm < 1) {
            int meters = (int) Math.round(distanceKm * 1000);
            return meters + "米";
        } else {
            return String.format("%.1f公里", distanceKm);
        }
    }

    public static BigDecimal calculateMinLatitude(BigDecimal centerLat, double radiusKm) {
        double degreesPerKm = 1.0 / 111.0;
        return centerLat.subtract(BigDecimal.valueOf(radiusKm * degreesPerKm));
    }

    public static BigDecimal calculateMaxLatitude(BigDecimal centerLat, double radiusKm) {
        double degreesPerKm = 1.0 / 111.0;
        return centerLat.add(BigDecimal.valueOf(radiusKm * degreesPerKm));
    }

    public static BigDecimal calculateMinLongitude(BigDecimal centerLat, BigDecimal centerLon, double radiusKm) {
        double degreesPerKm = 1.0 / (111.0 * Math.cos(Math.toRadians(centerLat.doubleValue())));
        return centerLon.subtract(BigDecimal.valueOf(radiusKm * degreesPerKm));
    }

    public static BigDecimal calculateMaxLongitude(BigDecimal centerLat, BigDecimal centerLon, double radiusKm) {
        double degreesPerKm = 1.0 / (111.0 * Math.cos(Math.toRadians(centerLat.doubleValue())));
        return centerLon.add(BigDecimal.valueOf(radiusKm * degreesPerKm));
    }
}
