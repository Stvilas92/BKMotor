package com.example.pruebasjuego.DrawObjects.nature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.pruebasjuego.DrawObjects.gameBars.DrawActionsBar;
import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObjects;
import com.example.pruebasjuego.GameManger.Escenario;
import com.example.pruebasjuego.Screen.Box;

import java.io.IOException;
import java.io.InputStream;

public class Nature implements GameObjects {
    private static final double RECT_HEIGTH = 1.5;
    private static final double RECT_WIDTH = 1;
    private static final int INIT_X = 2;
    private static final int SEPARATE = 1;
    private static final int RECTS_NUMBER = 2;
    private static final int INIT_ROCK = 50;
    private static final int INIT_FOOD = 150;
    private static final int INIT_WOOD = 50;

    private int sizeX = -1;
    private int sizeY = -1;
    private int[] boxesOcuped;
    private Box[] boxes;
    private int id,initBox,rectHeigth,sizeRectY,sizeRectX;
    private Bitmap natureBitmap,natureTypeBitmap;
    private Context context;
    private boolean selected = false;
    private Rect[] rectActions;
    private Paint p,pText;

    //Game Data
    private NatureType natureType;
    private NatureState natureState = NatureState.STTOPED;
    int initResources,actualResources;

    public Nature(Box[] boxes, int id, int initBox, Context context, NatureType natureType, DrawActionsBar drawActionsBar) {
        this.boxes = boxes;
        this.id = id;
        this.initBox = initBox;
        this.context = context;
        this.natureType = natureType;
        this.sizeRectY = (int)(boxes[0].getSizeY()*RECT_HEIGTH);
        this.sizeRectX = (int)(boxes[0].getSizeX()*RECT_WIDTH);
        rectHeigth = drawActionsBar.getInitY()+(((drawActionsBar.getScreenHeight()- drawActionsBar.getInitY()) - this.sizeRectY)/2);
        makeObjectToDraw();
        makeRectActions();
        this.p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        this.pText = new Paint();
        pText.setColor(Color.YELLOW);
        pText.setStyle(Paint.Style.FILL_AND_STROKE);
        pText.setTextSize(boxes[0].getSizeY()/2);
    }

    @Override
    public void drawObject(Canvas c, int x, int y) {
        for (int i = 0; i < boxesOcuped.length; i++) {
            c.drawBitmap(natureBitmap,x,y,null);
        }
    }

    @Override
    public void drawInActionBar(Canvas c) {
        for (int i = 0; i < rectActions.length; i++) {
            c.drawRect(rectActions[i],p);
            if( i == 0){
                c.drawBitmap(natureTypeBitmap,rectActions[i].left,rectActions[i].top,null);
            }else if( i == 1){
                c.drawText(""+actualResources+"/"+initResources,rectActions[i].left,rectActions[i].top+(boxes[0].getSizeY()/2),pText);
            }
        }
    }

    @Override
    public void onTouchActionBarObject(int x, int y) {

    }

    @Override
    public int getObjectID() {
        return this.id;
    }

    @Override
    public Bitmap getBitmap() {
        if(this.natureBitmap != null) {
            return this.natureBitmap;
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
                this.natureBitmap = getBitmapFromAssets("Nature/rock.png");
                this.natureBitmap = scaleByWidth(this.natureBitmap, this.boxes[0].getSizeY() * sizeY);
                this.natureTypeBitmap = getBitmapFromAssets("Resources/stone.png");
                this.natureTypeBitmap = scaleByWidth(this.natureTypeBitmap, this.boxes[0].getSizeY());
                getBoxesToDraw();
                this.boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING, DrawObjectSubtype.MAIN_BUILDING,this);
                this.initResources =  INIT_ROCK;
                break;

            case FOOD:
                this.sizeX = 1;
                this.sizeY = 1;
                this.natureBitmap = getBitmapFromAssets("Nature/food.png");
                this.natureBitmap = scaleByWidth(this.natureBitmap, this.boxes[0].getSizeY() * sizeY);
                this.natureTypeBitmap = getBitmapFromAssets("Resources/food.png");
                this.natureTypeBitmap = scaleByWidth(this.natureTypeBitmap, this.boxes[0].getSizeY());
                getBoxesToDraw();
                this.boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.TOWER,this);
                this.initResources =  INIT_FOOD;
                break;

            case WOOD:
                this.sizeX = 1;
                this.sizeY = 1;
                this.natureBitmap = getBitmapFromAssets("Nature/tree.png");
                this.natureBitmap = scaleByWidth(this.natureBitmap, this.boxes[0].getSizeY() * sizeY);
                this.natureTypeBitmap = getBitmapFromAssets("Resources/wood.png");
                this.natureTypeBitmap = scaleByWidth(this.natureTypeBitmap, this.boxes[0].getSizeY());
                getBoxesToDraw();
                this.boxes[initBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.WALL,this);
                this.initResources =  INIT_WOOD;
                break;

        }
        this.actualResources = this.initResources;
    }

    private void makeRectActions(){
        rectActions = new Rect[RECTS_NUMBER];

        for (int i = 0; i < rectActions.length; i++) {
            rectActions[i] = new Rect(INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i),rectHeigth,INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i)+sizeRectX,rectHeigth+sizeRectY);
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

    @Override
    public int  onTouchWhenSelected(int boxIndex) {
        return boxIndex;
    }
}
