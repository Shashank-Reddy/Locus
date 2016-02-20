package sara.locus;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (sharedPreferences.contains("isLoggedIn")) {
            Intent i = new Intent(getApplicationContext(),Drawer_Activity.class);
            i.putExtra("caller", "MainActivity");
            startActivity(i);
        }
        else {
            Intent i = new Intent(getApplicationContext(),Login_Activity.class);
            i.putExtra("caller", "MainActivity");
            startActivity(i);
        }
        editor.apply();
        finish();
    }
}
