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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.LineNumberInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class Drawer_Activity extends AppCompatActivity
{
    Map_Fragment mf=new Map_Fragment(); //create the fragment instance for the map fragment

    List<LatLng> coords=new ArrayList<LatLng>();//Used to store set of LatLngs received from server
    List<String> rNames=new ArrayList<String>();//Used to store set of Restraunt names received from serve
    List<String> rRatings=new ArrayList<String>();//Used to store set of Restraunt rating received from serve
    List<String> rId=new ArrayList<String>();


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
            Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).resize(120,120).into(userPhotoImageView);
        }
        catch (Exception e) {

        }

        TextView NameTextView = (TextView) findViewById(R.id.NameTextView);
        NameTextView.setText(name);

        TextView EmailTextView = (TextView) findViewById(R.id.EmailTextView);
        EmailTextView.setText(email);

        // Calling map_fragment from main(Drawer) activity
        FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
        FragmentTransaction transaction=manager.beginTransaction();//create an instance of Fragment-transaction
        transaction.add(R.id.map, mf, "Map_Fragment");
        transaction.commit();
        //

        String caller = getIntent().getStringExtra("caller");
        try {
            Class callerClass = Class.forName(caller);
        }
        catch (Exception e) {
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
                        String byeText = "Goodbye, "+name;
                        Toast toast = Toast.makeText(getApplicationContext(),
                                byeText,
                                Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.BOTTOM, 0, 10);
                        toast.show();
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();

                        Intent i = new Intent(getApplicationContext(),Login_Activity.class);
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
        }
        catch (Exception e) {
            e.getStackTrace();
        }
        if (caller == "Login_Activity") {

        }
        else {
            moveTaskToBack(true);
        }
    }

    public void sendToServer(View view)
    {

        EditText getDistance = (EditText) findViewById(R.id.eDistance); //eNTERED distance
        String fDistance=getDistance.getText().toString();

//        int fDistance=Integer.parseInt(getBudget.getText().toString());


        EditText getBudget = (EditText) findViewById(R.id.eBudget);
        String fBudget=getBudget.getText().toString();

//        int fBudget=Integer.parseInt(getBudget.getText().toString());

        EditText getHeadCount = (EditText) findViewById(R.id.eHeadCount);
        String fHeadCount=getHeadCount.getText().toString();

//        int fHeadCount=Integer.parseInt(getHeadCount.getText().toString());
        Location mLocation=mf.mLocation;

        double mLat=mLocation.getLatitude();
        double mLng=mLocation.getLongitude();

        LatLng mLatLng=new LatLng(mLat,mLng); //Eureka, it's working finally


        Toast toast = Toast.makeText(getApplicationContext(),
                fDistance+"|"+fBudget+"|"+fHeadCount+"|"+mLatLng,
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.show();
        //Control should go to the server from this point
       // fromServer(fDistance, fBudget, fHeadCount, mLat, mLng); //Control should return from server to the Activity at this point
        fromAzure(mLat, mLng, fDistance); //API waale LAtLngs
        mf.displayMarkers();//This is used to display the markers based on myLatLngs
        coords.clear();
        rNames.clear();
        rRatings.clear();
        rId.clear();

    }

    public void fromServer(String fDistance, String fBudget, String fHeadCount, double mLat, double mLng)
    {

        double Latitude[]={17.400384,17.400579}; //Get this from server
        double Longitude[]={78.489498,78.488653};//Get this from server (Test ke vaasthe KC,Agrawala)

        //If we are using Json, we have to get set of LatLngs extracted from Json and push them into the List locally.

        for (int i = 0 ; i < Latitude.length; i++)
                    {
                coords.add(new LatLng(Latitude[i], Longitude[i])); //adding items to LatLng List
                    }

    }

    public void fromAzure(final double mLat, final double mLng, final String fDistance) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://locus.arjun.ninja/api/geocode";
        final String sLat=Double.toString(mLat);
        final String sLng=Double.toString(mLng);
        final String Latitude[]=new String[100];
        final String Longitude[]=new String[100];


        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonRootObject = new JSONObject(response);
                            //Get the instance of JSONArray that contains JSONObjects
                            JSONArray jsonLatArray = jsonRootObject.getJSONArray("lats");
                            JSONArray jsonLangArray=jsonRootObject.getJSONArray("lngs");
                            JSONArray jsonNameArray=jsonRootObject.getJSONArray("names");
                            JSONArray jsonRatingArray=jsonRootObject.getJSONArray("ratings");
                            JSONArray jsonIdArray=jsonRootObject.getJSONArray("codes");

                            double length=jsonLatArray.length();
                            String len=Double.toString(length);
                            Log.d("Length",len);

                            //Iterate the jsonArray and print the info of JSONObjects
                            for(int i=0; i < jsonLatArray.length(); i++)
                            {
//                                JSONObject jsonLatObject = jsonLatArray.getJSONObject(i); //something wrong here
//                                Latitude[i]=jsonLatObject.toString();
                                Latitude[i]=jsonLatArray.optString(i);
                                double dLatitude=Double.parseDouble(Latitude[i]);
//                                JSONObject jsonLngObject = jsonLangArray.getJSONObject(i);
//                                Longitude[i]=jsonLngObject.toString();
                                Longitude[i]=jsonLangArray.optString(i);
                                double dLongitude=Double.parseDouble(Longitude[i]);
                                coords.add(new LatLng(dLatitude, dLongitude));
                                rNames.add(jsonNameArray.optString(i));
                                rRatings.add(jsonRatingArray.optString(i));
                                rId.add(jsonIdArray.optString(i));

                            }

                        }
                        catch (JSONException e) {e.printStackTrace();}
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO Auto-generated method stub
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("lat",sLat);
                params.put("lng",sLng);
                params.put("radius",fDistance);

                return params;
            }
        };
        queue.add(postRequest);

    }

}
