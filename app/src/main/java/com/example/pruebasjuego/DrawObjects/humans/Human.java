package com.example.pruebasjuego.DrawObjects.humans;

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
import java.util.HashMap;

public class Human implements GameObjects {
    private static final double RECT_HEIGTH = 1.5;
    private static final double RECT_WIDTH = 1.5;
    private static final int INIT_X = 2;
    private static final int SEPARATE = 3;
    private static final int RECTS_NUMBER_BUILDING = 5;
    private static final int RECTS_NUMBER_TOWER = 2;
    private static final int RECTS_NUMBER_WALL = 2;
    private static final int RECTS_NUMBER_CATAPULT = 2;
    private static final int INIT_LIFE = 100;
    private static final int SIZE_WALKING_VILLAGER = 8;
    private static final int SIZE_WALKING_CONSTRUCTOR = 8;
    private static final int SIZE_WALKING_SOLDIER = 12;
    private static final int SIZE_DEAD_VILLAGER = 4;
    private static final int SIZE_DEAD_CONSTRUCTOR = 4;
    private static final int SIZE_DEAD_SOLDIER = 13;
    private static final int SIZE_ACTION_VILLAGER = 7;
    private static final int SIZE_ACTION_CONSTRUCTOR = 8;
    private static final int SIZE_ACTION_SOLDIER = 12;


    private int sizeX = 1,sizeRectX;
    private int sizeY = 1,sizeRectY;
    private int[] boxesOcuped;
    private Box[] boxes;
    private int id, actualBox,rectHeigth;
    private Bitmap humanBitmap,bitmapVillager,bitmapConstructor,bitmapSoldier;
    private Bitmap[] humanStopped;
    private HashMap <HumanOrientation,Bitmap[]>humanWalking,humanAction,humanDead;
    private Context context;
    private boolean selected = false;
    private DrawActionsBar drawActionsBar;
    private Paint p,pText;
    private DrawObjectSubtype drawObjectSubtype;

    //Game variables
    private HumanType humanType;
    private HumanOrientation humanOrientation;
    private Rect[] rectActions;
    private int actualLife = INIT_LIFE;
    private HumanState humanState = HumanState.STTOPED;
    private int boxDestiny = -1;
    private int moveXIndex = 0, moveYIndex = 0,movingDifferenceX,movingDifferenceY;
    private int walkingIndex = 0,actionIndex = 0,deadIndex = 0;


    public Human(Box[] boxes, int id, int actualBox, Context context, HumanType humanType, DrawActionsBar drawActionsBar, HumanOrientation humanOrientation) {
        this.boxes = boxes;
        this.humanOrientation = humanOrientation;
        this.id = id;
        this.actualBox = actualBox;
        this.context = context;
        this.humanType = humanType;
        makeObjectToDraw();
        this.drawActionsBar = drawActionsBar;
        this.sizeRectY = (int)(boxes[0].getSizeY()*RECT_HEIGTH);
        this.sizeRectX = (int)(boxes[0].getSizeX()*RECT_WIDTH);
        rectHeigth = drawActionsBar.getInitY()+(((drawActionsBar.getScreenHeight()- drawActionsBar.getInitY()) - this.sizeRectY)/2);
        makeRectActions();
        this.p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.STROKE);
        this.pText = new Paint();
        pText.setColor(Color.YELLOW);
        pText.setStyle(Paint.Style.STROKE);
        pText.setTextSize(boxes[0].getSizeY()/2);
    }

    @Override
    public void drawObject(Canvas c,int x,int y) {
        switch (humanState){
            case STTOPED:
                c.drawBitmap(humanBitmap, x, y, null);
                break;

            case WALKING:
                if(boxDestiny >= 0) {
                    setMovementDirection();
                    c.drawBitmap(humanWalking.get(humanOrientation)[walkingIndex], x, y, null);
                    walkingIndex++;
                    if (walkingIndex >= humanWalking.values().size()) {
                        walkingIndex = 0;
                    }
                }
                break;


            case ONACTION:
                c.drawBitmap(humanAction.get(humanOrientation)[actionIndex], x, y, null);
                actionIndex++;
                if(actionIndex >= humanAction.values().size()) {
                    actionIndex = 0;
                }
                break;

            case DEAD:
                c.drawBitmap(humanDead.get(humanOrientation)[deadIndex], x, y, null);
                deadIndex++;
                if(deadIndex >= humanDead.values().size()) {
                    deadIndex = 0;
                }
                break;
        }
    }

    @Override
    public void drawInActionBar(Canvas c) {
        for (int i = 0; i < rectActions.length; i++) {
            c.drawRect(rectActions[i],p);
        }

        switch(humanType){
            case VILLAGER:
                c.drawBitmap(bitmapConstructor,rectActions[0].left,rectActions[0].top,null);
                c.drawBitmap(bitmapSoldier,rectActions[1].left,rectActions[1].top,null);
                c.drawBitmap(bitmapVillager,rectActions[2].left,rectActions[2].top,null);
                c.drawText(""+actualLife+"/"+INIT_LIFE,rectActions[3].left,rectActions[3].top+pText.getTextSize(),pText);
                c.drawText(humanState.toString(),rectActions[4].left,rectActions[4].top+pText.getTextSize(),pText);
                break;

            case CONSTRUCTOR:
                break;

            case SOLDIER:
                break;
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
        if(this.humanBitmap != null) {
            return this.humanBitmap;
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
                    boxesOcuped[index] = Escenario.getBoxByIndex(boxes[actualBox].getIndexX()+i,boxes[actualBox].getIndexX()+j);
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
        setUnitsBitmaps();
        switch (humanType) {
            case VILLAGER:
                this.humanBitmap = bitmapVillager;
                getBoxesToDraw();
                createHuman(actualBox,HumanType.VILLAGER);
                drawObjectSubtype = DrawObjectSubtype.VILLAGER;
                break;

            case SOLDIER:
                this.humanBitmap = bitmapSoldier;
                getBoxesToDraw();
                createHuman(actualBox,HumanType.SOLDIER);
                drawObjectSubtype = DrawObjectSubtype.SOLDIER;
                break;

            case CONSTRUCTOR:
                this.humanBitmap = bitmapConstructor;
                getBoxesToDraw();
                createHuman(actualBox,HumanType.CONSTRUCTOR);
                drawObjectSubtype = DrawObjectSubtype.CONSTRUCTOR;
                break;
        }
    }

    public Bitmap scaleByHeight(Bitmap res, int newHeight) {
        if (newHeight==res.getHeight()) return res;
        return res.createScaledBitmap(res, (res.getWidth() * newHeight) /
                res.getHeight(), newHeight, true);
    }

    private void makeRectActions(){
        switch(humanType){
            case VILLAGER:
                rectActions = new Rect[RECTS_NUMBER_BUILDING];
                break;

            case CONSTRUCTOR:
                rectActions = new Rect[RECTS_NUMBER_WALL];
                break;

            case SOLDIER:
                rectActions = new Rect[RECTS_NUMBER_TOWER];
                break;
        }

        for (int i = 0; i < rectActions.length; i++) {
            rectActions[i] = new Rect(INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i),rectHeigth,INIT_X*boxes[0].getSizeX()+((SEPARATE*boxes[0].getSizeX())*i)+sizeRectX,rectHeigth+sizeRectY);
        }
    }

    public int[] getBoxesOcuped() {
        return boxesOcuped;
    }

    public HumanState getHumanState() {
        return humanState;
    }

    public void setHumanState(HumanState state) {
        this.humanState = state;
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

    private int getBoxByIndex(int indexX,int indexY){
        for (int i = indexX*indexY; i < boxes.length; i++) {
            if(boxes[i].getIndexX() == indexX && boxes[i].getIndexY() == indexY){
                return i;
            }
        }
        return -1;
    }

    private void createHuman(int box,HumanType humanType){
        String unitPath = "";
        String unitAction = "";
        int sizeWalking= 0,sizeAction = 0, sizeDead = 0;
        switch (humanType) {
            case VILLAGER:
                unitPath = "Villager";
                unitAction = "laydown";
                boxes[box].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, DrawObjectSubtype.VILLAGER, this);
                sizeWalking = SIZE_WALKING_VILLAGER;
                sizeAction = SIZE_ACTION_VILLAGER;
                sizeDead = SIZE_DEAD_VILLAGER;
                break;

            case CONSTRUCTOR:
                unitPath = "Constructor";
                unitAction = "nailing endless";
                boxes[box].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, DrawObjectSubtype.CONSTRUCTOR, this);
                sizeWalking = SIZE_WALKING_CONSTRUCTOR;
                sizeAction = SIZE_ACTION_CONSTRUCTOR;
                sizeDead = SIZE_DEAD_CONSTRUCTOR;
                break;

            case SOLDIER:
                unitPath = "Soldier";
                unitAction = "attack";
                boxes[box].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, DrawObjectSubtype.SOLDIER, this);
                sizeWalking = SIZE_WALKING_SOLDIER;
                sizeAction = SIZE_ACTION_SOLDIER;
                sizeDead = SIZE_DEAD_SOLDIER;
                break;
        }

//        boxes[box].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, DrawObjectSubtype.VILLAGER, this);
        humanWalking = new HashMap<HumanOrientation,Bitmap[]>();
        humanAction  = new HashMap<HumanOrientation,Bitmap[]>();
        humanDead = new HashMap<HumanOrientation,Bitmap[]>();

        for (int i = 0; i < HumanOrientation.values().length ; i++) {
            Bitmap[] bitmapAux = new Bitmap[sizeWalking];
            for (int j = 0; j < sizeWalking; j++) {
                bitmapAux[j] = getBitmapFromAssets("Units/"+unitPath+"/Walking/walking "+HumanOrientation.values()[i].toString().substring(0,1).toLowerCase()+"000"+j+".png");
                bitmapAux[j] = scaleByHeight(bitmapAux[j], this.boxes[0].getSizeY() * sizeY);
            }
            humanWalking.put(HumanOrientation.values()[i],bitmapAux);
        }

        for (int i = 0; i < HumanOrientation.values().length ; i++) {
            Bitmap[] bitmapAux = new Bitmap[sizeAction];
            for (int j = 0; j < sizeAction; j++) {
                bitmapAux[j] = getBitmapFromAssets("Units/"+unitPath+"/Action/"+unitAction+" "+HumanOrientation.values()[i].toString().substring(0,1).toLowerCase()+"000"+j+".png");
                bitmapAux[j] = scaleByHeight(bitmapAux[j], this.boxes[0].getSizeY() * sizeY);
            }
            humanAction.put(HumanOrientation.values()[i],bitmapAux);
        }

        if ( humanType == HumanType.SOLDIER) {
            for (int i = 0; i < HumanOrientation.values().length; i++) {
                Bitmap[] bitmapAux = new Bitmap[sizeDead];
                for (int j = 0; j < sizeDead; j++) {
                    bitmapAux[j] = getBitmapFromAssets("Units/" + unitPath + "/Dead/tipping over " + HumanOrientation.values()[i].toString().substring(0, 1).toLowerCase() + "000" + j + ".png");
                    bitmapAux[j] = scaleByHeight(bitmapAux[j], this.boxes[0].getSizeY() * sizeY);
                }
                humanDead.put(HumanOrientation.values()[i], bitmapAux);
            }
        }
    }

    public boolean moveHuman(HumanMovementType humanMovementType){
        boolean flagMoveBox = false;
        int boxToMoveIndex = -1;

        switch (humanMovementType){
            case VERTICAL_UP:
                if(boxes[actualBox].getActualGameObjectIndexY() == 0){
                    int newBoxIndex = getBoxByIndex(boxes[actualBox].getIndexX(), boxes[actualBox].getIndexY() - 1);
                    if(boxes[newBoxIndex].getGameObject() == null){
                        boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                        actualBox = newBoxIndex;
                        boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN,drawObjectSubtype,this);
                        boxes[actualBox].setActualGameObjectIndexY(boxes[actualBox].getMovingYSize()-1);
                    }else{
                        return false;
//                        this.humanState = HumanState.STTOPED;
                    }

                }
                else{
                    boxes[actualBox].setActualGameObjectIndexY(boxes[actualBox].getActualGameObjectIndexY()-1);
                }
                break;

            case VERTICAL_DOWN:
                if(boxes[actualBox].getActualGameObjectIndexY() == boxes[actualBox].getMovingYSize()-1){
                    int newBoxIndex = getBoxByIndex(boxes[actualBox].getIndexX(), boxes[actualBox].getIndexY() + 1);
                    if(boxes[newBoxIndex].getGameObject() == null) {
                        boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                        actualBox = newBoxIndex;
                        boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, drawObjectSubtype, this);
                        boxes[actualBox].setActualGameObjectIndexY(0);
                    }else{
                        return false;
//                        this.humanState = HumanState.STTOPED;;
                    }
                }
                else{
                    boxes[actualBox].setActualGameObjectIndexY(boxes[actualBox].getActualGameObjectIndexY()+1);
                }
                break;

            case HORIZONTAL_LEFT:
                if(boxes[actualBox].getActualGameObjectIndexX() == 0){
                    int newBoxIndex = getBoxByIndex(boxes[actualBox].getIndexX()-1,boxes[actualBox].getIndexY());
                    if(boxes[newBoxIndex].getGameObject() == null){
                        boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                        actualBox = newBoxIndex;
                        boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN,drawObjectSubtype,this);
                        boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getMovingXSize()-1);
                    }else{
                        return false;
//                        this.humanState = HumanState.STTOPED;
                    }
                }
                else{
                    boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX()-1);
                }
                break;

            case HORIZONTAL_RIGHT:
                if(boxes[actualBox].getActualGameObjectIndexX() == boxes[actualBox].getMovingXSize()-1){
                    int newBoxIndex = getBoxByIndex(boxes[actualBox].getIndexX()+1,boxes[actualBox].getIndexY());
                    if(boxes[newBoxIndex].getGameObject() == null){
                        boxes[actualBox].setDrawObjectTypeAndSubtype(null, null, null);
                        actualBox = newBoxIndex;
                        boxes[actualBox].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN,drawObjectSubtype,this);
                        boxes[actualBox].setActualGameObjectIndexX(0);
                    }else{
                        return false;
//                        this.humanState = HumanState.STTOPED;
                    }
                }
                else{
                    boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX()+1);
                }
                break;
        }
        return true;
    }


    private void setMovementDirection(){
        boolean condition1 = actualBox == boxDestiny && (boxes[actualBox].getActualGameObjectIndexX() != boxes[actualBox].getMiddleIndexX() || boxes[actualBox].getActualGameObjectIndexY() != 0);
        boolean condition2 = actualBox != boxDestiny  && boxDestiny >= 0;

        if(condition1 || condition2){
            movingDifferenceX = boxes[actualBox].getIndexX()-boxes[boxDestiny].getIndexX();
            movingDifferenceY = boxes[actualBox].getIndexY()-boxes[boxDestiny].getIndexY();
            int differenceAbs = Math.abs(movingDifferenceX) - Math.abs(movingDifferenceY);
            String difference = "";
            if(differenceAbs == 0){
                difference = "0";
            }else{
                difference = differenceAbs > 0? "horizontal":"vertical";
            }

            switch (difference) {
                case "0":
                    switch (humanOrientation) {
                        case EST:
                            moveHuman(HumanMovementType.HORIZONTAL_RIGHT);
                            break;

                        case WEST:
                            moveHuman(HumanMovementType.HORIZONTAL_LEFT);
                            break;

                        case NORTH:
                            moveHuman(HumanMovementType.VERTICAL_UP);
                            break;

                        case SOUTH:
                            moveHuman(HumanMovementType.VERTICAL_DOWN);
                            break;
                    }
                    break;

                case "horizontal":
                    if (movingDifferenceX >= 0) {
                        humanOrientation = HumanOrientation.WEST;
                        if (!moveHuman(HumanMovementType.HORIZONTAL_LEFT)) {
                        continue vetical;
                        }
                    } else {
                        humanOrientation = HumanOrientation.EST;
                        if (moveHuman(HumanMovementType.HORIZONTAL_RIGHT)) {

                        }
                    }
                    break;

                case "vertical":
                    vetical:
                    if (movingDifferenceY <= 0) {
                        humanOrientation = HumanOrientation.SOUTH;
                        moveHuman(HumanMovementType.VERTICAL_DOWN);
                    } else {
                        humanOrientation = HumanOrientation.NORTH;
                        moveHuman(HumanMovementType.VERTICAL_UP);
                    }
                    break;
            }
        }

//            if(Math.abs(movingDifferenceX) == Math.abs(movingDifferenceY)) {
//                switch (humanOrientation){
//                    case EST:
//                        moveHuman(HumanMovementType.HORIZONTAL_RIGHT);
//                        break;
//
//                    case WEST:
//                        moveHuman(HumanMovementType.HORIZONTAL_LEFT);
//                        break;
//
//                    case NORTH:
//                        moveHuman(HumanMovementType.VERTICAL_UP);
//                        break;
//
//                    case SOUTH:
//                        moveHuman(HumanMovementType.VERTICAL_DOWN);
//                        break;
//                }
//            }else if(Math.abs(movingDifferenceX) > Math.abs(movingDifferenceY)){
//                if(movingDifferenceX >= 0){
//                    humanOrientation = HumanOrientation.WEST;
//                    if(!moveHuman(HumanMovementType.HORIZONTAL_LEFT)){
//
//                    }
//                }else{
//                    humanOrientation = HumanOrientation.EST;
//                    if(moveHuman(HumanMovementType.HORIZONTAL_RIGHT)){
//
//                    }
//                }
//            }else{
//                if(movingDifferenceY <= 0){
//                    humanOrientation = HumanOrientation.SOUTH;
//                    moveHuman(HumanMovementType.VERTICAL_DOWN);
//                }else{
//                    humanOrientation = HumanOrientation.NORTH;
//                    moveHuman(HumanMovementType.VERTICAL_UP);
//                }
//            }
        }else {
            this.humanState = HumanState.STTOPED;
        }
    }

    @Override
    public int onTouchWhenSelected(int boxIndex) {
        this.humanState = HumanState.WALKING;
        this.boxDestiny = boxIndex;
        return actualBox;
    }

    public int getActualBox() {
        return actualBox;
    }
}