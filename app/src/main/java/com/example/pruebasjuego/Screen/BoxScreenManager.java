package com.example.pruebasjuego.Screen;

import android.graphics.Bitmap;

import com.example.pruebasjuego.GameManger.Escenario;

public class BoxScreenManager {
    private int onScreenBoxes,initX = 0,initY = 0,boxInit;
    private Box[] boxesTotal,boxesToDraw;
    private Escenario escenario;

    public BoxScreenManager(int onScreenBoxes, Escenario escenario) {
        this.onScreenBoxes = onScreenBoxes;
        this.escenario = escenario;
        this.boxesTotal = this.escenario.getBoxes();
//        this.boxesToDraw = new Box[((onScreenBoxes+1)*(onScreenBoxes+1))];
        this.boxesToDraw = new Box[((onScreenBoxes)*(onScreenBoxes))];
        makeBoxesTodraw(0);
    }

    private void makeBoxesTodraw(int boxInit){
        int index = 0;
        for (int i = 0; i < onScreenBoxes; i++) {
            for (int j = 0; j < onScreenBoxes; j++) {
                if(index == 0){
                    boxesToDraw[0] = new Box(0,0,0,0,boxesTotal[0].getSizeX(),boxesTotal[0].getSizeY(),boxesTotal[0].getContext(),
                            null,null,true);
                }
                else if(i == onScreenBoxes || j == onScreenBoxes){
                    boxesToDraw[index] = new Box(i, j, initX + (boxesToDraw[0].getSizeX()*i), initY + (boxesToDraw[0].getSizeY()*j),
                            boxesTotal[0].getSizeX(), boxesTotal[0].getSizeY(), boxesTotal[0].getContext(), null, null,false);
                }
                else {
                        boxesToDraw[index] = new Box(i, j, initX + (boxesToDraw[0].getSizeX()*i), initY + (boxesToDraw[0].getSizeY()*j),
                        boxesTotal[0].getSizeX(), boxesTotal[0].getSizeY(), boxesTotal[0].getContext(), null, null,true);
                }
                index++;
            }
        }
    }

    public Box[] updateBoxesTodraw(int boxInit){
        int index = 0;
        int indexRead = 0;

        for (int i = 0; i < onScreenBoxes; i++) {
            for (int j = 0; j < onScreenBoxes; j++) {
                //en los edificios se le pasa la casilla donde empiezan a dibujar
                if(boxesTotal[boxInit+ indexRead].getDrawObjectType() != null && boxesTotal[boxInit+ indexRead].getDrawObjectSubtype() != null && boxesTotal[indexRead].isInteractable()) {
                    boxesToDraw[index].setDrawObjectTypeAndSubtype(boxesTotal[boxInit + indexRead].getDrawObjectType(), boxesTotal[boxInit + indexRead].getDrawObjectSubtype(),
                            boxesTotal[boxInit + indexRead].getGameObjects());
                }else{
                    boxesToDraw[index].setDrawObjectTypeAndSubtype(null,null,null);
                }
                boxesToDraw[index].xReference = (boxesTotal[boxInit+ indexRead].getIndexX());
                boxesToDraw[index].yReference = (boxesTotal[boxInit+ indexRead].getIndexY());
                boxesToDraw[index].xReferenceCoord = (boxesTotal[boxInit+ indexRead].getX());
                boxesToDraw[index].yReferenceCoord = (boxesTotal[boxInit+ indexRead].getIndexY());
                indexRead++;
                index++;
            }
            indexRead += onScreenBoxes;
        }

        return boxesToDraw;
    }

    public static void updateBoxesToDraw(){

    }

    public int getOnScreenBoxes() {
        return onScreenBoxes;
    }

    public void setOnScreenBoxes(int onScreenBoxes) {
        this.onScreenBoxes = onScreenBoxes;
    }

    public Box[] getBoxesTotal() {
        return boxesTotal;
    }

    public void setBoxesTotal(Box[] boxesTotal) {
        this.boxesTotal = boxesTotal;
    }

    public Box[] getBoxesToDraw() {
        return boxesToDraw;
    }

    public void setBoxesToDraw(Box[] boxesToDraw) {
        this.boxesToDraw = boxesToDraw;
    }
}
