package de.dynamic_core.pong;

import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
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
import org.andengine.util.color.Color;

/**
 * Created by florian on 19.05.14.
 */
public class GameMenu implements IGameManager, IOnSceneTouchListener {
    BitmapTextureAtlas bitmapTextureAtlas;
    TextureRegion mBallTextureRegion;
    Font mFont, mSmallFont;

    GamePlay activity;

    Sprite mBall;
    int WIDTH, HEIGHT;

    public GameMenu(int WIDTH, int HEIGHT) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
    }

    @Override
    public Scene generateSceneAndObjects(BaseGameActivity pBaseGameActivity) {
        activity = (GamePlay) pBaseGameActivity;

        pBaseGameActivity.getEngine().registerUpdateHandler(new FPSLogger());

        Scene pScene = new Scene();

        mBall = new Sprite(0, 0, mBallTextureRegion, pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        mBall.setPosition(WIDTH / 2, HEIGHT / 2);
        pScene.attachChild(mBall);

        DemoBallCollider ballCollider = new DemoBallCollider(this);
        mBall.registerEntityModifier(ballCollider);

        final FPSCounter fpsCounter = new FPSCounter();
        pBaseGameActivity.getEngine().registerUpdateHandler(fpsCounter);

        final Text gamename = new Text(40, HEIGHT/2-120, this.mFont, "PONG", 12, pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        pScene.attachChild(gamename);

        final Text startText = new Text(40, HEIGHT/2-30, this.mFont, "START GAME", 12, pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        pScene.attachChild(startText);

        final Text creditText = new Text(40, HEIGHT/2+40, this.mSmallFont, "created by Florian Sievers", 26, pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        pScene.attachChild(creditText);

        pScene.setOnSceneTouchListener(this);

        return pScene;
    }

    @Override
    public void loadResources(BaseGameActivity pBaseGameActivity) {
        // get the engine object from the base activity
        Engine engine = pBaseGameActivity.getEngine();

        // load textures into texture atlas and create regions for ball and player
        bitmapTextureAtlas = new BitmapTextureAtlas(pBaseGameActivity.getTextureManager(), 128, 128, TextureOptions.REPEATING_BILINEAR);
        BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
        mBallTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(bitmapTextureAtlas, pBaseGameActivity, "ball.png", 2, 2);
        engine.getTextureManager().loadTexture(bitmapTextureAtlas);

        // load font from assest and load it into the mFont member of this class
        FontFactory.setAssetBasePath("font/");
        final ITexture fontTexture = new BitmapTextureAtlas(pBaseGameActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mFont = FontFactory.createFromAsset(pBaseGameActivity.getFontManager(), fontTexture, pBaseGameActivity.getAssets(), "8-bit_wonder.ttf", 60, true, Color.GREEN.getABGRPackedInt());
        mFont.load();

        final ITexture smallFontTexture = new BitmapTextureAtlas(pBaseGameActivity.getTextureManager(), 512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
        mSmallFont = FontFactory.createFromAsset(pBaseGameActivity.getFontManager(), smallFontTexture, pBaseGameActivity.getAssets(), "8-bit_wonder.ttf", 20, true, Color.GREEN.getABGRPackedInt());
        mSmallFont.load();
    }

    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        float y = pSceneTouchEvent.getY();
        float x = pSceneTouchEvent.getX();

        activity.getEngine().setScene(activity.mGameManager.generateSceneAndObjects(activity));
        return true;
    }
}
