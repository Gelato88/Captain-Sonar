package com.mygdx.sonar_client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Assets {

    public static Skin buttonSkin;
    public static Skin textFieldSkin;

    public static Texture board;
    public static Texture mine;


    public static void load() {
        buttonSkin = new Skin(Gdx.files.internal("buttons/buttons.json"));
        textFieldSkin = new Skin(Gdx.files.internal("chat_field/chat_field.json"));

        board = new Texture(Gdx.files.internal("map.png"));
        mine = new Texture(Gdx.files.internal("mine.png"));
    }


}
