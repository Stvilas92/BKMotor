package com.example.pruebasjuego.GameManger;

import android.content.Context;

import com.example.pruebasjuego.DrawObjects.gameBars.DrawActionsBar;
import com.example.pruebasjuego.DrawObjects.DrawObjectSubtype;
import com.example.pruebasjuego.DrawObjects.DrawObjectType;
import com.example.pruebasjuego.DrawObjects.GameObject;
import com.example.pruebasjuego.DrawObjects.gameBars.DrawResourcesBar;
import com.example.pruebasjuego.DrawObjects.nature.Nature;
import com.example.pruebasjuego.DrawObjects.nature.NatureType;
import com.example.pruebasjuego.DrawObjects.buildings.Building;
import com.example.pruebasjuego.DrawObjects.buildings.BuildingType;
import com.example.pruebasjuego.Screen.Box;
import com.example.pruebasjuego.Screen.PointIndex;
import com.example.pruebasjuego.Utils.BitmapManager;
import com.example.pruebasjuego.Utils.GameTools;

import java.util.ArrayList;
import java.util.Scanner;

public class Escenario {
    private static final int OBJECTS_TOTAL = 50;
    private static final int PLAYER_INIT_X = 20;
    private static final int PLAYER_INIT_Y = 20;
    private static final int MAIN_BUILDING_INIT_X = 25;
    private static final int MAIN_BUILDING_INIT_Y = 25;

    private int indexID = 0;
    private ArrayList<PointIndex>indexesOccuped;
    private ArrayList<Integer>objectsposition;
    private ArrayList<GameObject>objectsToDraw;
    private Context context;
    private static Box[] boxes;
    private DrawActionsBar drawActionsBar;
    private DrawResourcesBar drawResourcesBar;
    private Box mainBuildingBox;
    private BitmapManager bitmapManager;

    public Escenario(Context context, Box[] boxes, DrawActionsBar drawActionsBar, DrawResourcesBar drawResourcesBar,BitmapManager bitmapManager) {
        this.bitmapManager = bitmapManager;
        this.objectsposition = new ArrayList<>();
        this.context = context;
        this.boxes = boxes;
        this.drawActionsBar = drawActionsBar;
        this.objectsToDraw = new ArrayList<>();
        this.indexesOccuped = new ArrayList<>();
        this.drawResourcesBar = drawResourcesBar;
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
            int type = (int)(Math.random()*3);


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
                        objectsToDraw.add(new Nature(boxes, indexID, GameTools.getBoxByIndex(boxes,x, y), context, NatureType.WOOD, drawActionsBar,bitmapManager));
                        indexID++;
                        indexesOccuped.add(new PointIndex(x,y));
                    } else if(type == 1) {
                        objectsToDraw.add(new Nature(boxes, indexID, GameTools.getBoxByIndex(boxes,x, y), context, NatureType.ROCK, drawActionsBar,bitmapManager));
                        indexID++;
                        indexesOccuped.add(new PointIndex(x,y));
                    }else {
                        objectsToDraw.add(new Nature(boxes, indexID,GameTools.getBoxByIndex(boxes,x, y), context, NatureType.FOOD, drawActionsBar,bitmapManager));
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
        mainBuildingBox = boxes[GameTools.getBoxByIndex(boxes,initIndexX,initIndexY)];
        objectsToDraw.add(new Building(boxes,indexID,GameTools.getBoxByIndex(boxes,initIndexX,initIndexY),context, BuildingType.MAIN, drawActionsBar,drawResourcesBar,bitmapManager));
        indexID++;
        objectsToDraw.add(new Building(boxes,indexID,GameTools.getBoxByIndex(boxes,23,23),context, BuildingType.TOWER, drawActionsBar,drawResourcesBar,bitmapManager));
    }



    public ArrayList<GameObject> getObjectsToDraw() {
        return objectsToDraw;
    }

    public GameObject getObjectByIndex(int id){
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

    public Box getMainBuildingBox() {
        return mainBuildingBox;
    }

    public BitmapManager getBitmapManager() {
        return bitmapManager;
    }
}
