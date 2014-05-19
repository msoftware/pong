package de.dynamic_core.pong;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by florian on 12.05.14.
 */
class DemoBallCollider implements IEntityModifier {
    static float initialSpeed = 10f;
    float speed = initialSpeed;
    public float vy = speed;
    public float vx = speed;
    private GameMenu gameManager;

    DemoBallCollider(GameMenu gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public void reset() {
        speed = initialSpeed;
        gameManager.mBall.setPosition(gameManager.WIDTH / 2, gameManager.HEIGHT / 2);
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
        } else if (x >= gameManager.WIDTH - gameManager.mBall.getWidth()) {
            vx = -speed;
        }

        if (y <= 0) {
            vy = +speed;
        } else if (y >= gameManager.HEIGHT - gameManager.mBall.getHeight()) {
            vy = -speed;
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
