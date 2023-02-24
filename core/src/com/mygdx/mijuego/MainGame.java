package com.mygdx.mijuego;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.mijuego.Extra.AssetMngr;
import com.mygdx.mijuego.Screens.GameOverScreen;
import com.mygdx.mijuego.Screens.GameScreen;
import com.mygdx.mijuego.Screens.TutorialScreen;


public class MainGame extends Game {
	//Creamos instancias de las pantallas
	public GameScreen gameScreen;
	public GameOverScreen gameOverScreen;
	public TutorialScreen tutorialScreen;
	public AssetMngr assetManager;

	@Override
	public void create() {
		//Inicializamos las pantallas
		this.assetManager = new AssetMngr();
		this.gameOverScreen = new GameOverScreen(this);
		this.gameScreen = new GameScreen(this);
		this.tutorialScreen = new TutorialScreen(this);
		//Y comenzamos el juego con la del tutorial
		setScreen(this.tutorialScreen);
	}
}
