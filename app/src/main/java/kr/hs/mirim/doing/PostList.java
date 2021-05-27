package kr.hs.mirim.doing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class PostList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);


        ListView listview = (ListView) findViewById(R.id.postList);
        final ArrayList<String> list = new ArrayList<>();


        for (int i = 0; i < 10; i++) {
            list.add(i+"hi");
        }
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, list);
        listview.setAdapter(adapter);
    }
}