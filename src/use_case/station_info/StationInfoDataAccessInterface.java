package use_case.station_info;

import entity.Station;
import entity.StationInterface;
import entity.Train;

import java.util.*;

//TODO: Review the changes made to getStation, setStation and getIncomingVehicles
// The changes were made in regard to their method parameters
public interface StationInfoDataAccessInterface {
    boolean incomingVehiclesNotEmpty(String stationName);

    StationInterface getStation(String inputStationName);

    void setStation(String stationName);

    List<Train> getIncomingVehicles(String inputStationName);
}
