package sara.locus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.security.Permission;
import java.security.Permissions;

/*
ConnectionCallbacks and OnConnectionFailedListener are designed to monitor the state of the GoogleApiClient,
which is used in this application for getting the user's current location.
OnInfoWindowClickListener is triggered when the user clicks on the info window that pops up over a marker on the map.
OnMapLongClickListener and OnMapClickListener are triggered when the user either taps or holds down on a portion of the map.
OnMarkerClickListener is called when the user clicks on a marker on the map,
which typically also displays the info window for that marker.
 */


public class Map_Fragment extends SupportMapFragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,LocationSource.OnLocationChangedListener
{
    public Map_Fragment()
    {
        //constructor
    }
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    Location mLocation;
    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 1;

    /*
    You may have noticed that the GoogleApiClient and listeners are created and bound from
    onViewCreated rather than the typical onCreate.
    This is because the GoogleMap object has not been initialized when onCreate is called,
    so we need to wait until the view is fully created before trying to call getMap in order
     to avoid a NullPointerException.
    */

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);
    // Creation of mGoogleApiClient initiates Location services
        mGoogleApiClient = new GoogleApiClient.Builder( getActivity() )
                .addConnectionCallbacks( this )
                .addOnConnectionFailedListener( this )
                .addApi( LocationServices.API )
                .build();
        initListeners();
    }
    /*
    The initListeners method binds the interfaces that are declared at
    the top of the class with the GoogleMap object associated with SupportMapFragment
     */

    private void initListeners() {
        getMap().setOnMarkerClickListener(this);
        getMap().setOnMapLongClickListener(this);
        getMap().setOnInfoWindowClickListener(this);
        getMap().setOnMapClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        if( mGoogleApiClient != null && mGoogleApiClient.isConnected() ) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {

        mCurrentLocation = LocationServices
                .FusedLocationApi
                .getLastLocation( mGoogleApiClient ); //mCurrentLocation returning null in the begining
        if(mCurrentLocation!=null)
        {
            initCamera(mCurrentLocation);
            mLocation=mCurrentLocation;
        }
        else
        {
            //handle by requesting the users location from GPS client
            Toast toast = Toast.makeText(getContext(),
                    "Accessing GPS location",
                    Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 10);
            toast.show();
            location_alternate();
           //Control is returned to this point successfully
        }



    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onLocationChanged(final Location location) {
        //mCurrentLocation = location; //This ain't helping to update the mCurrentLocation
    }

   //Method used in onConnected
    private void initCamera( Location location ) {
        CameraPosition position = CameraPosition.builder()
                .target( new LatLng( location.getLatitude(),
                        location.getLongitude() ) )
                .zoom( 16f )
                .bearing( 0.0f )
                .tilt( 0.0f )
                .build();

        getMap().animateCamera( CameraUpdateFactory
                .newCameraPosition( position ), null );

        getMap().setMapType( MAP_TYPES[curMapTypeIndex] );
        getMap().setTrafficEnabled( true );
        getMap().setMyLocationEnabled( true );
        getMap().getUiSettings().setZoomControlsEnabled(true);
        /*
        There's a few more interesting things that you can set using UiSettings,
        such as adding a compass or disabling gestures, which you can find in the
        Android reference documentation.
        */
    }

    //
    public void location_alternate()
    {
        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener()
        {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
               // mCurrentLocation=location; //This ain't helping to update the mCurrentLocation
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        //end of listener


        // Asking for location permissions at Run Time, because Android 6.0 _/\_

        final String[] LOCATION_PERMS=
                {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                };
        final int INITIAL_REQUEST=111;
        requestPermissions(LOCATION_PERMS,INITIAL_REQUEST);
        // Asking for permissions end

        // Checking if we have necessary permissions for Location Update
        if (locationManager != null)
        {
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        }
        // End of Checking permissions

    }

}