package com.example.pruebasjuego.DrawObjects;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.pruebasjuego.Screen.ScreenDivider;

import java.io.IOException;
import java.io.InputStream;

public class Constructor {
    Bitmap[] walkingS;
    Context context;
    ScreenDivider sd;
    int indexDraw = 0;
    String state = "stopped";
    boolean selected = false;

    public Constructor(Context context,ScreenDivider sd){
        walkingS = new Bitmap[8];
        this.context = context;
        this.sd = sd;

//        for (int i = 0; i < walkingS.length; i++) {
//            walkingS[i] = Bitmap.createScaledBitmap(getBitmapFromAssets("Constructor/Walking/"+state+" s000"+i+".png"),sd.boxSizeX*2,sd.boxSizeY*2,false);
//        }
    }

    public void draw(Canvas canvas){
        if(selected){

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

    public void walk(Canvas canvas,int x,int y){
        if(indexDraw == 8){
            indexDraw = 0;
        }

        canvas.drawBitmap(walkingS[indexDraw],x,y,null);
        indexDraw++;
    }
}
