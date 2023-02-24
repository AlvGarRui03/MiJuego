package com.mygdx.mijuego.Actors;

import static com.mygdx.mijuego.Extra.Utils.USER_SPOINK;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
public class Jumper extends Actor {

    private Animation<TextureRegion> jumperAnimation;
    private Vector2 position;
    private float stateTime;

    private World world;
    //Body y Fixture
    private Body body;
    private Fixture fixture;
    //Movimiento
    private float distance = 0;
    private float movLateral=0;
    //Estados
    public static final int STATE_NORMAL = 0;
    public static final int STATE_OUT = 1;
    private int stateJumper;

    /**
     *
     * @param world le introducimos el mundo
     * @param animation la animacion del personaje
     * @param position y la posicion donde se creará
     */
    public Jumper(World world, Animation<TextureRegion> animation,Vector2 position){
        //Le pasamos la animación
        this.jumperAnimation = animation;
        //Le pasamos la posición
        this.position = position;
        //El mundo
        this.world = world;
        //Inicializamos el stateTime
        this.stateTime = 0f;
        //Y el estado del jumper
        this.stateJumper = STATE_NORMAL;
        //Creamos el cuerpo y el fixture
        createBody();
        createFixture();

    }

    /**
     * Metodo para crear el cuerpo del Actor jumper
     */
    private void createBody(){
       //Creamos el bodyDef
        BodyDef bodyDef = new BodyDef();
        //Le pasamos la posición
        bodyDef.position.set(this.position);
        //Hacemos que no rote al colisionar
        bodyDef.fixedRotation=true;
        //Le damos el tipo dinamico para que le afecten las fuerzas
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //Creamos el body en el mundo
        this.body = this.world.createBody(bodyDef);
    }

    /**
     * Metodo para crear el fixture del Actor jumper
     */
    private void createFixture(){
        //Creamos la forma
        PolygonShape rectangle = new PolygonShape();
        rectangle.setAsBox(0.3f,0.45f);
        //Le pasamos al fixture la forma y el userData
        this.fixture = this.body.createFixture(rectangle, 10);
        this.fixture.setUserData(USER_SPOINK);
        //Eliminamos el polygon shape de memoria
        rectangle.dispose();
    }

    /**
     * Metodo que al pulsar la pantalla hace que el personaje vaya cargando un salto, al
     * soltarlo saltará con esa potencia(Siendo un máximo 45)
     * @param delta le pasamos delta puesto que se hará dentro del render
     */
    @Override
    public void act(float delta) {
        //Variable boleana que detecta si la pantalla esta siendo tocada o no
        boolean jump = Gdx.input.isTouched();
        //Guardamos la entrada del Giroscopio del movil
        movLateral= Gdx.input.getAccelerometerX();
        //Comprobamos que la pantalla esta siendo tocada y el personaje esta vivo
        if(jump && stateJumper==STATE_NORMAL){;
            //Cargamos la distancia del salto
            distance+=0.5f;

        }else {
            //Si la distancia es demasiada la igualamos a 44
            if(distance>45){
                distance=44;
            }
            //Aplicamos la fuerza y reiniciamos la distancia
            this.body.applyForceToCenter(new Vector2(movLateral*-10f,distance*90),false);
            distance=0;
        }


    }

    /**
     * Metodo para dibujar el jumper
     * @param batch le paramos el batch
     * @param parentAlpha y el padre
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        //Le ponemos la posicion y lo dibujamos
        setPosition(body.getPosition().x-0.35f, body.getPosition().y-0.7f);
        batch.draw(this.jumperAnimation.getKeyFrame(stateTime, true), getX(), getY(), 0.6f,1.1f );
        //Obtenemos delta de la gráfica
        stateTime += Gdx.graphics.getDeltaTime();
    }

    /**
     * Metodo para eliminar el fixture y el body del Actor Jumper
     */
    public void detach(){
        this.body.destroyFixture(this.fixture);
        this.world.destroyBody(this.body);
    }

    /**
     *
     * @return El estado del jumper
     */
    public int getJumperState(){
        return this.stateJumper;
    }

    /**
     *
     * @return Devuelve si el actor se encuentra por debajo del  mundo
     */
    public boolean jumperIsOut(){
        //Comprobamos que el jumper no esté muy por debajo del mundo
        return this.body.getPosition().y <= -3.2;
    }

    /**
     * Metodo que 'mata' al jumper
     * */
    public void jumperOut() {
        stateJumper = STATE_OUT;
        stateTime = 0f;
    }


}


