package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;

public class Enemy extends Actor {
    private static final int TOP = 0;
    private static final int BOTTOM = 1;
    private int direction = TOP;

    public Enemy(float x, float y, Texture texture, MyGdxGame game) {
        super(x, y, texture, game);
    }

    @Override
    void execute() {
        sprite.setX(sprite.getX() - 2);

//        if (direction == TOP) {
//            sprite.setX(sprite.getX() + 5);
//            if (sprite.getX() + sprite.getWidth() > game.w) {
//                direction = BOTTOM;
//            }
//        } else {
//            sprite.setX(sprite.getX() - 5);
//            if (sprite.getX() < 0) {
//                direction = TOP;
//            }
//        }

        dead = sprite.getX() < 0;
    }
}