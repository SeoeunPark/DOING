package kr.hs.mirim.doing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageReceive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageReceive extends Fragment {
    private FirebaseUser currentUser;
    private FirebaseAuth auth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MessageReceive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageReceive.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageReceive newInstance(String param1, String param2) {
        MessageReceive fragment = new MessageReceive();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_receive, container, false);

//        ListView rlistview = (ListView) v.findViewById(R.id.receivePost);
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> item = new HashMap<>();

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = FirebaseAuth.getInstance().getUid();

        fs.collection("Post").whereEqualTo("receiver", current_uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) { // 일치값이 있을경우
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        fs.collection("User").document((String) document.getData().get("sender")).get().addOnCompleteListener( docu -> {
                            item.put("sender", "보낸 사람 : "+(String) docu.getResult().get("name"));
                        });
                        item.put("title", "제목 : "+(String) document.getData().get("gist"));
                        list.add(item);
                        item.clear();
                    }
                    SimpleAdapter adapter = new SimpleAdapter(getActivity(), list, android.R.layout.simple_list_item_2, new String[]{"sender", "title"}, new int[] {android.R.id.text1, android.R.id.text2});
//                    rlistview.setAdapter(adapter);
                }else{ //일치값이 없을경우
                    Toast.makeText(getActivity(),"뭐냐 쪽지 없음", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }
}