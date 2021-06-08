package kr.hs.mirim.doing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class PostList extends AppCompatActivity {
    private FirebaseUser currentUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        ListView listview = (ListView) findViewById(R.id.postList);
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();
        HashMap<String, String> item = new HashMap<>();

        FirebaseFirestore fs = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = FirebaseAuth.getInstance().getUid();

        fs.collection("Post").whereEqualTo("sender", current_uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (!task.getResult().isEmpty()) { // 일치값이 있을경우
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        fs.collection("User").document(current_uid).get().addOnCompleteListener( docu -> {
                            item.put("sender", (String) docu.getResult().get("name"));
                            Log.d("tst",(String) docu.getResult().get("name") );
                        });
                        item.put("title", (String) document.getData().get("gist"));
                        Log.d("tst",(String) document.getData().get("gist") );
                        list.add(item);
                        item.clear();
                    }
                    SimpleAdapter adapter = new SimpleAdapter(PostList.this, list, android.R.layout.simple_list_item_2, new String[]{"item1", "item2"}, new int[] {android.R.id.text1, android.R.id.text2});
                    listview.setAdapter(adapter);
                }else{ //일치값이 없을경우
                    Toast.makeText(PostList.this,"뭐냐 쪽지 없음", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}