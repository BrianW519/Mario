package mario.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mario.screens.GameScreen;
import mario.screens.StartScreen;


public class MainGame extends Game {

	//Virtual Screen size and Box2D Scale(Pixels Per Meter)
	public static final int		V_WIDTH		= 400;
	public static final int		V_HEIGHT	= 208;
	//Make PPM float to fix division problem
	public static final float	PPM			= 100;

	public static Game			game;
	public Levels				levels;

	public SpriteBatch			batch;

	// Box2D Collision Bits
	public static final short	NOTHING		= 0;
	public static final short	GROUND		= 1;
	public static final short	MARIO		= 2;
	public static final short	BRICK		= 4;
	public static final short	COIN		= 8;
	public static final short	DESTROYED	= 16;
	public static final short	OBJECT		= 32;
	public static final short	ENEMY		= 64;
	public static final short	ENEMY_HEAD	= 128;
	public static final short	PIPE		= 256;
	public static final short	MARIO_HEAD	= 512;

	@Override public void create() {
		batch = new SpriteBatch();
		game = this;
		
		levels = new Levels(3);
		
		//Set screen to the game screen (should start at beginning screen)
		//Pass this class so it has dimensions, PPM, and spritebatch 
		setScreen(new StartScreen(this));
	}

	@Override public void dispose() {
		super.dispose();
		batch.dispose();
	}

	@Override public void render() {
		super.render();
	}

}
