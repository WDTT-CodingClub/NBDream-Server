package nbdream.farm.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {

    private double latitude;
    private double longitude;

    public static int findNearestLocation(Coordinates baseCoordinates, Map<Integer, Coordinates> coordinatesMap) {
        double minDistance = Double.MAX_VALUE;
        int nearestNo = -1;

        for (Map.Entry<Integer, Coordinates> entry : coordinatesMap.entrySet()) {
            double distance = haversine(baseCoordinates, entry.getValue());
            if (distance < minDistance) {
                minDistance = distance;
                nearestNo = entry.getKey();
            }
        }

        return nearestNo;
    }

    private static double haversine(Coordinates coord1, Coordinates coord2) {
        final int R = 6371; // Radius of the earth in km
        double latDistance = Math.toRadians(coord2.getLatitude() - coord1.getLatitude());
        double lonDistance = Math.toRadians(coord2.getLongitude() - coord1.getLongitude());
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(coord1.getLatitude())) * Math.cos(Math.toRadians(coord2.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to kilometers
        return distance;
    }
}
