package com.example.pruebasjuego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ScaleGestureDetector;
import android.view.View;

import com.example.pruebasjuego.Screen.GameView;

public class MainActivity extends AppCompatActivity {
    private GameView game;
    private float mScaleFactor = 1.0f;
    private ScaleGestureDetector mScaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mScaleGestureDetector = new ScaleGestureDetector(this,new ScaleListener());

        int options = View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // Oculta la barra de navegación
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        game = new GameView(getApplicationContext(),this.mScaleGestureDetector);
        game.setSystemUiVisibility(options);
        game.setKeepScreenOn(true);
        setContentView(game);
    }

    class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector){
            //TODO revisar escalabilidad
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f,Math.min(mScaleFactor, 10.0f));
            game.setScaleX(mScaleFactor);
            game.setScaleY(mScaleFactor);
            return true;
        }
    }
}
