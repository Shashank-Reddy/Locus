package sara.locus;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
        new AlertDialog.Builder(Drawer_Activity.this)
                .setTitle("Log Out")
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
        EditText getDistance = (EditText) findViewById(R.id.enterDistance);
       // String fDistance=getDistance.getText().toString();
        int fDistance=Integer.parseInt(getDistance.getText().toString());

        EditText getBudget = (EditText) findViewById(R.id.enterBudget);
        int fBudget=Integer.parseInt(getBudget.getText().toString());

        EditText getHeadCount = (EditText) findViewById(R.id.enterHeadCount);
        int fHeadCount=Integer.parseInt(getHeadCount.getText().toString());

        Toast toast = Toast.makeText(getApplicationContext(),
                getString(fDistance),
                Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 10);
        toast.show();

        Intent i = new Intent(getApplicationContext(),Drawer_Activity.class);
        startActivity(i);
    }

}
