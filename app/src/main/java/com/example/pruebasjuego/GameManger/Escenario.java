package com.example.pruebasjuego.GameManger;

import android.content.Context;

import com.example.pruebasjuego.DrawObjects.DrawActions;
import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObjects;
import com.example.pruebasjuego.DrawObjects.Nature.Nature;
import com.example.pruebasjuego.DrawObjects.Nature.NatureType;
import com.example.pruebasjuego.DrawObjects.buildings.Building;
import com.example.pruebasjuego.DrawObjects.buildings.BuildingType;
import com.example.pruebasjuego.Screen.Box;
import com.example.pruebasjuego.Screen.PointIndex;

import java.util.ArrayList;
import java.util.Scanner;

public class Escenario {
    private static final int OBJECTS_TOTAL = 100;
    private static final int PLAYER_INIT_X = 20;
    private static final int PLAYER_INIT_Y = 20;
    private static final int MAIN_BUILDING_INIT_X = 25;
    private static final int MAIN_BUILDING_INIT_Y = 25;

    private int indexID = 0;

    private ArrayList<PointIndex>indexesOccuped;
    private ArrayList<Integer>objectsposition;
    private ArrayList<GameObjects>objectsToDraw;
    private Context context;
    private static Box[] boxes;
    private DrawActions drawActions;

    public Escenario(Context context, Box[] boxes, DrawActions drawActions) {
        this.objectsposition = new ArrayList<>();
        this.context = context;
        this.boxes = boxes;
        this.drawActions = drawActions;
        this.objectsToDraw = new ArrayList<>();
        this.indexesOccuped = new ArrayList<>();
    }

    public void setScenario(){

    }

    public void setDrawObjectOnBox(int boxIndex, DrawObjectType type, DrawObjectSubtype subtype) {
        boxes[boxIndex].setDrawObjectTypeAndSubtype(type,subtype,null);
    }

    public ArrayList<Integer> getEscenarioFromAssets(String fichero) {
        Scanner sc = null;

        try
        {
            sc = new Scanner(fichero);
            if(sc.hasNext()){
                String line = sc.nextLine();
                if(line.split(",").length>0){
                    String [] splited =line.split(",");
                    for(String position : splited){
                        objectsposition.add(Integer.parseInt(position));
                    }
                }else{
                    objectsposition.add(Integer.parseInt(sc.nextLine()));
                }
            }
        } finally {
            sc.close();
        }
        return null;
    }

    public void generateRandomScenario(){
        boolean flagContinue = true;

        while (indexesOccuped.size() < OBJECTS_TOTAL){
            int x = (int)(Math.random()*32);
            int y = (int)(Math.random()*32);
            int type = (int)(Math.random()*2);


            for (int i = 0; i < indexesOccuped.size(); i++) {
                if(indexesOccuped.get(i).getIndexX() == x && indexesOccuped.get(i).getIndexY() == y) {
                    flagContinue = false;
                }
                //Controla que rocas y árboles no se solapen
                if(indexesOccuped.get(i).getIndexY() == y-1){
                    type = 0;
                }else if(indexesOccuped.get(i).getIndexY() == y+1){
                    type = 1;
                }
            }

            if(flagContinue) {
                if (x < PLAYER_INIT_X || y < PLAYER_INIT_Y) {
                    if (type == 0 ) {
                        objectsToDraw.add(new Nature(boxes, indexID, getBoxByIndex(x, y), context, NatureType.TREE,drawActions));
                        indexID++;
                        indexesOccuped.add(new PointIndex(x,y));
                    } else {
                        objectsToDraw.add(new Nature(boxes, indexID, getBoxByIndex(x, y), context, NatureType.ROCK,drawActions));
                        indexID++;
                        indexesOccuped.add(new PointIndex(x,y));
                    }
                }
            }else{
                flagContinue = true;
            }
        }

        generateMainBuilding(MAIN_BUILDING_INIT_X,MAIN_BUILDING_INIT_Y);
    }

    public void generateMainBuilding(int initIndexX,int initIndexY){
            objectsToDraw.add(new Building(boxes,indexID,getBoxByIndex(initIndexX,initIndexY),context, BuildingType.MAIN,drawActions));
            indexID++;
            objectsToDraw.add(new Building(boxes,indexID,getBoxByIndex(23,23),context, BuildingType.TOWER,drawActions));
//        setDrawObjectOnBox(getBoxByIndex(29,29),DrawObjectType.BUILDING,DrawObjectSubtype.TOWER);
//        setDrawObjectOnBox(getBoxByIndex(29,30),DrawObjectType.BUILDING,DrawObjectSubtype.CATAPULT);
//        setDrawObjectOnBox(getBoxByIndex(28,28),DrawObjectType.BUILDING,DrawObjectSubtype.WALL);
    }

    public static int getBoxByIndex(int indexX,int indexY){
        for (int i = indexX*indexY; i < boxes.length; i++) {
            if(boxes[i].getIndexX() == indexX && boxes[i].getIndexY() == indexY){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<GameObjects> getObjectsToDraw() {
        return objectsToDraw;
    }

    public GameObjects getObjectByIndex(int id){
        for (int i = 0; i < objectsToDraw.size(); i++) {
            if(objectsToDraw.get(i).getObjectID() == id){
                return objectsToDraw.get(i);
            }
        }

        return null;
    }

    public Box[] getBoxes() {
        return boxes;
    }
}
