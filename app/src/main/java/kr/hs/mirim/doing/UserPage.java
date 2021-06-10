package kr.hs.mirim.doing;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;

public class UserPage extends Fragment {
    private ImageView profile;
    private TextView nickname;
    private TextView nickname_nim;
    private TextView I_doing;
    private ImageView edit_pofile;
    private ImageView send_post;
    private Switch text_onoff_direct;
    private Button message;
    private String title;
    private CircleMenu circleMenu;

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
        nickname = (TextView)rootView.findViewById(R.id.nickname);
        nickname_nim = (TextView)rootView.findViewById(R.id.nickname_nim);
        I_doing = (TextView)rootView.findViewById(R.id.I_doing);
        edit_pofile = (ImageView) rootView.findViewById(R.id.edit_pofile);
        send_post = (ImageView) rootView.findViewById(R.id.direct);
        message = (Button)rootView.findViewById(R.id.message);
        circleMenu = rootView.findViewById(R.id.profile_circle);

        circleMenu.setMainMenu(Color.parseColor("#ffad76"),R.drawable.face1,R.drawable.ic_baseline_close_24)
                .addSubMenu(Color.parseColor("#ffad76"),R.drawable.face1)
                .addSubMenu(Color.parseColor("#ffd392"),R.drawable.face2)
                .addSubMenu(Color.parseColor("#ffb8f2"),R.drawable.face3)
                .addSubMenu(Color.parseColor("#cccccc"),R.drawable.face4)
                .addSubMenu(Color.parseColor("#baa9ff"),R.drawable.face5)
                .addSubMenu(Color.parseColor("#a6c8ff"),R.drawable.face6)
                .addSubMenu(Color.parseColor("#ff8d8d"),R.drawable.face7)
                .addSubMenu(Color.parseColor("#8a9eb5"),R.drawable.face8)
                .setOnMenuSelectedListener(new OnMenuSelectedListener() {
                    @Override
                    public void onMenuSelected(int index) {
                        switch (index){
                            case 0:
                                circleMenu.setMainMenu(Color.parseColor("#ffad76"),R.drawable.face1,R.drawable.ic_baseline_close_24);
                                Toast.makeText(getActivity(), "1", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                circleMenu.setMainMenu(Color.parseColor("#ffd392"),R.drawable.face2,R.drawable.ic_baseline_close_24);
                                Toast.makeText(getActivity(), "2", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                circleMenu.setMainMenu(Color.parseColor("#ffb8f2"),R.drawable.face3,R.drawable.ic_baseline_close_24);
                                Toast.makeText(getActivity(), "3", Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                circleMenu.setMainMenu(Color.parseColor("#cccccc"),R.drawable.face4,R.drawable.ic_baseline_close_24);
                                Toast.makeText(getActivity(), "4", Toast.LENGTH_SHORT).show();
                                break;
                            case 4:
                                circleMenu.setMainMenu(Color.parseColor("#baa9ff"),R.drawable.face5,R.drawable.ic_baseline_close_24);
                                Toast.makeText(getActivity(), "5", Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                circleMenu.setMainMenu(Color.parseColor("#a6c8ff"),R.drawable.face6,R.drawable.ic_baseline_close_24);
                                Toast.makeText(getActivity(), "6", Toast.LENGTH_SHORT).show();
                                break;
                            case 6:
                                circleMenu.setMainMenu(Color.parseColor("#ff8d8d"),R.drawable.face7,R.drawable.ic_baseline_close_24);
                                Toast.makeText(getActivity(), "7", Toast.LENGTH_SHORT).show();
                                break;
                            case 7:
                                circleMenu.setMainMenu(Color.parseColor("#8a9eb5"),R.drawable.face2,R.drawable.ic_baseline_close_24);
                                Toast.makeText(getActivity(), "8", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

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

