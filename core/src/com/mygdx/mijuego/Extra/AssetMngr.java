package com.mygdx.mijuego.Extra;

import static com.mygdx.mijuego.Extra.Utils.ATLAS;
import static com.mygdx.mijuego.Extra.Utils.BACKGROUND;
import static com.mygdx.mijuego.Extra.Utils.FLECHAS;
import static com.mygdx.mijuego.Extra.Utils.FONT_FNT;
import static com.mygdx.mijuego.Extra.Utils.FONT_IMG;
import static com.mygdx.mijuego.Extra.Utils.GAMEOVER_FNT;
import static com.mygdx.mijuego.Extra.Utils.GAMEOVER_FONT_IMG;
import static com.mygdx.mijuego.Extra.Utils.GAMEOVER_SOUND;
import static com.mygdx.mijuego.Extra.Utils.MUSIC_BG;
import static com.mygdx.mijuego.Extra.Utils.PLATFORM_IMG;
import static com.mygdx.mijuego.Extra.Utils.SPOINK_DOWN;
import static com.mygdx.mijuego.Extra.Utils.SPOINK_HIGH;
import static com.mygdx.mijuego.Extra.Utils.SPOINK_MID_1;
import static com.mygdx.mijuego.Extra.Utils.SPOINK_MID_2;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AssetMngr {
    private com.badlogic.gdx.assets.AssetManager assetManager;
    private TextureAtlas textureAtlas;
    public AssetMngr(){
        this.assetManager = new com.badlogic.gdx.assets.AssetManager();
        //Cargamos el atlas y la musica en el AssetManager
        assetManager.load(ATLAS, TextureAtlas.class);
        assetManager.load(MUSIC_BG, Music.class);
        assetManager.load(GAMEOVER_SOUND,Music.class);

        assetManager.finishLoading();
        //Obtenemos el textureAtlas
        this.textureAtlas = assetManager.get(ATLAS);
    }

    /**
     *
     * @return La region del BG
     */
    public TextureRegion getBackground(){
        return this.textureAtlas.findRegion(BACKGROUND);
    }

    /**
     *
     * @return La region de la plataforma
     */
    public TextureRegion getPlatform(){
        return this.textureAtlas.findRegion(PLATFORM_IMG);
    }

    /**
     *
     * @return La region de las flechas
     */
    public TextureRegion getFlechas(){return  this.textureAtlas.findRegion(FLECHAS);}

    /**
     *
     * @return La region del jumper de muestra
     */
    public TextureRegion getJumperImg(){return  this.textureAtlas.findRegion(SPOINK_DOWN);}

    /**
     *
     * @return La animacion del personaje(con una duracion de 0.25)
     */
    public Animation<TextureRegion> getJumperAnimation(){
        return new Animation<TextureRegion>(0.25f,
                textureAtlas.findRegion(SPOINK_DOWN),
                textureAtlas.findRegion(SPOINK_MID_1),
                textureAtlas.findRegion(SPOINK_MID_2),
                textureAtlas.findRegion(SPOINK_HIGH));

    }

    /**
     *
     * @return Devuelve la fuente del contador
     */
    public BitmapFont getFont(){
        return new BitmapFont(Gdx.files.internal(FONT_FNT),Gdx.files.internal(FONT_IMG), false);
    }

    /**
     *
     * @return Devuelve la musica de fondo
     */
    public Music getMusicBG(){
        return this.assetManager.get(MUSIC_BG);
    }

    /**
     *
     * @return Devuelve la musica de GameOver
     */
    public Music getGameOverSound(){
        return this.assetManager.get(GAMEOVER_SOUND);
    }

    /**
     *
     * @return Obtenemos el otro tipo de fuente
     */
    public BitmapFont getGameOverFont(){
        return new BitmapFont(Gdx.files.internal(GAMEOVER_FNT),Gdx.files.internal(GAMEOVER_FONT_IMG), false);
    }
}
