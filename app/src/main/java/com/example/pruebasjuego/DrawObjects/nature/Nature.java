package com.example.pruebasjuego.DrawObjects.nature;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.pruebasjuego.DrawObjects.OnTouchBarObjectResult;
import com.example.pruebasjuego.Utils.BitmapManager;
import com.example.pruebasjuego.DrawObjects.gameBars.DrawActionsBar;
import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObject;
import com.example.pruebasjuego.GameManger.Escenario;
import com.example.pruebasjuego.Screen.Box;
import com.example.pruebasjuego.Utils.GameTools;

public class Nature implements GameObject {
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
    private int id, actualBox,rectHeigth,sizeRectY,sizeRectX;
    private Bitmap natureBitmap,natureTypeBitmap;
    private Context context;
    private boolean selected = false;
    private Rect[] rectActions;
    private Paint p,pText;
    private BitmapManager bitmapManager;

    //Game Data
    private NatureType natureType;
    private NatureState natureState = NatureState.STTOPED;
    int initResources,actualResources;

    public Nature(Box[] boxes, int id, int actualBox, Context context, NatureType natureType, DrawActionsBar drawActionsBar, BitmapManager bitmapManager) {
        this.bitmapManager = bitmapManager;
        this.boxes = boxes;
        this.id = id;
        this.actualBox = actualBox;
        this.context = context;
        this.natureType = natureType;
        this.sizeRectY = (int)(boxes[0].getSizeY()*RECT_HEIGTH);
        this.sizeRectX = (int)(boxes[0].getSizeX()*RECT_WIDTH);
        rectHeigth = drawActionsBar.getInitY()+(((drawActionsBar.getScreenHeight()- drawActionsBar.getInitY()) - this.sizeRectY)/2);
        makeObjectToDraw();
        makeRectActions();
        this.p = new Paint();
        p.setColor(Color.YELLOW);
        p.setStyle(Paint.Style.STROKE);
        this.pText = new Paint();
        pText.setColor(Color.YELLOW);
        pText.setStyle(Paint.Style.FILL_AND_STROKE);
        pText.setTextSize(boxes[0].getSizeY()/2);
    }

    @Override
    public void drawObject(Canvas c, int x, int y) {
            c.drawBitmap(natureBitmap,x,y,null);
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
    public OnTouchBarObjectResult onTouchActionBarObject(int x, int y) {
        return OnTouchBarObjectResult.NONE;
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
                    boxesOcuped[index] = actualBox;
                }else{
                    //TODO: aqui tambien se llamaba desde escenario
                    boxesOcuped[index] = GameTools.getBoxByIndex(boxes,boxes[actualBox].getIndexX()+i,boxes[actualBox].getIndexX()+j);
                }
                index++;
            }
        }
    }


    private void makeObjectToDraw(){
        switch (natureType) {
            case ROCK:
                this.natureBitmap = bitmapManager.getNatureRock();
                this.natureTypeBitmap =  bitmapManager.getNatureTypeRock();
                getBoxesToDraw();
                this.boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING, DrawObjectSubtype.MAIN_BUILDING,this);
                this.initResources =  INIT_ROCK;
                break;

            case FOOD:
                this.natureBitmap = bitmapManager.getNatureFood();
                this.natureTypeBitmap =  bitmapManager.getNatureTypeFood();
                getBoxesToDraw();
                this.boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.TOWER,this);
                this.initResources =  INIT_FOOD;
                break;

            case WOOD:
                this.natureBitmap = bitmapManager.getNatureWood();
                this.natureTypeBitmap =  bitmapManager.getNatureTypeWood();
                getBoxesToDraw();
                this.boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.BUILDING,DrawObjectSubtype.WALL,this);
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

    @Override
    public boolean isSelectingMode() {
        return false;
    }

    @Override
    public void setSelectingMode(boolean selectingMode) {

    }

    @Override
    public void onTouchObject(boolean selectingMode, int x,int y,int boxSelected) {
        if(!isSelected() && !selectingMode){
            GameTools.deselectedAll(boxes);
            selected = true;
        }
    }

    public int getActualResources() {
        return actualResources;
    }

    public void setActualResources(int actualResources) {
        this.actualResources = actualResources;
        if(this.actualResources <= 0){
            boxes[actualBox].setDrawObjectTypeAndSubtype(null,null,null);
        }
    }

    public int getInitResources() {
        return initResources;
    }

    public void setInitResources(int initResources) {
        this.initResources = initResources;
    }

    public NatureType getNatureType() {
        return natureType;
    }

    public void setNatureType(NatureType natureType) {
        this.natureType = natureType;
    }


}
