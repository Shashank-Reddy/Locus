package sara.locus;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class locationDetails extends AppCompatActivity {
    String rId;
    String Phone;
    String Address;
    String isPlaceOpen;
    String webAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle bundle = getIntent().getExtras();
        if(bundle.getString("rName")!= null)
        {
            String rName=bundle.getString("rName");
            setTitle(rName);
        }

        if(bundle.getString("rId")!= null)
        {
            String rName=bundle.getString("rId");
            rId=rName.substring(17);
            Log.d("id",rId);
        }



//        ImageView img=(ImageView) findViewById(R.id.location_image);
//        Bitmap bMap = BitmapFactory.decodeResource(getResources(), R.mipmap.kc);
//        img.setImageBitmap(bMap);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int INITIAL_REQUEST = 111;
                final String LOCATION_PERMS[]={Manifest.permission.CALL_PHONE};
                requestPermissions(LOCATION_PERMS, INITIAL_REQUEST); //Works only with API23

                if(Phone.contentEquals("null")) {
                    Snackbar.make(view, "Phone number not Available", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    //opening dial pad code
                    Intent callIntent = new Intent(Intent.ACTION_DIAL); //ACTION_DIAL or ACTION_CALL
                    String p = "tel:"+Phone;
                    callIntent.setData(Uri.parse(p));
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
                    {startActivity(callIntent);}
                    else
                    {
                        Log.d("Error","Phone number couldn't be fetched , Server Error");
                    }
                }
            }
        });

        getDetails(rId);
    }

    public void getDetails(final String rId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://locus.arjun.ninja/api/place";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        try {
                            JSONObject jsonRootObject = new JSONObject(response);
                            Address=jsonRootObject.optString("address");
                            Phone=jsonRootObject.optString("phone_number");
                            webAddress=jsonRootObject.optString("website");
                            isPlaceOpen=jsonRootObject.optString("open_now");
                            TextView location_details=(TextView)findViewById(R.id.Address);
                            location_details.setText(Address);

                            TextView web_Address=(TextView)findViewById(R.id.webAddress);
                            web_Address.setText(webAddress);
                            web_Address.setClickable(true);
                            web_Address.setMovementMethod(LinkMovementMethod.getInstance());
                            web_Address.setText(Html.fromHtml(webAddress));

                            TextView isOpen=(TextView)findViewById(R.id.isOpen);
                            if(isPlaceOpen.equals("true")) {
                                isOpen.setText("Open");
                                isOpen.setTextColor(Color.parseColor("#33cc33"));
                            }
                            else {
                                isOpen.setText("Closed");
                                isOpen.setTextColor(Color.parseColor("#ff3333"));
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
                params.put("place_id",rId);
                return params;
            }
        };
        queue.add(postRequest);

    }

}
