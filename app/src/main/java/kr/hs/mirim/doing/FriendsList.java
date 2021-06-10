package kr.hs.mirim.doing;

import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendsList extends Fragment implements View.OnClickListener{
    private String title;
    private int page;
    private TextView logout;
    private String key2;
    private FirebaseAuth auth;
    private TextView add_friend;
    private String user_id = null;

    private SearchView searchView;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MyFriendList> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference drUser;
    private DatabaseReference drMF;

    private int alfriend = 0;

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
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_friends_list, container, false);

        add_friend = (TextView) rootView.findViewById(R.id.Add_friend);
        searchView = rootView.findViewById(R.id.searchView);
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
        drMF = database.getReference("my_friends").child(user_id); // 현재 유저 친구리스트db
        drUser = database.getReference("users"); // 유저db
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        drUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendUpdate();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
            }
        });

//        databaseReference2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                arrayList.clear();
//                for(DataSnapshot snapshot2 : snapshot.getChildren()){
//                    databaseReference2.child((String) snapshot2.child("code").getValue()).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//                            Log.e("firebase", "Error getting data", error.toException());
//                        }
//                    });
//                    databaseReference2.child((String) snapshot2.child("code").getValue()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DataSnapshot> task) {
//                            if (task.isSuccessful()) {
//                                MyFriendList MyFriendList = task.getResult().getValue(MyFriendList.class);
//                                MyFriendList.setUid((String) snapshot2.child("code").getValue());
//                                arrayList.add(MyFriendList);
//                                adapter = new FriendAdapter(arrayList, getContext());
//                                adapter.notifyDataSetChanged();
//                                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
//                            }
//                            else {
//
//                            }
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.e("Fraglike", String.valueOf(error.toException())); // 에러문 출력
//            }
//        });

        //아라야 여기가 문제야!! -> 알겠어!!
//        databaseReference1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot2) {
//                arrayList.clear();
//                Toast.makeText(getActivity(), "이건 바깥 디비 실행", Toast.LENGTH_SHORT).show();
//                for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {// 반복문으로 데이터 List를 추출해냄
//                    String key2 = (String) snapshot2.child("code").getValue();
//                    databaseReference2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            Toast.makeText(getActivity(), "이건 안쪽 디비 실행", Toast.LENGTH_SHORT).show();
//                            // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {// 반복문으로 데이터 List를 추출해냄
//                                String key = snapshot.getKey();
//                                if(key2.length()>0) {
//                                    if (key.equals(key2)) {
//                                        MyFriendList MyFriendList = snapshot.getValue(MyFriendList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
//                                        arrayList.add(MyFriendList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준다
//                                    }
//                                }
//                            }
//                            adapter.notifyDataSetChanged();
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // 디비를 가져오던중 에러 발생 시
//                            Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
//                        }
//
//                    });
//                }
//                adapter = new FriendAdapter(arrayList, getContext());
//                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
//            }
//
//
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
//            }
//        });


        if (searchView != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }

        //친구 추가하기
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setTitle("친구코드를 입력해주세요");
                final EditText inputname = new EditText(getActivity());
                inputname.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(inputname);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String username = inputname.getText().toString();
                        if (!TextUtils.isEmpty(username)) {
                            db.collection("User").whereEqualTo("user_code", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (!task.getResult().isEmpty()) { // 입력한 코드의 사용자가 있을경우
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            drMF.orderByChild("code").equalTo(document.getId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                @Override
                                                public void onSuccess(DataSnapshot dataSnapshot) {
                                                    if (!dataSnapshot.exists()) { // 사용자의 친구리스트에 없을경우
                                                        HashMap<String, String> my_friends = new HashMap<>();
                                                        my_friends.put("code", document.getId());
                                                        FirebaseDatabase.getInstance().getReference().child("my_friends").child(user_id).push().setValue(my_friends);
                                                        Toast.makeText(getActivity(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }else{
                                                        Toast.makeText(getActivity(), "이미 등록된 친구입니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }else { Toast.makeText(getActivity(), "없는 코드입니다.", Toast.LENGTH_SHORT).show();}
                                }
                            });
                        }else { Toast.makeText(getActivity(), "코드를 입력해주세요.", Toast.LENGTH_SHORT).show();}

//                        alfriend = 0;
//                        String username = inputname.getText().toString();
//                        if (!TextUtils.isEmpty(username)) {
//                            FirebaseFirestore db = FirebaseFirestore.getInstance();
//                            db.collection("User").whereEqualTo("user_code", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                @Override
//                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                    if (!task.getResult().isEmpty()) { // 일치값이 있을경우
//                                        for (QueryDocumentSnapshot document : task.getResult()) {
//                                            databaseReference1.addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(DataSnapshot dataSnapshot2) {
//                                                    boolean isExist = true;
//                                                    HashMap<String, String> my_friends = new HashMap<>();
//                                                    for (DataSnapshot snapshot2 : dataSnapshot2.getChildren()) {// 반복문으로 데이터 List를 추출해냄
//                                                        if (document.getId().equals((String) snapshot2.child("code").getValue())) {
//                                                            Toast.makeText(getActivity(), "이미 등록된 친구입니다.", Toast.LENGTH_SHORT).show();
//                                                            isExist = false;
//                                                            break;
//                                                        }
//                                                    }
//                                                    if (isExist) {
//                                                        my_friends.put("code", document.getId());
//                                                        FirebaseDatabase.getInstance().getReference().child("my_friends").child(user_id).push().setValue(my_friends);
//                                                        Toast.makeText(getActivity(), "추가되었습니다.", Toast.LENGTH_SHORT).show();
//                                                    }
//                                                }
//                                                @Override
//                                                public void onCancelled(DatabaseError databaseError) {
//                                                }
//                                            });
//                                        }
//                                    } else { Toast.makeText(getActivity(), "없는 코드입니다.", Toast.LENGTH_SHORT).show(); }
//                                }
//                            });
//                        }else{Toast.makeText(getActivity(), "코드를 입력해주세요..", Toast.LENGTH_SHORT).show();}
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                dialog.show();
            }
        });

        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        logout = (TextView) rootView.findViewById(R.id.logout_btn);

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

    private ArrayList<MyFriendList> review(){

        ArrayList<MyFriendList> reviewlist = new ArrayList<>();
//        Log.d("1", String.valueOf(reviewlist));
//        drMF.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                for(DataSnapshot dss : dataSnapshot.getChildren()){
//                    if(!dss.getKey().equals("id")){
//
//                        MyFriendList MyFriendList = drUser.child((String) dss.child("code").getValue()).get().getResult().getValue(MyFriendList.class);
//                        MyFriendList.setUid((String) dss.child("code").getValue());
//                        reviewlist.add(MyFriendList);
//                    }
//                }
//            }
//        });
        Log.d("2", String.valueOf(reviewlist));
        return reviewlist;
    }


    private void search(String str) {
        ArrayList<MyFriendList> myList = new ArrayList<>();
        for (MyFriendList object : arrayList) {
            if (object.getName().toLowerCase().contains(str.toLowerCase())) {
                myList.add(object);
            }
        }
        FriendAdapter adapterClass = new FriendAdapter(myList, getContext());
        recyclerView.setAdapter(adapterClass); // 리사이클러뷰에 어댑터 연결
    }
//
//    protected void AddNewFriend(String friendUid){
//        Map<String, Object> AddFriend = new HashMap<>();
//        AddFriend.put(friendUid, friendUid);
//        databaseReference1.updateChildren(AddFriend);
//    }

    @Override
    public void onClick(View view) {

    }

    private void friendUpdate(){
        //현재 유저의 친구리스트에 있는 유저 uid 뽑음
        drMF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                adapter = new FriendAdapter(arrayList, getContext());
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                for(DataSnapshot dss : snapshot.getChildren()){
                    if(!dss.getKey().equals("id")){
                        drUser.child((String) dss.child("code").getValue()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                MyFriendList MyFriendList = task.getResult().getValue(MyFriendList.class);
                                MyFriendList.setUid((String) dss.child("code").getValue());
                                arrayList.add(MyFriendList);
                                adapter = new FriendAdapter(arrayList, getContext());
                                adapter.notifyDataSetChanged();
                                recyclerView.setAdapter(adapter); // 리사이클러뷰에 어댑터 연결
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("firebase", "Error getting data", error.toException());
            }
        });
    }
}