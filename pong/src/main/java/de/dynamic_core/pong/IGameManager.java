package de.dynamic_core.pong;

import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

/**
 * Created by florian on 13.02.14.
 */
public interface IGameManager {
    public void loadResources(BaseGameActivity pBaseGameActivity);

    public Scene generateSceneAndObjects(BaseGameActivity pBaseGameActivity);
}
