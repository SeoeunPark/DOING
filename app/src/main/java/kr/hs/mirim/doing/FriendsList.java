package kr.hs.mirim.doing;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsList extends Fragment {
    private String title;
    private int page;
    private Button logout;
    private FirebaseAuth auth;
    private Button add_friend;
    private String user_id = null;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MyFriendList> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference1;
    private DatabaseReference databaseReference2;

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
        add_friend = (Button)rootView.findViewById(R.id.Add_friend);

        //recyclerview
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 기존성능 강화
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User 객체를 담을 어레이 리스트 (어댑터쪽으로)

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();


        database = FirebaseDatabase.getInstance(); // 파이어베이스 데이터베이스 연동
        databaseReference1 = database.getReference("my_friends"); // DB 테이블 연결

        databaseReference2 = database.getReference("users"); // DB 테이블 연결
        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
                arrayList.clear(); // 기존 배열리스트가 존재하지않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {// 반복문으로 데이터 List를 추출해냄
                        MyFriendList MyFriendList = snapshot.getValue(MyFriendList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
                        arrayList.add(MyFriendList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); // 리스트 저장 및 새로고침해야 반영이 됨
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });
        adapter = new FriendAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결

        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("친구코드를 입력해주세요");
                final EditText inputname = new EditText(getActivity());
                inputname.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(inputname);
                //코드 입력하고 ok 버튼 눌렀을 때
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = inputname.getText().toString();
                        if(!TextUtils.isEmpty(username)){
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            db.collection("User").whereEqualTo("user_code",username).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (queryDocumentSnapshots.isEmpty()) {
                                        Toast.makeText(getActivity(),"없는 코드입니다.",Toast.LENGTH_SHORT).show();
                                    }else{
                                        //없는 코드이기 때문에 친구 추가 가능
                                        db.collection("User").whereEqualTo("user_code",username).get().addOnCompleteListener( tasks ->{
                                            for(QueryDocumentSnapshot document : tasks.getResult()){
                                                HashMap<String, String> my_friends = new HashMap<>();
                                                my_friends.put(username, document.getId());
                                                FirebaseDatabase.getInstance().getReference().child("my_friends").child(user_id).push().setValue(my_friends);
                                                Toast.makeText(getActivity(),"추가되었습니다.",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        Toast.makeText(getActivity(),"추가되었습니다.",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
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