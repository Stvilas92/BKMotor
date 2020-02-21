package com.example.pruebasjuego.Screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObjects;

import java.io.IOException;
import java.io.InputStream;

public class Box {
    public static final int MOVING_X_SIZE = 8;
    public static final int MOVING_Y_SIZE = 4;

    int xReference,yReference;
    int xReferenceCoord,yReferenceCoord;

    private int x,y,finalX,finalY,indexX,indexY,sizeX,sizeY;
    private Bitmap floor,object;
    private Context context;
    private DrawObjectType drawObjectType;
    private DrawObjectSubtype drawObjectSubtype;
    private GameObjects gameObject;
    private int[] movingX,movingY;
    private int actualGameObjectIndexX,actualGameObjectIndexY ,middleIndexX;
    private boolean interactable;
    private Paint p=new Paint();


    public Box(int indexX, int indexY, int x, int y, int sizeX, int sizeY, Context context, DrawObjectType drawObjectType, DrawObjectSubtype drawObjectSubtype, boolean interactable) {
        this.x = x;
        p.setColor(Color.YELLOW);
        p.setStrokeWidth(1);
        p.setStyle(Paint.Style.STROKE);
        p.setTextSize(sizeY/2);
        this.y = y;
        this.interactable = interactable;
        this.finalX = x+sizeX;
        this.finalY = y+sizeY;
        this.indexX = indexX;
        this.indexY = indexY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.context  = context;
        this.floor = getBitmapFromAssets("surface.png");
        this.floor = Bitmap.createScaledBitmap(this.floor, this.getSizeX(), this.getSizeX(),false);
        this.drawObjectType = drawObjectType;
        this.movingX = new int[MOVING_X_SIZE];
        this.movingY = new int[MOVING_Y_SIZE];

        this.middleIndexX = MOVING_X_SIZE/2;
        this.actualGameObjectIndexX = middleIndexX;
        this.actualGameObjectIndexY = 0;

        for (int i = 0; i < movingX.length; i++) {
            movingX[i] = x+(i*(sizeX/MOVING_X_SIZE));
        }

        for (int i = 0; i < movingY.length; i++) {
            movingY[i] = y+(i*(sizeY/MOVING_Y_SIZE));
        }
    }

    public void drawBox(Canvas c){
        if(drawObjectType != null && drawObjectSubtype != null){
            drawObject(c);
        }
    }

    private void drawObject(Canvas c){
        this.gameObject.drawObject(c, movingX[actualGameObjectIndexX], movingY[actualGameObjectIndexY]);

        if(gameObject.isSelected()){
            c.drawRect(new Rect(this.x,this.y,this.x+(this.gameObject.getSizeX()*this.sizeX),this.y+(this.gameObject.getSizeY()*this.sizeY)),p);
        }
    }

    public void drawFloor(Canvas c){
        c.drawBitmap(this.floor,this.x,this.y,null);
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

    public DrawObjectType getDrawObjectType() {
        return drawObjectType;
    }

    public DrawObjectSubtype getDrawObjectSubtype() {
        return drawObjectSubtype;
    }

    public void setDrawObjectTypeAndSubtype(DrawObjectType drawObjectType, DrawObjectSubtype drawObjectSubtype,GameObjects gameObjects) {
        this.drawObjectType = drawObjectType;
        this.drawObjectSubtype = drawObjectSubtype;
        this.gameObject = gameObjects;
        if(drawObjectType != null){
            if(gameObjects == null) {
                this.object = null;
            }else{
                this.object = gameObjects.getBitmap();
            }
        }
    }

    public int getSizeX() {
        return sizeX;
    }

    public void setSizeX(int sizeX) {
        this.sizeX = sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSizeY(int sizeY) {
        this.sizeY = sizeY;
    }

    public int getIndexX() {
        return indexX;
    }

    public void setIndexX(int indexX) {
        this.indexX = indexX;
    }

    public int getIndexY() {
        return indexY;
    }

    public void setIndexY(int indexY) {
        this.indexY = indexY;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getFinalX() {
        return finalX;
    }

    public void setFinalX(int finalX) {
        this.finalX = finalX;
    }

    public int getFinalY() {
        return finalY;
    }

    public void setFinalY(int finalY) {
        this.finalY = finalY;
    }

    public Bitmap getFloor() {
        return floor;
    }

    public void setFloor(Bitmap floor) {
        this.floor = floor;
    }

    public Bitmap getObject() {
        return object;
    }

    public void setObject(Bitmap object) {
        this.object = object;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setDrawObjectType(DrawObjectType drawObjectType) {
        this.drawObjectType = drawObjectType;
    }

    public void setDrawObjectSubtype(DrawObjectSubtype drawObjectSubtype) {
        this.drawObjectSubtype = drawObjectSubtype;
    }

    public boolean isInteractable() {
        return interactable;
    }

    public void setInteractable(boolean interactable) {
        this.interactable = interactable;
    }

    public GameObjects getGameObject() {
        return gameObject;
    }

    public void setGameObject(GameObjects gameObject) {
        this.gameObject = gameObject;
    }

    public int getxReference() {
        return xReference;
    }

    public void setxReference(int xReference) {
        this.xReference = xReference;
    }

    public int getyReference() {
        return yReference;
    }

    public void setyReference(int yReference) {
        this.yReference = yReference;
    }

    public int getxReferenceCoord() {
        return xReferenceCoord;
    }

    public void setxReferenceCoord(int xReferenceCoord) {
        this.xReferenceCoord = xReferenceCoord;
    }

    public int getyReferenceCoord() {
        return yReferenceCoord;
    }

    public void setyReferenceCoord(int yReferenceCoord) {
        this.yReferenceCoord = yReferenceCoord;
    }

    public int getActualGameObjectIndexX() {
        return actualGameObjectIndexX;
    }

    public void setActualGameObjectIndexX(int actualGameObjectIndexX) {
        this.actualGameObjectIndexX = actualGameObjectIndexX;
    }

    public int getActualGameObjectIndexY() {
        return actualGameObjectIndexY;
    }

    public void setActualGameObjectIndexY(int actualGameObjectIndexY) {
        this.actualGameObjectIndexY = actualGameObjectIndexY;
    }

    public static int getMovingXSize() {
        return MOVING_X_SIZE;
    }

    public static int getMovingYSize() {
        return MOVING_Y_SIZE;
    }

    public int getMiddleIndexX() {
        return middleIndexX;
    }

}
