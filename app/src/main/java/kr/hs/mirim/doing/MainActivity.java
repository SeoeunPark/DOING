package kr.hs.mirim.doing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Main_Activity";
    private BottomNavigationView mBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBottomNavigationView=findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_container,new FriendsList()).commit();
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_menu1 :
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new FriendsList()).commit();
                        break;
                    case R.id.navigation_menu2:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new Message()).commit();
                        break;
                    case R.id.navigation_menu3:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,new UserPage()).commit();
                        break;
                }
                return true;
            }
        });
    }//end of OnCreate
}