package kr.hs.mirim.doing;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class sendPost extends AppCompatActivity {
    String[] customString = new String[5];

    private FirebaseUser currentUser;
    private FirebaseAuth auth;
    kr.hs.mirim.doing.ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,customString);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn.setAdapter(adapter);
        spn.setSelection(0);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentEt.getText().toString().trim();
                progressDialog.show();
                if(content.equals("")){
                    progressDialog.dismiss();
                    Toast.makeText(sendPost.this,"내용을 입력해주세요",Toast.LENGTH_SHORT).show();
                }else {
                    uploadPost(content, "보내는 사람",spn.getSelectedItem().toString());
                }
            }
        });


    }


    public void uploadPost(String message, String receiver, String shortMessage){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String current_uid = FirebaseAuth.getInstance().getUid();

        Date dt = new Date();
        SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Log.d("DATE",full_sdf.format(dt));

        HashMap<String, Object> postMap = new HashMap<>();
        postMap.put("content", message);
        postMap.put("sender", current_uid);
        postMap.put("receiver", receiver);
        postMap.put("read", false);
        postMap.put("time",full_sdf.format(dt));
        postMap.put("gist",shortMessage);


        db.collection("Post").document().set(postMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                finish();
                progressDialog.dismiss();
                Toast.makeText(sendPost.this, "쪽지를 전송했습니다", Toast.LENGTH_SHORT).show();
                Intent gotoPostList = new Intent(getApplicationContext(), PostList.class);
                startActivity(gotoPostList);
                return;
            }else{
                String error = task.getException().getMessage();
                Toast.makeText(sendPost.this,"전송 실패: "+error,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //발신자 정보 설정
    public void setReceiver(String receiver){

    }
}