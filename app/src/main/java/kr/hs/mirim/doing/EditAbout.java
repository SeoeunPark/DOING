package kr.hs.mirim.doing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditAbout extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String user_id = null;
    private EditText edit_about;
    private Button edit_about_save;
    private TextView now_about;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_about);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();

        edit_about_save = findViewById(R.id.edit_about_save);
        edit_about = findViewById(R.id.edit_about);
        now_about = findViewById(R.id.now_about);

        mDatabase.child("users").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                MyFriendList userInfo = task.getResult().getValue(MyFriendList.class);
//                edit_about.setText(userInfo.getAbout());
                now_about.setText(userInfo.getAbout());
            }
        });

        edit_about_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserData(edit_about.getText().toString(),"about");
                ActivityCompat.finishAffinity(EditAbout.this);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

    }
    private void setUserData(String n, String key){
        Map<String, Object> conditionUpdates = new HashMap<>();
        conditionUpdates.put("/users/" + user_id+"/"+key, n);
        mDatabase.updateChildren(conditionUpdates);
    }
}