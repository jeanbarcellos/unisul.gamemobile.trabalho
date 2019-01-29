package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;

public class Bomb extends Actor {
    public Bomb(float x, float y, Texture texture, MyGdxGame game) {
        super(x, y, texture, game);
        game.sndBomb.play();
    }

    @Override
    void execute() {
        sprite.setX(sprite.getX() + 5);
        dead = sprite.getY() > game.w;
        for (Actor m : game.actors) {
            if (m != this && collide(m)) {
                dead = true;
                m.explode();
            }
        }
    }
}