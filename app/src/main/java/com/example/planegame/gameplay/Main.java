package com.example.planegame.gameplay;

import android.content.Context;

import com.badlogic.gdx.Game;

public class Main extends Game {

    GameScreen gameScreen;
    Context context;
    Main(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void create() {
        gameScreen = new GameScreen(this.context);
        setScreen(gameScreen);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void resize(int width, int height) {
        gameScreen.resize(width, height);
    }

    @Override
    public void dispose() {
        gameScreen.dispose();
    }
}
