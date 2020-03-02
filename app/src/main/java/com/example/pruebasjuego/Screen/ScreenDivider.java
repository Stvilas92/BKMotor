package com.example.pruebasjuego.Screen;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.pruebasjuego.Utils.BitmapManager;

public class ScreenDivider {
    private int w,h,boxSizeX,boxSizeY;
    private Box[] boxes;

    public ScreenDivider(int initX, int finalX, int initY, int finalY, int div, Context context ) {
        this.w = finalX-initX;
        this.h = finalY-initY;
        this.boxSizeX = w/div;
        this.boxSizeY = h/div;
        Bitmap floor = BitmapManager.getOnlySurface(boxSizeX,boxSizeY,context);
        boxes = new Box[((div)*(div))];

        int index= 0;
        for (int i = 0; i < div; i++) {
            for (int j = 0; j < div; j++) {
                boxes[index] = new Box(i,j,initX+(i*this.boxSizeX),initY+(j*this.boxSizeY),boxSizeX,boxSizeY,context,null,null,true,floor);
                index++;
            }
        }
    }

    public Box[] getBoxes() {
        return boxes;
    }
}
