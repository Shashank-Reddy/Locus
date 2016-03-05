package sara.locus;


import android.Manifest;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.ErrorListener;


public class Drawer_Activity extends AppCompatActivity {
    Map_Fragment mf = new Map_Fragment(); //create the fragment instance for the map fragment

    List<LatLng> coords = new ArrayList<LatLng>(); //Used to store set of LatLngs received from server

    Location mLocation = mf.mLocation;
    double mLat = mLocation.getLatitude();
    double mLng = mLocation.getLongitude();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);


        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        try {
            Uri photoURI = Uri.parse(sharedPreferences.getString("photoURI", null));
            ImageView userPhotoImageView = (ImageView) findViewById(R.id.userPhotoImageView);
            String url = photoURI.toString();
            Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).resize(120, 120).into(userPhotoImageView);
        } catch (Exception e) {

        }

        TextView NameTextView = (TextView) findViewById(R.id.NameTextView);
        NameTextView.setText(name);

        TextView EmailTextView = (TextView) findViewById(R.id.EmailTextView);
        EmailTextView.setText(email);

        // Calling map_fragment from main(Drawer) activity
        FragmentManager manager = getSupportFragmentManager();//create an instance of fragment manager
        FragmentTransaction transaction = manager.beginTransaction();//create an instance of Fragment-transaction
        transaction.add(R.id.map, mf, "Map_Fragment");
        transaction.commit();
        //

        String caller = getIntent().getStringExtra("caller");
        try {
            Class callerClass = Class.forName(caller);
        } catch (Exception e) {
            e.getStackTrace();
        }


    }

    public void logOut(View view) {
        new AlertDialog.Builder(Drawer_Activity.this)
                .setTitle("\t\t\t\t\tLog Out")
                .setMessage(R.string.dialog_log_out)
                .setPositiveButton(R.string.logout, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with logout
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                        String name = sharedPreferences.getString("name", null);
                        String byeText = "Goodbye, " + name;
                        Toast toast = Toast.makeText(getApplicationContext(),
                                byeText,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 10);
                        toast.show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent i = new Intent(getApplicationContext(), Login_Activity.class);
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }


    @Override
    public void onBackPressed() {
        String caller = getIntent().getStringExtra("caller");
        try {
            Class callerClass = Class.forName(caller);
        } catch (Exception e) {
            e.getStackTrace();
        }
        if (caller == "Login_Activity") {

        } else {
            moveTaskToBack(true);
        }
    }

    public void sendToServer(View view) {
        EditText getDistance = (EditText) findViewById(R.id.eDistance); //eNTERED distance
        String fDistance = getDistance.getText().toString();

//        int fDistance=Integer.parseInt(getBudget.getText().toString());


        EditText getBudget = (EditText) findViewById(R.id.eBudget);
        String fBudget = getBudget.getText().toString();

//        int fBudget=Integer.parseInt(getBudget.getText().toString());

        EditText getHeadCount = (EditText) findViewById(R.id.eHeadCount);
        String fHeadCount = getHeadCount.getText().toString();

//        int fHeadCount=Integer.parseInt(getHeadCount.getText().toString());


        LatLng mLatLng = new LatLng(mLat, mLng); //Eureka, it's working finally


        Toast toast = Toast.makeText(getApplicationContext(),
                fDistance + "|" + fBudget + "|" + fHeadCount + "|" + mLatLng,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.show();
        //Control should go to the server from this point
        fromServer(fDistance, fBudget, fHeadCount, mLat, mLng); //Control should return from server to the Activity at this point
        mf.displayMarkers();//This is used to display the markers based on LatLngs


    }

    public void fromServer(String fDistance, String fBudget, String fHeadCount, double mLat, double mLng) {

//        double Latitude[]={17.400384,17.400579}; //Get this from server
//        double Longitude[]={78.489498,78.488653};//Get this from server (Test ke vaasthe KC,Agrawala)
//
//        //If we are using Json, we have to get set of LatLngs extracted from Json and push them into the List locally.
//
//        for (int i = 0 ; i < Latitude.length; i++)
//                    {
//                coords.add(new LatLng(Latitude[i], Longitude[i])); //adding items to LatLng List
//                    }

}

    public Request post(String Lat, String Lon,String url
                        Response.Listener listener, Response.ErrorListener errorListener) {
        Lat = new Double(mLat).toString();
        Lon = new Double(mLng).toString();
        url = "http://locus.arjun.ninja/api/geocode";
        JSONObject params = new JSONObject();
        params.put("lat", Lat);
        params.put("lon", Lon);
        Request req = new Request(
                Method.POST,
                url,
                params.toString(),
                listener,
                errorListener
        );

        return req;
    }

}
