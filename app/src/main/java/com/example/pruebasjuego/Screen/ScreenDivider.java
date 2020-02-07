package com.example.pruebasjuego.Screen;

import android.content.Context;

public class ScreenDivider {
    private int w,h,boxSizeX,boxSizeY;
    private Box[] boxes;

    public ScreenDivider(int initX, int finalX, int initY, int finalY, int div, Context context) {
        this.w = finalX-initX;
        this.h = finalY-initY;
        this.boxSizeX = w/div;
        this.boxSizeY = h/div;

        boxes = new Box[((div)*(div))];

        int index= 0;
        for (int i = 0; i < div; i++) {
            for (int j = 0; j < div; j++) {
                boxes[index] = new Box(i,j,initX+(i*this.boxSizeX),initY+(j*this.boxSizeY),boxSizeX,boxSizeY,context,null,null,true);
                index++;
            }
        }
    }

    public Box[] getBoxes() {
        return boxes;
    }

    public void setBoxes(Box[] boxes) {
        this.boxes = boxes;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }
}
