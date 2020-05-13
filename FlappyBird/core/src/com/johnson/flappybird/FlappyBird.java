package com.johnson.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture gameover;
	ShapeRenderer shapeRenderer;

	int flapState = 0;
	float birdY = 0;
	float velocity = 0;
	Circle birdCircle;
	Rectangle topRectangle;
	Rectangle bottomRectangle;

	int gameState = 0;

	Texture topTube;
	Texture bottomTube;
	int gap = 400;

	float maxTubeOffset;
	Random rand;
	float tubeVelocity = 4;
	int numberOfTubes = 4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;

	int score = 0;
	int scoringTube = 0;
BitmapFont font;

	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		gameover = new Texture("gameover.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap/2 - 100;
		rand = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3 / 4;

		startGame();

		shapeRenderer = new ShapeRenderer();
	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2 - birds[flapState].getHeight()/2;

		for (int i = 0; i < numberOfTubes; i++){

			tubeOffset[i] = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 900);

			tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
		}
		birdCircle = new Circle();
		topRectangle = new Rectangle();
		bottomRectangle = new Rectangle();
	}
	@Override
	public void render () {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gameState == 1) {

			if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
				score++;

				Gdx.app.log("Score", String.valueOf(score));
				if (scoringTube < numberOfTubes - 1) {
					scoringTube++;
				} else {
					scoringTube = 0;
				}
			}

			if (Gdx.input.justTouched()) {
				velocity = -20;
			}
			for (int i = 0; i < numberOfTubes; i++) {
				if (tubeX[i] < -topTube.getWidth()) {
					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (rand.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 900);
				} else {
					tubeX[i] = tubeX[i] - tubeVelocity;

				}

				batch.draw(topTube, tubeX[i] - topTube.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomTube, tubeX[i] - bottomTube.getWidth() / 2, Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

				topRectangle.set(tubeX[i] - topTube.getWidth() / 2, Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), Gdx.graphics.getHeight() / 2 - gap / 2 - tubeOffset[i]);
				bottomRectangle.set(tubeX[i] - bottomTube.getWidth() / 2, 0, bottomTube.getWidth(), Gdx.graphics.getHeight() / 2 - gap / 2 + tubeOffset[i]);
				//shapeRenderer.rect(topRectangle.x, topRectangle.y, topRectangle.width, topRectangle.height);
				//shapeRenderer.rect(bottomRectangle.x, bottomRectangle.y, bottomRectangle.width, bottomRectangle.height);
				if (Intersector.overlaps(birdCircle, topRectangle) || Intersector.overlaps(birdCircle, bottomRectangle)) {
					gameState = 2;
				}
			}

			if (birdY > 0) {
				velocity++;
				birdY -= velocity;
			} else {
				gameState = 2;
			}
		} else if (gameState == 0){
			if (Gdx.input.justTouched()) {
				gameState = 1;
			}
		} else if (gameState == 2){
			batch.draw(gameover, Gdx.graphics.getWidth()/2 - gameover.getWidth()/2, Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);
			if (Gdx.input.justTouched()){
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0;
				gameState = 1;
			}
		}

		if (flapState == 0) {
			flapState = 1;
		} else {
			flapState = 0;
		}

		batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
		font.draw(batch, String.valueOf(score), 100, 200);
		batch.end();


		birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2 - 10);


		//shapeRenderer.setColor(Color.RED);

		//shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
		shapeRenderer.end();
	}

	
	@Override
	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
		topTube.dispose();
		bottomTube.dispose();
	}
}
