package com.example.pruebasjuego.DrawObjects.humans;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.example.pruebasjuego.DrawObjects.OnTouchBarObjectResult;
import com.example.pruebasjuego.Utils.BitmapManager;
import com.example.pruebasjuego.DrawObjects.buildings.Building;
import com.example.pruebasjuego.DrawObjects.buildings.BuildingType;
import com.example.pruebasjuego.DrawObjects.gameBars.DrawActionsBar;
import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObject;
import com.example.pruebasjuego.DrawObjects.gameBars.DrawResourcesBar;
import com.example.pruebasjuego.DrawObjects.nature.Nature;
import com.example.pruebasjuego.Screen.Box;

import java.util.HashMap;

/**
 * Represents a human. Human can be do different things depending on the type (VILLAGER,CONSTRUCTOR,SOLDIER)
 */
public class Human implements GameObject {
    private static final double RECT_HEIGTH = 1.5;
    private static final double RECT_WIDTH = 1.5;
    private static final int INIT_X = 2;
    private static final int SEPARATE = 3;
    private static final int RECTS_NUMBER_HUMAN = 3;
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
    private static final int REPAIR = 1;
    private static final int COLLECT = 1;
    private static final int ATACK = 10;


    private int sizeX = 1,sizeRectX;
    private int sizeY = 1,sizeRectY;
    private int[] boxesOcuped;
    private Box[] boxes;
    private int id, actualBox,rectHeigth;
    private Bitmap humanBitmap,bitmapVillager,bitmapConstructor,bitmapSoldier,exitBitmap,actionBitmap;
    private Bitmap[] humanStopped;
    private HashMap <HumanOrientation,Bitmap[]>humanWalking,humanAction,humanDead;
    private Context context;
    private boolean selected = false;
    private DrawActionsBar drawActionsBar;
    private DrawResourcesBar drawResourcesBar;
    private Paint p,pText;
    private DrawObjectSubtype drawObjectSubtype;
    private Canvas c;

    //Game variables
    private HumanType humanType;
    private HumanOrientation humanOrientation;
    private Rect[] rectActions;
    private Runnable[] actions;
    private int actualLife = INIT_LIFE;
    private HumanState humanState = HumanState.STTOPED;
    private int boxDestiny = -1;
    private int moveXIndex = 0, moveYIndex = 0,movingDifferenceX,movingDifferenceY;
    private int walkingIndex = 0,actionIndex = 0,deadIndex = 0;
    private boolean flagActionEnd = false;
    private boolean selecttingMode = false;
    private GameObject objectObjetive;


    public Human(Box[] boxes, int id, int actualBox, Context context, HumanType humanType, DrawActionsBar drawActionsBar, HumanOrientation humanOrientation, DrawResourcesBar drawResourcesBar) {
        this.boxes = boxes;
        this.drawResourcesBar = drawResourcesBar;
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
        p.setColor(Color.TRANSPARENT);
        p.setStyle(Paint.Style.FILL_AND_STROKE);
        this.pText = new Paint();
        pText.setColor(Color.YELLOW);
        pText.setStyle(Paint.Style.STROKE);
        pText.setTextSize(boxes[0].getSizeY()/2);
    }

    /**
     * Draw the human in a canvas in the xy selected position. The human draw will depend on the human state
     * @param c Canvas where the human will be draw.
     * @param x x selected position
     * @param y y selected position
     */
    @Override
    public void drawObject(Canvas c,int x,int y) {
        this.c = c;
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
                if(boxDestiny >= 0) {
                    setMovementDirection();
                    c.drawBitmap(humanWalking.get(humanOrientation)[walkingIndex], x, y, null);
                    walkingIndex++;
                    if (walkingIndex >= humanWalking.values().size()) {
                        walkingIndex = 0;
                    }
                }else {
                    if(flagActionEnd){
                        humanState =  HumanState.STTOPED;
                        selecttingMode = false;
                    }else {
                        c.drawBitmap(humanAction.get(humanOrientation)[actionIndex], x, y, null);
                        actionIndex++;
                        if (actionIndex >= humanAction.values().size()) {
                            actionIndex = 0;
                            doAction(objectObjetive);
                        }
                    }
                }
                break;

            case DEAD:
                c.drawBitmap(humanDead.get(humanOrientation)[deadIndex], x, y, null);
                deadIndex++;
                if(deadIndex >= humanDead.values().size()) {
                    deadIndex = 0;
                }
                boxes[actualBox].setDrawObjectTypeAndSubtype(null,null,null);
                break;
        }
    }

    /**
     * Draw the rects and bitmaps action bar. Each human have a different action bar.
     * @param c
     */
    @Override
    public void drawInActionBar(Canvas c) {
        for (int i = 0; i < rectActions.length; i++) {
            c.drawRect(rectActions[i],p);
            c.drawBitmap(drawActionsBar.getBitmapButton(),rectActions[i].left,rectActions[i].top,null);
        }

        c.drawBitmap(actionBitmap,rectActions[0].left,rectActions[0].top,null);
        c.drawText(""+actualLife+"/"+INIT_LIFE,rectActions[1].left,rectActions[1].top+pText.getTextSize(),pText);
        c.drawBitmap(exitBitmap,rectActions[2].left,rectActions[2].top,null);
    }

    /**
     * Run the action contained on the rect, pressed by the user.
     * @param x X coordenate pressed by the user
     * @param y Y coordenate pressed by the user
     * @return Result of the action.
     */
    @Override
    public OnTouchBarObjectResult onTouchActionBarObject(int x, int y) {
        for (int i = 0; i < rectActions.length; i++) {
            if(rectActions[i].contains(x,y)){
                c.drawBitmap(drawActionsBar.getBitmapButtonPressed(),rectActions[i].left,rectActions[i].top,null);
                actions[i].run();

                if(i == rectActions.length-1){
                    return OnTouchBarObjectResult.DROP_ALL_SELECTED;
                }else {
                    return OnTouchBarObjectResult.NONE;
                }
            }
        }
        return OnTouchBarObjectResult.NONE;
    }

    /**
     * Get the human ID
     * @return
     */
    @Override
    public int getObjectID() {
        return this.id;
    }

    /**
     * Get the general bitmap of the human
     * @return humanBitmap
     */
    @Override
    public Bitmap getBitmap() {
        if(this.humanBitmap != null) {
            return this.humanBitmap;
        }else {
            return null;
        }
    }

    /**
     * Create the bitmap general to draw and createHuman to create the rest bitmaps and
     * put the DrawObject type and subtype on the actualBox.
     */
    private void makeObjectToDraw(){
        setUnitsBitmaps();
        switch (humanType) {
            case VILLAGER:
                this.humanBitmap = bitmapVillager;
                createHuman(actualBox,HumanType.VILLAGER);
                drawObjectSubtype = DrawObjectSubtype.VILLAGER;
                break;

            case SOLDIER:
                this.humanBitmap = bitmapSoldier;
                createHuman(actualBox,HumanType.SOLDIER);
                drawObjectSubtype = DrawObjectSubtype.SOLDIER;
                break;

            case CONSTRUCTOR:
                this.humanBitmap = bitmapConstructor;
                createHuman(actualBox,HumanType.CONSTRUCTOR);
                drawObjectSubtype = DrawObjectSubtype.CONSTRUCTOR;
                break;
        }
    }

    /**
     * Create the Rects array using to draw the options of the DrawActionBar and
     * create the actions of each rect that it will be realized if the user touch on the Rects
     */
    private void makeRectActions(){
        rectActions = new Rect[RECTS_NUMBER_HUMAN];
        actions = new Runnable[RECTS_NUMBER_HUMAN];

        actions[0] = (() -> {selecttingMode = true;});
        actions[1] = (() -> {return;});
        actions[2] = (() -> setSelected(false));
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

    /**
     * Set if a human is selected right now or no
     * @param selected indicate if a human is selected right now or no
     */
    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Create actionBitmap,exitBitmap and bitmapSoldier,bitmapVillager or bitmapConstructor
     * depending on the humanType
     */
    public void setUnitsBitmaps(){
        switch (this.humanType) {
            case SOLDIER:
                this.bitmapSoldier = BitmapManager.getBitmapFromAssets("Units/Soldier/Walking/stopped0000.png",context);
                this.bitmapSoldier = BitmapManager.scaleByHeight(this.bitmapSoldier, this.boxes[0].getSizeY() * sizeY);
                this.actionBitmap = BitmapManager.getBitmapFromAssets("BarIcons/sword.png",context);
                this.actionBitmap = BitmapManager.scaleByHeight(this.actionBitmap, this.boxes[0].getSizeY() * sizeY);
                break;

            case VILLAGER:
                this.bitmapVillager = BitmapManager.getBitmapFromAssets("Units/Villager/Walking/stopped0000.png",context);
                this.bitmapVillager = BitmapManager.scaleByHeight(this.bitmapVillager, this.boxes[0].getSizeY() * sizeY);
                this.actionBitmap = BitmapManager.getBitmapFromAssets("BarIcons/hand.png",context);
                this.actionBitmap = BitmapManager.scaleByHeight(this.actionBitmap, this.boxes[0].getSizeY() * sizeY);
                break;

            case CONSTRUCTOR:
                this.bitmapConstructor = BitmapManager.getBitmapFromAssets("Units/Constructor/Walking/stopped0000.png",context);
                this.bitmapConstructor = BitmapManager.scaleByHeight(this.bitmapConstructor, this.boxes[0].getSizeY() * sizeY);
                this.actionBitmap = BitmapManager.getBitmapFromAssets("BarIcons/hand.png",context);
                this.actionBitmap = BitmapManager.scaleByHeight(this.actionBitmap, this.boxes[0].getSizeY() * sizeY);
                break;
        }
        this.exitBitmap = BitmapManager.getBitmapFromAssets("BarIcons/red_boxCross.png",context);
        this.exitBitmap = BitmapManager.scaleByHeight(this.exitBitmap, this.boxes[0].getSizeY() * sizeY);
    }

    /**
     * Get a index of the box witch contains indexX and indexY
     * @param indexX index of X of the box
     * @param indexY index of Y of the box
     * @return box witch contains indexX and indexY
     */
    private int getBoxByIndex(int indexX,int indexY){
        for (int i = indexX*indexY; i < boxes.length; i++) {
            if(boxes[i].getIndexX() == indexX && boxes[i].getIndexY() == indexY){
                return i;
            }
        }
        return -1;
    }

    /**
     * Set a DrawObjectType and Subtype on the actualBox depending on the humanType.
     * In addition create all the bitmaps needed to draw a human in all states.
     * @param box Box to modify.
     * @param humanType HumanType using to set DrawObjectType and Subtype in the box
     */
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

        humanWalking = new HashMap<HumanOrientation,Bitmap[]>();
        humanAction  = new HashMap<HumanOrientation,Bitmap[]>();
        humanDead = new HashMap<HumanOrientation,Bitmap[]>();

        for (int i = 0; i < HumanOrientation.values().length ; i++) {
            Bitmap[] bitmapAux = new Bitmap[sizeWalking];
            for (int j = 0; j < sizeWalking; j++) {
                bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/"+unitPath+"/Walking/walking "+HumanOrientation.values()[i].toString().substring(0,1).toLowerCase()+"000"+j+".png",context);
                bitmapAux[j] = BitmapManager.scaleByHeight(bitmapAux[j], this.boxes[0].getSizeY() * sizeY);
            }
            humanWalking.put(HumanOrientation.values()[i],bitmapAux);
        }

        for (int i = 0; i < HumanOrientation.values().length ; i++) {
            Bitmap[] bitmapAux = new Bitmap[sizeAction];
            for (int j = 0; j < sizeAction; j++) {
                bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/"+unitPath+"/Action/"+unitAction+" "+HumanOrientation.values()[i].toString().substring(0,1).toLowerCase()+"000"+j+".png",context);
                bitmapAux[j] = BitmapManager.scaleByHeight(bitmapAux[j], this.boxes[0].getSizeY() * sizeY);
            }
            humanAction.put(HumanOrientation.values()[i],bitmapAux);
        }

        if ( humanType == HumanType.SOLDIER) {
            for (int i = 0; i < HumanOrientation.values().length; i++) {
                Bitmap[] bitmapAux = new Bitmap[sizeDead];
                for (int j = 0; j < sizeDead; j++) {
                    bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/" + unitPath + "/Dead/tipping over " + HumanOrientation.values()[i].toString().substring(0, 1).toLowerCase() + "000" + j + ".png",context);
                    bitmapAux[j] = BitmapManager.scaleByHeight(bitmapAux[j], this.boxes[0].getSizeY() * sizeY);
                }
                humanDead.put(HumanOrientation.values()[i], bitmapAux);
            }
        }
    }

    /**
     * Move a human inside on the actualBox
     */
    public void moveHumanOnActualBox(){
        int indexX = boxes[actualBox].getActualGameObjectIndexX();
        int indexY = boxes[actualBox].getActualGameObjectIndexY();

        if(indexX >  boxes[actualBox].getMiddleIndexX()){
            boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX()-1);
        }else if( indexY != 0){
            boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX()+1);
        }else if( indexY != 0){
            boxes[actualBox].setActualGameObjectIndexY(boxes[actualBox].getActualGameObjectIndexY()-1);
        }
    }

    /**
     * Move the human in any direction. If human finds a  obstacle, the function will move the human on the opposite direction.
     * When human rise boxDestiny, human will be stop itself.
     * @param humanMovementType
     */
    public void moveHuman(HumanMovementType humanMovementType){

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
                        moveHorizontal();
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
                        moveHorizontal();
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
                        moveVertical();
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
                        moveVertical();
                    }
                }
                else{
                    boxes[actualBox].setActualGameObjectIndexX(boxes[actualBox].getActualGameObjectIndexX()+1);
                }
                break;
        }
    }


    /**
     * Calculate orientation and direction of the human and move itself
     * in the direction and orientation calculated
     */
    private void setMovementDirection(){
        boolean condition1 = actualBox == boxDestiny && (boxes[actualBox].getActualGameObjectIndexX() != boxes[actualBox].getMiddleIndexX() || boxes[actualBox].getActualGameObjectIndexY() != 0);
        boolean condition2 = actualBox != boxDestiny  && boxDestiny >= 0;

        if(condition1 || condition2){
            if(actualBox == boxDestiny){
                moveHumanOnActualBox();
                return;
            }
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
                        moveHuman(HumanMovementType.HORIZONTAL_LEFT);
                    } else {
                        humanOrientation = HumanOrientation.EST;
                        moveHuman(HumanMovementType.HORIZONTAL_RIGHT);
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
        else {
            if( this.humanState == HumanState.ONACTION){
                boxDestiny = -1;
            }else{
                this.humanState = HumanState.STTOPED;
                boxDestiny = -1;
            }
        }
    }

    /**
     * Move a human vertically depending on the value of movingDifferenceX
     */
    public void moveHorizontal(){
        if (movingDifferenceX >= 0) {
            humanOrientation = HumanOrientation.WEST;
            moveHuman(HumanMovementType.HORIZONTAL_LEFT);
        } else {
            humanOrientation = HumanOrientation.EST;
            moveHuman(HumanMovementType.HORIZONTAL_RIGHT);
        }
    }

    /**
     * Move a human horizontally depending on the value of movingDifferenceY
     */
    public void moveVertical(){
        if (movingDifferenceY <= 0) {
            humanOrientation = HumanOrientation.SOUTH;
            moveHuman(HumanMovementType.VERTICAL_DOWN);
        } else {
            humanOrientation = HumanOrientation.NORTH;
            moveHuman(HumanMovementType.VERTICAL_UP);
        }
    }

    /**
     * Calculates boxDestiny variable when a OnTouchEvent is produced
     * when a human is selected
     * @param boxIndex
     * @return
     */
    @Override
    public int onTouchWhenSelected(int boxIndex) {
        this.boxDestiny = boxIndex;
        this.objectObjetive = boxes[boxIndex].getGameObject();

        if(boxes[boxIndex].getGameObject() == null && selecttingMode) {
            this.humanState = HumanState.WALKING;
        }else if(selecttingMode){
            this.humanState = HumanState.ONACTION;
            this.boxDestiny = boxIndex -1;
        }
        return actualBox;
    }

    @Override
    public boolean isSelectingMode() {
        return selecttingMode;
    }

    @Override
    public void setSelectingMode(boolean selectingMode) {
        this.selecttingMode = selectingMode;
    }

    /**
     *  Get the actual box of the human.
     * @return actual box of the human.
     */
    public int getActualBox() {
        return actualBox;
    }

    /**
     *  Get the actual life of the human.
     * @return actual life of the human.
     */
    public int getActualLife() {
        return actualLife;
    }

    /**
     * Set the actual life of the human.
     * @param actualLife
     */
    public void setActualLife(int actualLife) {
        this.actualLife = actualLife;
        if(actualLife <= 0){
            humanState = HumanState.DEAD;
        }
    }

    /**
     * Do the human action in depends of the HumanType. The human action is received by a GameObject
     * @param gameObject GameObject that receives the human action.
     */
    public void doAction(GameObject gameObject){
        switch (humanType){
            case CONSTRUCTOR:
                if(gameObject.getClass().equals(Building.class)){
                    if( ((Building)gameObject).getActualLife() != 100) {
                        switch (((Building) gameObject).getBuildingType()){
                            case MAIN:
                                if(drawResourcesBar.getActualStone() >= REPAIR && drawResourcesBar.getActualWood() >= REPAIR){
                                    ((Building)gameObject).setActualLife(((Building)gameObject).getActualLife()+REPAIR);
                                }
                                break;
                            case TOWER:
                                if( drawResourcesBar.getActualWood() >= REPAIR){
                                    ((Building)gameObject).setActualLife(((Building)gameObject).getActualLife()+REPAIR);
                                }
                                break;
                        }
                    }else{
                        flagActionEnd = true;
                    }
                }else{
                    int indexX = boxes[actualBox].getIndexX();
                    int indexY = boxes[actualBox].getIndexY();
                    if(boxes[getBoxByIndex(indexX-1,indexY)].getGameObject() == null){
                        boxes[getBoxByIndex(indexX-1,indexY)].
                                setDrawObjectTypeAndSubtype(DrawObjectType.NATURE,DrawObjectSubtype.TOWER,
                                        new Building(boxes,0,getBoxByIndex(indexX-1,indexY),context, BuildingType.TOWER,
                                                drawActionsBar,drawResourcesBar));
                    }else if(boxes[getBoxByIndex(indexX+1,indexY)].getGameObject() == null){
                        boxes[getBoxByIndex(indexX+1,indexY)].
                                setDrawObjectTypeAndSubtype(DrawObjectType.NATURE,DrawObjectSubtype.TOWER,
                                        new Building(boxes,0,getBoxByIndex(indexX+1,indexY),context, BuildingType.TOWER,
                                                drawActionsBar,drawResourcesBar));
                    }else if(boxes[getBoxByIndex(indexX,indexY-1)].getGameObject() == null){
                        boxes[getBoxByIndex(indexX,indexY-1)].
                                setDrawObjectTypeAndSubtype(DrawObjectType.NATURE,DrawObjectSubtype.TOWER,
                                        new Building(boxes,0,getBoxByIndex(indexX,indexY-1),context, BuildingType.TOWER,
                                                drawActionsBar,drawResourcesBar));
                    }else if(boxes[getBoxByIndex(indexX,indexY+1)].getGameObject() == null){
                        boxes[getBoxByIndex(indexX-1,indexY+1)].
                                setDrawObjectTypeAndSubtype(DrawObjectType.NATURE,DrawObjectSubtype.TOWER,
                                        new Building(boxes,0,getBoxByIndex(indexX,indexY+1),context, BuildingType.TOWER,
                                                drawActionsBar,drawResourcesBar));
                    }
                }
                break;

            case VILLAGER:
                if(gameObject.getClass().equals(Nature.class)){
                    if( ((Nature)gameObject).getActualResources() > 0) {
                        ((Nature) gameObject).setActualResources(((Nature) gameObject).getActualResources() - COLLECT);
                        switch (((Nature) gameObject).getNatureType()){
                            case FOOD:
                                drawResourcesBar.setActualFood(drawResourcesBar.getActualFood()+COLLECT);
                                break;

                            case ROCK:
                                drawResourcesBar.setActualStone(drawResourcesBar.getActualStone()+COLLECT);
                                break;

                            case WOOD:
                                drawResourcesBar.setActualWood(drawResourcesBar.getActualWood()+COLLECT);
                                break;
                        }
                    }else{
                        flagActionEnd = true;
                    }
                }
                break;

            case SOLDIER:
                if(gameObject.getClass().equals(Enemy.class)){
                    ((Enemy) gameObject).setActualLife(((Enemy) gameObject).getActualLife() + ATACK);
                    if(((Enemy) gameObject).getActualLife() <= 0) {
                        flagActionEnd = true;
                    }
                }
                break;
        }
    }
}