package data_access;

import api.GoStationAPI;
import entity.Vehicle;
import entity.StationFactory;
import use_case.search.SearchDataAccessInterface;

import java.io.*;
import java.util.*; // resolves import for List and ArrayList
import java.util.HashMap;
import java.util.Map;
import entity.Station;

// We will name it as FileStationDataAccessObject for now. When we start to implement vehicles, we will change it as requires
// We might need to create different DA0 java files based on what data we are pulling (station, train or bus)
public class FileStationDataAccessObject implements SearchDataAccessInterface {
    private final File stationTxtFile;
    private final Map<String, Station> stations = new HashMap<>();
    private final StationFactory stationFactory;

    public FileStationDataAccessObject(String txtFilePath, StationFactory stationFactory) throws IOException {

        this.stationFactory = stationFactory;
        stationTxtFile = new File(txtFilePath);

        // Reading the provided txt file that has a path specified by attribute txtFilePath
        try(BufferedReader reader = new BufferedReader(new FileReader(stationTxtFile))){
            String line;
            reader.readLine(); // call the readline() method once outside the loop as we do not want to read the first line of txt file, since that line contains just the headers.
            while((line = reader.readLine()) != null)  {

                String[] parsedStationInfo = line.split(","); //splitting  by "," since information in txt file is seperated by ","
                String parsedStationName = parsedStationInfo[1];
                String parsedStationID = parsedStationInfo[0];

                String parsedStationParentLine = parsedStationInfo[5];
                Float parsedStationLatitude = Float.valueOf(parsedStationInfo[2]); // converting string type to float object type. Through potential autoboxing, this float object type is converted to primitative type.
                Float parsedStationLongtitude = Float.valueOf(parsedStationInfo[3]);

                // We want to minimize API calls (even though we have unlimited quota) so such information
                // will only be displayed after a specific station is queried
                List <String> parsedStationAmenities = new ArrayList<String>(); //This is empty at the time of reading txt file, this will be populated through API calls
                List <Vehicle> parsedStationVehicles = new ArrayList<Vehicle>(); //This is empty at the time of reading txt file, this will be populated through API calls

                // For reference, here are the order of arguments in order to pass into stationFactory.create():
                //(name, stationId, parentLine, latitude, longitude, amenitiesList, incomingVehicles)
                Station station = stationFactory.create(parsedStationName, parsedStationID, parsedStationParentLine, parsedStationLatitude, parsedStationLongtitude, parsedStationAmenities, parsedStationVehicles);

                stations.put(parsedStationName, station);
            }
        }
    }
    @Override
    public Station getStation (String inputStationName) {
        Station station = stations.get(inputStationName);

        // We will obtain the station amenities
        station.setAmenitiesList(getStationAmenities(station.getId()));

        return station;
    }

    @Override
    public String getStationParentLine(String inputStationName) {

        return (stations.get(inputStationName)).getParentLine();
    }

    @Override
    public List<String> getStationAmenities(String inputStationName) {
        GoStationAPI api = new GoStationAPI();
        return api.getStationAmenities(inputStationName);
    }

    public boolean stationExist(String identifier){
        return stations.containsKey(identifier); //TODO: MASSIVE ASSUMPTION HERE THAT THE USER types input in correct casing
                                                 // May need to resolve this by converting user input to lowercase -> then comparing to txt names (which will also be compared in lowercase form?)

    }
}
