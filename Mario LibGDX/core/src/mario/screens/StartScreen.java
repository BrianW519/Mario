package mario.screens;

import java.util.ArrayList;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mario.game.CollisionListener;
import mario.game.HUD;
import mario.game.MainGame;
import mario.objects.Brick;
import mario.objects.Coin;
import mario.objects.Pipe;
import mario.sprite.Mario;
import mario.sprite.Mario.State;


public class StartScreen extends GameScreen {

	Mario								mario;
	ArrayList<Pipe>						pipes;

	// Define Keys
	private static final int			LEFT	= Keys.DPAD_LEFT;
	private static final int			RIGHT	= Keys.DPAD_RIGHT;
	private static final int			UP		= Keys.DPAD_UP;
	private static final int			DOWN	= Keys.DPAD_DOWN;
	private static final int			SPACE	= Keys.SPACE;

	// Loads Maps
	private TmxMapLoader				mapLoader;
	private TiledMap					map;
	private OrthogonalTiledMapRenderer	renderer;

	// Images
	private TextureAtlas				images;

	// Add PPM here too to shorten code
	public static final float			PPM		= MainGame.PPM;
	private World						world;


	Box2DDebugRenderer					debugRender;
	private OrthographicCamera			gameCam;
	private Viewport					gamePort;


	public StartScreen(MainGame game) {
		gameCam = new OrthographicCamera();
		gamePort = new FitViewport(MainGame.V_WIDTH / PPM, MainGame.V_HEIGHT / PPM, gameCam);

		super.game = game;

		// Create atlas of images
		images = new TextureAtlas("assets/Mario_Images.pack");

		mapLoader = new TmxMapLoader();
		map = mapLoader.load("assets/Levels/StartMenu.tmx");
		renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
		// Set Game Camera Position at beginning
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

		game.batch.enableBlending();

		// Create world where all bodies will be stored
		world = new World(new Vector2(0, -10), true);
		debugRender = new Box2DDebugRenderer();

		// Create ground and blocks and all world elements
		pipes = new ArrayList<Pipe>();
		createWorld(world);
		// Create Mario
		mario = new Mario(world, this, 200, 32);

		//Set collision detection listener
		world.setContactListener(new CollisionListener());

	}

	private void createWorld(World world) {
		BodyDef bdef = new BodyDef();
		PolygonShape shape = new PolygonShape();
		FixtureDef fdef = new FixtureDef();
		Body body;
		// Gets Ground layer from map objects and creates static body
		for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM,
					(rect.getY() + rect.getHeight() / 2) / PPM);

			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}

		// Create Bricks - NON BREAKABLE
		for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			//Create new brick, give it the gamescreen
			new Brick(this, object, false);
		}

		// Create Pipes
		for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();

			bdef.type = BodyDef.BodyType.StaticBody;
			bdef.position.set((rect.getX() + rect.getWidth() / 2) / PPM,
					(rect.getY() + rect.getHeight() / 2) / PPM);

			body = world.createBody(bdef);

			shape.setAsBox(rect.getWidth() / 2 / PPM, rect.getHeight() / 2 / PPM);
			fdef.shape = shape;
			body.createFixture(fdef);
		}

		// Create Pipe Tops
		int numberOfPipes = 2;
		for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
			//Create new pipe, give it the gamescreen

			//Depending on which pipe, assign action
			if (numberOfPipes == 2)
				pipes.add(new Pipe(this, object, "Instructions"));
			if (numberOfPipes == 1)
				pipes.add(new Pipe(this, object, "Start Game"));

			numberOfPipes--;
		}
	}

	protected void keyPressCheck(float time) {
		// If Mario goes left
		if (Gdx.input.isKeyPressed(LEFT))
			mario.moveLeft();

		// If Mario goes right
		if (Gdx.input.isKeyPressed(RIGHT))
			mario.moveRight();

		// If Mario jumps
		if (Gdx.input.isKeyPressed(SPACE) || Gdx.input.isKeyPressed(UP)) {
			mario.jump();
		}

		//if down arrow pressed, check to enter pipes
		if (Gdx.input.isKeyPressed(DOWN)) {
			for (Pipe pipe : pipes)
				if (pipe.isActive()) {
					//Use the pipe
					pipe.usePipe();
					//make pipe inactive so its only used once
					pipe.inactive();
				}
		}

	}

	private void update(float time) {
		// Check if any keys are pressed
		keyPressCheck(time);

		// Step world 60 frames every second
		world.step(1 / 60f, 6, 2);

		// update Mario
		mario.update(time);
		// Update his state
		mario.updateState();

		// Render what is showing on the camera
		renderer.setView(gameCam);
	}

	@Override public void render(float delta) {
		// Updates keys and camera and other things
		// Do this BEFORE rendering
		update(delta);

		// Clear and replace background with color
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render Map
		renderer.render();

		// Show Debug render lines
		debugRender.render(world, gameCam.combined);

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();

		// Draw Mario giving sprite batch
		mario.draw(game.batch);

		game.batch.end();
	}

	public TextureAtlas getImages() {
		return images;
	}

	@Override public void dispose() {
		game.batch.dispose();
	}

	@Override public void show() {
		// TODO Auto-generated method stub

	}

	@Override public void resize(int width, int height) {
		gamePort.update(width, height);

	}

	@Override public void pause() {
		// TODO Auto-generated method stub

	}

	@Override public void resume() {
		// TODO Auto-generated method stub

	}

	@Override public void hide() {
		// TODO Auto-generated method stub

	}

	public World getWorld() {
		return world;
	}

	public TiledMap getMap() {
		return map;
	}
}
