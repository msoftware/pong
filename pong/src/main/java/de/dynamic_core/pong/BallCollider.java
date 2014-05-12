package de.dynamic_core.pong;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;

/**
* Created by florian on 12.05.14.
*/
class BallCollider implements IEntityModifier {
    private GameManager gameManager;
    float speed = 5f;
    public float vy = speed;
    public float vx = speed;
    PlayerHit lastPlayerHit = PlayerHit.None;
    GameStats gameStats;

    BallCollider(GameManager gameManager, GameStats gameStats) {
        this.gameManager = gameManager;
        this.gameStats = gameStats;
    }

    @Override
    public void reset() {
        lastPlayerHit = PlayerHit.None;
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
            this.gameStats.rightPlayerPoints += 1;
        } else if (x >= gameManager.WIDTH - gameManager.mBall.getWidth()) {
            vx = -speed;
            this.gameStats.leftPlayerPoints += 1;
        }

        if (y <= 0) {
            vy = speed;
        } else if (y >= gameManager.HEIGHT - gameManager.mBall.getHeight()) {
            vy = -speed;
        }

        if (gameManager.mBall.collidesWith(gameManager.mPlayerRight) && lastPlayerHit != PlayerHit.playerRight) {
            // Set last player hit to patch multiple hit events on same player
            lastPlayerHit = PlayerHit.playerRight;
            vx *= -1;
            //vy *= -1;
            speed = speed + .05f;
            float player_y = gameManager.mPlayerRight.getY() + gameManager.mPlayerRight.getHeight() / 2;
            float ball_y = gameManager.mBall.getY() + gameManager.mBall.getHeight() / 2;
            vy = speed + Math.abs(player_y - ball_y) / 10;
            if ((player_y - ball_y) > 0f) {
                vy *= -1;
            }
        }

        if (gameManager.mBall.collidesWith(gameManager.mPlayerLeft) && lastPlayerHit != PlayerHit.playerLeft) {
            // Set last player hit to patch multiple hit events on same player
            lastPlayerHit = PlayerHit.playerLeft;
            vx *= -1;
            //vy *= -1;
            speed = speed + .05f;
            float player_y = gameManager.mPlayerLeft.getY() + gameManager.mPlayerLeft.getHeight() / 2;
            float ball_y = gameManager.mBall.getY() + gameManager.mBall.getHeight() / 2;
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
