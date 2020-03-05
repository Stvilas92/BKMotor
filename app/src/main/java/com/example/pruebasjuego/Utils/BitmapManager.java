package com.example.pruebasjuego.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.example.pruebasjuego.DrawObjects.buildings.BuildingType;
import com.example.pruebasjuego.DrawObjects.humans.HumanOrientation;
import com.example.pruebasjuego.DrawObjects.humans.HumanType;
import com.example.pruebasjuego.DrawObjects.nature.NatureType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class BitmapManager {
    private static final int SIZE_WALKING_VILLAGER = 8;
    private static final int SIZE_WALKING_CONSTRUCTOR = 8;
    private static final int SIZE_WALKING_SOLDIER = 12;
    private static final int SIZE_DEAD_VILLAGER = 4;
    private static final int SIZE_DEAD_CONSTRUCTOR = 4;
    private static final int SIZE_DEAD_SOLDIER = 13;
    private static final int SIZE_ACTION_VILLAGER = 7;
    private static final int SIZE_ACTION_CONSTRUCTOR = 8;
    private static final int SIZE_ACTION_SOLDIER = 12;

    private Bitmap bitmapSurface;
    private Bitmap natureRock,natureWood,natureFood;
    private Bitmap natureTypeRock,natureTypeWood,natureTypeFood;
    private Bitmap buildMain,buildTower;
    private Bitmap bitmapSoldier,bitmapConstructor,bitmapVillager;
    private Bitmap bitmapExit,bitmapActionSword,bitmapActionHandWhite,bitmapActionHandYellow;
    private HashMap<HumanOrientation,Bitmap[]> soldierAction,soldierWalking,soldierDead;
    private HashMap<HumanOrientation,Bitmap[]>  villagerAction,villagerWalking,villagerDead;
    private HashMap<HumanOrientation,Bitmap[]>  constructorAction,constructorWalking,constructorDead;
    private int sizeBoxX,sizeBoxY;
    private Context context;

    public BitmapManager(int boxSizeX,int boxSizeY,Context context){
        this.context = context;
        this.sizeBoxX = boxSizeX;
        this.sizeBoxY = boxSizeY;
        getAllBitmapts();
    }

    public static Bitmap getOnlySurface(int sizeBoxX,int sizeBoxY,Context context){
        Bitmap bitmapSurface = getBitmapFromAssets("surface.png",context);
        bitmapSurface = Bitmap.createScaledBitmap(bitmapSurface, sizeBoxX, sizeBoxY,false);
        return  bitmapSurface;
    }

    public static Bitmap scaleByHeight(Bitmap res, int newHeight) {
        if (newHeight==res.getHeight()) return res;
        return res.createScaledBitmap(res, (res.getWidth() * newHeight) /
                res.getHeight(), newHeight, true);
    }

    public static Bitmap scale(Bitmap res,int newWidth,int newHeight){
        return res.createScaledBitmap(res,newWidth, newHeight,true);
    }

    public static Bitmap getBitmapFromAssets(String fichero, Context context) {
        try
        {
            InputStream is= context.getAssets().open(fichero);
            return BitmapFactory.decodeStream(is);
        }
        catch (IOException e) {
            return null;
        }
    }

    private void getAllBitmapts(){
        getNatureBitmaps(NatureType.ROCK);
        getNatureBitmaps(NatureType.WOOD);
        getNatureBitmaps(NatureType.FOOD);
        getBuildBitmaps(BuildingType.MAIN);
        getBuildBitmaps(BuildingType.TOWER);
        getUnitsBitmaps(HumanType.VILLAGER);
        getUnitsBitmaps(HumanType.CONSTRUCTOR);
        getUnitsBitmaps(HumanType.SOLDIER);
    }

    private void getNatureBitmaps(NatureType natureType){
        switch (natureType) {
            case ROCK:
                this.natureRock = getBitmapFromAssets("Nature/rock.png",context);
                this.natureRock = scaleByHeight(this.natureRock,sizeBoxY );
                this.natureTypeRock =  getBitmapFromAssets("Resources/stone.png",context);
                this.natureTypeRock = scaleByHeight(this.natureTypeRock, sizeBoxY);
                break;

            case FOOD:
                this.natureFood = getBitmapFromAssets("Nature/food.png",context);
                this.natureFood = scaleByHeight(this.natureFood,sizeBoxY );
                this.natureTypeFood =  getBitmapFromAssets("Resources/food.png",context);
                this.natureTypeFood = scaleByHeight(this.natureTypeFood, sizeBoxY);
                break;

            case WOOD:
                this.natureWood = getBitmapFromAssets("Nature/tree.png",context);
                this.natureWood = scaleByHeight(this.natureWood,sizeBoxY );
                this.natureTypeWood =  getBitmapFromAssets("Resources/wood.png",context);
                this.natureTypeWood = scaleByHeight(this.natureTypeWood, sizeBoxY);
                break;
        }
    }

    private void getBuildBitmaps(BuildingType buildingType){
        switch (buildingType) {
            case MAIN:
                //mejorar
                this.buildMain = getBitmapFromAssets("Buildings/main.png",context);
                this.buildMain = scaleByHeight(this.buildMain,sizeBoxY * 2 );
                break;

            case TOWER:
                this.buildTower = getBitmapFromAssets("Buildings/tower.png",context);
                this.buildTower = scaleByHeight(this.buildTower,sizeBoxY );
                break;
        }
    }

    public void getUnitsBitmaps(HumanType humanType){
        switch (humanType) {
            case SOLDIER:
                this.bitmapSoldier = BitmapManager.getBitmapFromAssets("Units/Soldier/Walking/stopped0000.png",context);
                this.bitmapSoldier = BitmapManager.scaleByHeight(this.bitmapSoldier, sizeBoxY);
                getUnitMovementBitmap(humanType);
                break;

            case VILLAGER:
                this.bitmapVillager = BitmapManager.getBitmapFromAssets("Units/Villager/Walking/stopped0000.png",context);
                this.bitmapVillager = BitmapManager.scaleByHeight(this.bitmapVillager, sizeBoxY);
                getUnitMovementBitmap(humanType);
                break;

            case CONSTRUCTOR:
                this.bitmapConstructor = BitmapManager.getBitmapFromAssets("Units/Constructor/Walking/stopped0000.png",context);
                this.bitmapConstructor = BitmapManager.scaleByHeight(this.bitmapConstructor, sizeBoxY);
                getUnitMovementBitmap(humanType);
                break;

            case ENEMY:
                this.bitmapSoldier = BitmapManager.getBitmapFromAssets("Units/Soldier/Walking/stopped0000.png",context);
                this.bitmapSoldier = BitmapManager.scaleByHeight(this.bitmapSoldier, sizeBoxY);
                getUnitMovementBitmap(humanType);
                break;
        }
        this.bitmapExit = BitmapManager.getBitmapFromAssets("BarIcons/red_boxCross.png",context);
        this.bitmapExit = BitmapManager.scaleByHeight(this.bitmapExit, sizeBoxY);
        this.bitmapActionSword = BitmapManager.getBitmapFromAssets("BarIcons/sword.png",context);
        this.bitmapActionSword = BitmapManager.scaleByHeight(this.bitmapActionSword, sizeBoxY);
        this.bitmapActionHandWhite = BitmapManager.getBitmapFromAssets("BarIcons/hand.png",context);
        this.bitmapActionHandWhite = BitmapManager.scaleByHeight(this.bitmapActionHandWhite, sizeBoxY);
        this.bitmapActionHandYellow = BitmapManager.getBitmapFromAssets("BarIcons/hand.png",context);
        this.bitmapActionHandYellow = BitmapManager.scaleByHeight(this.bitmapActionHandYellow, sizeBoxY);
    }

    /**
     * Set a DrawObjectType and Subtype on the actualBox depending on the humanType.
     * In addition create all the bitmaps needed to draw a human in all states.
     * @param humanType HumanType using to set DrawObjectType and Subtype in the box
     */
    private void  getUnitMovementBitmap(HumanType humanType){
        String unitPath = "";
        String unitAction = "";
        int sizeWalking= 0,sizeAction = 0, sizeDead = 0;
        HashMap<HumanOrientation,Bitmap[]> humanWalking;
        HashMap<HumanOrientation,Bitmap[]> humanAction;
        HashMap<HumanOrientation,Bitmap[]> humanDead;

        switch (humanType) {
            case VILLAGER:
                unitPath = "Villager";
                unitAction = "laydown";
                sizeWalking = SIZE_WALKING_VILLAGER;
                sizeAction = SIZE_ACTION_VILLAGER;
                sizeDead = SIZE_DEAD_VILLAGER;
                break;

            case CONSTRUCTOR:
                unitPath = "Constructor";
                unitAction = "nailing endless";
                sizeWalking = SIZE_WALKING_CONSTRUCTOR;
                sizeAction = SIZE_ACTION_CONSTRUCTOR;
                sizeDead = SIZE_DEAD_CONSTRUCTOR;
                break;

            case SOLDIER:
                unitPath = "Soldier";
                unitAction = "attack";
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
                if(j > 9){
                    bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/" + unitPath + "/Walking/walking " + HumanOrientation.values()[i].toString().substring(0, 1).toLowerCase() + "00" + j + ".png", context);
                }else {
                    bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/" + unitPath + "/Walking/walking " + HumanOrientation.values()[i].toString().substring(0, 1).toLowerCase() + "000" + j + ".png", context);
                }
                bitmapAux[j] = BitmapManager.scaleByHeight(bitmapAux[j], sizeBoxY);
            }
            humanWalking.put(HumanOrientation.values()[i],bitmapAux);
        }

        for (int i = 0; i < HumanOrientation.values().length ; i++) {
            Bitmap[] bitmapAux = new Bitmap[sizeAction];
            for (int j = 0; j < sizeAction; j++) {
                if(j > 9){
                    bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/"+unitPath+"/Action/"+unitAction+" "+HumanOrientation.values()[i].toString().substring(0,1).toLowerCase()+"00"+j+".png",context);
                }else {
                    bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/"+unitPath+"/Action/"+unitAction+" "+HumanOrientation.values()[i].toString().substring(0,1).toLowerCase()+"000"+j+".png",context);
                }
                bitmapAux[j] = BitmapManager.scaleByHeight(bitmapAux[j], sizeBoxY);
            }
            humanAction.put(HumanOrientation.values()[i],bitmapAux);
        }

        if ( humanType == HumanType.SOLDIER) {
            for (int i = 0; i < HumanOrientation.values().length; i++) {
                Bitmap[] bitmapAux = new Bitmap[sizeDead];
                for (int j = 0; j < sizeDead; j++) {
                    if(j > 9){
                        bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/" + unitPath + "/Dead/tipping over " + HumanOrientation.values()[i].toString().substring(0, 1).toLowerCase() + "00" + j + ".png", context);
                    }else {
                        bitmapAux[j] = BitmapManager.getBitmapFromAssets("Units/" + unitPath + "/Dead/tipping over " + HumanOrientation.values()[i].toString().substring(0, 1).toLowerCase() + "000" + j + ".png", context);
                    }bitmapAux[j] = BitmapManager.scaleByHeight(bitmapAux[j], sizeBoxY);
                }
                humanDead.put(HumanOrientation.values()[i], bitmapAux);
            }
        }

        switch (humanType) {
            case VILLAGER:
                villagerAction = humanAction;
                villagerWalking = humanWalking;
                break;

            case CONSTRUCTOR:
                constructorAction = humanAction;
                constructorWalking = humanWalking;
                break;

            case SOLDIER:
                soldierAction = humanAction;
                soldierWalking = humanWalking;
                soldierDead = humanDead;
                break;
        }
    }

    public Bitmap getNatureRock() {
        return natureRock;
    }

    public Bitmap getNatureWood() {
        return natureWood;
    }

    public Bitmap getNatureFood() {
        return natureFood;
    }

    public Bitmap getNatureTypeRock() {
        return natureTypeRock;
    }

    public Bitmap getNatureTypeWood() {
        return natureTypeWood;
    }

    public Bitmap getNatureTypeFood() {
        return natureTypeFood;
    }

    public Bitmap getBuildMain() {
        return buildMain;
    }

    public Bitmap getBuildTower() {
        return buildTower;
    }

    public Bitmap getBitmapSoldier() {
        return bitmapSoldier;
    }

    public Bitmap getBitmapConstructor() {
        return bitmapConstructor;
    }

    public Bitmap getBitmapVillager() {
        return bitmapVillager;
    }

    public Bitmap getBitmapExit() {
        return bitmapExit;
    }

    public Bitmap getBitmapActionSword() {
        return bitmapActionSword;
    }

    public HashMap<HumanOrientation, Bitmap[]> getConstructorAction() {
        return constructorAction;
    }

    public HashMap<HumanOrientation, Bitmap[]> getConstructorWalking() {
        return constructorWalking;
    }

    public HashMap<HumanOrientation, Bitmap[]> getConstructorDead() {
        return constructorDead;
    }

    public HashMap<HumanOrientation, Bitmap[]> getSoldierAction() {
        return soldierAction;
    }

    public HashMap<HumanOrientation, Bitmap[]> getSoldierWalking() {
        return soldierWalking;
    }

    public HashMap<HumanOrientation, Bitmap[]> getSoldierDead() {
        return soldierDead;
    }

    public HashMap<HumanOrientation, Bitmap[]> getVillagerAction() {
        return villagerAction;
    }

    public HashMap<HumanOrientation, Bitmap[]> getVillagerWalking() {
        return villagerWalking;
    }

    public HashMap<HumanOrientation, Bitmap[]> getVillagerDead() {
        return villagerDead;
    }

    public Bitmap getBitmapSurface() {
        return bitmapSurface;
    }

    public Bitmap getBitmapActionHandWhite() {
        return bitmapActionHandWhite;
    }

    public Bitmap getBitmapActionHandYellow() {
        return bitmapActionHandYellow;
    }
}
