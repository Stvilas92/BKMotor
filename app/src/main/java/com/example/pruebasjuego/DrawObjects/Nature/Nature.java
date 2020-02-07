package com.example.pruebasjuego.DrawObjects.Nature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObjects;
import com.example.pruebasjuego.DrawObjects.buildings.BuildingState;
import com.example.pruebasjuego.DrawObjects.buildings.BuildingType;
import com.example.pruebasjuego.GameManger.Escenario;
import com.example.pruebasjuego.Screen.Box;

import java.io.IOException;
import java.io.InputStream;

public class Nature implements GameObjects {

    private int sizeX = -1;
    private int sizeY = -1;
    private int[] boxesOcuped;
    private Box[] boxes;
    private int id,initBox;
    private Bitmap buildBitmap;
    private Context context;
    private boolean selected = false;
    private NatureType natureType;
    private NatureState natureState = NatureState.STTOPED;

    public Nature( Box[] boxes, int id, int initBox, Context context,NatureType natureType) {
        this.boxes = boxes;
        this.id = id;
        this.initBox = initBox;
        this.context = context;
        this.natureType = natureType;
        makeObjectToDraw();
    }

    @Override
    public void drawObject(Canvas c, int x, int y) {
        for (int i = 0; i < boxesOcuped.length; i++) {
            c.drawBitmap(buildBitmap,x,y,null);
        }
    }

    @Override
    public int getObjectID() {
        return this.id;
    }

    @Override
    public Bitmap getBitmap() {
        if(this.buildBitmap != null) {
            return this.buildBitmap;
        }else {
            return null;
        }
    }

    private void getBoxesToDraw(){
        boxesOcuped = new int[sizeX*sizeY];
        int index = 0 ;

        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if(i == 0 && j == 0){
                    boxesOcuped[index] = initBox;
                }else{
                    boxesOcuped[index] = Escenario.getBoxByIndex(boxes[initBox].getIndexX()+i,boxes[initBox].getIndexX()+j);
                }
                index++;
            }
        }
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

    private void makeObjectToDraw(){
        switch (natureType) {
            case ROCK:
                this.sizeX = 1;
                this.sizeY = 1;
                this.buildBitmap = getBitmapFromAssets("Nature/rock.png");
                this.buildBitmap = scaleByWidth(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING, DrawObjectSubtype.MAIN_BUILDING,this);
                break;

            case FOOD:
                this.sizeX = 1;
                this.sizeY = 1;
                this.buildBitmap = getBitmapFromAssets("Nature/food.png");
                this.buildBitmap = scaleByWidth(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.TOWER,this);
                break;

            case TREE:
                this.sizeX = 1;
                this.sizeY = 1;
                this.buildBitmap = getBitmapFromAssets("Nature/tree.png");
                this.buildBitmap = scaleByWidth(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.WALL,this);
                break;

        }
    }

    public Bitmap scaleByWidth(Bitmap res, int newHeight) {
        if (newHeight==res.getHeight()) return res;
        return res.createScaledBitmap(res, (res.getWidth() * newHeight) /
                res.getHeight(), newHeight, true);
    }

    public int[] getBoxesOcuped() {
        return boxesOcuped;
    }

    public NatureState getNatureState() {
        return natureState;
    }

    public void setNatureState(NatureState natureState) {
        this.natureState = natureState;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public int getSizeX() {
        return this.sizeX;
    }

    @Override
    public int getSizeY() {
        return this.sizeY;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
