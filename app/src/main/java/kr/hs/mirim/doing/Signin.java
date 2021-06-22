package kr.hs.mirim.doing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Signin extends AppCompatActivity {
    private FirebaseUser currentUser = null;
    private FirebaseAuth mAuth = null;
    private String user_email = null;
    private String user_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mAuth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            user_email = currentUser.getEmail();
        }

        //로그인 버튼 이벤트
        Button loginbtn = (Button)findViewById(R.id.login_btn);
        Button demo1 = findViewById(R.id.demo1);
        Button demo2 = findViewById(R.id.demo2);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText email_edit = (EditText)findViewById(R.id.signin_email);
                EditText pass_edit = (EditText)findViewById(R.id.signin_pass);
                String loginEmail = email_edit.getText().toString();
                String loginPass = pass_edit.getText().toString();

                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)) {
                    mAuth.signInWithEmailAndPassword(loginEmail,loginPass).addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                currentUser = mAuth.getCurrentUser();
                                user_email = currentUser.getEmail();
                                if(currentUser != null && currentUser.isEmailVerified()){
                                    Toast.makeText(Signin.this,"환영합니다 :)",Toast.LENGTH_SHORT).show();
                                    //홈으로 이동
                                    Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(goHome);
                                    finish();
                                }else {
                                    Toast.makeText(Signin.this,"메일 링크를 확인해주세요",Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                String error = task.getException().getMessage();
                                Toast.makeText(Signin.this,"error :"+error,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(Signin.this,"이메일과 비밀번호를 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
        demo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword("ala0327@naver.com","ala0327").addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            user_email = currentUser.getEmail();
                            if(currentUser != null && currentUser.isEmailVerified()){
                                Toast.makeText(Signin.this,"환영합니다 :)",Toast.LENGTH_SHORT).show();
                                //홈으로 이동
                                Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(goHome);
                                finish();
                            }else {
                                Toast.makeText(Signin.this,"메일 링크를 확인해주세요",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Signin.this,"error :"+error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        demo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword("ara0327ara@gmail.com","ala0327").addOnCompleteListener(Signin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            user_email = currentUser.getEmail();
                            if(currentUser != null && currentUser.isEmailVerified()){
                                Toast.makeText(Signin.this,"환영합니다 :)",Toast.LENGTH_SHORT).show();
                                //홈으로 이동
                                Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(goHome);
                                finish();
                            }else {
                                Toast.makeText(Signin.this,"메일 링크를 확인해주세요",Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            String error = task.getException().getMessage();
                            Toast.makeText(Signin.this,"error :"+error,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        //회원가입
        TextView go_signup = (TextView) findViewById(R.id.goto_signup);
        go_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupEmail.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        currentUser = mAuth.getCurrentUser();

        //자동로그인
        if(currentUser != null && currentUser.isEmailVerified()){
            currentUser = mAuth.getCurrentUser();
            user_email=currentUser.getEmail();
            user_id = currentUser.getUid();
            gotoHome();
        }
    }

    private void gotoHome(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(user_id.toString()).get().addOnCompleteListener(this, new OnCompleteListener<DocumentSnapshot>() {
                @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(Signin.this,"환영합니다",Toast.LENGTH_SHORT).show();
                    Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(goHome);
                    finish();
                }else {
                    String error = task.getException().getMessage();
                    Toast.makeText(Signin.this,"error :"+error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}