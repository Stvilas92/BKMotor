package com.example.pruebasjuego.DrawObjects.buildings;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.pruebasjuego.DrawObjects.OnTouchBarObjectResult;
import com.example.pruebasjuego.DrawObjects.gameBars.DrawActionsBar;
import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObject;
import com.example.pruebasjuego.DrawObjects.gameBars.DrawResourcesBar;
import com.example.pruebasjuego.DrawObjects.humans.Human;
import com.example.pruebasjuego.DrawObjects.humans.HumanOrientation;
import com.example.pruebasjuego.DrawObjects.humans.HumanType;
import com.example.pruebasjuego.GameManger.Escenario;
import com.example.pruebasjuego.Screen.Box;
import java.io.IOException;
import java.io.InputStream;

public class Building implements GameObject {
    private static final double RECT_HEIGTH = 1.5;
    private static final double RECT_WIDTH = 1.5;
    private static final int INIT_X = 2;
    private static final int SEPARATE = 3;
    private static final int RECTS_NUMBER_BUILDING = 5;
    private static final int RECTS_NUMBER_TOWER = 2;
    private static final int RECTS_NUMBER_WALL = 2;
    private static final int RECTS_NUMBER_CATAPULT = 2;
    private static final int INIT_LIFE = 100;
    private static final int SOLDIER_FOOD_COST = 50;
    private static final int CONSTRUCTOR_FOOD_COST = 100;
    private static final int VILLAGER_FOOD_COST = 30;

    private int sizeX = -1,sizeRectX;
    private int sizeY = -1,sizeRectY;
    private int[] boxesOcuped;
    private Box[] boxes;
    private int id,initBox,rectHeigth;
    private Bitmap buildBitmap,bitmapVillager,bitmapConstructor,bitmapSoldier;
    private Context context;
    private BuildingState state = BuildingState.STOPPED;
    private boolean selected = false;
    private DrawActionsBar drawActionsBar;
    private Paint p,pText;
    private Canvas c;

    //Game variables
    private BuildingType buildingType;
    private Rect[] rectActions;
    private int actualLife = INIT_LIFE;
    private BuildingState buildingState = BuildingState.STOPPED;
    private Runnable[] actions;
    private DrawResourcesBar drawResourcesBar;
    private boolean selectingMode = false;

    public Building(Box[] boxes, int id, int initBox, Context context, BuildingType buildingType, DrawActionsBar drawActionsBar, DrawResourcesBar drawResourcesBar) {
        this.boxes = boxes;
        this.id = id;
        this.initBox = initBox;
        this.context = context;
        this.buildingType = buildingType;
        this.drawResourcesBar = drawResourcesBar;
        makeObjectToDraw();
        this.drawActionsBar = drawActionsBar;
        this.sizeRectY = (int)(boxes[0].getSizeY()*RECT_HEIGTH);
        this.sizeRectX = (int)(boxes[0].getSizeX()*RECT_WIDTH);
        rectHeigth = drawActionsBar.getInitY()+(((drawActionsBar.getScreenHeight()- drawActionsBar.getInitY()) - this.sizeRectY)/2);
        makeRectActions();
        this.p = new Paint();
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        this.pText = new Paint();
        pText.setColor(Color.YELLOW);
        pText.setStyle(Paint.Style.STROKE);
        pText.setTextSize(boxes[0].getSizeY()/2);
    }

    @Override
    public void drawObject(Canvas c,int x,int y) {
        this.c = c;
        c.drawBitmap(buildBitmap,x,y,null);
    }

    @Override
    public void drawInActionBar(Canvas c) {
        for (int i = 0; i < rectActions.length; i++) {
            c.drawRect(rectActions[i],p);
            c.drawBitmap(drawActionsBar.getBitmapButton(),rectActions[i].left,rectActions[i].top,null);
        }

        switch(buildingType){
            case MAIN:
                c.drawBitmap(bitmapVillager,rectActions[0].left,rectActions[0].top,null);
                c.drawBitmap(bitmapConstructor,rectActions[1].left,rectActions[1].top,null);
                c.drawBitmap(bitmapSoldier,rectActions[2].left,rectActions[2].top,null);
                c.drawText(""+actualLife+"/"+INIT_LIFE,rectActions[3].left,rectActions[3].top+pText.getTextSize(),pText);
                c.drawText(buildingState.toString(),rectActions[4].left,rectActions[4].top+pText.getTextSize(),pText);
                break;

            case WALL:
                break;

            case TOWER:
                break;

            case CATAPULT:
                break;
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

    public OnTouchBarObjectResult onTouchActionBarObject(int x, int y){
        for (int i = 0; i < rectActions.length; i++) {
            if(rectActions[i].contains(x,y)){
                c.drawBitmap(drawActionsBar.getBitmapButtonPressed(),rectActions[i].left,rectActions[i].top,null);
                if(i == rectActions.length-1){
                    this.selectingMode = false;
                    this.selected = false;
                    return OnTouchBarObjectResult.DROP_ALL_SELECTED;
                }else {
                    actions[i].run();
                    return OnTouchBarObjectResult.NONE;
                }
            }
        }
        return OnTouchBarObjectResult.NONE;
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
                setUnitsBitmaps();


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
        switch(buildingType){
            case MAIN:
                rectActions = new Rect[RECTS_NUMBER_BUILDING];
                actions = new Runnable[RECTS_NUMBER_BUILDING-2];
                for (int i = 0; i < actions.length; i++) {
                    final int index = i;
                    actions[i] = (() -> setBuildingState(BuildingState.values()[index]));
                }
                break;

            case WALL:
                rectActions = new Rect[RECTS_NUMBER_WALL];
                break;

            case TOWER:
                rectActions = new Rect[RECTS_NUMBER_TOWER];
                break;

            case CATAPULT:
                rectActions = new Rect[RECTS_NUMBER_CATAPULT];
                break;
        }

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

    public void setUnitsBitmaps(){
        this.bitmapConstructor = getBitmapFromAssets("Units/Constructor/Walking/stopped0000.png");
        this.bitmapConstructor = scaleByHeight(this.bitmapConstructor, this.boxes[0].getSizeY() * sizeY);
        this.bitmapSoldier = getBitmapFromAssets("Units/Soldier/Walking/stopped0000.png");
        this.bitmapSoldier = scaleByHeight(this.bitmapSoldier, this.boxes[0].getSizeY() * sizeY);
        this.bitmapVillager = getBitmapFromAssets("Units/Villager/Walking/stopped0000.png");
        this.bitmapVillager = scaleByHeight(this.bitmapVillager, this.boxes[0].getSizeY() * sizeY);
    }

    public BuildingState getBuildingState() {
        return buildingState;
    }

    public void setBuildingState(BuildingState buildingState) {
        this.buildingState = buildingState;

        switch (this.buildingState){
            case STOPPED:
                break;
            case ATTACKING:
                break;
            case DEFENDING:
                break;
            case CREATING_SOLDIER:
                if(drawResourcesBar.getActualFood() >= SOLDIER_FOOD_COST) {
                    Human soldier = new Human(boxes, 1, initBox - 1, context, HumanType.SOLDIER, drawActionsBar, HumanOrientation.SOUTH, drawResourcesBar);
                    this.setBuildingState(BuildingState.STOPPED);
                    drawResourcesBar.setActualFood(drawResourcesBar.getActualFood()-SOLDIER_FOOD_COST);
                }
                break;
            case CREATING_VILLAGER:
                if(drawResourcesBar.getActualFood() >= VILLAGER_FOOD_COST) {
                    Human villager = new Human(boxes, 1, initBox - 1, context, HumanType.VILLAGER, drawActionsBar, HumanOrientation.SOUTH, drawResourcesBar);
                    this.setBuildingState(BuildingState.STOPPED);
                    drawResourcesBar.setActualFood(drawResourcesBar.getActualFood()-VILLAGER_FOOD_COST);
                }
                break;
            case CREATING_CONSTRUCOR:
                if(drawResourcesBar.getActualFood() >= CONSTRUCTOR_FOOD_COST) {
                    Human constructor = new Human(boxes, 1, initBox - 1, context, HumanType.CONSTRUCTOR, drawActionsBar, HumanOrientation.SOUTH, drawResourcesBar);
                    this.setBuildingState(BuildingState.STOPPED);
                    drawResourcesBar.setActualFood(drawResourcesBar.getActualFood()-CONSTRUCTOR_FOOD_COST);
                }
                break;
        }
    }

    @Override
    public int onTouchWhenSelected(int boxIndex) {
        return boxIndex;
    }

    @Override
    public boolean isSelectingMode() {
        return selectingMode;
    }

    @Override
    public void setSelectingMode(boolean selectingMode) {
        this.selectingMode =  selectingMode;

    }

    public int getActualLife() {
        return actualLife;
    }

    public void setActualLife(int actualLife) {
        this.actualLife = actualLife;
    }

    public BuildingType getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(BuildingType buildingType) {
        this.buildingType = buildingType;
    }
}
