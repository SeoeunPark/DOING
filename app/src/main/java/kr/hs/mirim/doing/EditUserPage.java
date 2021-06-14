package kr.hs.mirim.doing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class EditUserPage extends AppCompatActivity {
    private EditText edit_nickname;
    private EditText edit_about;
    private Button edit_save;
    private TextView logout_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String user_id = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_page);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        edit_nickname = findViewById(R.id.edit_nickname);
        edit_about = findViewById(R.id.edit_about);
        edit_save = findViewById(R.id.edit_save);
        logout_btn = findViewById(R.id.logout_btn);

        mDatabase.child("users").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                MyFriendList userInfo = task.getResult().getValue(MyFriendList.class);
                edit_about.setText(userInfo.getAbout());
                edit_nickname.setText(userInfo.getName());
            }
        });

        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserData(edit_nickname.getText().toString(),"name");
                setUserData(edit_about.getText().toString(),"about");
                ActivityCompat.finishAffinity(EditUserPage.this);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));

            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Signin.class));
                finish();
            }
        });

    }
    private void setUserData(String n, String key){
        Map<String, Object> conditionUpdates = new HashMap<>();
        conditionUpdates.put("/users/" + user_id+"/"+key, n);
        mDatabase.updateChildren(conditionUpdates);
    }
}