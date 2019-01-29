
package com.mygdx.game.Background;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ParallaxBackground extends Actor {

    private int scroll;
    private Array<Texture> layers;
    private final int LAYER_SPEED_DIFFERENCE = 2;

    float x, y, width, heigth, scaleX, scaleY;
    int originX, originY, rotation, srcX, srcY;
    boolean flipX, flipY;

    private int speed;

//    public ParallaxBackground(int eitaW, int eitaH, int velocidade) {
//        this.setSize(eitaW, eitaH);
//        this.setSpeed(velocidade);

    public ParallaxBackground() {
        this.initTextures();

        for (int i = 0; i < layers.size; i++) {
            layers.get(i).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }
        scroll = 0;
        speed = 0;

        x = y = originX = originY = rotation = srcY = 0;
        width = Gdx.graphics.getWidth();
        heigth = Gdx.graphics.getHeight();
        scaleX = scaleY = 1;
        flipX = flipY = false;

        System.out.print(scaleX);
    }

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);

        scroll += speed;
        for (int i = 0; i < layers.size; i++) {
            srcX = scroll + i * this.LAYER_SPEED_DIFFERENCE * scroll;
            batch.draw(layers.get(i), x, y, originX, originY, width, heigth, scaleX, scaleY, rotation, srcX, srcY, layers.get(i).getWidth(), layers.get(i).getHeight(), flipX, flipY);
        }
    }

    private void initTextures(){
        Array<Texture> textures = new Array<Texture>();

        for(int i = 1; i <=6;i++){
            textures.add(new Texture(Gdx.files.internal("parallax/img"+i+".png")));
            textures.get(textures.size-1).setWrap(Texture.TextureWrap.MirroredRepeat, Texture.TextureWrap.MirroredRepeat);
        }

        this.layers = textures;
    }


    public void run() {
        srcX -= speed;
//        bx -= speed;
//        if (ax <= -texture.getWidth()) {
//            ax = bx + texture.getWidth();
//        }
//        if (bx <= -texture.getWidth()) {
//            bx = ax + texture.getWidth();
//        }
    }
}
