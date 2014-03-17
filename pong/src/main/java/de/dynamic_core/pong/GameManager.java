package de.dynamic_core.pong;

import android.opengl.GLES20;
import org.andengine.engine.Engine;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.particle.ParticleSystem;
import org.andengine.entity.particle.SpriteParticleSystem;
import org.andengine.entity.particle.emitter.BaseCircleParticleEmitter;
import org.andengine.entity.particle.emitter.CircleParticleEmitter;
import org.andengine.entity.particle.initializer.AlphaParticleInitializer;
import org.andengine.entity.particle.initializer.BlendFunctionParticleInitializer;
import org.andengine.entity.particle.initializer.VelocityParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
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
 * Created by florian on 13.02.14.
 */
public class GameManager implements IGameManager, IOnSceneTouchListener {
    BitmapTextureAtlas bitmapTextureAtlas;
    TextureRegion mBallTextureRegion;
    TextureRegion mPlayerTextureRegion;
    Font mFont;

    Sprite mBall;
    Sprite mPlayer;
    int WIDTH, HEIGHT;

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

        mPlayer = new Sprite(0, 0, mPlayerTextureRegion, pBaseGameActivity.getEngine().getVertexBufferObjectManager());
        mPlayer.setPosition(WIDTH - mPlayer.getWidth() - 150, HEIGHT / 2 - mPlayer.getHeight() / 2);
        mPlayer.setScaleY(1.5f);

        pScene.attachChild(mPlayer);

        BallCollider ballCollider = new BallCollider();
        mBall.registerEntityModifier(ballCollider);

        final FPSCounter fpsCounter = new FPSCounter();
        pBaseGameActivity.getEngine().registerUpdateHandler(fpsCounter);

        final Text fpsText = new Text(10, 10, this.mFont, "FPS:", 12, pBaseGameActivity.getEngine().getVertexBufferObjectManager());

        pScene.registerUpdateHandler(new TimerHandler(1 / 20.0f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                fpsText.setText("FPS: " + String.format("%3.3f", fpsCounter.getFPS()));
            }
        }));
        pScene.attachChild(fpsText);

        SpriteParticleSystem part = new SpriteParticleSystem(new CircleParticleEmitter(WIDTH/2,HEIGHT/2, 50), 5, 10, 5, mBallTextureRegion, pBaseGameActivity.getVertexBufferObjectManager());
        //part.addParticleInitializer(new AlphaParticleInitializer<Sprite>(0));
        part.addParticleInitializer(new BlendFunctionParticleInitializer<Sprite>(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE));
        part.addParticleInitializer(new VelocityParticleInitializer<Sprite>(-10, 10, -20, -10));
        part.addParticleInitializer(new ExpireParticleInitializer<Sprite>(6));
        part.addParticleModifier(new AlphaParticleModifier<Sprite>(5, 6, 1, 0));
        pScene.attachChild(part);
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
        if (y < 0) {
            y = 0;
        }
        if (y > HEIGHT - mPlayer.getHeight()) {
            y = HEIGHT - mPlayer.getHeight();
        }
        mPlayer.setY(y);
        return true;
    }

    private class BallCollider implements IEntityModifier {
        float speed = 5f;
        public float vy = speed;
        public float vx = speed;

        @Override
        public void reset() {
        }

        @Override
        public boolean isFinished() {
            return false;
        }

        @Override
        public boolean isAutoUnregisterWhenFinished() {
            return false;
        }

        @Override
        public void setAutoUnregisterWhenFinished(boolean pRemoveWhenFinished) {
        }

        @Override
        public IEntityModifier deepCopy() throws DeepCopyNotSupportedException {
            return null;
        }

        @Override
        public float getSecondsElapsed() {
            return 0;
        }

        @Override
        public float getDuration() {
            return 0;
        }

        @Override
        public float onUpdate(float pSecondsElapsed, IEntity pItem) {
            float x = pItem.getX();
            float y = pItem.getY();

            if (x <= 0) {
                vx = +speed;
            } else if (x >= WIDTH - mBall.getWidth()) {
                vx = -speed;
            }

            if (y <= 0) {
                vy = speed;
            } else if (y >= HEIGHT - mBall.getHeight()) {
                vy = -speed;
            }

            if (mBall.collidesWith(mPlayer)) {
                vx *= -1;
                //vy *= -1;
                speed = speed + .05f;
                float player_y = mPlayer.getY() + mPlayer.getHeight() / 2;
                float ball_y = mBall.getY() + mBall.getHeight() / 2;
                vy = speed + Math.abs(player_y - ball_y) / 10;
                if ((player_y - ball_y) > 0f) {
                    vy *= -1;
                }
            }

            pItem.setPosition(x + vx, y + vy);

            return 0;
        }

        @Override
        public void addModifierListener(IModifierListener<IEntity> pModifierListener) {
        }

        @Override
        public boolean removeModifierListener(IModifierListener<IEntity> pModifierListener) {
            return false;
        }
    }
}
