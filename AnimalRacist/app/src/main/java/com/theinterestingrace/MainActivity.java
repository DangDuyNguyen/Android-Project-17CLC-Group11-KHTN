package com.theinterestingrace;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends Activity {
    CheckBox cb_player1,cb_player2, cb_player3;
    RadioButton breezeBtn,windyBtn,stormBtn;
    ImageView iv_play;
    TextView tv_point;
    Button bt_continue;
    RadioGroup group_btn;
    int windPower;
    GifImageView  player1,player2,player3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();// hàm thực hiện ánh xạ

        ///////////////////////////////
        // ẩn nút Replay trước khi bắt đầu
        bt_continue.setVisibility(View.INVISIBLE);
        //thời gian tối đa để chơi 1 game là 1 phút, mỗi lần nhảy số là 50 mili
        final CountDownTimer countDownTimer = new CountDownTimer(60000,50) {
            @Override
            public void onTick(long l) {
                int goal = 750;

                // thuật toán chính của chương trình
                Random random = new Random();
                player1.setPadding(player1.getPaddingLeft()+random.nextInt(10+10)-windPower,0,0,0);

                player2.setPadding(player2.getPaddingLeft()+random.nextInt(10+10)-windPower,0,0,0);

                player3.setPadding(player2.getPaddingLeft()+random.nextInt(10+10)-windPower,0,0,0);

                int chosen = isChosen();
                if(player1.getPaddingLeft()>=goal && chosen ==1)
                {
                    player1.setImageResource(R.drawable.pinkguyidle);
                    player2.setImageResource(R.drawable.froglose);
                    player3.setImageResource(R.drawable.blueguylose);

                    this.cancel();
                    Toast.makeText(MainActivity.this, "You win", Toast.LENGTH_SHORT).show();
                    tv_point.setText(Integer.parseInt(tv_point.getText().toString())+10+"");
                    bt_continue.setVisibility(View.VISIBLE);
                }
                else if(player2.getPaddingLeft()>=goal && chosen ==2)
                {
                    player2.setImageResource(R.drawable.frogidle);
                    player3.setImageResource(R.drawable.blueguylose);
                    player1.setImageResource(R.drawable.pinkguylose);

                    this.cancel();
                    Toast.makeText(MainActivity.this, "You win ", Toast.LENGTH_SHORT).show();
                    tv_point.setText(Integer.parseInt(tv_point.getText().toString())+10+"");
                    bt_continue.setVisibility(View.VISIBLE);
                }
                else if(player3.getPaddingLeft()>=goal && chosen ==3)
                {
                    player3.setImageResource(R.drawable.blueboyidle);
                    player1.setImageResource(R.drawable.pinkguylose);
                    player2.setImageResource(R.drawable.froglose);

                    this.cancel();
                    Toast.makeText(MainActivity.this, "You win", Toast.LENGTH_SHORT).show();
                    bt_continue.setVisibility(View.VISIBLE);
                    tv_point.setText(Integer.parseInt(tv_point.getText().toString())+10+"");
                }
                if((player1.getPaddingLeft()>=goal && chosen !=1)||(player2.getPaddingLeft()>=goal && chosen !=2)||(player3.getPaddingLeft()>=goal && chosen != 3))
                {
                    if(player3.getPaddingLeft()>=goal )
                    {
                    player3.setImageResource(R.drawable.blueboyidle);
                    player1.setImageResource(R.drawable.pinkguylose);
                    player2.setImageResource(R.drawable.froglose);
                    }
                    else if(player2.getPaddingLeft()>=goal ) {
                        player2.setImageResource(R.drawable.frogidle);
                        player3.setImageResource(R.drawable.blueguylose);
                        player1.setImageResource(R.drawable.pinkguylose);
                    }
                    else if(player1.getPaddingLeft()>=goal ) {
                        player1.setImageResource(R.drawable.pinkguyidle);
                        player2.setImageResource(R.drawable.froglose);
                        player3.setImageResource(R.drawable.blueguylose);
                    }
                        this.cancel();
                    Toast.makeText(MainActivity.this, "You lose", Toast.LENGTH_SHORT).show();
                    bt_continue.setVisibility(View.VISIBLE);
                    tv_point.setText(Integer.parseInt(tv_point.getText().toString())-10+"");
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
                    Toast.makeText(MainActivity.this, "Please choose your character", Toast.LENGTH_SHORT).show();
                }
                if (isWindChosen()==0){
                    Toast.makeText(MainActivity.this, "Please choose wind power", Toast.LENGTH_SHORT).show();
                }
                if (isChosen()!=0 && isWindChosen()!=0)// nếu như chưa chọn con vật nào để đua
                {
                    if(Integer.parseInt(tv_point.getText().toString())<=0)
                    {
                        // nếu điểm âm thì đặt lại như ban đầu
                        Toast.makeText(MainActivity.this, "You lost all of your point", Toast.LENGTH_SHORT).show();
                        //=============== nếu sửa để thoát thì ở đây====================
                        tv_point.setText("100");
                    }
                    iv_play.setVisibility(View.INVISIBLE);
                    bt_continue.setVisibility(View.INVISIBLE);
                    group_btn.setVisibility(View.INVISIBLE);

                    // khi bắt đầu chơi thì chặn người dùng thao tác checkbox --. tránh việc người dùng chọn lại
                    disable();
                    //
                    player1.setImageResource(R.drawable.pinkguyrun);
                    player2.setImageResource(R.drawable.frogrun);
                    player3.setImageResource(R.drawable.blueboyrun);
                    windPower=isWindChosen();

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

                // hiện button play và ẩn button Continue
                iv_play.setVisibility(View.VISIBLE);
                group_btn.setVisibility(View.VISIBLE);
                bt_continue.setVisibility(View.INVISIBLE);
            }
        });

        // check nếu chọn checkbox này thì hủy chọn 2 checkbox còn lại
        cb_player1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    cb_player2.setChecked(false);
                    cb_player3.setChecked(false);
                }
            }
        });
        cb_player2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    cb_player3.setChecked(false);
                    cb_player1.setChecked(false);
                }
            }
        });
        cb_player3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
                    cb_player2.setChecked(false);
                    cb_player1.setChecked(false);
                }
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

        breezeBtn= (RadioButton) findViewById(R.id.breezeBtn);
        windyBtn=(RadioButton)findViewById(R.id.windyBtn);
        stormBtn=(RadioButton)findViewById(R.id.stormBtn);

        tv_point = (TextView)findViewById(R.id.TextView_point);
        iv_play = (ImageView)findViewById(R.id.ImageView_play);
        group_btn= (RadioGroup)findViewById(R.id.group_Btn);
        bt_continue = (Button)findViewById(R.id.Button_replay);
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
}