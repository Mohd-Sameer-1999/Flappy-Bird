package com.mohdsameer.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.AssetLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.util.Random;

public class FlappyBIrd extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture gameOver;
//	TextureRegion[] animationFrames;
//	Animation<TextureRegion> animation;
//	float elapsedTime = 0;



//	ShapeRenderer shapeRenderer;
	Texture[] birds;
	Circle birdCircle;
	Texture toptube;
	Texture bottomtube;
	int flapstate = 0;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float gravity = 1;

	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangle = new Rectangle[numberOfTubes];
	Rectangle[] bottomTubeRectangle = new Rectangle[numberOfTubes];
	int score = 0;
	int scoringTube = 0;
	BitmapFont font ;
	BitmapFont startPlaying;

//	buttons
	ImageButton restartButton;
	TextButton startButton;
	Stage stage;
	TextButton.TextButtonStyle textButtonStyle;
	BitmapFont buttonFont;
	Skin skin;
	TextureAtlas buttonAtlas;
	Texture restart;
	TextureRegion textureRegionRestart;
	TextureRegionDrawable textureRegionDrawableRestart;
	TextureRegion textureRegionGameOver;
	TextureRegionDrawable textureRegionDrawableGameOver;
	Image gameOverImage;


	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");


		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
		startPlaying = new BitmapFont();
		startPlaying.setColor(Color.WHITE);
		startPlaying.getData().setScale(4);
//		shapeRenderer = new ShapeRenderer();

		birds = new Texture[2];
		birdCircle = new Circle();
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		toptube = new Texture("toptube.png");
		bottomtube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;

		gameOverStage();

		startGame();


	}


	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2 - birds[flapstate].getHeight()/2;

		for(int i=0 ; i<numberOfTubes; i++){
			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - bottomtube.getWidth()/2 + Gdx.graphics.getWidth() +i*distanceBetweenTubes;

			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();

		}

	}

	public void gameOverStage(){
		restart = new Texture("restart.png");
		textureRegionRestart = new TextureRegion(restart);
		textureRegionDrawableRestart = new TextureRegionDrawable(textureRegionRestart);
		restartButton = new ImageButton(textureRegionDrawableRestart);
		restartButton.setScale(15);
		restartButton.setPosition(Gdx.graphics.getWidth()/2 - restart.getWidth()/2, Gdx.graphics.getHeight()/2);
		restartButton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				super.clicked(event, x, y);
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;

			}
		});

		gameOver = new Texture("gameover.png");
		textureRegionGameOver = new TextureRegion(gameOver);
		textureRegionDrawableGameOver = new TextureRegionDrawable(textureRegionGameOver);
		gameOverImage = new Image(textureRegionDrawableGameOver);
		gameOverImage.setPosition(Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2 + 150);




		stage = new Stage(new ScreenViewport());
		stage.addActor(restartButton);
		stage.addActor(gameOverImage);
		Gdx.input.setInputProcessor(stage);
	}
	
//	public void gameOverScreen(){
//		TextureRegion gameOver1 = new TextureRegion(gameOver, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
//		TextureRegion gameOver2 = new TextureRegion(gameOver, Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight()/3);
//		TextureRegion gameOver3 = new TextureRegion(gameOver, Gdx.graphics.getWidth()/4, Gdx.graphics.getHeight()/4);
//		TextureRegion gameOver4 = new TextureRegion(gameOver, Gdx.graphics.getWidth()/5, Gdx.graphics.getHeight()/5);
//		TextureRegion gameOver5 = new TextureRegion(gameOver, Gdx.graphics.getWidth()/6, Gdx.graphics.getHeight()/6);
//		TextureRegion gameOver6 = new TextureRegion(gameOver, Gdx.graphics.getWidth()/7, Gdx.graphics.getHeight()/7);
//		TextureRegion gameOver7 = new TextureRegion(gameOver, Gdx.graphics.getWidth()/8, Gdx.graphics.getHeight()/8);
//		animationFrames = new TextureRegion[]{gameOver1, gameOver2, gameOver3, gameOver4, gameOver5, gameOver6, gameOver7};
//		animation = new Animation<>(0.2f, animationFrames);
//	}


	@Override
	public void render () {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2) {

				score++;

				Gdx.app.log("Score", String.valueOf(score));

				if(scoringTube < numberOfTubes-1){
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}

			if(Gdx.input.justTouched()){
				velocity = -20;

			}
			for(int i=0 ; i<numberOfTubes; i++) {

				if(tubeX[i] < - toptube.getWidth()){
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
				}else {
					tubeX[i] -= tubeVelocity;


				}



				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);

				topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], toptube.getWidth(), toptube.getHeight());
				bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(), bottomtube.getHeight());



			}

			if (birdY > 0 ) {
				velocity += gravity;
				birdY -= velocity;
			}else {

				gameState = 2;

			}


		}else if(gameState == 0){
			startPlaying.draw(batch,"Tap to start the game", Gdx.graphics.getWidth()/2 - 200 - birds[flapstate].getWidth()/2 , 100);
			if(Gdx.input.justTouched()){
				gameState = 1;
			}

		}else if (gameState == 2){
			stage.draw();

//			batch.draw(gameOver, Gdx.graphics.getWidth()/2 - gameOver.getWidth()/2, Gdx.graphics.getHeight()/2);
//
//			if(Gdx.input.justTouched()){
//				gameState = 1;
//
//				startGame();
//				score = 0;
//				scoringTube = 0;
//				velocity = 0;
//			}
//
//			startPlaying.draw(batch,"Tap to play again", Gdx.graphics.getWidth()/2 - 150 - birds[flapstate].getWidth()/2 , Gdx.graphics.getHeight()/2 - 100);






		}
		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}

		batch.draw(birds[flapstate], Gdx.graphics.getWidth() / 2 - birds[flapstate].getWidth() / 2, birdY);
		font.draw(batch, String.valueOf(score), Gdx.graphics.getWidth()/2 - birds[flapstate].getWidth()/2 + 35, Gdx.graphics.getHeight()/2 + 800);
		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapstate].getHeight()/2, birds[flapstate].getWidth()/2);
		

//		shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//		shapeRenderer.setColor(Color.RED);
//		shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);

		for(int i=0 ; i<numberOfTubes; i++) {
//			shapeRenderer.rect(topTubeRectangle[i].getX(), topTubeRectangle[i].getY(), topTubeRectangle[i].getWidth(), topTubeRectangle[i].getHeight());
//			shapeRenderer.rect(bottomTubeRectangle[i].getX(), bottomTubeRectangle[i].getY(), bottomTubeRectangle[i].getWidth(), bottomTubeRectangle[i].getHeight());

			if(Intersector.overlaps(birdCircle, topTubeRectangle[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangle[i])){
				gameState = 2;
			}
		}
		batch.end();

//		shapeRenderer.end();

	}


}
