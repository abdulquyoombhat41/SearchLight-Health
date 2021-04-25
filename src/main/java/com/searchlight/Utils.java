package com.searchlight;

public class Utils {
  //suppose time taken to travel 1km is 60min
  public double TIME_TAKEN_TO_TRAVEL_1_KM = 60;

  public static double distance(double lat1, double long1, double lat2, double long2) {
    if((lat1 == lat2) &&(long1 == long2)) {
      return 0;
    } else {
      double delta = long1 - long2;
      double distance = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) +
          Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(delta));
      distance = Math.acos(distance);
      distance = Math.toDegrees(distance);
      distance = distance * 60 * 1.1515;
      return distance * 1.609344;
    }
  }
  public static int calculateTime(double distance) {
    return (int)Math.round(distance*60);
  }

}
