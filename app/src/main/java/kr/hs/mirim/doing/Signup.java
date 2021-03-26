package kr.hs.mirim.doing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Signup extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        LinearLayout goto_email = (LinearLayout) findViewById(R.id.go_to_email);
        goto_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_email = new Intent(getApplicationContext(), SignupEmail.class);
                startActivity(go_email);
            }
        });




    }

}