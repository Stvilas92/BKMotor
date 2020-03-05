package com.example.pruebasjuego.Screen;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.buildings.Building;
import com.example.pruebasjuego.DrawObjects.buildings.BuildingType;
import com.example.pruebasjuego.DrawObjects.gameBars.DrawActionsBar;
import com.example.pruebasjuego.DrawObjects.gameBars.DrawResourcesBar;
import com.example.pruebasjuego.DrawObjects.humans.Human;
import com.example.pruebasjuego.DrawObjects.humans.HumanOrientation;
import com.example.pruebasjuego.DrawObjects.humans.HumanState;
import com.example.pruebasjuego.DrawObjects.humans.HumanType;
import com.example.pruebasjuego.GameManger.Escenario;
import com.example.pruebasjuego.Utils.BitmapManager;
import com.example.pruebasjuego.Utils.GameTools;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private static final int DIVIDER_MIN_SECONDS = 5000;
    private static final int DIVIDER_SECONDS_INIT = 60000;

    private ScaleGestureDetector mScaleGestureDetector;
    private SurfaceHolder surfaceHolder;
    private Context context;
    private GameThread gameThread;
    private boolean runnig = false;
    Paint p ;
    public static final int DIV = 32;
    public static final int SIZEGAME = 2;
    public static final int ONSCREENINIT = 16;
    private DrawActionsBar drawActionsBar;
    private DrawResourcesBar drawResourcesBar;
    private int indexSelectedX = 0;
    private int indexSelectedY = 0;
    private boolean flagInit = true,flagMove = false;
    private int w;
    private int h;
    private int historicalX = -1,historicalY = -1,actualX =-1,actualY = -1,totalX = 0,totalY = 0;
    private ScreenDivider screenDivider;
    private BoxScreenManager boxScreenManager;
    private Box[] boxes,boxesToDraw;
    private int boxInit,boxGameObjectSelected;
    private Escenario escenario;
    private BitmapManager bitmapManager;
    private long timeMilInit;
    private int enemiesTotal = 0,enemiesSecondsDivider = DIVIDER_SECONDS_INIT;


    public GameView(Context context,ScaleGestureDetector mScaleGestureDetector) {
        super(context);
        this.mScaleGestureDetector = mScaleGestureDetector;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.context = context;
        gameThread = new GameThread();
        setFocusable(true);
        timeMilInit = System.currentTimeMillis();
        p = new Paint();
        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.YELLOW);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.w = width;
        this.h = height;
        getInitBoxes();
        gameThread.setRunning(true);
        if (gameThread.getState() == Thread.State.NEW) gameThread.start();
        if (gameThread.getState() == Thread.State.TERMINATED) {
            gameThread = new GameThread();
            gameThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.setRunning(false);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);
//        synchronized (surfaceHolder) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    historicalX = (int) event.getX();
                    historicalY = (int) event.getY();
                    actualX = 0;
                    actualY = 0;

                case MotionEvent.ACTION_MOVE:


                    int moveX = 0, moveY = 0;
                    boolean up = false, left = false;
                    actualX = (int) event.getX();
                    actualY = (int) event.getY();
                    totalX += actualX - historicalX;
                    totalY += actualY - historicalY;


                    //Calculo de las casillas a mover
                    if (Math.abs(totalX) > boxes[0].getSizeX()) {
                        moveX = 1;
                        left = (actualX - historicalX) > 0;
                        totalX = 0;
                    }

                    if (Math.abs(totalY) > boxes[0].getSizeY()) {
                        moveY = 1;
                        up = (actualY - historicalY) > 0;
                        totalY = 0;
                    }

                    if (moveX > 0 || moveY > 0) {
                        moveScreen(moveX, moveY, up, left);
                    }

                    historicalX = (int) event.getX();
                    historicalY = (int) event.getY();


                    break;
                case MotionEvent.ACTION_UP:
                    historicalX = 0;
                    historicalY = 0;
                    int boxSelected = GameTools.getSelected(boxes);

                    if (event.getY() >= drawActionsBar.getInitY() && boxSelected >= 0) {
                        boxes[boxSelected].getGameObject().onTouchObject(false, (int) event.getX(), (int) event.getY(), boxSelected);
                    } else {
                        int boxTouchedIndex = getBoxBylocationForTouch((int) event.getX(), (int) event.getY());

                        if (boxTouchedIndex >= 0) {
                            Box box = boxes[boxTouchedIndex];
                            if (boxSelected > 0) {
                                if (boxes[boxSelected].getGameObject().isSelectingMode()) {
                                    boxes[boxSelected].getGameObject().onTouchObject(true, (int) event.getX(), (int) event.getY(), boxTouchedIndex);
                                } else {
                                    if (box.getGameObject() == null) {
                                        boxes[boxSelected].getGameObject().onTouchObject(false, (int) event.getX(), (int) event.getY(), boxTouchedIndex);
                                    } else {
                                        box.getGameObject().onTouchObject(false, (int) event.getX(), (int) event.getY(), boxSelected);
                                    }
                                }
                            } else if (box.getGameObject() != null) {
                                box.getGameObject().onTouchObject(false, (int) event.getX(), (int) event.getY(), boxSelected);
                            }
                        }
                    }
                    break;
            }
            return true;
//        }
    }

    public void drawGame(Canvas canvas){
        drawVisibleBoxes(canvas);
        drawResourcesBar.draw(canvas);

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void drawVisibleBoxes(Canvas c){
        if(((Building)escenario.getMainBuildingBox().getGameObject()).getActualLife() <= 0){
            runnig = false;
            return;
        }
        int indexBar = -1;
        //La superficie debe dibujarse primero
        for (int i = 0; i < boxScreenManager.getBoxesToDraw().length; i++) {
            boxesToDraw[i].drawFloor(c);
        }

        for (int i = 0; i < boxScreenManager.getBoxesToDraw().length; i++) {
            boxesToDraw[i].drawBox(c);

            if (boxesToDraw[i].getGameObject() != null) {
                if (boxesToDraw[i].getGameObject().isSelected()) {
                    c.drawRect(boxesToDraw[i].getX(), boxesToDraw[i].getY(), boxesToDraw[i].getFinalX(), boxesToDraw[i].getFinalY(),p);
                    indexBar = i;
                }
            }

//            boxesOnGameChecker.draw(c,boxInit,ONSCREENINIT);
//            pruebas
//            Paint p = new Paint();
//            p.setStrokeWidth(5);
//
//            p.setColor(Color.RED);
//            p.setStyle(Paint.Style.STROKE);
//            c.drawRect(boxesToDraw[i].getX(), boxesToDraw[i].getY(), boxesToDraw[i].getFinalX(), boxesToDraw[i].getFinalY(),p);
//
//            p.setColor(Color.YELLOW);
//            p.setTextSize(boxesToDraw[i].getSizeY()/2);
//            c.drawText(boxesToDraw[i].xReference+":"+boxesToDraw[i].yReference,
//                    boxesToDraw[i].getX(),boxesToDraw[i].getY()+boxesToDraw[i].getSizeY(),p);
        }

        if(indexBar > 0 &&  boxesToDraw[indexBar].getGameObject().isSelected()) {
            drawActionsBar.draw(c);
            boxesToDraw[indexBar].getGameObject().drawInActionBar(c);
        }
        boxesToDraw = boxScreenManager.updateBoxesTodraw(boxInit);

        if(enemiesTotal == 0){
            createEnemy();
            enemiesTotal++;
        }else if(System.currentTimeMillis() - timeMilInit % enemiesSecondsDivider == 0 && enemiesSecondsDivider > DIVIDER_MIN_SECONDS){
            createEnemy();
            enemiesTotal++;
            enemiesSecondsDivider = enemiesSecondsDivider/2;
        }
    }

    private void createEnemy(){
        boxes[0].setDrawObjectTypeAndSubtype(DrawObjectType.HUMAN, DrawObjectSubtype.ENEMY,
                new Human(boxes,0,0,context, HumanType.ENEMY,drawActionsBar, HumanOrientation.SOUTH,drawResourcesBar,bitmapManager));
        ((Human)boxes[0].getGameObject()).setHumanState(HumanState.ONACTION);
        ((Human)boxes[0].getGameObject()).setBoxDestiny(getMainBuildingIndex());
    }

    private int getMainBuildingIndex(){
        for (int i = 0; i < boxes.length; i++) {
            if(boxes[i].getGameObject() != null && boxes[i].getGameObject().getClass().equals(Building.class) && ((Building)boxes[i].getGameObject()).getBuildingType() == BuildingType.MAIN){
                return i;
            }
        }
        return -1;
    }

    private int getInitBox(){
        for (int i = 0; i < boxes.length; i++) {
            if(boxes[i].getX() >= 0 && boxes[i].getY()>=0){
                return i-DIV-1;
            }
        }
        return -1;
    }

    private void  getInitBoxes() {
        if(flagInit) {
            synchronized (surfaceHolder) {
                screenDivider = new ScreenDivider(w - (w * SIZEGAME), w, h - (h * SIZEGAME), h, DIV, context);
                boxes = screenDivider.getBoxes();
                boxInit = getInitBox();
                drawResourcesBar = new DrawResourcesBar(10,20,300,boxes[boxInit].getSizeY(),boxes[boxInit].getSizeX(),boxes[boxInit].getSizeY(),w,context);
                drawActionsBar = new DrawActionsBar(h-(boxes[boxInit].getSizeY()*2),boxes[boxInit].getSizeX(),boxes[boxInit].getSizeY(),w,h,getContext());
                this.bitmapManager = new BitmapManager(boxes[0].getSizeX(),boxes[0].getSizeY(),context);
                escenario = new Escenario(context, boxes, drawActionsBar,drawResourcesBar,bitmapManager);
                escenario.generateRandomScenario();
                escenario.getMainBuildingBox();
            }
            synchronized (surfaceHolder) {
                boxScreenManager = new BoxScreenManager(ONSCREENINIT, escenario);
                boxesToDraw = boxScreenManager.updateBoxesTodraw(boxInit);
                flagInit = false;
            }
        }
    }


    private int getBoxBylocationForTouch(int pointX, int pointY){
        for (int i = 0; i < boxesToDraw.length; i++) {
            if(pointX >= boxesToDraw[i].getX()  && pointX <= (boxesToDraw[i].getX() +boxesToDraw[i].getSizeX())&&
                    pointY >= boxesToDraw[i].getY()  && pointY <= (boxesToDraw[i].getY() +boxesToDraw[i].getSizeY())){

                Box box =boxes[GameTools.getBoxByIndex(boxes,boxesToDraw[i].getxReference(),boxesToDraw[i].getyReference())];

                if(indexSelectedX>=0 && indexSelectedY>=0 &&box.getGameObject() != null) {
                    Box boxDeleted = boxes[GameTools.getBoxByIndex(boxes,indexSelectedX,indexSelectedY)];

//                    if(boxDeleted.getGameObject() != null){
//                        boxDeleted.getGameObject().setSelected(false);
//                    }
                    indexSelectedX = box.getIndexX();
                    indexSelectedY = box.getIndexY();
                }

                for (int j = 0; j < boxes.length; j++) {
                    if(box == boxes[j]){
                        return j;
                    }
                }
            }
        }
        return -1;
    }

    private void moveScreen(int moveX,int moveY,boolean moveUp, boolean moveLeft){
        if(moveLeft){
            if(moveUp){
                validateIndexes(boxes[boxInit].getIndexX()-moveX,boxes[boxInit].getIndexY()-moveY);
            }else{
                validateIndexes(boxes[boxInit].getIndexX()-moveX,boxes[boxInit].getIndexY()+moveY);
            }
        }else{
            if(moveUp){
                validateIndexes(boxes[boxInit].getIndexX()+moveX,boxes[boxInit].getIndexY()-moveY);
            }else{
                validateIndexes(boxes[boxInit].getIndexX()+moveX,boxes[boxInit].getIndexY()+moveY);
            }
        }

        boxesToDraw = boxScreenManager.updateBoxesTodraw(boxInit);
    }

    private void  validateIndexes(int indexX,int indexY){
        if(indexX < 0){
            indexX = 0;
        }else if(indexX >= ONSCREENINIT){
            indexX = ONSCREENINIT;
        }

        if(indexY < 0){
            indexY = 0;
        }else if(indexY >= ONSCREENINIT){
            indexY = ONSCREENINIT;
        }

        boxInit = GameTools.getBoxByIndex(boxes,indexX,indexY);
    }


    class GameThread extends Thread {
        public GameThread() {
        }

        @Override
        public void run() {
            while (runnig) {
                Canvas c = null;
                try {
                    if (!surfaceHolder.getSurface().isValid())
                        continue;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        c = surfaceHolder.lockHardwareCanvas();
                    } else c = surfaceHolder.lockCanvas();
                    synchronized (surfaceHolder) {
                        drawGame(c);
                    }
                } finally {
                    if (c != null) {
                        surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        void setRunning(boolean flag) {
            runnig = flag;
        }
    }
}