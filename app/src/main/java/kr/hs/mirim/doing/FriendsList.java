package kr.hs.mirim.doing;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class FriendsList extends Fragment {
    private String title;
    private int page;
    private Button logout;
    private FirebaseAuth auth;

    //인디케이터 만들기
    // newInstance constructor for creating fragment with arguments
    public static FriendsList newInstance(int page, String title) {
        FriendsList fragment = new FriendsList();
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
        ViewGroup rootView =(ViewGroup) inflater.inflate(R.layout.fragment_friends_list, container, false);

        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        logout = (Button)rootView.findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.getInstance().signOut();
                startActivity(new Intent(getActivity(), Signin.class));
                getActivity().finish();
            }
        });
        return rootView;
    }
}