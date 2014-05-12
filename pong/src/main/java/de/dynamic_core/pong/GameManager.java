package de.dynamic_core.pong;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSCounter;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

/**
 * Created by florian on 13.02.14.
 */
public class GameManager implements IGameManager, IOnSceneTouchListener {
    BitmapTextureAtlas bitmapTextureAtlas;
    TextureRegion mBallTextureRegion;
    TextureRegion mPlayerTextureRegion;
    Font mFont;

    Sprite mBall;
    Sprite mPlayerLeft;
    Sprite mPlayerRight;
    int WIDTH, HEIGHT;

    GameStats gameStats = new GameStats();

    public GameManager(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    @Override
    public Scene generateSceneAndObjects(BaseGameActivity pBaseGameActivity) {
        pBaseGameActivity.getEngine().registerUpdateHandler(new FPSLogger());

        Scene pScene = new Scene();

        mBall = new Sprite(0, 0, mBallTextureRegion, pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        mBall.setPosition(40, 40);
        pScene.attachChild(mBall);

        mPlayerRight = new Sprite(0, 0, mPlayerTextureRegion, pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        mPlayerRight.setPosition(WIDTH - mPlayerRight.getWidth() - 100, HEIGHT / 2 - mPlayerRight.getHeight() / 2);
        mPlayerRight.setScaleY(1.5f);

        pScene.attachChild(mPlayerRight);

        mPlayerLeft = new Sprite(0, 0, mPlayerTextureRegion, pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        mPlayerLeft.setPosition(100, HEIGHT / 2 - mPlayerLeft.getHeight() / 2);
        mPlayerLeft.setScaleY(1.5f);

        pScene.attachChild(mPlayerLeft);

        BallCollider ballCollider = new BallCollider(this, this.gameStats);
        mBall.registerEntityModifier(ballCollider);

        final FPSCounter fpsCounter = new FPSCounter();
        pBaseGameActivity.getEngine().registerUpdateHandler(fpsCounter);

        final Text fpsText = new Text(10, 10, this.mFont, "FPS:", 12, pBaseGameActivity.getEngine().getVertexBufferObjectManager());

        final Text leftPlayerPoints = new Text(WIDTH / 2 - 30, 10, this.mFont, "   ", 3, new TextOptions(HorizontalAlign.RIGHT), pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        final Text rightPlayerPoints = new Text(WIDTH / 2 + 30, 10, this.mFont, "   ", 3, new TextOptions(HorizontalAlign.LEFT), pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        pScene.registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                leftPlayerPoints.setText(String.format("%3d", gameStats.leftPlayerPoints));
                rightPlayerPoints.setText(String.format("%3d", gameStats.rightPlayerPoints));
                fpsText.setText("FPS: " + String.format("%3.1f", fpsCounter.getFPS()));
            }
        }));
        pScene.attachChild(fpsText);
        pScene.attachChild(leftPlayerPoints);
        pScene.attachChild(rightPlayerPoints);

        pScene.setOnSceneTouchListener(this);

        return pScene;
    }

    @Override
    public void loadResources(BaseGameActivity pBaseGameActivity) {
        Engine engine = pBaseGameActivity.getEngine();

        bitmapTextureAtlas = new BitmapTextureAtlas(pBaseGameActivity.getTextureManager(), 128, 128, TextureOptions.REPEATING_BILINEAR);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        mBallTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, pBaseGameActivity, "ball.png", 2, 2);
        mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, pBaseGameActivity, "player.png", 44, 2);
        engine.getTextureManager().loadTexture(bitmapTextureAtlas);

        FontFactory.setAssetBasePath("font/");
        final ITexture fontTexture = new BitmapTextureAtlas(pBaseGameActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mFont = FontFactory.createFromAsset(pBaseGameActivity.getFontManager(), fontTexture, pBaseGameActivity.getAssets(), "8-bit_wonder.ttf", 20, true, Color.GREEN.getABGRPackedInt());
        mFont.load();
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        float y = pSceneTouchEvent.getY();
        float x = pSceneTouchEvent.getX();
        if (x > (this.WIDTH / 2)) {
            if (y < 0) {
                y = 0;
            }
            if (y > HEIGHT - mPlayerRight.getHeight()) {
                y = HEIGHT - mPlayerRight.getHeight();
            }
            mPlayerRight.setY(y);
        }

        if (x < (this.WIDTH / 2)) {
            if (y < 0) {
                y = 0;
            }
            if (y > HEIGHT - mPlayerLeft.getHeight()) {
                y = HEIGHT - mPlayerLeft.getHeight();
            }
            mPlayerLeft.setY(y);
        }
        return true;
    }

}
