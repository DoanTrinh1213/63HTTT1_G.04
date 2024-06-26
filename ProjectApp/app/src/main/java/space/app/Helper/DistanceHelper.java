package space.app.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DistanceHelper {
    private static DistanceHelper instance;
    private Map<String, Double> distanceMap;

    private DistanceHelper() {
        this.distanceMap = new HashMap<>();
    }

    public static synchronized DistanceHelper getInstance() {
        if (instance == null) {
            instance = new DistanceHelper();
        }
        return instance;
    }

    public void addDistance(String key, double distance) {
        this.distanceMap.put(key, distance);
    }

    public double getDistance(String key) {
        return this.distanceMap.getOrDefault(key, -1.0);
    }

    public Map<String, Double> getAllDistances() {
        return this.distanceMap;
    }

    public List<String> getDistanceNearlyYou() {
        return getDistanceNearlyYou(5);
    }

    public List<String> getDistanceNearlyYou(float distance) {
        List<String> nearbyPlaces = new ArrayList<>();
        List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(distanceMap.entrySet());
        Collections.sort(sortedEntries,
                (entry1, entry2) ->
                        entry1.getValue().compareTo(entry2.getValue()));
        for (Map.Entry<String, Double> entry : sortedEntries) {
            if (entry.getValue() <= distance) {
                nearbyPlaces.add(entry.getKey());
            }
        }
        return nearbyPlaces;
    }

    public void deleteAllDistance(){
        this.distanceMap.clear();
    }

    public List<String> shortDistance(){
        List<String> disStrings = new ArrayList<>();List<Map.Entry<String, Double>> sortedEntries = new ArrayList<>(distanceMap.entrySet());
        Collections.sort(sortedEntries,
                (entry1, entry2) ->
                        entry1.getValue().compareTo(entry2.getValue()));
        for(Map.Entry<String,Double> entry:sortedEntries){
            disStrings.add(entry.getKey());
        }
        return disStrings;
    }
}
