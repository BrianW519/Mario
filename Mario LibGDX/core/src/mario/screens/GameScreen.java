package mario.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mario.game.CollisionListener;
import mario.game.MainGame;
import mario.objects.Brick;
import mario.objects.Coin;
import mario.objects.Pipe;
import mario.sprite.Goomba;
import mario.sprite.Mario;
import mario.sprite.Mario.State;


public class GameScreen implements Screen {

	public MainGame						game;

	Mario								mario;
	ArrayList<Pipe>						pipes;
	Array<Goomba>						goombas;

	// Define Keys
	private static final int			LEFT	= Keys.DPAD_LEFT;
	private static final int			RIGHT	= Keys.DPAD_RIGHT;
	private static final int			UP		= Keys.DPAD_UP;
	private static final int			DOWN	= Keys.DPAD_DOWN;
	private static final int			SPACE	= Keys.SPACE;

	// Loads Maps
	public int							level;
	private TmxMapLoader				mapLoader;
	private TiledMap					map;
	private MapProperties				mapProperties;
	private OrthogonalTiledMapRenderer	renderer;

	// Images
	private TextureAtlas				images;

	// Add PPM here too to shorten code
	public static final float			PPM		= MainGame.PPM;
	public HUD							hud;
	private World						world;

	Box2DDebugRenderer					debugRender;
	private OrthographicCamera			gameCam;
	private Viewport					gamePort;


	// ===================================================================================
	// ================================= Constructor =====================================
	// ===================================================================================
	public GameScreen(MainGame game, int level) {

		gameCam = new OrthographicCamera();													//New camera to follow mario
		gamePort = new FitViewport(MainGame.V_WIDTH / PPM, MainGame.V_HEIGHT / PPM, gameCam);

		this.game = game;
		this.level = level;

		// Create Game HUD with time and points (takes in sprite batch from MainGame)
		hud = new HUD(game.batch);

		images = new TextureAtlas("assets/Mario_Images.pack");								// Create atlas of images

		mapLoader = new TmxMapLoader();														// Load Level and render it
		switch (level) {
			case 1:
				map = mapLoader.load("assets/Levels/Level1.tmx");
				break;
			case 2:
				map = mapLoader.load("assets/Levels/Level2.tmx");
				break;
			case 3:
				map = mapLoader.load("assets/Levels/Level3.tmx");
				break;
		}
		mapProperties = map.getProperties();												//Get map properties for width later
		renderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);
		
		gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);// Set Game Camera Position at beginning

		game.batch.enableBlending();

		world = new World(new Vector2(0, -10), true);										// Create world where all bodies will be stored
		debugRender = new Box2DDebugRenderer();

		createWorld();																		// Create ground and blocks and all world elements
		mario = new Mario(world, this, 32, 64);												//Create Mario

		world.setContactListener(new CollisionListener());									//Set collision detection listener

	}

	public GameScreen() {

	}

	// ===================================================================================
	// ================================= Create World ====================================
	// ===================================================================================
	private void createWorld() {
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

		// Create Bricks
		for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
			//Create new brick, give it the gamescreen
			new Brick(this, object, true);
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
			fdef.filter.categoryBits = MainGame.OBJECT;
			body.createFixture(fdef);
		}

		// Create Coins
		for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
			//Create new brick, give it the gamescreen
			new Coin(this, object);
		}

		// Create Goombas
		goombas = new Array<Goomba>();
		for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
			Rectangle rect = ((RectangleMapObject) object).getRectangle();
			goombas.add(new Goomba(world, this, rect.getX(), rect.getY()));
			Gdx.app.log("Goomba", "New goomba created");
		}


		pipes = new ArrayList<Pipe>();

		// Create Pipe Tops 
		int numberOfPipes = 1;
		for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) { //Create new pipe, give it the gamescreen

			//Depending on which pipe, assign action 
			if (numberOfPipes == 1)
				pipes.add(new Pipe(this, object, "Finish Level"));


			numberOfPipes--;
		}


	}

	// ===================================================================================
	// =============================== Update + Render ===================================
	// ===================================================================================
	protected void keyPressCheck(float time) {
		// If Mario goes left
		if (Gdx.input.isKeyPressed(LEFT)) {
			mario.moveLeft();
		}

		// If Mario goes right
		if (Gdx.input.isKeyPressed(RIGHT)) {
			mario.moveRight();
		}

		// If Mario jumps
		if (Gdx.input.isKeyJustPressed(SPACE) || Gdx.input.isKeyJustPressed(UP)) {
			mario.jump();
		}

		//if down arrow pressed, check to enter pipes
		if (Gdx.input.isKeyPressed(DOWN)) {
			for (Pipe pipe : pipes)
				if (pipe.isActive()) {
					pipe.usePipe();															//Use the pipe
					pipe.inactive();														//Make pipe inactive so its only used once
				}
		}

	}

	private void update(float time) {
		keyPressCheck(time);																// Check if any keys are pressed

		world.step(1 / 60f, 6, 2);															// Step world 60 frames every second

		mario.update(time);																	// Update Mario
		mario.updateState();																// Update his state
		mario.deadCheck();																	// Check if mario is dead

		//For all goombas
		for (Goomba goomba : goombas) {
			goomba.update(time);
			if (goomba.getX() < mario.getX() + 300 / MainGame.PPM && !goomba.setToDestroy)	//If goomba is close enough to mario, start moving it
				goomba.body.setActive(true);

			if (goomba.getX() < mario.getX() - 300 && !goomba.setToDestroy)
				goomba.body.setActive(false);
		}

		hud.update(time, mario.getLives());													//Update the HUD with the time

		if (mario.body.getPosition().x > gamePort.getWorldWidth() / 2)						// Set game cam to mario position
			if (mario.body.getPosition().x < 36)
				gameCam.position.x = mario.body.getPosition().x;

		gameCam.update();																	// Update camera
		renderer.setView(gameCam);															// Render what is showing on the camera
	}

	@Override public void render(float delta) {
		//Do this at beginning to render game over text first
		if (mario.currentState == State.DEAD) {
			try {
				Thread.sleep(4000);															//After death, pause for 4 seconds
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//Set back to start screen
			game.setScreen(new StartScreen(game));
		}



		update(delta);																		//Updates keys and camera and other things

		// Clear and replace background with color
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Render Map
		renderer.render();

		// Show Debug render lines
		//debugRender.render(world, gameCam.combined);

		game.batch.setProjectionMatrix(gameCam.combined);
		game.batch.begin();

		// Draw Mario giving sprite batch
		mario.draw(game.batch);
		//draw all goombas
		for (Goomba goomba : goombas)
			if (!goomba.defeated || goomba.timer < 1)
				goomba.draw(game.batch);


		game.batch.end();

		// Now draw HUD display on top
		game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
		hud.stage.draw();


	}


	// ===================================================================================
	// ==================================== Getters ======================================
	// ==================================== & Setters ====================================
	// ===================================================================================
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
