package com.example.pruebasjuego.Screen;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.pruebasjuego.DrawObjects.DrawActions;
import com.example.pruebasjuego.DrawObjects.DrawResources;
import com.example.pruebasjuego.GameManger.Escenario;
import com.example.pruebasjuego.GameManger.BoxesOnGameChecker;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private ScaleGestureDetector mScaleGestureDetector;
    private SurfaceHolder surfaceHolder; // Interfaz abstracta para manejar la superficie de dibujado
    private Context context; // Contexto de la aplicación
    private GameThread gameThread; // GameThread encargado de dibujar y actualizar la física
    private boolean runnig = false; // Control del gameThread

    public static final int DIV = 32;
    public static final int SIZEGAME = 2;
    public static final int ONSCREENINIT = 16;
    private DrawActions drawActions;
    private DrawResources drawResources;
    private int indexSelectedX = 0;
    private int indexSelectedY = 0;
    private boolean flagInit = true,flagMove = false;
    private int w;
    private int h;
    private int historicalX = -1,historicalY = -1,actualX =-1,actualY = -1,totalX = 0,totalY = 0;
    private ScreenDivider screenDivider;
    private BoxScreenManager boxScreenManager;
    private BoxesOnGameChecker boxesOnGameChecker;
    private Box[] boxes,boxesToDraw;
    private int boxInit;
    private Escenario escenario;

    public GameView(Context context,ScaleGestureDetector mScaleGestureDetector) {
        super(context);
        this.mScaleGestureDetector = mScaleGestureDetector;
        this.surfaceHolder = getHolder();
        this.surfaceHolder.addCallback(this);
        this.context = context;
        gameThread = new GameThread();
        setFocusable(true);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.w = width;
        this.h = height;
        getInitBoxes();
        gameThread.setFuncionando(true);
        if (gameThread.getState() == Thread.State.NEW) gameThread.start();
        if (gameThread.getState() == Thread.State.TERMINATED) {
            gameThread = new GameThread();
            gameThread.start();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        gameThread.setFuncionando(false);
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mScaleGestureDetector.onTouchEvent(event);

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                historicalX = (int)event.getX();
                historicalY = (int)event.getY();
                actualX = 0;
                actualY = 0;

            case MotionEvent.ACTION_MOVE:
                synchronized (surfaceHolder) {


                    int moveX = 0,moveY = 0;
                    boolean up = false,left= false;
                    actualX = (int)event.getX();
                    actualY = (int)event.getY();
                    totalX += actualX-historicalX;
                    totalY += actualY-historicalY;


                    //Calculo de las casillas a mover
                    if (Math.abs(totalX)>boxes[0].getSizeX()){
                        moveX = 1;
                        left = (actualX-historicalX) > 0;
                        totalX=0;
                    }

                    if (Math.abs(totalY)>boxes[0].getSizeY()){
                        moveY = 1;
                        up = (actualY-historicalY) > 0;
                        totalY=0;
                    }

                    if(moveX > 0 || moveY > 0){
                        moveScreen(moveX,moveY,up,left);
                    }

                    historicalX = (int)event.getX();
                    historicalY = (int)event.getY();

                }
                break;
            case MotionEvent.ACTION_UP:
                historicalX = 0;
                historicalY = 0;
                checkSelected((int)event.getX(),(int)event.getY());
                break;
        }
        return true;
    }

    public void checkSelected(int x, int y){
        Box box = getBoxBylocationForTouch(x,y);
        if(box != null) {
            if(box.getGameObjects() != null) {
                box.getGameObjects().setSelected(!box.getGameObjects().isSelected());
            }
        }
    }


    public void drawGame(Canvas canvas){
        drawVisibleBoxes(canvas);
        drawResources.draw(canvas);

        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void drawVisibleBoxes(Canvas c){
        int indexBar = -1;
        //La superficie debe dibujarse primero
        for (int i = 0; i < boxScreenManager.getBoxesToDraw().length; i++) {
            boxesToDraw[i].drawFloor(c);
        }

        for (int i = 0; i < boxScreenManager.getBoxesToDraw().length; i++) {
            boxesToDraw[i].drawBox(c);
            if (boxesToDraw[i].getGameObjects() != null) {
                if (boxesToDraw[i].getGameObjects().isSelected()) {
                    indexBar = i;
                }
            }

//            boxesOnGameChecker.draw(c,boxInit,ONSCREENINIT);

            //pruebas
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

        if(indexBar > 0) {
            drawActions.draw(c);
            boxesToDraw[indexBar].getGameObjects().drawInActionBar(c);
        }
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
                drawResources = new DrawResources(10,20,30,boxes[boxInit].getSizeY(),boxes[boxInit].getSizeX(),boxes[boxInit].getSizeY(),w,context);
                drawActions = new DrawActions(0,0,0,h-(boxes[boxInit].getSizeY()*2),boxes[boxInit].getSizeX(),boxes[boxInit].getSizeY(),w,h);
                escenario = new Escenario(context, boxes,drawActions);
                escenario.generateRandomScenario();
                boxesOnGameChecker = new BoxesOnGameChecker(escenario);
            }
            synchronized (surfaceHolder) {
                boxScreenManager = new BoxScreenManager(ONSCREENINIT, escenario);
                boxesToDraw = boxScreenManager.updateBoxesTodraw(boxInit);
                flagInit = false;
            }
        }
    }

    private int getBoxByIndex(int indexX,int indexY){
        for (int i = indexX*indexY; i < boxes.length; i++) {
            if(boxes[i].getIndexX() == indexX && boxes[i].getIndexY() == indexY){
                return i;
            }
        }
        return -1;
    }

    private Box getBoxBylocationForTouch(int pointX, int pointY){
        for (int i = 0; i < boxesToDraw.length; i++) {
            if(pointX >= boxesToDraw[i].getX()  && pointX <= (boxesToDraw[i].getX() +boxesToDraw[i].getSizeX())&&
                    pointY >= boxesToDraw[i].getY()  && pointY <= (boxesToDraw[i].getY() +boxesToDraw[i].getSizeY())){

                Box box =boxes[getBoxByIndex(boxesToDraw[i].getxReference(),boxesToDraw[i].getyReference())];

                if(indexSelectedX>=0 && indexSelectedY>=0 &&box.getGameObjects() != null) {
                    Box boxDeleted = boxes[getBoxByIndex(indexSelectedX,indexSelectedY)];

                    if(boxDeleted.getGameObjects() != null){
                        boxDeleted.getGameObjects().setSelected(false);
                    }
                    indexSelectedX = box.getIndexX();
                    indexSelectedY = box.getIndexY();
                }

                return box;
            }
        }
        return null;
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

        boxInit = getBoxByIndex(indexX,indexY);
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

        void setFuncionando(boolean flag) {
            runnig = flag;
        }

    }

}