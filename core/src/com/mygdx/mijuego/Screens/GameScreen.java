package com.mygdx.mijuego.Screens;

import static com.mygdx.mijuego.Extra.Utils.SCREEN_HEIGHT;
import static com.mygdx.mijuego.Extra.Utils.SCREEN_WIDTH;
import static com.mygdx.mijuego.Extra.Utils.USER_BORDER_LEFT;
import static com.mygdx.mijuego.Extra.Utils.USER_BORDER_RIGHT;
import static com.mygdx.mijuego.Extra.Utils.USER_BORDER_TOP;
import static com.mygdx.mijuego.Extra.Utils.WORLD_HEIGHT;
import static com.mygdx.mijuego.Extra.Utils.WORLD_WIDTH;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.mijuego.Actors.Jumper;
import com.mygdx.mijuego.Actors.Platform;
import com.mygdx.mijuego.Extra.AssetMngr;
import com.mygdx.mijuego.MainGame;

public class GameScreen extends BaseScreen{
    //Pantalla
    private Stage stage;
    private Jumper jumper;
    private Image background;
    private World world;
    //Depuracion
    private Box2DDebugRenderer debugRenderer;
    //Almacenamiento actores
    private Array<Platform> arrayPlatforms;
    //Camara
    private OrthographicCamera orthographicCamera;
    //Musicas
    private Music musicbg;
    private Music gameOverSound;
    //Cuerpos
    private Body bodyRight;
    private Body bodyLeft;
    private Body bodyTop;
    private Fixture fixtureRight;
    private Fixture fixtureLeft;
    private Fixture fixtureTop;
    //Puntuacion
    private int gameScore;
    private OrthographicCamera fontCamera;
    private BitmapFont score;

     public GameScreen(MainGame mainGame) {
        super(mainGame);
        //Inicializamos el mundo con la gravedad
        this.world = new World(new Vector2(0,-10), false);
        //Creamos el fitviewport con las medidas del mundo
         FitViewport fitViewport = new FitViewport(WORLD_WIDTH,WORLD_HEIGHT);
         this.stage = new Stage(fitViewport);
         //Inicializamos el array de plataformas
         this.arrayPlatforms = new Array();
         //Depuracion
         this.debugRenderer = new Box2DDebugRenderer();
         //Inicializamos la camara
         this.orthographicCamera = (OrthographicCamera) this.stage.getCamera();
         //Preparamos la puntuacion
         prepareScore();
         //Y el sonido
         prepareGameSound();
     }

    /**
     * Añadimos el fondo obteniendo la imagen del atlas
     */
    public void addBackground(){
        this.background = new Image(mainGame.assetManager.getBackground());
        this.background.setPosition(0,0);
        this.background.setSize(WORLD_WIDTH,WORLD_HEIGHT);
        this.stage.addActor(this.background);
    }

    /**
     * Añadimos el actor Jumper(Actor principal del juego)
     */
    public void addJumper(){
        Animation<TextureRegion> jumperSprite = mainGame.assetManager.getJumperAnimation();
        this.jumper = new Jumper(world,jumperSprite,new Vector2(2.4f,  4.6f));
        this.stage.addActor(this.jumper);
    }

    /**
     * Creamos las plataformas iniciales que serán siempre las mismas
     */
    public void addInitialPlatforms(){
        TextureRegion platform = mainGame.assetManager.getPlatform();
        //Creamos los actores
        Platform plataformaInicial = new Platform(this.world,platform,new Vector2(2.4f,4f));
        Platform plataforma1 = new Platform(this.world,platform,new Vector2(0.4f,6f));
        Platform plataforma2 = new Platform(this.world,platform,new Vector2(4f,8f));
        Platform plataforma3 = new Platform(this.world,platform,new Vector2(1f,10f));
        Platform plataforma4 = new Platform(this.world,platform,new Vector2(4.2f,12f));
        //Añadimos los actores al stage
        this.stage.addActor(plataformaInicial);
        this.stage.addActor(plataforma1);
        this.stage.addActor(plataforma2);
        this.stage.addActor(plataforma3);
        this.stage.addActor(plataforma4);
        //Añadimos las plataformas al Array
        arrayPlatforms.add(plataformaInicial);
        arrayPlatforms.add(plataforma1);
        arrayPlatforms.add(plataforma2);
        arrayPlatforms.add(plataforma3);
        arrayPlatforms.add(plataforma4);


    }

    /**
     * Metodo que se comunica con la tarjeta gráfica
     * @param delta variable de la gráfica
     */
    @Override
    public void render(float delta) {
         //Activamos la gravedad
         this.world.step(delta,6,2);
         //Iniciamos los actores
         this.stage.act();
         //Dibujamos los actores
         this.stage.draw();
        //Si la plataforma se encuentra fuera de la pantalla se eliminará y se creará otra arriba
         removePlatform();
        //Dibujamos la puntuacion
         this.stage.getBatch().setProjectionMatrix(this.fontCamera.combined);
        this.stage.getBatch().begin();
        this.score.draw(this.stage.getBatch(), ""+this.gameScore,SCREEN_WIDTH/2, 725);
        this.stage.getBatch().end();
        //Comprobamos que el jumper no esta fuera de pantalla
         gameOver();
         //Depuración
         //debugRenderer.render(this.world,orthographicCamera.combined);
    }

    /**
     * Metodo para que se ejecute cuando se enseñe la Screen
     */
    @Override
    public void show() {
        //Añadimos el fondo y el jumper
        addBackground();
        addJumper();
        //Activamos la música en bucle
        this.musicbg.setLooping(true);
        this.musicbg.play();
        //Añadimos las plataformas
        addInitialPlatforms();
        //Añadimos los bordes invisibles
        addBorders();


     }

    /**
     * Metodo para cuando se esconda la Screen
     */
    @Override
    public void hide() {
        //Eliminamos el jumper
        this.jumper.detach();
        this.jumper.remove();
        //Paramos la música
        this.musicbg.stop();
    }

    /**
     * Metodo para eliminar el stage
     */
    @Override
    public void dispose() {
        this.stage.dispose();
    }

    /**
     * Metodo para añadir una nueva plataforma
     */
    public void addPlatform(){
         TextureRegion trPlatform = mainGame.assetManager.getPlatform();
         //Su posicion será una aleatoria dentro del mundo
         float posRandomX = MathUtils.random(0f, 4.8f);
         //Creamos la plataforma
         Platform platform = new Platform(this.world,trPlatform,new Vector2(posRandomX,10));
         //La añadimos al array
         arrayPlatforms.add(platform);
         //Y al escenario
         this.stage.addActor(platform);

    }

    /**
     * Metodo que prepara el marcador obteniendo la fuente del mismo
     */
    private void prepareScore(){
        //Situamos la puntuacion a 0
        this.gameScore = 0;
        //Obtenemos la fuente y escala
        this.score = this.mainGame.assetManager.getFont();
        this.score.getData().scale(1f);

        //Inicializamos la camara
        this.fontCamera = new OrthographicCamera();
        this.fontCamera.setToOrtho(false, SCREEN_WIDTH,SCREEN_HEIGHT);
        this.fontCamera.update();

    }

    /**
     * Metodo que se ejecuta cuando se pierde
     */
    public void gameOver(){
        //Comprobamos si el jumper esta fuera
      if(this.jumper.jumperIsOut()){
          //Le aplicamos el estado de muerto
         this.jumper.jumperOut();
         //Limpiamos el array
         arrayPlatforms.clear();
         //Paramos la música
          this.musicbg.stop();
          //Hacemos sonar la musica de gameOver
          this.gameOverSound.play();
          //Ponemos la pantalla de gameOver
          mainGame.setScreen(mainGame.gameOverScreen);

      }

    }

    /**
     * Metodo que elimina las plataformas
     */
    public void removePlatform(){
        //Para cada plataforma
        for (Platform platform : this.arrayPlatforms) {
            //Si el mundo no esta bloqueado
            if(!world.isLocked()) {
                //Si la plataforma esta fuera de pantalla se elimina
                if(platform.isOutOfScreen()) {
                    platform.detach();
                    platform.remove();
                    arrayPlatforms.removeValue(platform,false);
                    //Se sube la puntuacion puesto que se ha pasado un nivel
                    this.gameScore++;
                    //Se añade una nueva plataforma arriba
                    addPlatform();
                }
            }
        }
    }

    /**
     * Metodo para obtener los sonidos
     */
    private void prepareGameSound() {
        this.musicbg = this.mainGame.assetManager.getMusicBG();
        this.gameOverSound = this.mainGame.assetManager.getGameOverSound();

    }

    /**
     * Metodo para generar los bordes invisibles
     */
    public void addBorders(){
        //Creamos el borde de la izquierda
        BodyDef bodyDefLeft = new BodyDef();
        bodyDefLeft.position.set(-2,0);
        bodyDefLeft.type = BodyDef.BodyType.StaticBody;
        this.bodyLeft = world.createBody(bodyDefLeft);
        PolygonShape edgeLeft = new PolygonShape();
        edgeLeft.setAsBox(2f, 10f);
        this.fixtureLeft = this.bodyLeft.createFixture(edgeLeft, 3);
        this.fixtureLeft.setUserData(USER_BORDER_LEFT);
        edgeLeft.dispose();
        //El de la derecha
        BodyDef bodyDefRight = new BodyDef();
        bodyDefRight.position.set(WORLD_WIDTH,0);
        bodyDefRight.type = BodyDef.BodyType.StaticBody;
        this.bodyRight = world.createBody(bodyDefRight);
        PolygonShape edgeRight = new PolygonShape();
        edgeRight.setAsBox(0.1f, 10f);
        this.fixtureRight = this.bodyRight.createFixture(edgeRight, 3);
        this.fixtureRight.setUserData(USER_BORDER_RIGHT);
        edgeRight.dispose();
        //Y el de arriba
        BodyDef bodyDefTop = new BodyDef();
        bodyDefTop.position.set(-0.5f,11);
        bodyDefTop.type = BodyDef.BodyType.StaticBody;
        this.bodyTop=world.createBody(bodyDefTop);
        PolygonShape edgeTop = new PolygonShape();
        edgeTop.setAsBox(WORLD_WIDTH+1,2);
        this.fixtureTop = this.bodyTop.createFixture(edgeTop,3);
        this.fixtureTop.setUserData(USER_BORDER_TOP);
        edgeTop.dispose();
    }
}



