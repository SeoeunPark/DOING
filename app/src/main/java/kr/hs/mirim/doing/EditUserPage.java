package kr.hs.mirim.doing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditUserPage extends AppCompatActivity {
    private LinearLayout edit_nickname;
    private LinearLayout edit_about;
    private Button edit_save;
    private TextView logout_btn;
    private DatabaseReference mDatabase;
    private FirebaseAuth auth;
    private String user_id = null;
    private TextView copy_code;
    private TextView copy_link;
    private Button copy_code_btn;
    private Button copy_link_btn;
    private FirebaseDatabase database;
    private DatabaseReference drMF;
    Dialog logout_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_page);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();
        copy_code = findViewById(R.id.copy_code);
        edit_nickname = findViewById(R.id.edit_nickname);
        edit_about = findViewById(R.id.edit_about);
        logout_btn = findViewById(R.id.logout_btn);
        copy_link = findViewById(R.id.copy_link);
        copy_code_btn = findViewById(R.id.copy_code_btn);
        copy_link_btn  = findViewById(R.id.copy_link_btn);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        logout_dialog = new Dialog(EditUserPage.this);
        logout_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        logout_dialog.setContentView(R.layout.logout_dialog);

        copy_link.setText("https://doing.emirim.kr/?id="+user_id);

        DocumentReference docRef = db.collection("User").document(user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String mycode = document.getString("user_code");
                        copy_code.setText(mycode);
                    } else {

                    }
                }
            }
        });

        copy_code_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("CODE", copy_code.getText()); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);
                //복사가 되었다면 토스트메시지 노출
                Toast.makeText(EditUserPage.this, "코드가 복사되었습니다.", Toast.LENGTH_SHORT).show();
            }
            });

        copy_link_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("LINK", copy_link.getText()); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);
                //복사가 되었다면 토스트메시지 노출
                Toast.makeText(EditUserPage.this, "링크가 복사되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });
        edit_nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditName.class));
            }
        });

        edit_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditAbout.class));
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    public void logout(){
        logout_dialog.show(); // 다이얼로그 띄우기
        logout_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 투명 배경

        // 아니오 버튼
        TextView noBtn = logout_dialog.findViewById(R.id.cancelButton);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                logout_dialog.dismiss(); // 다이얼로그 닫기
            }
        });
        // 네 버튼
        logout_dialog.findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 원하는 기능 구현
                auth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Signin.class));
                logout_dialog.dismiss(); // 다이얼로그 닫기
                finish();
            }
        });
    }
}
