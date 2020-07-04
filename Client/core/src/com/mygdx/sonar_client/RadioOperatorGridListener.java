package com.mygdx.sonar_client;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class RadioOperatorGridListener extends ClickListener {

    private int i;
    private int j;

    public RadioOperatorGridListener(int i, int j) {
        super();
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

}
