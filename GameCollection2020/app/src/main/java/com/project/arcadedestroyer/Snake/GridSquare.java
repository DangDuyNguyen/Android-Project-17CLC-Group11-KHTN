package com.project.arcadedestroyer.Snake;

import android.graphics.Color;

import com.project.arcadedestroyer.Method.GameType;


public class GridSquare {
  private int mType;

  public GridSquare(int type) {
    mType = type;
  }

  public int getColor() {
    switch (mType) {
      case GameType.GRID:
        return Color.WHITE;
      case GameType.FOOD:
        return Color.BLUE;
      case GameType.SNAKE:
        return Color.parseColor("#FF4081");
    }
    return Color.WHITE;
  }

  public void setType(int type) {
    mType = type;
  }
}
