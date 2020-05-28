package com.theinterestingrace;

import android.app.Activity;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.PorterDuff.Mode;

import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends Activity {
    CheckBox cb_player1,cb_player2, cb_player3;
    ImageView iv_play;
    TextView tv_point;
    Button bt_continue;
    GifImageView  player1,player2,player3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapping();// hàm thực hiện ánh xạ

        ///////////////////////////////
        // ẩn nút Replay trước khi bắt đầu
        bt_continue.setVisibility(View.INVISIBLE);

        final CountDownTimer countDownTimer = new CountDownTimer(60000,50) {
            @Override
            public void onTick(long l) {
                int goal = 750;

                // thuật toán chính của chương trình
                Random random = new Random();
                player1.setPadding(player1.getPaddingLeft()+random.nextInt(10+10)-7,0,0,0);

                player2.setPadding(player2.getPaddingLeft()+random.nextInt(10+10)-7,0,0,0);

                player3.setPadding(player2.getPaddingLeft()+random.nextInt(10+10)-7,0,0,0);

                int chosen = isChosen();
                if(player1.getPaddingLeft()>=goal && chosen ==1)
                {
                    player1.setImageResource(R.drawable.pinkguyidle);
                    player2.setImageResource(R.drawable.froglose);
                    player3.setImageResource(R.drawable.blueguylose);

                    this.cancel();
                    Toast.makeText(MainActivity.this, "You are winner", Toast.LENGTH_SHORT).show();
                    tv_point.setText(Integer.parseInt(tv_point.getText().toString())+10+"");
                    bt_continue.setVisibility(View.VISIBLE);
                }
                if(player2.getPaddingLeft()>=goal && chosen ==2)
                {
                    player2.setImageResource(R.drawable.frogidle);
                    player3.setImageResource(R.drawable.blueguylose);
                    player1.setImageResource(R.drawable.pinkguylose);

                    this.cancel();
                    Toast.makeText(MainActivity.this, "You are winner ", Toast.LENGTH_SHORT).show();
                    tv_point.setText(Integer.parseInt(tv_point.getText().toString())+10+"");
                    bt_continue.setVisibility(View.VISIBLE);
                }
                if(player3.getPaddingLeft()>=goal && chosen ==3)
                {
                    player3.setImageResource(R.drawable.blueboyidle);
                    player1.setImageResource(R.drawable.pinkguylose);
                    player2.setImageResource(R.drawable.froglose);

                    this.cancel();
                    Toast.makeText(MainActivity.this, "You are winner", Toast.LENGTH_SHORT).show();
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
                    if(player2.getPaddingLeft()>=goal ) {
                        player2.setImageResource(R.drawable.frogidle);
                        player3.setImageResource(R.drawable.blueguylose);
                        player1.setImageResource(R.drawable.pinkguylose);
                    }
                    if(player1.getPaddingLeft()>=goal ) {
                        player1.setImageResource(R.drawable.pinkguyidle);
                        player2.setImageResource(R.drawable.froglose);
                        player3.setImageResource(R.drawable.blueguylose);
                    }
                        this.cancel();
                    Toast.makeText(MainActivity.this, "You are loser", Toast.LENGTH_SHORT).show();
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
                if (isChosen() != 0) {
                    if(Integer.parseInt(tv_point.getText().toString())<=0)
                    {
                        // nếu điểm âm thì đặt lại như ban đầu
                        Toast.makeText(MainActivity.this, "You lost all of your point\n This is new game", Toast.LENGTH_SHORT).show();
                        tv_point.setText("100");
                    }
                    iv_play.setVisibility(View.INVISIBLE);
                    bt_continue.setVisibility(View.INVISIBLE);
                    // khi bắt đầu chơi thì chặn người dùng thao tác checkbox và seekbar --. tránh việc người dùng chọn lại
                    disable();
                    //
                    player1.setImageResource(R.drawable.pinkguyrun);
                    player1.setColorFilter(R.color.colorPrimary);
                    player2.setImageResource(R.drawable.frogrun);
                    player3.setImageResource(R.drawable.blueboyrun);

                    countDownTimer.start();

                }
                else// nếu như chưa chọn con vật nào để đua
                    Toast.makeText(MainActivity.this, "Please choose a animal", Toast.LENGTH_SHORT).show();
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

        tv_point = (TextView)findViewById(R.id.TextView_point);
        iv_play = (ImageView)findViewById(R.id.ImageView_play);

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
