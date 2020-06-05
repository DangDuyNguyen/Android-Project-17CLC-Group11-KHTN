package com.ndduy.gamecollection2020;

import android.app.Activity;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Random;


import pl.droidsonroids.gif.GifImageView;

public class AnimalRacingActivity extends Activity {
    CheckBox cb_player1,cb_player2, cb_player3;
    RadioButton breezeBtn,windyBtn,stormBtn;
    ImageView iv_play,weather,winlose;
    TextView tv_point;
    Button bt_continue;
    FrameLayout choosing,result;
    RadioGroup group;
    int windPower;
    GifImageView  player1,player2,player3,cloud,pictureplayer1,pictureplayer2,pictureplayer3,first,second,third;
    ConstraintLayout layout;
    Button AR_close_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animal_racing);
        mapping();// hàm thực hiện ánh xạ
        hidden();
        ///////////////////////////////
        // ẩn nút Replay trước khi bắt đầu
        result.setVisibility(View.INVISIBLE);
        //thời gian tối đa để chơi 1 game là 1 phút, mỗi lần nhảy số là 50 mili
        final CountDownTimer countDownTimer = new CountDownTimer(60000,50) {
            @Override
            public void onTick(long l) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int device_width = displayMetrics.widthPixels;
                int goal = device_width*80/100;

                // thuật toán chính của chương trình
                Random random = new Random();
                player1.setPadding(player1.getPaddingLeft()+random.nextInt(10+10)-windPower,0,0,0);

                player2.setPadding(player2.getPaddingLeft()+random.nextInt(10+10)-windPower,0,0,0);

                player3.setPadding(player3.getPaddingLeft()+random.nextInt(10+10)-windPower,0,0,0);

                int chosen = isChosen();
                if(player1.getPaddingLeft()>=goal && chosen ==1)
                {
                    player1_win();
                    this.cancel();
                    win();
                }
                else if(player2.getPaddingLeft()>=goal && chosen ==2)
                {
                    player2_win();
                    this.cancel();
                    win();
                }
                else if(player3.getPaddingLeft()>=goal && chosen ==3)
                {
                    player3_win();
                    this.cancel();
                    win();
                }
                if((player1.getPaddingLeft()>=goal && chosen !=1)||(player2.getPaddingLeft()>=goal && chosen !=2)||(player3.getPaddingLeft()>=goal && chosen != 3))
                {
                    if(player3.getPaddingLeft()>=goal )
                    {
                        player3_win();
                    }
                    else if(player2.getPaddingLeft()>=goal ) {
                        player2_win();
                    }
                    else if(player1.getPaddingLeft()>=goal ) {
                        player1_win();
                    }
                    this.cancel();
                    lose();
                }

            }

            @Override
            public void onFinish() {

            }
        };
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isChosen() == 0) {
                    Toast.makeText(AnimalRacingActivity.this, "Please choose your character", Toast.LENGTH_SHORT).show();
                }
                if (isWindChosen()==0){
                    Toast.makeText(AnimalRacingActivity.this, "Please choose wind power", Toast.LENGTH_SHORT).show();
                }
                if (isChosen()!=0 && isWindChosen()!=0)// nếu như chưa chọn con vật nào để đua
                {
                    if(Integer.parseInt(tv_point.getText().toString())<=0)
                    {
                        // nếu điểm âm thì đặt lại như ban đầu
                        Toast.makeText(AnimalRacingActivity.this, "You lost all of your point", Toast.LENGTH_SHORT).show();
                        //=============== nếu sửa để thoát thì ở đây====================
                        tv_point.setText("100");
                    }
                    reveal();
                    iv_play.setVisibility(View.INVISIBLE);
                    result.setVisibility(View.INVISIBLE);
                    choosing.setVisibility(View.INVISIBLE);

                    // khi bắt đầu chơi thì chặn người dùng thao tác checkbox --. tránh việc người dùng chọn lại
                    disable();
                    //
                    player1.setImageResource(R.drawable.pinkguyrun);
                    player2.setImageResource(R.drawable.frogrun);
                    player3.setImageResource(R.drawable.blueboyrun);
                    windPower=isWindChosen();

                    switch (windPower){
                        case 3:
                            cloud.setImageResource(R.drawable.cloud);
                            layout.setBackgroundResource(R.drawable.track);
                            break;
                        case 5:
                            cloud.setImageResource(R.drawable.cloud);
                            layout.setBackgroundResource(R.drawable.stormtrack);
                            break;
                        case 7:
                            cloud.setImageResource(R.drawable.stormcloud);
                            layout.setBackgroundResource(R.drawable.stormtrack);
                            break;
                    }

                    countDownTimer.start();
                }
            }
        });

        bt_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // mỗi khi nhấn button Continue --> đặt các con vật về vị trí xuất phát
                player1.setPadding(0,0,0,0);
                player1.setImageResource(R.drawable.pinkguyidle);
                player2.setPadding(0,0,0,0);
                player2.setImageResource(R.drawable.frogidle);
                player3.setPadding(0,0,0,0);
                player3.setImageResource(R.drawable.blueboyidle);
                // cho phép thao tác với checkbox để chọn con vật
                enable();
                hidden();
                // hiện button play và ẩn button Continue
                iv_play.setVisibility(View.VISIBLE);
                choosing.setVisibility(View.VISIBLE);
                result.setVisibility(View.INVISIBLE);
            }
        });

        // check nếu chọn checkbox này thì hủy chọn 2 checkbox còn lại
        cb_player1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    enable();
                    cb_player1.setEnabled(false);
                    cb_player2.setChecked(false);
                    cb_player3.setChecked(false);
                    pictureplayer1.setImageResource(R.drawable.pinkguyrun);
                    pictureplayer2.setImageResource(R.drawable.frogidle);
                    pictureplayer3.setImageResource(R.drawable.blueboyidle);
                }
            }
        });
        cb_player2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    enable();
                    cb_player2.setEnabled(false);
                    cb_player3.setChecked(false);
                    cb_player1.setChecked(false);
                    pictureplayer1.setImageResource(R.drawable.pinkguyidle);
                    pictureplayer2.setImageResource(R.drawable.frogrun);
                    pictureplayer3.setImageResource(R.drawable.blueboyidle);
                }
            }
        });
        cb_player3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    enable();
                    cb_player3.setEnabled(false);
                    cb_player2.setChecked(false);
                    cb_player1.setChecked(false);
                    pictureplayer1.setImageResource(R.drawable.pinkguyidle);
                    pictureplayer2.setImageResource(R.drawable.frogidle);
                    pictureplayer3.setImageResource(R.drawable.blueboyrun);
                }
            }
        });

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (isWindChosen()){
                    case 2:
                        weather.setImageResource(R.drawable.breeze);
                        break;
                    case 5:
                        weather.setImageResource(R.drawable.windy);
                        break;
                    case 7:
                        weather.setImageResource(R.drawable.storm);
                        break;
                }
            }
        });

        //this button will send score to main lobby and close this activity
        AR_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("coin", convertToCoin(Integer.parseInt(tv_point.getText().toString())));
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });

    }
    private void mapping()
    {
        cb_player1 = (CheckBox)findViewById(R.id.CheckBox_player1);
        cb_player2 = (CheckBox)findViewById(R.id.CheckBox_player2);
        cb_player3 = (CheckBox)findViewById(R.id.CheckBox_player3);

        player1= (GifImageView) findViewById(R.id.player1);
        player2= (GifImageView) findViewById(R.id.player2);
        player3=(GifImageView) findViewById(R.id.player3);

        pictureplayer1=findViewById(R.id.pictureplayer1);
        pictureplayer2=findViewById(R.id.pictureplayer2);
        pictureplayer3=findViewById(R.id.pictureplayer3);

        breezeBtn= (RadioButton) findViewById(R.id.breezeBtn);
        windyBtn=(RadioButton)findViewById(R.id.windyBtn);
        stormBtn=(RadioButton)findViewById(R.id.stormBtn);

        cloud = (GifImageView)findViewById(R.id.cloud) ;
        layout= (ConstraintLayout)findViewById(R.id.background);

        tv_point = (TextView)findViewById(R.id.TextView_point);
        iv_play = (ImageView)findViewById(R.id.ImageView_play);
        weather = findViewById(R.id.weather);
        choosing=findViewById(R.id.choosing);
        group=findViewById(R.id.group);

        first=findViewById(R.id.first);
        second=findViewById(R.id.second);
        third=findViewById(R.id.third);

        result=findViewById(R.id.result);
        winlose=findViewById(R.id.winlose);

        bt_continue = (Button)findViewById(R.id.Button_replay);

        AR_close_btn = (Button)findViewById(R.id.AR_close_button);
    }
    // trả về lựa chọn của người chơi
    private int isChosen()
    {
        if(cb_player1.isChecked())
            return 1;
        if(cb_player2.isChecked())
            return 2;
        if(cb_player3.isChecked())
            return 3;
        return 0;
    }

    private int isWindChosen()
    {
        if(breezeBtn.isChecked())
            return 2;
        if(windyBtn.isChecked())
            return 5;
        if(stormBtn.isChecked())
            return 7;
        return 0;
    }
    //hàm chặn thao tác
    private void disable()
    {
        cb_player1.setEnabled(false);
        cb_player2.setEnabled(false);
        cb_player3.setEnabled(false);
    }

    // hàm cho phép thao tác
    private void enable()
    {
        cb_player1.setEnabled(true);
        cb_player2.setEnabled(true);
        cb_player3.setEnabled(true);

    }

    private void hidden()
    {
        player1.setVisibility(View.INVISIBLE);
        player2.setVisibility(View.INVISIBLE);
        player3.setVisibility(View.INVISIBLE);
    }

    private void reveal()
    {
        player1.setVisibility(View.VISIBLE);
        player2.setVisibility(View.VISIBLE);
        player3.setVisibility(View.VISIBLE);
    }

    private void win()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_point.setText(Integer.parseInt(tv_point.getText().toString())+10+"");
                winlose.setImageResource(R.drawable.win);
                result.setVisibility(View.VISIBLE);
            }
        },1500);

    }

    private void lose()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                result.setVisibility(View.VISIBLE);
                winlose.setImageResource(R.drawable.lose);
                tv_point.setText(Integer.parseInt(tv_point.getText().toString())-10+"");
            }
        },1500);
    }

    private void player1_win(){
        player1.setImageResource(R.drawable.pinkguyidle);
        player2.setImageResource(R.drawable.froglose);
        player3.setImageResource(R.drawable.blueguylose);
        first.setImageResource(R.drawable.pinkguyidle);
        check_second_win(23);
    }
    private void player2_win(){
        player2.setImageResource(R.drawable.frogidle);
        player3.setImageResource(R.drawable.blueguylose);
        player1.setImageResource(R.drawable.pinkguylose);
        first.setImageResource(R.drawable.frogidle);
        check_second_win(13);

    }
    private void player3_win(){
        player3.setImageResource(R.drawable.blueboyidle);
        player1.setImageResource(R.drawable.pinkguylose);
        player2.setImageResource(R.drawable.froglose);
        first.setImageResource(R.drawable.blueboyidle);
        check_second_win(12);

    }

    private void check_second_win(int playerA_playerB){
        switch (playerA_playerB)
        {
            case 12:
                if(player1.getPaddingLeft()-player2.getPaddingLeft()>0) {
                    second.setImageResource(R.drawable.pinkguyidle);
                    third.setImageResource(R.drawable.frogidle);
                }
                else {
                    third.setImageResource(R.drawable.pinkguyidle);
                    second.setImageResource(R.drawable.frogidle);
                }
                break;

            case 13:
                if(player1.getPaddingLeft()-player3.getPaddingLeft()>0) {
                    second.setImageResource(R.drawable.pinkguyidle);
                    third.setImageResource(R.drawable.blueboyidle);
                }
                else {
                    second.setImageResource(R.drawable.blueboyidle);
                    third.setImageResource(R.drawable.pinkguyidle);
                }
                break;
            case 23:
                if(player3.getPaddingLeft()-player2.getPaddingLeft()>0) {
                    second.setImageResource(R.drawable.blueboyidle);
                    third.setImageResource(R.drawable.frogidle);
                }
                else {
                    third.setImageResource(R.drawable.blueboyidle);
                    second.setImageResource(R.drawable.frogidle);
                }
                break;
            default:
                second.setImageResource(R.drawable.blueguylose);
                third.setImageResource(R.drawable.blueguylose);
                break;
        }

    }

    //convert from score to coin
    private int convertToCoin(int score){
        return score*20/100;
    }
}
