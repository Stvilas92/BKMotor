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
    int xReference,yReference;
    int xReferenceCoord,yReferenceCoord;

    private int x,y,finalX,finalY,indexX,indexY,sizeX,sizeY;
    private Bitmap floor,object;
    private Context context;
    private DrawObjectType drawObjectType;
    private DrawObjectSubtype drawObjectSubtype;
    private GameObjects gameObjects;
    private boolean interactable;
    private Paint p=new Paint();


    public Box(int indexX, int indexY, int x, int y, int sizeX, int sizeY, Context context, DrawObjectType drawObjectType, DrawObjectSubtype drawObjectSubtype,boolean interactable) {
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
//        if(drawObjectType != null && drawObjectSubtype != null){
//            setBitmapForObject();
//        }
    }

    public void drawBox(Canvas c){
        if(drawObjectType != null && drawObjectSubtype != null){
            drawObject(c);
        }
    }

    private void drawObject(Canvas c){
        c.drawBitmap(this.object,this.x,this.y,null);
        if(gameObjects.isSelected()){
            c.drawRect(new Rect(this.x,this.y,this.x+(this.gameObjects.getSizeX()*this.sizeX),this.y+(this.gameObjects.getSizeY()*this.sizeY)),p);
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
        this.gameObjects = gameObjects;
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

    public GameObjects getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(GameObjects gameObjects) {
        this.gameObjects = gameObjects;
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
}
