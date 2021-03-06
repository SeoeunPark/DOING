package kr.hs.mirim.doing;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.view.Window;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FriendsList extends Fragment implements View.OnClickListener{
    private String title;
    private int page;
    private String key2;
    private FirebaseAuth auth;
    private TextView add_friend;
    private String user_id = null;
    private TextView sort_friend;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<MyFriendList> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference drUser;
    private DatabaseReference drMF;
    private Dialog add_dialog; // ????????? ???????????????
    private int sort =0;
    private int alfriend = 0;
    private MyFriendList mfl;
    private boolean firstClick = true;

    //??????????????? ?????????
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
        sort_friend = rootView.findViewById(R.id.Sort_friend);
        //recyclerview
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); // ?????????????????? ???????????? ??????
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); // User ????????? ?????? ????????? ????????? (??????????????????)
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        user_id = user.getUid();

        add_dialog = new Dialog(getActivity());
        add_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // ????????? ??????
        add_dialog.setContentView(R.layout.add_friends_dialog);

        database = FirebaseDatabase.getInstance(); // ?????????????????? ?????????????????? ??????
        drMF = database.getReference("my_friends").child(user_id); // ?????? ?????? ???????????????db
        drUser = database.getReference("users"); // ??????db
        FirebaseFirestore db = FirebaseFirestore.getInstance();


        //????????? ????????? ??????
        drUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                changeData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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
        sort_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sort == 0) {
                    sort_friend.setText("?????? ???");
                    sort += 1;
                    Collections.sort(arrayList, new Ascending());
                    adapter.notifyDataSetChanged();
                } else if (sort == 1) {
                    sort_friend.setText("???????????? ???");
                    Collections.sort(arrayList, new Descending());
                    adapter.notifyDataSetChanged();
                    sort = 0;
                }
            }
        });
//?????? ????????????
        add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    add_dialog.show();
                    add_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView ok_btn = add_dialog.findViewById(R.id.okButton);
                    TextView cancel_btn = add_dialog.findViewById(R.id.cancelButton);
                    EditText inputname = add_dialog.findViewById(R.id.inputname);
                    inputname.setText("");
                    ok_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (firstClick) {
                                firstClick = false;
                                String username = inputname.getText().toString();
                                if (!TextUtils.isEmpty(username)) {
                                    db.collection("User").whereEqualTo("user_code", username).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (!task.getResult().isEmpty()) { // ????????? ????????? ???????????? ????????????
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    if(document.getId().equals(user_id)){
                                                        Toast.makeText(getActivity(), "????????? ????????? ??? ????????????.", Toast.LENGTH_SHORT).show();
                                                        firstClick = true;
                                                    }else{
                                                        drMF.orderByChild("code").equalTo(document.getId()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DataSnapshot dataSnapshot) {
                                                                if (!dataSnapshot.exists()) { // ???????????? ?????????????????? ????????????
                                                                    HashMap<String, String> my_friends = new HashMap<>();
                                                                    my_friends.put("code", document.getId());
                                                                    FirebaseDatabase.getInstance().getReference().child("my_friends").child(user_id).push().setValue(my_friends);
                                                                    Toast.makeText(getActivity(), "?????????????????????.", Toast.LENGTH_SHORT).show();
                                                                    firstClick = true;
                                                                }else{
                                                                    Toast.makeText(getActivity(), "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                                                                    firstClick = true;
                                                                }
                                                            }
                                                        });
                                                    }
                                                }
                                            }else { Toast.makeText(getActivity(), "?????? ???????????????.", Toast.LENGTH_SHORT).show();}
                                            firstClick = true;
                                        }
                                    });
                                }else { Toast.makeText(getActivity(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show();}
                                firstClick = true;
                                add_dialog.dismiss();
                            }

                        }
                    });
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            add_dialog.dismiss();
                        }
                    });

            }
        });

        // Inflate the layout for this fragment
        auth = FirebaseAuth.getInstance();
        return rootView;
    }
    private void search(String str) {
        ArrayList<MyFriendList> myList = new ArrayList<>();
        for (MyFriendList object : arrayList) {
            if (object.getName().toLowerCase().contains(str.toLowerCase())) {
                myList.add(object);
            }
        }
        FriendAdapter adapterClass = new FriendAdapter(myList, getContext());
        recyclerView.setAdapter(adapterClass); // ????????????????????? ????????? ??????
    }

    @Override
    public void onClick(View view) {

    }

    private void changeData(){
        //?????? ?????? ?????? ??????
        drMF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList= new ArrayList<>();
//                adapter = new FriendAdapter(arrayList, getContext());
//                adapter.notifyDataSetChanged();
//                recyclerView.setAdapter(adapter); // ????????????????????? ????????? ??????
                for(DataSnapshot dss : snapshot.getChildren()){
                    if(!dss.getKey().equals("id")){ //id??? ????????????
                        drUser.child((String) dss.child("code").getValue()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if(task.isSuccessful()) {
                                    mfl = task.getResult().getValue(MyFriendList.class);
                                    mfl.setUid((String) dss.child("code").getValue());
                                    arrayList.add(mfl);
                                    adapter = new FriendAdapter(arrayList, getContext());
                                    Collections.sort(arrayList, new Descending());
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter); // ????????????????????? ????????? ??????
                                }
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