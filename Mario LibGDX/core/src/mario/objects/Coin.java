package mario.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import mario.game.MainGame;
import mario.screens.GameScreen;
import mario.sprite.Mario;


public class Coin extends interactiveObject {

	public Coin(GameScreen screen, MapObject object) {
		super(screen, object);
		fixture.setUserData(this);
		setCategoryFilter(MainGame.COIN);						// Set to coin ID

	}

	@Override public void onHeadHit() {
		Gdx.app.log("Coin", "Broken");

			setCategoryFilter(MainGame.DESTROYED);				//Set to destroyed ID (not collidable)
			getCell().setTile(null);							//Clear the texture
			
			screen.hud.addScore(15);							//Add points
		}

}
