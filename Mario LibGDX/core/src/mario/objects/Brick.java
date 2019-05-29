package mario.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import mario.game.MainGame;
import mario.screens.GameScreen;
import mario.sprite.Mario;


public class Brick extends interactiveObject {

	private boolean breakable;

	public Brick(GameScreen screen, MapObject object, boolean breakable) {
		super(screen, object);
		fixture.setUserData(this);
		// Set to coin bit
		setCategoryFilter(MainGame.BRICK);

		this.breakable = breakable;

	}

	@Override public void onHeadHit() {
		Gdx.app.log("Brick", "Collision");

		if (breakable) {
			//Set to destroyed bit
			setCategoryFilter(MainGame.DESTROYED);
			getCell().setTile(null);
		}

	}

}
