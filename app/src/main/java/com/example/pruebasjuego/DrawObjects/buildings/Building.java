package com.example.pruebasjuego.DrawObjects.buildings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import com.example.pruebasjuego.DrawObjects.DrawActions;
import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObjects;
import com.example.pruebasjuego.GameManger.Escenario;
import com.example.pruebasjuego.Screen.Box;

import java.io.IOException;
import java.io.InputStream;

public class Building implements GameObjects {
    private static final double RECT_HEIGTH = 1.5;
    private static final double RECT_WIDTH = 1.5;
    private static final int INIT_X = 2;
    private static final int SEPARATE = 3;
    private static final int RECTS_NUMBER = 4;

    private int sizeX = -1,sizeRectX;
    private int sizeY = -1,sizeRectY;
    private int[] boxesOcuped;
    private Box[] boxes;
    private int id,initBox,rectHeigth;
    private Bitmap buildBitmap;
    private Context context;
    private BuildingState state = BuildingState.STOPPED;
    private boolean selected = false;
    private BuildingType buildingType;
    private Rect[] rectActions;
    private DrawActions drawActions;
    private Paint p;


    public Building(Box[] boxes, int id, int initBox, Context context, BuildingType buildingType, DrawActions drawActions) {
        this.boxes = boxes;
        this.id = id;
        this.initBox = initBox;
        this.context = context;
        this.buildingType = buildingType;
        makeObjectToDraw();
        this.drawActions = drawActions;
        this.sizeRectY = (int)(boxes[0].getSizeY()*RECT_HEIGTH);
        this.sizeRectX = (int)(boxes[0].getSizeX()*RECT_WIDTH);
        rectHeigth = drawActions.getInitY()+(((drawActions.getScreenHeight()-drawActions.getInitY()) - this.sizeRectY)/2);
        makeRectActions();
        this.p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void drawObject(Canvas c,int x,int y) {
        for (int i = 0; i < boxesOcuped.length; i++) {
            c.drawBitmap(buildBitmap,x,y,null);
        }
    }

    @Override
    public void drawInActionBar(Canvas c) {
        for (int i = 0; i < rectActions.length; i++) {
            c.drawRect(rectActions[i],p);
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
        switch (buildingType) {
            case MAIN:
                this.sizeX = 2;
                this.sizeY = 2;
                this.buildBitmap = getBitmapFromAssets("Buildings/main.png");
                this.buildBitmap = scaleByHeight(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.MAIN_BUILDING,this);
                break;

            case TOWER:
                this.sizeX = 1;
                this.sizeY = 1;
                this.buildBitmap = getBitmapFromAssets("Buildings/tower.png");
                this.buildBitmap = scaleByHeight(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.TOWER,this);
                break;

            case WALL:
                this.sizeX = 1;
                this.sizeY = 1;
                this.buildBitmap = getBitmapFromAssets("Buildings/wall.png");
                this.buildBitmap = scaleByHeight(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.WALL,this);
                break;

            case CATAPULT:
                this.sizeX = 1;
                this.sizeY = 1;
                this.buildBitmap = getBitmapFromAssets("Buildings/catapult.png");
//                this.buildBitmap = Bitmap.createScaledBitmap(this.buildBitmap, this.boxes[0].getSizeX() * sizeX, this.boxes[0].getSizeY() * sizeY, false);
                this.buildBitmap = scaleByHeight(this.buildBitmap, this.boxes[0].getSizeY() * sizeY);
                getBoxesToDraw();
                boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.CATAPULT,this);
                break;
        }
    }

    public Bitmap scaleByHeight(Bitmap res, int newHeight) {
        if (newHeight==res.getHeight()) return res;
        return res.createScaledBitmap(res, (res.getWidth() * newHeight) /
                res.getHeight(), newHeight, true);
    }

    private void makeRectActions(){
        rectActions = new Rect[RECTS_NUMBER];

        for (int i = 0; i < rectActions.length; i++) {
            rectActions[i] = new Rect(INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i),rectHeigth,INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i)+sizeRectX,rectHeigth+sizeRectY);
        }
    }

    public int[] getBoxesOcuped() {
        return boxesOcuped;
    }

    public BuildingState getState() {
        return state;
    }

    public void setState(BuildingState state) {
        this.state = state;
    }

    @Override
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
