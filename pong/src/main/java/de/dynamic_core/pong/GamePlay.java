package de.dynamic_core.pong;

import android.widget.Toast;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.input.touch.controller.MultiTouch;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by florian on 10.02.14.
 */
public class GamePlay extends SimpleBaseGameActivity {
    final int WIDTH = 800;
    final int HEIGHT = 480;
    Camera mCamera;
    IGameManager mGameManager, mGameMenu;

    @Override
    protected void onCreateResources() {
        mGameManager = new GameManager(WIDTH, HEIGHT);
        mGameManager.loadResources(this);

        mGameMenu = new GameMenu(WIDTH, HEIGHT);
        mGameMenu.loadResources(this);
    }

    @Override
    protected Scene onCreateScene() {
        //return mGameManager.generateSceneAndObjects(this);
        return mGameMenu.generateSceneAndObjects(this);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0, 0, WIDTH, HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
        engineOptions.getTouchOptions().setNeedsMultiTouch(true);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

        if (MultiTouch.isSupported(this)) {
            if (MultiTouch.isSupportedDistinct(this)) {
                //Toast.makeText(this, "MultiTouch detected; Both controls will work properly!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "MultiTouch detected, but your device has problems distinguishing between fingers.", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(Falling back to SingleTouch)", Toast.LENGTH_LONG).show();
        }

        return engineOptions;
    }
}