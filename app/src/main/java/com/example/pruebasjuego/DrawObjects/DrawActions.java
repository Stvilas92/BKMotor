package com.example.pruebasjuego.DrawObjects;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

public class DrawActions {
    private int actualWood,actualStone,actualFood;
    private int initY,boxSizeX,boxSizeY,screenWidth,screenHeight;
    private Paint pBackgound;

    public DrawActions(int actualWood, int actualStone, int actualFood, int initY, int boxSizeX, int boxSizeY,int screenWidth,int screenHeight) {
        this.actualWood = actualWood;
        this.actualStone = actualStone;
        this.actualFood = actualFood;
        this.initY = initY;
        this.boxSizeX = boxSizeX;
        this.boxSizeY = boxSizeY;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        pBackgound = new Paint();
        pBackgound.setColor(Color.GRAY);
    }

    public void draw(Canvas c){
        c.drawRect(new Rect(0,initY,screenWidth,initY+boxSizeY*2),pBackgound);
    }


    public int getActualWood() {
        return actualWood;
    }

    public void setActualWood(int actualWood) {
        this.actualWood = actualWood;
    }

    public int getActualStone() {
        return actualStone;
    }

    public void setActualStone(int actualStone) {
        this.actualStone = actualStone;
    }

    public int getActualFood() {
        return actualFood;
    }

    public void setActualFood(int actualFood) {
        this.actualFood = actualFood;
    }

    public int getInitY() {
        return initY;
    }

    public void setInitY(int initY) {
        this.initY = initY;
    }

    public int getBoxSizeX() {
        return boxSizeX;
    }

    public void setBoxSizeX(int boxSizeX) {
        this.boxSizeX = boxSizeX;
    }

    public int getBoxSizeY() {
        return boxSizeY;
    }

    public void setBoxSizeY(int boxSizeY) {
        this.boxSizeY = boxSizeY;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public Paint getpBackgound() {
        return pBackgound;
    }

    public void setpBackgound(Paint pBackgound) {
        this.pBackgound = pBackgound;
    }
}
