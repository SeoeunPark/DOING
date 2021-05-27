package kr.hs.mirim.doing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class sendPost extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private String user_id = null;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post);

        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("my_friends").child(user_id); // DB 테이블 연결

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                arrayList.clear();
                for (DataSnapshot snapshot2 : dataSnapshot.getChildren()) {// 반복문으로 데이터 List를 추출해냄
                    String key2 = (String) snapshot2.child("code").getValue();
                    Log.d("code", key2);
//                    databaseReference2.addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            // 파이어베이스 데이터베이스의 데이터를 받아오는 곳
//                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {// 반복문으로 데이터 List를 추출해냄
//                                String key = snapshot.getKey();
//                                if (key.equals(key2)) {
//                                    MyFriendList MyFriendList = snapshot.getValue(MyFriendList.class); // 만들어뒀던 User 객체에 데이터를 담는다.
//                                    arrayList.add(MyFriendList); // 담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준다
//                                }
//
//                            }
//                            adapter.notifyDataSetChanged();
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // 디비를 가져오던중 에러 발생 시
//                            Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
//                        }
//
//                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Fraglike", String.valueOf(databaseError.toException())); // 에러문 출력
            }
        });

        Date dt = new Date();
        SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Log.d("DATE",full_sdf.format(dt).toString());


        Spinner spinner = findViewById(R.id.spinner);

//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, );
    }
}