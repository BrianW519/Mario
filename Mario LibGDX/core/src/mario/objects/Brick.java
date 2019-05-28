package mario.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import mario.game.GameScreen;
import mario.game.MainGame;
import mario.sprite.Mario;

public class Brick extends interactiveObject {

    public Brick(GameScreen screen, MapObject object) {
	super(screen, object);
	fixture.setUserData(this);
	// Set to coin bit
	setCategoryFilter(MainGame.BRICK);

    }

    @Override
    public void onHeadHit() {
	Gdx.app.log("Brick", "Collision");
	//Set to destroyed bit
	setCategoryFilter(MainGame.DESTROYED);
	getCell().setTile(null);
    }

}
