package kr.hs.mirim.doing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

public class UserPage extends Fragment {
    private ImageView profile;
    private TextView nickname;
    private TextView nickname_nim;
    private TextView I_doing;
    private Switch text_onoff_direct;
    private ImageButton edit_pofile;
    private ImageButton send_post;
    private Button message;
    private String title;
    private int page;

    //인디케이터 만들기
    // newInstance constructor for creating fragment with arguments
    public static UserPage newInstance(int page, String title) {
        UserPage fragment = new UserPage();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt",0);
        title = getArguments().getString("someTitle");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_user_page, container, false);
        profile =   (ImageView)rootView.findViewById(R.id.profile_circle);
        nickname = (TextView)rootView.findViewById(R.id.nickname);
        nickname_nim = (TextView)rootView.findViewById(R.id.nickname_nim);
        I_doing = (TextView)rootView.findViewById(R.id.I_doing);
        text_onoff_direct = (Switch) rootView.findViewById(R.id.switch_onoff_direct);
        edit_pofile = (ImageButton) rootView.findViewById(R.id.edit_pofile);
        send_post = (ImageButton) rootView.findViewById(R.id.direct);
        message = (Button)rootView.findViewById(R.id.message);

        //프로필수정 화면으로 이동
        edit_pofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(view.getContext(), EditUserPage.class);
                startActivity(goHome);
            }
        });

        send_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(view.getContext(), sendPost.class);
                startActivity(goHome);
            }
        });
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(view.getContext(), ShowMessage.class);
                startActivity(goHome);
            }
        });


        return rootView;
    }

    public UserPage() {
    }

}

