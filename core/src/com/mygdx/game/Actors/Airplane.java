package com.mygdx.game.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.MyGdxGame;

public class Airplane extends Actor {

    public Airplane(float x, float y, Texture texture, MyGdxGame game) {
        super(x, y, texture, game);
    }

    @Override
    void execute() {
        // Direcionar para esquerda e direita
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            sprite.setX(sprite.getX() - 10);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            sprite.setX(sprite.getX() + 10);
        }

        // Direcionar para cima e baixo
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            sprite.setY(sprite.getY() + 10);
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            sprite.setY(sprite.getY() - 10);
        }

        // Caso tecle espaço ou clique/toque tela, dispara o TIRO
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) || Gdx.input.justTouched()) {
            game.shoot(
                    sprite.getX() + (sprite.getWidth() + 1),
                    sprite.getY() + (sprite.getHeight() / 2 ));
        }

        // acelerômtro
        if (game.accel) {
            sprite.setX(sprite.getX() + Gdx.input.getAccelerometerY() * 5);
        }

        for (Actor m : game.actors) {
            // caso colidir com ator diferente dele mesmo
            if (m != this && collide(m)) {
                game.end = true; // encerra o jogo
                m.dead = true; // define o ator como morto
                explode(); // executa explosão
            }
        }
    }
}
