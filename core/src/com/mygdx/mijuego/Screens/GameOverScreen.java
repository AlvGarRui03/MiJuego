package com.mygdx.mijuego.Screens;


import static com.mygdx.mijuego.Extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.mijuego.Extra.Utils.SCREEN_WIDTH;
import static com.mygdx.mijuego.Extra.Utils.WORLD_HEIGHT;
import static com.mygdx.mijuego.Extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.mijuego.MainGame;

public class GameOverScreen extends BaseScreen {
    //Pantalla
    private Stage stage;
    private Image background;
    private World world;
    //Camara
    private OrthographicCamera orthographicCamera;
    //Texto
    private OrthographicCamera fontCamera;
    private BitmapFont touchText;
    private BitmapFont textGameOver;
    //Boleano para saber si se debe pasar de pantalla
    private boolean jumpToNextScreen;

    public GameOverScreen(MainGame mainGame) {
        super(mainGame);
        jumpToNextScreen =false;
        this.world = new World(new Vector2(0,0),true);
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT);
        this.stage = new Stage(fitViewport);
        this.orthographicCamera = (OrthographicCamera) this.stage.getCamera();
        //Obtenemos las fuentes
        prepareGameOverFont();
    }

    /**
     * Metodo que se comunica con la gráfica
     * @param delta
     */
    @Override
    public void render(float delta) {
        //Iniciamos el mundo
        this.world.step(delta,6,2);
        //Iniciamos los actores
        this.stage.act();
        //Los dibujamos
        this.stage.draw();
        //Situamos el texto
        this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        this.textGameOver.draw(this.stage.getBatch()," GAME OVER",2f, SCREEN_HEIGHT-SCREEN_HEIGHT/4);
        this.touchText.draw(this.stage.getBatch(),"  Touch \n    To  \nPlay Again",2.5f, SCREEN_HEIGHT/2);
        this.stage.getBatch().end();
        //Si el usuario toca la pantalla se pasará al juego
        if(isTouched()){
            mainGame.setScreen(new GameScreen(mainGame));
        }
    }

    /**
     *
     *Mostramos el fondo
     */
    @Override
    public void show() {
        addBackground();


    }

    /**
     * Metodo para crear el fondo
     */
    public void addBackground(){
        //Obtenemos la imagen del fondo y la colocamos
        this.background = new Image(mainGame.assetManager.getBackground());
        this.background.setPosition(0,0);
        this.background.setSize(WORLD_WIDTH,WORLD_HEIGHT);
        this.stage.addActor(this.background);
    }

    /**
     * Obtenemos las fuentes
     */
    public void prepareGameOverFont(){
        //Obtenemos los dos tipos de fuente
        this.textGameOver = this.mainGame.assetManager.getFont();
        this.textGameOver.getData().scale(1.3f);
        this.touchText = this.mainGame.assetManager.getGameOverFont();
        this.touchText.getData().scale(0.4f);
        //Inicializamos la cámara
        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH,SCREEN_HEIGHT);
        this.fontCamera.update();

    }

    /**
     * Metodo que detecta el toque en la pantalla
     * @return Devuelve un boleano true si la pantalla esta siendo tocada
     */
    public boolean isTouched(){
          jumpToNextScreen = Gdx.input.isTouched();
         return jumpToNextScreen;

    }

    /**
     * Metodo para hacer dispose del mundo y escenario
     */
    public void dispose() {
        this.world.dispose();
        this.stage.dispose();
    }
}
