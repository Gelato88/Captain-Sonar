package com.mygdx.sonar_client;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Role {

    SpriteBatch batch;
    Stage stage;

    public Role() {
        stage = new Stage();
    }

    public Role(SpriteBatch batch) {
        stage = new Stage();
        this.batch = batch;
    }

    public Stage getStage() {
        return stage;
    }

    public abstract void update();

    public abstract void render();


}
