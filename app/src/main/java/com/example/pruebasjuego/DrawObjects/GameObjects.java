package com.example.pruebasjuego.DrawObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public interface GameObjects {
    public void drawObject(Canvas c,int x,int y);
    public int getObjectID();
    public Bitmap getBitmap();
    public boolean isSelected();
    public int getSizeX();
    public int getSizeY();
    public void setSelected(boolean selected);
}
