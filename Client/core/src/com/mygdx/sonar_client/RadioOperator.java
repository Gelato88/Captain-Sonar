package com.mygdx.sonar_client;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RadioOperator extends Role {

    private Button markers[][];
    private boolean mineMarkers[][];

    private Button up;
    private Button down;
    private Button left;
    private Button right;
    private Button clear;
    private Button toggleMine;

    private boolean mineMode;

    public RadioOperator(SpriteBatch batch) {
        super(batch);
        mineMode = false;
        markers = new Button[Settings.BOARD_GRID_LENGTH][Settings.BOARD_GRID_LENGTH];
        for(int i = 0; i < markers.length; i++) {
            for(int j = 0; j < markers[i].length; j++) {
                markers[i][j] = new Button(Assets.buttonSkin, "marker");
                markers[i][j].setSize(16, 16);
                markers[i][j].setPosition(32*j + 68+583, 32*i + 56+84);
                markers[i][j].addListener(new RadioOperatorGridListener(i, j) {
                    @Override
                    public void clicked(InputEvent e, float x, float y) {
                        if(mineMode) {
                            int i = getI();
                            int j = getJ();
                            mineMarkers[i][j] = !mineMarkers[i][j];
                            markers[i][j].setChecked(!markers[i][j].isChecked());
                        }
                    }
                });
                stage.addActor(markers[i][j]);
            }
        }
        mineMarkers = new boolean[Settings.BOARD_GRID_LENGTH][Settings.BOARD_GRID_LENGTH];
        for(int i = 0; i < mineMarkers.length; i++) {
            for(int j = 0; j < mineMarkers[i].length; j++) {
                mineMarkers[i][j] = false;
            }
        }

        up = new Button(Assets.buttonSkin, "rad_op_up");
        down = new Button(Assets.buttonSkin, "rad_op_down");
        left = new Button(Assets.buttonSkin, "rad_op_left");
        right = new Button(Assets.buttonSkin, "rad_op_right");
        clear = new Button(Assets.buttonSkin, "rad_op_clear");
        toggleMine = new Button(Assets.buttonSkin, "mine");

        up.setSize(40, 40);
        down.setSize(40, 40);
        left.setSize(40, 40);
        right.setSize(40, 40);
        clear.setSize(40, 40);
        toggleMine.setSize(40, 40);

        up.setPosition(488, 389);
        down.setPosition(488, 299);
        left.setPosition(443, 344);
        right.setPosition(533, 344);
        clear.setPosition(488, 344);
        toggleMine.setPosition(488, 434);

        up.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                moveUp();
            }
        });
        down.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                moveDown();
            }
        });
        left.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                moveLeft();
            }
        });
        right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                moveRight();
            }
        });
        clear.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                clearAll();
            }
        });
        toggleMine.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                toggleMineMode();
            }
        });

        stage.addActor(up);
        stage.addActor(down);
        stage.addActor(left);
        stage.addActor(right);
        stage.addActor(clear);
        stage.addActor(toggleMine);
    }

    public void update() {
        render();
    }

    public void render() {

        batch.begin();
        batch.enableBlending();
        batch.draw(Assets.board, 583, 84, Settings.BOARD_FOCUS_WIDTH, Settings.BOARD_FOCUS_HEIGHT);

        for(int i = 0; i < mineMarkers.length; i++) {
            for(int j = 0; j < mineMarkers[i].length; j++) {
                if(mineMarkers[i][j]) {
                    batch.draw(Assets.mine, 32*j + 64+583, 32*i + 52+85, 24, 24);
                }
            }
        }

        batch.end();

        batch.begin();
        batch.enableBlending();
        stage.act();
        stage.draw();
        batch.end();

    }

    /* Moves all markings up once.
     * Only moves up to edge.
     */
    private void moveUp() {
        for(int j = 0; j < Settings.BOARD_GRID_LENGTH; j++) {
            if(markers[Settings.BOARD_GRID_LENGTH-1][j].isChecked()) {
                return;
            }
        }
        for(int i = markers.length-1; i > 0; i--) {
            for(int j = 0; j < markers[i].length; j++) {
                if(markers[i-1][j].isChecked()) {
                    markers[i][j].setChecked(true);
                    markers[i-1][j].setChecked(false);
                }
            }
        }
    }

    /* Moves all markings down once.
     * Only moves up to edge.
     */
    private void moveDown() {
        for(int j = 0; j < Settings.BOARD_GRID_LENGTH; j++) {
            if(markers[0][j].isChecked()) {
                return;
            }
        }
        for(int i = 0; i < markers.length-1; i++) {
            for(int j = 0; j < markers[i].length; j++) {
                if(markers[i+1][j].isChecked()) {
                    markers[i][j].setChecked(true);
                    markers[i+1][j].setChecked(false);
                }
            }
        }
    }

    /* Moves all markings left once.
     * Only moves up to edge.
     */
    private void moveLeft() {
        for(int i = 0; i < Settings.BOARD_GRID_LENGTH; i++) {
            if(markers[i][0].isChecked()) {
                return;
            }
        }
        for(int i = 0; i < markers.length; i++) {
            for(int j = 0; j < markers[i].length-1; j++) {
                if(markers[i][j+1].isChecked()) {
                    markers[i][j].setChecked(true);
                    markers[i][j+1].setChecked(false);
                }
            }
        }
    }

    /* Moves all markings right once.
     * Only moves up to edge.
     */
    private void moveRight() {
        for(int i = 0; i < Settings.BOARD_GRID_LENGTH; i++) {
            if(markers[i][Settings.BOARD_GRID_LENGTH-1].isChecked()) {
                return;
            }
        }
        for(int i = 0; i < markers.length; i++) {
            for(int j = markers[i].length-1; j > 0; j--) {
                if(markers[i][j-1].isChecked()) {
                    markers[i][j].setChecked(true);
                    markers[i][j-1].setChecked(false);
                }
            }
        }
    }

    /* Unchecks all markings.
     *
     */
    private void clearAll() {
        for(int i = 0; i < markers.length; i++) {
            for(int j = 0; j < markers[i].length; j++) {
                markers[i][j].setChecked(false);
            }
        }
    }

    private void toggleMineMode() {
        mineMode = !mineMode;
    }

}
