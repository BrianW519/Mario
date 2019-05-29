package mario.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapObject;
import mario.game.MainGame;
import mario.screens.GameScreen;
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

		// Set to coin bit
		setCategoryFilter(MainGame.OBJECT);

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
			screen.game.setScreen(new GameScreen(screen.game));
		else if (action == "Instructions")
			Gdx.app.log("Pipe", "Instructions");
	}


	@Override public void onHeadHit() {

	}

}
