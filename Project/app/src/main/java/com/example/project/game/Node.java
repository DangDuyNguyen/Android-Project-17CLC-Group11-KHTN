package com.example.project.game;

public class Node {
    Node left, right, up, down;
    Node columnHead;
    int row;
    int col;
    int count;

    public Node() {
        left = right = up = down = null;
        row = col = -1;
        count = 0;
    }
}