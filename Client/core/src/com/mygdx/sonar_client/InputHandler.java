package com.mygdx.sonar_client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class InputHandler {

    private Client client;

    public InputHandler(Client client) {
        this.client = client;
    }

    public void update() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER) && client.field.hasKeyboardFocus()) {
            client.send();
        }
    }

}
