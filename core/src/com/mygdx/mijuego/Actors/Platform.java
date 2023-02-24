package com.mygdx.mijuego.Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.mygdx.mijuego.Extra.Utils;

public class Platform extends Actor {
    //Datos de la plataforma
    private static final float PLATFORM_WIDTH = 1.95f;
    private static final float PLATFORM_HEIGHT = 0.6f;
    private static final float SPEED = -0.2f;
    private TextureRegion platformTR;
    private Fixture platformFixture;
    private World world;
    private Body platformBody;
    public Platform(World world, TextureRegion platformTR, Vector2 position){
        this.world=world;
        this.platformTR=platformTR;
        createPlatorm(position);
        createFixture();
    }

    /**
     * Metodo para crear el cuerpo de la plataforma
     * @param position Posicion donde colocar el cuerpo
     */
    private void createPlatorm(Vector2 position){
        BodyDef def = new BodyDef();
        def.position.set(position);
        def.type = BodyDef.BodyType.KinematicBody;
        platformBody = world.createBody(def);
        platformBody.setUserData(Utils.PLATFORM);
        platformBody.setLinearVelocity(new Vector2(0,SPEED));
    }

    /**
     * Metodo para crear el fixture
     */
    private void createFixture() {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(PLATFORM_WIDTH/2, (PLATFORM_HEIGHT/2)-0.075f);
        this.platformFixture = platformBody.createFixture(shape, 8);
        shape.dispose();
    }

    /**
     * Metodo act
     * @param delta
     */
    @Override
    public void act(float delta) {
        super.act(delta);
    }

    /**
     * Metodo para dibujar la plataforma
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        setPosition(this.platformBody.getPosition().x - (PLATFORM_WIDTH/2), this.platformBody.getPosition().y - (PLATFORM_HEIGHT/2) );
        batch.draw(this.platformTR, getX(),getY(),PLATFORM_WIDTH,PLATFORM_HEIGHT);
    }

    /**
     * Metodo que comprueba si la plataforma se encuentra fuera de la pantalla
     * @return Boleana de si esta la plataforma fuera de pantalla
     */
    public boolean isOutOfScreen(){
        return this.platformBody.getPosition().y <= -PLATFORM_HEIGHT-0.6f;
    }

    /**
     * Metodo que elimina el cuerpo y fixture
     */
    public void detach(){
        platformBody.destroyFixture(platformFixture);
        world.destroyBody(platformBody);


    }
}

