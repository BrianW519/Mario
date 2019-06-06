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
	
		setCategoryFilter(MainGame.BRICK);						// Set to brick ID

		this.breakable = breakable;								// If brick is breakable or not (for starting bricks to be unbreakable

	}

	@Override public void onHeadHit() {
		Gdx.app.log("Brick", "Collision");

		if (breakable) {										//If the brick is able to be broken
			setCategoryFilter(MainGame.DESTROYED);				//Set to destroyed ID (not collidable)
			getCell().setTile(null);							//Clear the texture
			
			screen.hud.addScore(5);								//Add points
		}

	}

}
