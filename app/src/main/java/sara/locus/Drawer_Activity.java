package sara.locus;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
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

        Map_Fragment mf=new Map_Fragment(); //create the fragment instance for the map fragment
        FragmentManager manager=getSupportFragmentManager();//create an instance of fragment manager

        FragmentTransaction transaction=manager.beginTransaction();//create an instance of Fragment-transaction

        transaction.add(R.id.map,mf, "Map_Fragment");
        transaction.commit();


    }

}
