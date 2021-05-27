package kr.hs.mirim.doing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

public class sendPost extends AppCompatActivity implements View.OnClickListener{
    Button[] customStringBtn;
    String[] customString = new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_post);
        customStringBtn = new Button[]{findViewById(R.id.custom_message1), findViewById(R.id.custom_message2), findViewById(R.id.custom_message3), findViewById(R.id.custom_message4), findViewById(R.id.custom_message5)};

        Date dt = new Date();
        SimpleDateFormat full_sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Log.d("DATE",full_sdf.format(dt).toString());



    }

    @Override
    public void onClick(View view){
        findViewById(view.getId()).setPressed(true);
        if(view.getId() == R.id.sendBtn){

        }
    }
}