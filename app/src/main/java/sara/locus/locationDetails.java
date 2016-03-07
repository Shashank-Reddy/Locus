package sara.locus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
                Snackbar.make(view, "Feature not yet coded", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        getDetails(rId);
    }

    public void getDetails(final String rId) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://locus.arjun.ninja/api/restaurant/res_id";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);

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
                params.put("res_id",rId);

                return params;
            }
        };
        queue.add(postRequest);

    }

}
