package kr.hs.mirim.doing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class EditUserPage extends AppCompatActivity {
    private ImageView edit_profile;
    private TextView edit_nickname;
    private EditText edit_nickname_nim;
    private EditText edit_I_doing;
    private Switch edit_onoff_direct;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_page);
        edit_profile = (ImageView) findViewById(R.id.edit_profile_circle);
        edit_nickname = (TextView) findViewById(R.id.edit_nickname);
        edit_nickname_nim = (EditText) findViewById(R.id.edit_nickname_nim);
        edit_I_doing = (EditText) findViewById(R.id.edit_I_doing);
        edit_onoff_direct = (Switch) findViewById(R.id.edit_onoff_direct);

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

    }
}