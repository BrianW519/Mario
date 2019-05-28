package mario.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import mario.game.GameScreen;
import mario.game.MainGame;
import mario.sprite.Mario;

public class Coin extends interactiveObject {

    public Coin(GameScreen screen, MapObject object) {
	super(screen, object);
	fixture.setUserData(this);
	// Set to coin bit
	setCategoryFilter(MainGame.COIN);

    }

    @Override
    public void onHeadHit() {
	Gdx.app.log("Coin", "Collision");
	// Set to destroyed bit
	setCategoryFilter(MainGame.DESTROYED);
	getCell().setTile(null);
    }

}
