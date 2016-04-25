package com.example.tsao.memorychallenge;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class win_activity extends AppCompatActivity {
    Button bt;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_activity);
        bt = (Button) findViewById(R.id.restart);
        tv = (TextView) findViewById(R.id.showScore);
        Intent in = this.getIntent();
        String sc = in.getStringExtra(com.example.tsao.memorychallenge.MainActivity.SCORE);
        tv.setText("YOUR SCORE : " + sc);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
