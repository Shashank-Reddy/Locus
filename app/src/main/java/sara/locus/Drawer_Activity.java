package sara.locus;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


public class Drawer_Activity extends AppCompatActivity
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);


        String name = sharedPreferences.getString("name", null);
        String email = sharedPreferences.getString("email", null);
        Uri photoURI = Uri.parse(sharedPreferences.getString("photoURI", null));


        ImageView userPhotoImageView = (ImageView) findViewById(R.id.userPhotoImageView);
        String url = photoURI.toString();
        Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).resize(120,120).into(userPhotoImageView);

        TextView NameTextView = (TextView) findViewById(R.id.NameTextView);
        NameTextView.setText(name);

        TextView EmailTextView = (TextView) findViewById(R.id.EmailTextView);
        EmailTextView.setText(email);
        // Calling map_fragment from main(Drawer) activity
        Map_Fragment mf=new Map_Fragment(); //create the fragment instance for the map fragment
        FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager
        FragmentTransaction transaction=manager.beginTransaction();//create an instance of Fragment-transaction
        transaction.add(R.id.map,mf, "Map_Fragment");
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
        // delete sharedPReferences
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


}
