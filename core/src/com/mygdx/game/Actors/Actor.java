package com.mygdx.game.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Intersector;
import com.mygdx.game.MyGdxGame;

public class Actor {
    public MyGdxGame game;
    public Sprite sprite;

    public boolean dead = false; // Morto ???

    private boolean dying = false; // Morrendo ???

    private int explosion = 4;
    private int explosionCount = 0;

    /**
     *
     * @param x Eixo X
     * @param y Eixo Y
     * @param texture Imagem
     * @param game Game
     */
    Actor(float x, float y, Texture texture, MyGdxGame game) {
        this.game = game;
        this.sprite = new Sprite(texture);
        this.sprite.setPosition(x, y);
    }

    // metodos de execusão
    void execute() {
    }

    public void run() {
        if (dead) return; // se morrer
        if (exploding()) return; // se explodir

        execute();

        // limita o ator a atuar somente na tela
        // esquerda
        if (sprite.getX() < 0) {
            sprite.setX(0);
        }
//        // direita
//        if (sprite.getX() + sprite.getWidth() > game.w) {
//            sprite.setX(game.w - sprite.getWidth());
//        }

        if (sprite.getY() < (0 + 150)) {
            sprite.setY(0 + 150);
        }

        if (sprite.getY() + sprite.getHeight() > game.h) {
            sprite.setY(game.h - sprite.getHeight());
        }

    }

    // desenhar
    public void draw() {
        if (dead) return;
        if (exploding()) {
            game.batch.draw(game.explosion[explosion],
                    sprite.getX() + (sprite.getWidth() / 2) - (game.bomb.getWidth() / 2),
                    sprite.getY() + (sprite.getWidth() / 2) - (game.bomb.getHeight() / 2));
            return;
        }
        sprite.draw(game.batch);
    }

    private boolean exploding() {
        if (!dying) return false;
        explosionCount++;
        if (explosionCount > 10) {
            explosionCount = 0;
            explosion--;
            if (explosion <= 0) dead = true;
        }
        return true;
    }

    void explode() {
        game.sndExplosion.play();
        dying = true;
    }

    //boolean collide(Actor b) {
    //    return spriteCollide(this.sprite, b.sprite)
    //            || spriteCollide(b.sprite, this.sprite);
    //}

    boolean collide(Actor other) {
        return Intersector.overlaps(
                sprite.getBoundingRectangle(),
                other.sprite.getBoundingRectangle());
    }


    private boolean spriteCollide(Sprite a, Sprite b) {
        float x = a.getX();
        float y = a.getY();
        float w = a.getWidth();
        float h = a.getHeight();
        return pointCollide(x, y, b) ||
                pointCollide(x + w, y, b) ||
                pointCollide(x, y + h, b) ||
                pointCollide(x + w, y + h, b);
    }

    private boolean pointCollide(float px, float py, Sprite s) {
        float x = s.getX();
        float y = s.getY();
        float w = s.getWidth();
        float h = s.getHeight();
        return (px > x && px < x + w && py > y && py < y + h);
    }
}