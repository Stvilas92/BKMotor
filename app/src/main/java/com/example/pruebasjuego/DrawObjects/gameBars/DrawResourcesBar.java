package com.example.pruebasjuego.DrawObjects.gameBars;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

public class DrawResourcesBar {
    private static final int INIT_RECT_INDEX_FOOD = 2;
    private static final int INIT_RECT_INDEX_WOOD = 6;
    private static final int INIT_RECT_INDEX_STONE = 10;
    private static final int RECT_WITDH = 2;

    private Bitmap foodBitmap,woodBitmap,stoneBitmap;
    private int actualWood,actualStone,actualFood;
    private int initY,boxSizeX,boxSizeY,screenWidth;
    private Paint pText,pResources;
    private Rect rBackgroud,rFood,rWood,rStone;
    private Context context;

    public DrawResourcesBar(int actualWood, int actualStone, int actualFood, int initY, int boxSizeX, int boxSizeY, int screenWidth, Context context) {
        this.actualWood = actualWood;
        this.actualStone = actualStone;
        this.actualFood = actualFood;
        this.initY = initY;
        this.boxSizeX = boxSizeX;
        this.boxSizeY = boxSizeY;
        this.screenWidth = screenWidth;
        this.context = context;

        this.pText = new Paint();
        this.pText.setColor(Color.YELLOW);
        this.pText.setTextSize(boxSizeY/2);

        this.pResources = new Paint();
        this.pResources.setColor(Color.BLACK);
        this.rBackgroud = new Rect(0,0,screenWidth,initY);
        this.rFood = getRect(INIT_RECT_INDEX_FOOD);
        this.rWood = getRect(INIT_RECT_INDEX_WOOD);
        this.rStone = getRect(INIT_RECT_INDEX_STONE);
        this.foodBitmap = getBitmap("Resources/food.png");
        this.stoneBitmap = getBitmap("Resources/stone.png");
        this.woodBitmap = getBitmap("Resources/wood.png");
    }

    public void draw(Canvas c){
        c.drawRect(rStone,pResources);
        c.drawRect(rFood,pResources);
        c.drawRect(rWood,pResources);
        c.drawBitmap(foodBitmap,(boxSizeX*INIT_RECT_INDEX_FOOD),0,null);
        c.drawBitmap(woodBitmap,(boxSizeX*INIT_RECT_INDEX_WOOD),0,null);
        c.drawBitmap(stoneBitmap,(boxSizeX*INIT_RECT_INDEX_STONE),0,null);
        c.drawText(""+actualFood,boxSizeX*(INIT_RECT_INDEX_FOOD+1),initY/2, pText);
        c.drawText(""+actualStone,boxSizeX*(INIT_RECT_INDEX_STONE+1),initY/2, pText);
        c.drawText(""+actualWood,boxSizeX*(INIT_RECT_INDEX_WOOD+1),initY/2, pText);
    }

    private Rect getRect(int indexLeft){
        Rect r = new Rect();
        r = new Rect(boxSizeX*indexLeft,0,(boxSizeX*indexLeft)+(boxSizeX*RECT_WITDH),initY);
        return r;
    }

    private Bitmap getBitmap(String bitmap){
        Bitmap b = getBitmapFromAssets(bitmap);
        return scaleByHeight(b,boxSizeY);
    }

    public Bitmap getBitmapFromAssets(String fichero) {
        try
        {
            InputStream is= context.getAssets().open(fichero);
            return BitmapFactory.decodeStream(is);
        }
        catch (IOException e) {
            return null;
        }
    }

    public Bitmap scaleByHeight(Bitmap res, int newHeight) {
        if (newHeight==res.getHeight()) return res;
        return res.createScaledBitmap(res, (res.getWidth() * newHeight) /
                res.getHeight(), newHeight, true);
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
}
