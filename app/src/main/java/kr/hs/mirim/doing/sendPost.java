package kr.hs.mirim.doing;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class sendPost extends AppCompatActivity {
    String[] customString = new String[5];
    private String receiver_name;

    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    kr.hs.mirim.doing.ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post);

        Intent intent = getIntent();
        String sendUid = intent.getStringExtra("uid");
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = FirebaseAuth.getInstance().getUid();
        progressDialog = new ProgressDialog(this);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.setCancelable(false);

        customString = new String[]{"기본", "긴급사항입니다!", "면담원해요", "놀자", "전화주세요"};

        Button sendBtn = findViewById(R.id.sendBtn);
        EditText contentEt = findViewById(R.id.sendContents);
        Spinner spn = findViewById(R.id.spinner);
        TextView toName = findViewById(R.id.toName);
        receiver_name = intent.getStringExtra("name");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,customString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        spn.setSelection(0);
        toName.setText(receiver_name);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEt.getText().toString().trim();
                progressDialog.show();
                if(content.equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(sendPost.this,"내용을 입력해주세요",Toast.LENGTH_SHORT).show();
                }else {
                    uploadPost(content, sendUid ,spn.getSelectedItem().toString());
                }
            }
        });


    }


    public void uploadPost(String message, String receiver, String shortMessage){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String current_uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        Date dt = new Date();
        SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");


        mDatabase.child("users").child(current_uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                MyFriendList userInfo = task.getResult().getValue(MyFriendList.class);

                HashMap < String, Object > postMap = new HashMap<>();
                postMap.put("content", message);
                postMap.put("sender", current_uid);
                postMap.put("receiver", receiver);
                postMap.put("receiver_name", receiver_name);
                postMap.put("sender_name", userInfo.getName());
                postMap.put("read", false);
                postMap.put("time",full_sdf.format(dt));
                postMap.put("gist",shortMessage);


                db.collection("Post").document().set(postMap).addOnCompleteListener(dbtask -> {
                    if (dbtask.isSuccessful()) {
                        Toast.makeText(sendPost.this, "쪽지를 전송했습니다", Toast.LENGTH_SHORT).show();
                        finish();
                        return;
                    }else{
                        String error = dbtask.getException().getMessage();
                        Toast.makeText(sendPost.this,"전송 실패: "+error,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

}