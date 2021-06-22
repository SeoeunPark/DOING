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

public class EditName extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String user_id = null;
    private EditText edit_name;
    private TextView now_name;
    private Button edit_name_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();

        edit_name_save = findViewById(R.id.edit_name_save);
        edit_name = findViewById(R.id.edit_nickname);
        now_name = findViewById(R.id.now_name);

        mDatabase.child("users").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                MyFriendList userInfo = task.getResult().getValue(MyFriendList.class);
                now_name.setText(userInfo.getName());
            }
        });

        edit_name_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserData(edit_name.getText().toString(),"name");
                ActivityCompat.finishAffinity(EditName.this);
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