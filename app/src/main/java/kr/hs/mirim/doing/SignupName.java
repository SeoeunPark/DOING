package kr.hs.mirim.doing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignupName extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String user_id = null;
    private String user_email = null;

    private String dup_id = null;
    private String input_id = null;

    private EditText userName;
    private EditText userCode;
    private Button gotoHome;
    private Button btn_dup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_name);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user_id = user.getUid();
        user_email = user.getEmail();

        userName = findViewById(R.id.name_space);
        userCode = findViewById(R.id.userCode);
        gotoHome = findViewById(R.id.go_to_home);
        btn_dup = findViewById(R.id.duplicate);

        gotoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = userName.getText().toString();
                String code = userCode.getText().toString();

                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(code)){
                    if(dup_id != null && dup_id.equals(code)){
                        addUser(name, code);
                    }else{
                        Toast.makeText(SignupName.this,"중복체크를 해주세요!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignupName.this,"모두 입력해주세요~",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_dup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dupCheck();
            }
        });


    }
    private void addUser(String name, String userCode){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user_id = user.getUid();
        user_email = user.getEmail();

        ArrayList friends = new ArrayList();
        HashMap<String, Object> userMap = new HashMap<>();
        HashMap<String, Object> userMap2 = new HashMap<>();

        userMap.put("name",name);
        userMap.put("email",user_email);
        userMap.put("user_code", userCode);

        userMap2.put("name",name);
        userMap2.put("about","");
        userMap2.put("condition",0);
        userMap2.put("ing","");
        userMap2.put("level",0);

        db.collection("User").document(user_id).set(userMap).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("users").child(user_id).setValue(userMap2);
                    FirebaseDatabase.getInstance().getReference().child("my_friends").child(user_id).setValue(user_id);

                    Toast.makeText(SignupName.this,"환영~",Toast.LENGTH_SHORT).show();
                    Intent goLogin = new Intent(getApplicationContext(), Signin.class);
                    startActivity(goLogin);
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(SignupName.this,"error :"+error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //중복 제거
    private void dupCheck(){
        userCode = findViewById(R.id.userCode);
        input_id = userCode.getText().toString();
        if(!TextUtils.isEmpty(input_id)){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("User").whereEqualTo("user_code",input_id).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (queryDocumentSnapshots.isEmpty()) {
                        dup_id = input_id;
                        Toast.makeText(SignupName.this,"사용가능합니다",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SignupName.this,"이미 있는 코드입니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }else {
            Toast.makeText(SignupName.this,"고유코드를 입력해주세요!",Toast.LENGTH_SHORT).show();
        }


    }
}