package com.example.pruebasjuego.Utils;

import com.example.pruebasjuego.Screen.Box;

public class GameTools {

    /**
     * Get a index of the box witch contains indexX and indexY on a array of boxes
     * @param boxes array of boxes
     * @param indexX index of X of the box
     * @param indexY index of Y of the box
     * @return box witch contains indexX and indexY
     */
    public static int getBoxByIndex(Box[] boxes,int indexX, int indexY){
        for (int i = indexX*indexY; i < boxes.length; i++) {
            if(boxes[i].getIndexX() == indexX && boxes[i].getIndexY() == indexY){
                return i;
            }
        }
        return -1;
    }

    /**
     * Put property of all gameObjects of a boxes array to false
     * @param boxes boxes array
     */
    public static void deselectedAll(Box[] boxes){
        for (int i = 0; i < boxes.length; i++) {
            if(boxes[i].getGameObject() != null  && boxes[i].getGameObject().isSelected()){
                boxes[i].getGameObject().setSelected(false);
            }
        }
    }


    public static int getSelected(Box[] boxes){
        for (int i = 0; i < boxes.length; i++) {
            if(boxes[i].getGameObject()!= null && boxes[i].getGameObject().isSelected()){
                return i;
            }
        }
        return -1;
    }
}
