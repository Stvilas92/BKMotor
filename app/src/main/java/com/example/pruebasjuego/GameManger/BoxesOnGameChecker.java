package com.example.pruebasjuego.GameManger;

import android.graphics.Canvas;

import com.example.pruebasjuego.DrawObjects.GameObjects;

import java.util.ArrayList;

public class BoxesOnGameChecker {
    private Escenario escenario;
    private ArrayList<GameObjects>objectsToDraw;

    public BoxesOnGameChecker(Escenario escenario) {
        this.escenario = escenario;
        this.objectsToDraw = escenario.getObjectsToDraw();
    }

    public void draw(Canvas c,int actualInitBox, int screenBoxes){
        for (int i = 0; i < objectsToDraw.size(); i++) {
            objectsToDraw.get(i).drawObject(c,actualInitBox,screenBoxes);
        }
    }
}
