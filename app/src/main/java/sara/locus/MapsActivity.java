package sara.locus;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
        @Override

        //Map code
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_maps);
       SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }

    @Override
    public void onMapReady(GoogleMap map) {
       // Add a marker in kmit and move the camera.
        LatLng kmit = new LatLng(17.397129,78.490193);
       map.addMarker(new MarkerOptions().position(kmit).title("Marker in kmit"));
        map.moveCamera(CameraUpdateFactory.newLatLng(kmit));
       map.animateCamera(CameraUpdateFactory.zoomTo(15));

    }
    //End of map code

}