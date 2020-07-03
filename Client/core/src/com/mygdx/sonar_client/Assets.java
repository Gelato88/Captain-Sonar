package com.mygdx.sonar_client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    public static Skin buttonSkin;

    public static void load() {
        buttonSkin = new Skin(Gdx.files.internal("buttons/buttons.json"));
    }


}
