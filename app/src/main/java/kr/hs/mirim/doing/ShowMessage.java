package kr.hs.mirim.doing;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class ShowMessage extends AppCompatActivity {
    TabLayout tabs;
    Fragment MessageSend;
    Fragment MessageRecieve;
    Fragment selected = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_message);
        MessageSend = new MessageSend();
        MessageRecieve = new MessageReceive();
        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("받은 쪽지"));
        tabs.addTab(tabs.newTab().setText("보낸 쪽지"));

        getSupportFragmentManager().beginTransaction().replace(R.id.contatiner, MessageRecieve).commit();

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    selected = MessageRecieve;
                } else if (position == 1) {
                    selected = MessageSend;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.contatiner, selected).commit();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}