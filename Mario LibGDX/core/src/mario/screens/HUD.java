package mario.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mario.game.MainGame;


public class HUD implements Disposable {

	public Stage			stage;
	private Viewport		viewport;

	// Mario score/time Tracking Variables
	private Integer			worldTimer;
	private boolean			timeUp; // true when the world timer reaches 0
	private float			timeCount;
	private static Integer	score;

	// Scene2D widgets
	private Label			timeLabel;
	private static Label	scoreLabel;
	private Label			timeText;
	private Label			livesLabel;
	private Label			scoreText;
	private Label			livesText;
	private Label			gameOverText;

	public HUD(SpriteBatch sb) {
		worldTimer = 120;
		timeCount = 0;
		score = 0;

		// Use different viewport seperate from GameScreen
		viewport = new FitViewport(MainGame.V_WIDTH, MainGame.V_HEIGHT, new OrthographicCamera());
		stage = new Stage(viewport, sb);

		Table table = new Table();
		table.top();
		table.setFillParent(true);													//Make the table fill the entire stage

		// Define Labels
		livesText = new Label("LIVES", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		scoreText = new Label("SCORE", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeText = new Label("TIME", new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		livesLabel = new Label("1-1", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		scoreLabel = new Label(String.format("%06d", score),
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		timeLabel = new Label(String.format("%03d", worldTimer),
				new Label.LabelStyle(new BitmapFont(), Color.WHITE));

		gameOverText = new Label("", new Label.LabelStyle(new BitmapFont(), Color.RED));

		//Add labels to table with padding on top
		table.add(livesText).expandX().padTop(10);
		table.add(scoreText).expandX().padTop(10);
		table.add(timeText).expandX().padTop(10);
		//Add second row to table
		table.row();
		table.add(livesLabel).expandX();
		table.add(scoreLabel).expandX();
		table.add(timeLabel).expandX();

		table.row();
		table.add().expandX();
		table.add(gameOverText).padTop(50);
		table.add().expandX();


		//Add table to the stage
		stage.addActor(table);

	}

	public void gameOver(int why) {
		if (why == 0)														//Game over b/c mario died
			gameOverText.setText("Game Over");
		else if (why == 1)
			gameOverText.setText("Time Up");								//Game over b/c time was up
	}

	public void update(float dt, int lives) {
		timeCount += dt;
		if (timeCount >= 1) {												//Once a second passes, subtract from time
			if (worldTimer > 0) {
				worldTimer--;
			} else {
				timeUp = true;
			}
			timeLabel.setText(String.format("%03d", worldTimer));
			timeCount = 0;
		}

		livesLabel.setText(String.format("%01d", lives));
	}

	public void addScore(int value) {
		score += value;
		scoreLabel.setText(String.format("%06d", score));
	}

	@Override public void dispose() {
		stage.dispose();
	}

	public boolean isTimeUp() {
		return timeUp;
	}

}
