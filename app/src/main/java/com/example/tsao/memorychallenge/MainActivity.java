package com.example.tsao.memorychallenge;

import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class MainActivity extends AppCompatActivity {
    int[] bt_id = {R.id.b01,R.id.b02,R.id.b03,R.id.b04,
                R.id.b05,R.id.b06,R.id.b07,R.id.b08,
                R.id.b09,R.id.b10,R.id.b11,R.id.b12,
                R.id.b13,R.id.b14,R.id.b15,R.id.b16};
    String[] ans = {"A","A","B","B",
                    "C","C","D","D",
                    "E","E","F","F",
                    "G","G","H","H"};

    int tsec;
    int count_down = 0;//-1 down 0 stop 1 up
    Timer timer1;
    Button start_bt;
    TextView timer;
    int click_count;
    int last_id;
    int score;
    int stage;//0 begin 1 start 2 end

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (int id : bt_id){
            TextView bt = (TextView) findViewById(id);
            bt.setOnClickListener(flip);
            bt.setVisibility(View.INVISIBLE);
        }
        start_bt = (Button) findViewById(R.id.start);
        start_bt.setOnClickListener(start);
        timer = (TextView) findViewById(R.id.timer);
        timer1 = new Timer();
        stage=0;
    }
    private String getAns(int id){
        for (int i=0;i<16;i++)
            if (id == bt_id[i])
                return ans[i];
        return "";
    }
    private void random_shuffle(){
        for (int i=0;i<16;i++)
            for (int j=i+1;j<16;j++){
                int ran = (int) (Math.random()*2);
                if (ran == 1){
                    String tmp = ans[i];
                    ans[i] = ans[j];
                    ans[j] = tmp;
                }
            }
    }
    private String genTime(){
        String s = "";
        if (tsec/60 <10)
            s += "0"+(tsec/60);
        else
            s += (tsec/60);
        s += ":";
        if (tsec%60 <10)
            s += "0"+(tsec%60);
        else
            s += (tsec%60);
        return s;
    }
    private void gameStartInit(){
        count_down = 1;
        for (int id : bt_id){
            TextView bt = (TextView) findViewById(id);
            bt.setText("");
        }
        click_count = 0;
        score = 0;
    }
    private android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg){
            switch (msg.what){
                case 0:
                    gameStartInit();
                    timer.setText("START!!!");
                    break;
                case 1:
                    timer.setText(genTime());
            }
        }
    };

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            if (count_down == -1)
                tsec -= 1;
            else if (count_down == 1)
                tsec += 1;
            else
                return;

            Message message = new Message();
            if (tsec == 0){
                message.what = 0;
            }
            else
                message.what = 1;
            handler.sendMessage(message);


        }
    };
    private View.OnClickListener start = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (stage != 0)
                return;
            stage = 1;
            random_shuffle();
            for (int i=0;i<16;i++){
                TextView bt = (TextView) findViewById(bt_id[i]);
                bt.setVisibility(View.VISIBLE);
                bt.setText(ans[i]);
            }
            tsec = 5; count_down = -1;
            timer1.schedule(task,0,1000);

        }
    };
    private void endGame(){
        count_down = 0;
        timer.setText("You WIN!\nYour time is "+genTime());
        stage = 2;
        timer1.cancel();
    }
    private View.OnClickListener flip = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d("Press","Button Pressed "+v.getId());
            TextView bt = (TextView) findViewById(v.getId());
            if (bt.getText()!="")
                return;
            bt.setText(getAns(v.getId()));
            click_count ++;

            if (click_count == 1)
                last_id = v.getId();
            else if (click_count == 2){
                TextView bt2 = (TextView) findViewById(last_id);
                if (bt2.getText() != bt.getText()){
                    try{
                        Thread.sleep(700);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();}
                    Log.d("DELAY","delay"+bt.getText());
                    bt.setText("");
                    bt2.setText("");
                }
                else {
                    score ++;
                    Log.d("Score",Integer.toString(score));
                }
                click_count = 0;
            }

            if (score == 8){
                endGame();
            }
        }
    };
}
