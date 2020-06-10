package com.project.arcadedestroyer.Snake;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.project.arcadedestroyer.Method.GameType;
import com.project.arcadedestroyer.R;

public class SnakeActivity extends Activity implements View.OnClickListener {

  private SnakePanelView mSnakePanelView;
  TextView Score;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_snake);
    mSnakePanelView = findViewById(R.id.snake_view);

    findViewById(R.id.left_btn).setOnClickListener(this);
    findViewById(R.id.right_btn).setOnClickListener(this);
    findViewById(R.id.top_btn).setOnClickListener(this);
    findViewById(R.id.bottom_btn).setOnClickListener(this);
    findViewById(R.id.start_btn).setOnClickListener(this);
    findViewById(R.id.Snake_close_button).setOnClickListener(this);
    Score = (TextView) findViewById(R.id.TextView_point);

    Score.setOnClickListener(this);

    final Handler handler = new Handler();
    final Runnable r = new Runnable() {
      public void run() {
        updateScore();
        handler.postDelayed(this, 500);
      }
    };
    handler.postDelayed(r, 500);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.left_btn:
        mSnakePanelView.setSnakeDirection(GameType.LEFT);
        break;
      case R.id.right_btn:
        mSnakePanelView.setSnakeDirection(GameType.RIGHT);
        break;
      case R.id.top_btn:
        mSnakePanelView.setSnakeDirection(GameType.TOP);
        break;
      case R.id.bottom_btn:
        mSnakePanelView.setSnakeDirection(GameType.BOTTOM);
        break;
      case R.id.start_btn:
        mSnakePanelView.reStartGame();
        break;

      case R.id.Snake_close_button:
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Snake_coin", Integer.parseInt(Score.getText().toString()));
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
        break;
    }
  }

  private void updateScore(){
    Score.setText(Integer.toString(mSnakePanelView.getmScore()));
  }
}