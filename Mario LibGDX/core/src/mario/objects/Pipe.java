package mario.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapObject;
import mario.game.MainGame;
import mario.screens.GameScreen;
import mario.screens.LevelScreen;
import mario.screens.StartScreen;
import mario.sprite.Mario;


public class Pipe extends interactiveObject {

	//Pipe activator
	private boolean	active;

	private String	action;

	public Pipe(GameScreen screen, MapObject object, String action) {
		super(screen, object);
		fixture.setUserData(this);

		active = false;

		this.action = action;

		// Set to object bit
		setCategoryFilter(MainGame.PIPE);

	}

	public void active() {
		active = true;
	}

	public void inactive() {
		active = false;
	}

	//Check if pipe is activated by mario standing on it
	public boolean isActive() {
		return active;
	}

	public void usePipe() {
		Gdx.app.log("Pipe", "Used");

		//Check what pipe does and execute
		if (action == "Start Game")
			screen.game.setScreen(new LevelScreen(screen.game));								//Start game
		else if (action == "Instructions")
			Gdx.app.log("Pipe", "Instructions");

		//Enter Level
		else if (action == "Level 1")
			screen.game.setScreen(new GameScreen(screen.game, 1));
		else if (action == "Level 2" && screen.game.levels.isUnlocked(2))
			screen.game.setScreen(new GameScreen(screen.game, 2));
		else if (action == "Level 3" && screen.game.levels.isUnlocked(3))
			screen.game.setScreen(new GameScreen(screen.game, 3));


		//Complete current level and unlock new level
		else if (action == "Finish Level") {
			screen.game.levels.complete(screen.level);
			screen.game.levels.unlock(screen.level + 1);
			Gdx.app.log("Level", "Complete!");
			screen.game.setScreen(new LevelScreen(screen.game));								//Go back to level Screen
		}
	}


	@Override public void onHeadHit() {

	}

}
