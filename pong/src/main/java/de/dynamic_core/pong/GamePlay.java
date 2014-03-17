package de.dynamic_core.pong;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.SimpleBaseGameActivity;

/**
 * Created by florian on 10.02.14.
 */
public class GamePlay extends SimpleBaseGameActivity {
    Camera mCamera;
    final int WIDTH = 800;
    final int HEIGHT = 480;

    IGameManager mGameManager;

    @Override
    protected void onCreateResources() {
        mGameManager = new GameManager(WIDTH, HEIGHT);
        mGameManager.loadResources(this);
    }

    @Override
    protected Scene onCreateScene() {
        return mGameManager.generateSceneAndObjects(this);
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        mCamera = new Camera(0, 0, WIDTH, HEIGHT);
        EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(), mCamera);
        engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

        return engineOptions;
    }

}