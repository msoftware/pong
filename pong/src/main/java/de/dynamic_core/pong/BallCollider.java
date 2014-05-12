package de.dynamic_core.pong;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.sprite.Sprite;

/**
 * Created by florian on 12.05.14.
 */
class BallCollider implements IEntityModifier {
    static float initialSpeed = 5f;
    float speed = initialSpeed;
    public float vy = speed;
    public float vx = speed;
    PlayerHit lastPlayerHit = PlayerHit.None;
    GameStats gameStats;
    private GameManager gameManager;

    BallCollider(GameManager gameManager, GameStats gameStats) {
        this.gameManager = gameManager;
        this.gameStats = gameStats;
    }

    @Override
    public void reset() {
        lastPlayerHit = PlayerHit.None;
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
            this.reset();
            this.gameStats.rightPlayerPoints += 1;
            return 0;
        } else if (x >= gameManager.WIDTH - gameManager.mBall.getWidth()) {
            vx = -speed;
            this.reset();
            this.gameStats.leftPlayerPoints += 1;
            return 0;
        }

        if (y <= 0) {
            vy = speed;
        } else if (y >= gameManager.HEIGHT - gameManager.mBall.getHeight()) {
            vy = -speed;
        }

        if (gameManager.mBall.collidesWith(gameManager.mPlayerRight) && lastPlayerHit != PlayerHit.playerRight) {
            // Set last player hit to patch multiple hit events on same player
            lastPlayerHit = PlayerHit.playerRight;
            this.changeBallDirection(gameManager.mBall, gameManager.mPlayerRight);
        }

        if (gameManager.mBall.collidesWith(gameManager.mPlayerLeft) && lastPlayerHit != PlayerHit.playerLeft) {
            // Set last player hit to patch multiple hit events on same player
            lastPlayerHit = PlayerHit.playerLeft;
            this.changeBallDirection(gameManager.mBall, gameManager.mPlayerLeft);
        }
        pItem.setPosition(x + vx, y + vy);

        return 0;
    }

    private void changeBallDirection(Sprite ball, Sprite player) {
        vx *= -1;
        //vy *= -1;
        speed = speed + .05f;
        float player_y = player.getY() + player.getHeight() / 2;
        float ball_y = ball.getY() + ball.getHeight() / 2;
        vy = speed + Math.abs(player_y - ball_y) / 10;
        if ((player_y - ball_y) > 0f) {
            vy *= -1;
        }
    }

    @Override
    public void addModifierListener(IModifierListener<IEntity> pModifierListener) {
    }

    @Override
    public boolean removeModifierListener(IModifierListener<IEntity> pModifierListener) {
        return false;
    }
}
