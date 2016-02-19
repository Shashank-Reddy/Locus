package sara.locus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by SHASHANK on 19-02-2016.
 */
public class Map_Fragment extends Fragment implements OnMapReadyCallback {
    public static Map_Fragment newInstance() {
        Map_Fragment fragment = new Map_Fragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_maps, null, false);

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }


    public void onMapReady(GoogleMap map) {
        // Add a marker in kmit and move the camera.
        LatLng kmit = new LatLng(17.397129,78.490193);
        map.addMarker(new MarkerOptions().position(kmit).title("YOU ARE HERE"));
        map.moveCamera(CameraUpdateFactory.newLatLng(kmit));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));

    }
}
