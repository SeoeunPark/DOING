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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupEmail extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String userEmail;
    private String userPass;
    private String userRepass;
    private String user_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_email);
        mAuth = FirebaseAuth.getInstance();

        final EditText editTextEmail = (EditText)findViewById(R.id.email);
        final EditText editTextPassword = (EditText)findViewById(R.id.pass);
        final EditText editTextRepass = (EditText)findViewById(R.id.repass);

        Button go_next = (Button)findViewById(R.id.go_to_name);

        go_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = editTextEmail.getText().toString();
                userPass = editTextPassword.getText().toString();
                userRepass = editTextRepass.getText().toString();

                //빈 값이 있는지 확인
                if(!TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass)) {
                    //비밀번호 길이 확인
                    if(userPass.length()>=6){
                        //재입력 비밀번호와 비밀번호 값 비교
                        if (userPass.equals(userRepass)) {
                            mAuth.createUserWithEmailAndPassword(userEmail,userPass).addOnCompleteListener(SignupEmail.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        user_id = user.getUid();
                                        mAuth.setLanguageCode("kr");
                                        mAuth.getCurrentUser().sendEmailVerification();
                                        Intent go_name = new Intent(getApplicationContext(), SignupName.class);
                                        startActivity(go_name);
                                    }else{
                                        String error = task.getException().getMessage();
                                        Toast.makeText(SignupEmail.this,"error :"+error,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }else{
                            Toast.makeText(SignupEmail.this,"비밀번호를 다시 확인해주세요",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignupEmail.this,"비밀번호가 너무 짧아요",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(SignupEmail.this,"모두 입력해주세요",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}