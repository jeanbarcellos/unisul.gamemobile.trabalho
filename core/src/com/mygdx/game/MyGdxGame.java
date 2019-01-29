package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Actors.Actor;
import com.mygdx.game.Actors.Bomb;
import com.mygdx.game.Actors.Enemy;
import com.mygdx.game.Actors.Airplane;
import com.mygdx.game.Background.ParallaxBackground;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MyGdxGame extends ApplicationAdapter {
    public SpriteBatch batch;
    public int w, h; // larguras

    public boolean end = false; // Fim???
    public boolean accel; // acelerômetro

    public Sound sndBomb; // Som da bomba
    public Sound sndExplosion; // Som da explosão

    public Texture bomb;
    public Texture explosion[] = new Texture[5];

    public List<Actor> actors = new ArrayList<Actor>(); // Lista de atores ccrrentes
    private List<Actor> news = new ArrayList<Actor>(); // Novos atores

    private Texture airplane;

    private Texture enemy;
    private Texture gameover;
    private Texture gamepaused;

    private Music music; // música

    private boolean paused = false; // Pausar o Jogo?
    private boolean playMusic = true; // Executar a musica?

    private Random rand = new Random(); // Randomizador

    private int enemyCounter = 0; //
    private int enemyMaxCounter = 50;

    // Background paralax
    private Stage stage;
    private ParallaxBackground parallaxBackground;


    @Override
    public void create() {
        w = Gdx.graphics.getWidth(); // Largura da tela
        h = Gdx.graphics.getHeight(); // ALtura da tela

        batch = new SpriteBatch();

        // acelerômetro
        accel = Gdx.input.isPeripheralAvailable(Input.Peripheral.Accelerometer);

        // imagem voando
        airplane = new Texture("airplane.png");
        bomb = new Texture("bomb.png");
        enemy = new Texture("enemy.png");

        // imagens game over e pause
        gameover = new Texture("gameover.png");
        gamepaused = new Texture("paused.png");

        // Textura da explosão
        explosion[0] = new Texture("explosion_0.png");
        explosion[1] = new Texture("explosion_1.png");
        explosion[2] = new Texture("explosion_2.png");
        explosion[3] = explosion[1];
        explosion[4] = explosion[0];

        // Seta o som da bomba
        sndBomb = Gdx.audio.newSound(Gdx.files.internal("bomb.wav"));

        // Seta o som da explosão
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("explosion.wav"));

        // Seta a música
        music = Gdx.audio.newMusic(Gdx.files.internal("music2.ogg"));
        music.setLooping(true);
        music.play();

        // Seta a imagem de fundo - paralax
        stage = new Stage(new ScreenViewport());
        parallaxBackground = new ParallaxBackground();
        parallaxBackground.setSize(w, h);
        parallaxBackground.setSpeed(1);
        stage.addActor(parallaxBackground);

        // adiciona o navio na lista de atore
        actors.add(new Airplane(20, h / 2, airplane, this));
    }

    private void execute() {
        // Tecla ESC - Encerra o Jogo
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        // se o jogo acabar
        if (end) {
            // se teclar enter reinicia
            if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isTouched()) {
                end = false;
                actors.add(new Airplane(w / 2, 20, airplane, this));
            }
        } else {
            // Tecla P = Pausa o Jogo
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
                paused = !paused;
            }
            // Tecla M = Deixa mudo
            if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
                playMusic = !playMusic;
            }
        }

        // Executa a música
        if (playMusic) {
            // Se pausar ou acabar o jogo, pausa a musica
            if (paused || end) {
                if (music.isPlaying()) music.stop();
            } else {
                if (!music.isPlaying()) music.play();
            }
        } else {
            if (music.isPlaying()) music.stop();
        }

        // caso não estiver pausado, roda
        if (!paused) {
            // Executa o paralax fundo
            //background.run();

            for (Actor m : actors) {
                m.run();
            }

            // exibe os inimigos
            this.enemies();

            // atulizar
            this.refresh();
        }
    }

    @Override
    public void render() {
        execute();

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        // Início ----------------------------------------------------------------------------------
        batch.begin();

        // Rendeniza os atores
        for (Actor m : actors) {
            m.draw();
        }

        // Caso jogo terminar, imprime no meio da tela a mensagem GameOver
        if (end) {
            batch.draw(gameover, (w / 2) - (gameover.getWidth() / 2), h / 2);
        }

        // Caso jogo for pausado, imprime no meio da tela a mensagem Pause
        if (paused) {
            batch.draw(gamepaused, (w / 2) - (gamepaused.getWidth() / 2), h / 2);
        }

        batch.end();
        // Fim -------------------------------------------------------------------------------------
    }

    /**
     * Inimigos
     */
    private void enemies() {
        enemyCounter++;
        if (enemyCounter > enemyMaxCounter) {
            enemyMaxCounter = rand.nextInt(200) + 200;
            enemyCounter = 0;
            news.add(new Enemy(w, rand.nextInt(h - enemy.getHeight()), enemy, this));
            //news.add(new Enemy(rand.nextInt(w - enemy.getWidth()), h, enemy, this));
        }
    }

    /**
     * Tiro
     *
     * @param x
     * @param y
     */
    public void shoot(float x, float y) {
        news.add(new Bomb(x + (bomb.getWidth() / 2), y, bomb, this));
    }

    /**
     * Atualizando os quadros
     */
    private void refresh() {
        List<Actor> aux = actors; // bkp da lista de atores

        actors = new ArrayList<Actor>(); // esvazia a lista de adores

        // faz varredura pra eliminar os atores mortos
        for (Actor m : aux) {
            // se o ator não morreu mantem na lista
            if (!m.dead) {
                actors.add(m);
            }
        }

        actors.addAll(news); // adiciona os novos atores a lista com atores atuais

        news.clear(); // limpa a lista nova
    }

    @Override
    public void dispose() {
        batch.dispose();
        bomb.dispose();
        airplane.dispose();
        enemy.dispose();
        gameover.dispose();
        explosion[0].dispose();
        explosion[1].dispose();
        explosion[2].dispose();
        sndBomb.dispose();
        sndExplosion.dispose();
        music.dispose();

        stage.dispose();
    }
}