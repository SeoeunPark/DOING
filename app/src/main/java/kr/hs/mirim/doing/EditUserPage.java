package kr.hs.mirim.doing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class EditUserPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView edit_profile;
    private TextView edit_nickname;
    private EditText edit_nickname_nim;
    private EditText edit_I_doing;
    private EditText edit_about;
    private EditText edit_level;
    private EditText edit_condition;
    private Switch edit_onoff_direct;
    private Button edit_save;

    private String user_about = ""; //about
    private String user_id=""; //id
    private String user_email = ""; //이메일
    private int user_condition = 0; //1~8까지의
    private String user_ing = ""; //하는 중
    private int user_level = 0; // 단계
    private String user_name = ""; //유저네임

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_page);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        edit_profile = (ImageView) findViewById(R.id.edit_profile_circle); // 프로필 상태
        edit_nickname_nim = (EditText) findViewById(R.id.edit_nickname_nim); // 님은
        edit_onoff_direct = (Switch) findViewById(R.id.edit_onoff_direct); // 쪽지 허용 비허용
        edit_save =(Button)findViewById(R.id.edit_save); //저장버튼

        edit_about = (EditText) findViewById(R.id.edit_about); // 한줄소개
        edit_condition = (EditText)findViewById(R.id.edit_condition); //1~3
        edit_I_doing = (EditText) findViewById(R.id.edit_I_doing); //~중
        edit_level = (EditText) findViewById(R.id.edit_level); //1,2,3
        edit_nickname = (TextView) findViewById(R.id.edit_nickname); // 이름


        //이름수정
        edit_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(EditUserPage.this);
                dialog.setTitle("이름을 입력해주세요");
                final EditText inputname = new EditText(EditUserPage.this);
                inputname.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(inputname);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = inputname.getText().toString();
                        edit_nickname.setText(username);
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();;
                    }
                });
                dialog.show();
            }
        });
        //님임 수정
        edit_nickname_nim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CharSequence[] items ={"은","는","이는"};
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(EditUserPage.this);
                alertDialogBuilder.setTitle("옵션 선택");

                alertDialogBuilder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        edit_nickname_nim.setText(items[id]);
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        edit_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditUser();
            }
        });
        }
        private void EditUser(){
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseUser user = mAuth.getCurrentUser();
            user_id = user.getUid();
            user_email = user.getEmail();

            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put("about",edit_about.getText().toString());
            userMap.put("condition",2);
            userMap.put("ing",edit_I_doing.getText().toString());
            userMap.put("level",1);
            userMap.put("name",edit_nickname.getText().toString());

            db.collection("User").document(user_id).set(userMap, SetOptions.merge()).addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Intent main = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(main);
                    }else {
                        String error = task.getException().getMessage();
                    }
                }
            });
    }
}