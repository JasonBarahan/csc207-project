package interface_adapter.search;

import interface_adapter.ViewManagerModel;
import interface_adapter.station_general_info.StationGeneralInfoState;
import interface_adapter.station_general_info.StationGeneralInfoViewModel;
import use_case.station_general_info.StationGeneralInfoOutputBoundary;
import use_case.station_general_info.StationGeneralInfoOutputData;

public class SearchPresenter implements StationGeneralInfoOutputBoundary {
    private final SearchViewModel searchViewModel;
    private final StationGeneralInfoViewModel stationInfoViewModel;
    private final ViewManagerModel viewManagerModel;

    public SearchPresenter(SearchViewModel searchViewModel, StationGeneralInfoViewModel stationInfoViewModel, ViewManagerModel viewManagerModel) {
        this.searchViewModel = searchViewModel;
        this.stationInfoViewModel = stationInfoViewModel;
        this.viewManagerModel = viewManagerModel;
    }

    public void prepareSuccessView(StationGeneralInfoOutputData response){
        String retrieveStationName = response.getStationName();
        String retrieveStationAmenities = response.getStationAmenities();
        String retrieveParentLine = response.getStationParentLine();

        // Step 1: Resetting the station error value in the searchState to be null (in case any failed search attempts came before this "successful" attempt)
        SearchState searchState = searchViewModel.getState();
        searchState.setStateStationName(retrieveStationName);
      
        //  This line sets the state's station error attribute value to null.
        //  This happens ONLY IF the prepareSuccessView has been triggered
        //  This is because should there be an error message stored in the state, we want to CLEAR THAT to display the success view
        searchState.setStateStationError(null);        

        // Step 2: Setting values in the SearchPanelView

        // Setting the station name value and updating state with change
        StationGeneralInfoState stationAmenitiesInfoState = stationInfoViewModel.getState();
        stationAmenitiesInfoState.setStateStationName(retrieveStationName);

        // Setting the station amenities list and updating state with change
        stationAmenitiesInfoState.setStateStationAmenities(retrieveStationAmenities);

        // Setting the station amenities list and updating state with change
        stationAmenitiesInfoState.setStateStationParentLine(retrieveParentLine);

        stationInfoViewModel.firePropertyChanged();

        viewManagerModel.setActiveView(stationInfoViewModel.getViewName());
        viewManagerModel.firePropertyChanged();
    }

    public void prepareFailView(String stationRetrievalError){
        SearchState searchState = searchViewModel.getState();
        searchState.setStateStationError(stationRetrievalError);
        searchViewModel.firePropertyChanged();
    }
}
