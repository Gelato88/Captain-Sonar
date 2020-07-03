package com.mygdx.sonar_client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class RadioOperator extends Role {

    Texture board;
    Button markers[][];

    public RadioOperator(SpriteBatch batch) {
        super(batch);
        board = new Texture(Gdx.files.internal("map.png"));
        markers = new Button[15][15];
        for(int i = 0; i < markers.length; i++) {
            for(int j = 0; j < markers[i].length; j++) {
                markers[i][j] = new Button(Assets.buttonSkin, "marker");
                markers[i][j].setSize(16, 16);
                markers[i][j].setPosition(32*j + 68+583, 32*i + 56+84);
                stage.addActor(markers[i][j]);
            }
        }
    }

    public void update() {
        render();
    }

    public void render() {

        batch.begin();
        batch.enableBlending();
        batch.draw(board, 583, 84, Settings.BOARD_FOCUS_WIDTH, Settings.BOARD_FOCUS_HEIGHT);
        batch.end();

        batch.begin();
        batch.enableBlending();
        stage.act();
        stage.draw();
        batch.end();

    }

    private void clearAll() {
        for(int i = 0; i < markers.length; i++) {
            for(int j = 0; j < markers[i].length; j++) {
                markers[i][j].setChecked(false);
            }
        }
    }

}
