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
public class TutorialScreen extends BaseScreen {
    //Pantalla
    private Stage stage;
    private Image background;
    private Image jumper;
    private Image arrows;
    private World world;
    //Camara
    private OrthographicCamera orthographicCamera;
    //Texto
    private OrthographicCamera fontCamera;
    private BitmapFont touchText;
    private BitmapFont textRotation;
    public TutorialScreen(MainGame mainGame) {
        super(mainGame);
        this.world = new World(new Vector2(0,0),true);
        FitViewport fitViewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT);
        this.stage = new Stage(fitViewport);
        this.orthographicCamera = (OrthographicCamera) this.stage.getCamera();
        //Obtenemos las fuentes
        prepareFont();

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
        this.touchText.draw(this.stage.getBatch(),"\nHold The \nScreen For \nCharging \nThe Jump",2f, SCREEN_HEIGHT-1);
        this.touchText.draw(this.stage.getBatch(),"Rotate the \nGyroscope \n  for \nhorizontal \nmoving ",2.5f, SCREEN_HEIGHT/2+2);
        this.stage.getBatch().end();
        //Si el usuario toca la pantalla se pasará al juego
        if(isTouched()){
            mainGame.setScreen(new GameScreen(mainGame));
        }
    }
    @Override
    /**
     * Mostramos el fondo
     */
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
        //Obtenemos la imagen de muestra del Jumper
        this.jumper = new Image(mainGame.assetManager.getJumperImg());
        this.jumper.setPosition(WORLD_WIDTH/2-0.5f,4.5f);
        this.jumper.setSize(1f,1.15f);
        this.stage.addActor(jumper);
        //Obtenemos la imagen de las flechas
        this.arrows = new Image(mainGame.assetManager.getFlechas());
        this.arrows.setPosition(WORLD_WIDTH/2-0.5f,0.5f);
        this.arrows.setSize(1,1.15f);
        this.stage.addActor(arrows);
    }

    /**
     * Obtenemos la fuente
     */
    public void prepareFont(){
        //Obtenemos los dos tipos de fuente
        this.textRotation = this.mainGame.assetManager.getFont();
        this.textRotation.getData().scale(1.3f);
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
        boolean jumpNextScreen = Gdx.input.isTouched();
        return jumpNextScreen;

    }

    /**
     * Metodo para hacer dispose del mundo y escenario
     */
    public void dispose() {
        this.world.dispose();
        this.stage.dispose();
    }
}
