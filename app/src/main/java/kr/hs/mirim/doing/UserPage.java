package kr.hs.mirim.doing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.slider.Slider;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class UserPage extends Fragment {
    private ImageView profile;
    private TextView nickname;
    private TextView nickname_nim;
    private EditText I_doing;
    private ImageView edit_pofile;
    private Switch text_onoff_direct;
    private ImageView message;
    private String title;
    private Slider busy;
    private CircleMenu circleMenu;
    private ImageView showColor;
    private TextView about;
    private long lastTimeBackPressed;

    private FirebaseAuth auth;
    private String user_id = null;
    private DatabaseReference mDatabase;
    private long mLastClickTime = 0;
    private long mLastClickTime_a = 0;

    String[] conditionColor;
    int[] conditionFace;

    private String shared = "condition";
    private int select = 0;

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

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(shared, 0);
        select = sharedPreferences.getInt("key",0);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_user_page, container, false);
        nickname = (TextView)rootView.findViewById(R.id.nickname);
        nickname_nim = (TextView)rootView.findViewById(R.id.nickname_nim);
        I_doing = (EditText)rootView.findViewById(R.id.I_doing);
        edit_pofile = (ImageView) rootView.findViewById(R.id.edit_pofile);
        message = rootView.findViewById(R.id.message);
        circleMenu = rootView.findViewById(R.id.profile_circle);
        showColor = rootView.findViewById(R.id.showColor);
        busy = rootView.findViewById(R.id.busy);
        about = rootView.findViewById(R.id.about);
        ImageView edit_doing = rootView.findViewById(R.id.edit_doing);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();


        conditionColor = new String[]{"#ffad76", "#ffd392", "#ffb8f2", "#cccccc", "#baa9ff", "#a6c8ff", "#ff8d8d", "#8a9eb5"};
        conditionFace = new int[]{R.drawable.face1, R.drawable.face2, R.drawable.face3, R.drawable.face4, R.drawable.face5, R.drawable.face6, R.drawable.face7, R.drawable.face8};

        circleMenu.setMainMenu(Color.parseColor(conditionColor[select]),conditionFace[select],R.drawable.ic_baseline_close_24);
        circleMenu.addSubMenu(Color.parseColor(conditionColor[0]),R.drawable.face1)
                .addSubMenu(Color.parseColor(conditionColor[1]),R.drawable.face2)
                .addSubMenu(Color.parseColor(conditionColor[2]),R.drawable.face3)
                .addSubMenu(Color.parseColor(conditionColor[3]),R.drawable.face4)
                .addSubMenu(Color.parseColor(conditionColor[4]),R.drawable.face5)
                .addSubMenu(Color.parseColor(conditionColor[5]),R.drawable.face6)
                .addSubMenu(Color.parseColor(conditionColor[6]),R.drawable.face7)
                .addSubMenu(Color.parseColor(conditionColor[7]),R.drawable.face8);

        //slider바 값이 바뀔 때
        busy.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull Slider slider, float value, boolean fromUser) {
                if(value==0){
                    showColor.setColorFilter(Color.parseColor("#62FF2A"));
                    setUserData((int)value,"level");
                    //여유로울 때
                }else if (value==1){
                    showColor.setColorFilter(Color.parseColor("#FFE32A"));
                    setUserData((int)value,"level");
                    //바쁠 때
                }else{
                    showColor.setColorFilter(Color.parseColor("#FF4D2A"));
                    setUserData((int)value,"level");
                    //매우 바쁠 때
                }
            }
        });
        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            public void onMenuSelected(int index) {
                circleMenu.setMainMenu(Color.parseColor(conditionColor[index]),conditionFace[index],R.drawable.ic_baseline_close_24);
                setUserData(index+1,"condition");
                select = index;
            }
        });

        mDatabase.child("users").child(user_id).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                MyFriendList userInfo = task.getResult().getValue(MyFriendList.class);
                Log.d("test", userInfo.getCondition()+"");
                I_doing.setText(userInfo.getIng());
                about.setText(userInfo.getAbout());
                nickname.setText(userInfo.getName());
                busy.setValue(userInfo.getLevel());
            }
        });

        nickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    Intent goName = new Intent(view.getContext(), EditName.class);
                    startActivity(goName);
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SystemClock.elapsedRealtime() - mLastClickTime_a < 1000){
                    Intent goAbout = new Intent(view.getContext(), EditAbout.class);
                    startActivity(goAbout);
                    return;
                }
                mLastClickTime_a = SystemClock.elapsedRealtime();
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

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goHome = new Intent(view.getContext(), ShowMessage.class);
                startActivity(goHome);
            }
        });

        I_doing.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newtext = I_doing.getText().toString();
                Log.d("new", newtext);
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newtext = I_doing.getText().toString();
                Log.d("finish", newtext);
                setUserData(newtext,"ing");
            }
        });




        edit_doing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                I_doing.setTextIsSelectable(true);
//                I_doing.performClick();
//                I_doing.setSelectAllOnFocus(true);
//                I_doing.setSelection(I_doing.getText().length());
//                InputMethodManager inputMMg = (InputMethodManager) getContext().getSystemService(INPUT_METHOD_SERVICE);
//                inputMMg.showSoftInput(I_doing, InputMethodManager.SHOW_IMPLICIT);

            }
        });



        return rootView;
    }

    private void setUserData(Object n, String key){
        Map<String, Object> conditionUpdates = new HashMap<>();
        conditionUpdates.put("/users/" + user_id+"/"+key, n);
        mDatabase.updateChildren(conditionUpdates);
    }

    public UserPage() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(shared, 0);
        SharedPreferences.Editor editor  = sharedPreferences.edit();
        editor.putInt("key", select);
        editor.commit();

    }
}