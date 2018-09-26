package org.techtown.share_chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

public class MapViewFragmentNaver extends NMapFragment
        implements NMapView.OnMapStateChangeListener, NMapPOIdataOverlay.OnStateChangeListener {

    NMapView mapView;
    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null) {
            //moveMapCenter();
        } else {
            Log.e("map init error", nMapError.message);
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fregment, container, false);
        mapView = (NMapView) v.findViewById(R.id.map_view);
        mapView.setClientId(getResources().getString(R.string.n_key));
        mapView.setClickable(true);
        return v;
    }

    public void onStart() {
        super.onStart();
        mapView.setBuiltInZoomControls(true, null);
        mapView.setOnMapStateChangeListener(this);
        NMapController mapController = mapView.getMapController();
        NMapViewerResourceProvider mapViewerResourceProvider = new NMapViewerResourceProvider(getActivity());
        NMapOverlayManager mapOverlayManager = new NMapOverlayManager(getActivity(), mapView, mapViewerResourceProvider);
        //moveMapCenter();
    }

    @Override
    public void onFocusChanged(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

    }

    @Override
    public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

    }
    /*private void moveMapCenter() {
        NGeoPoint currentPoint = new NGeoPoint(lng, lat);
        mapController.setMapCenter(currentPoint);

        NMapPOIdata poiData = new NMapPOIdata(2, mapViewerResourceProvider);
        poiData.addPOIitem(lng, lat, "현재 위치", NMapPOIflagType.FROM, 0);
        poiData.addPOIitem(lng2, lat2, "도착 위치", NMapPOIflagType.TO, 0);
        poiData.endPOIdata();

        NMapPOIdataOverlay poiDataOverlay = mapOverlayManager.createPOIdataOverlay(poiData, null);
        poiDataOverlay.showAllPOIdata(0);
        poiDataOverlay.setOnStateChangeListener(this);

    }*/
}