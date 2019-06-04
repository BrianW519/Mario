package mario.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import mario.game.MainGame;
import mario.screens.GameScreen;


public class Goomba extends Sprite {

	private float		speed	= 0.5f;										 // 10 pixels per second.
	private float		xPos;
	private float		yPos;
	public float		timer;												//Timer to keep dead goomba on screen shortly


	public Body			body;
	public World		world;
	public Texture		texture;
	public Sprite		sprite;

	public State		currentState;
	private Vector2		velocity;
	public boolean		setToDestroy;
	public boolean		defeated;

	private GameScreen	screen;

	public enum State {
		ALIVE, DEAD
	};

	public Goomba(World world, GameScreen screen, float x, float y) {
		// Get goomba images from atlas
		super(screen.getImages().findRegion("goomba"), 16, 0, 16, 16);
		this.world = world;
		this.screen = screen;

		// Set Starting Position
		xPos = x;
		yPos = y;

		velocity = new Vector2(-1, -2);
		defeated = false;
		setToDestroy = false;
		timer = 0;

		createGoomba();
		body.setActive(false);

		// set how big it should be
		setBounds(getX(), getY(), 16 / MainGame.PPM, 16 / MainGame.PPM);
	}


	public void createGoomba() {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(xPos / MainGame.PPM, yPos / MainGame.PPM);
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		body = world.createBody(bodyDef);

		FixtureDef fdef = new FixtureDef();
		CircleShape shape = new CircleShape();
		shape.setRadius(6 / MainGame.PPM);
		fdef.filter.categoryBits = MainGame.ENEMY;
		fdef.filter.maskBits = MainGame.GROUND |
				MainGame.COIN |
				MainGame.BRICK |
				MainGame.ENEMY |
				MainGame.OBJECT |
				MainGame.MARIO;

		fdef.shape = shape;
		body.createFixture(fdef).setUserData(this);

		//Create head that mario can hit
		PolygonShape head = new PolygonShape();
		Vector2[] vertice = new Vector2[4];
		vertice[0] = new Vector2(-5, 8).scl(1 / MainGame.PPM);
		vertice[1] = new Vector2(5, 8).scl(1 / MainGame.PPM);
		vertice[2] = new Vector2(-3, 3).scl(1 / MainGame.PPM);
		vertice[3] = new Vector2(3, 3).scl(1 / MainGame.PPM);
		head.set(vertice);

		fdef.shape = head;
		fdef.restitution = 0.5f;
		fdef.filter.categoryBits = MainGame.ENEMY_HEAD;
		body.createFixture(fdef).setUserData(this);

	}

	// ===================================================================================
	// ====================================  Methods  ====================================
	// ===================================================================================
	public void update(float time) {

		timer += time;

		if (setToDestroy && !defeated) {
			defeated = true;
			world.destroyBody(body);
			body.setUserData(null);
			body = null;
			timer = 0;

		} else if (!defeated) {
			setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);		// Attach sprite image to body
			body.setLinearVelocity(velocity);																//set speed to velocity specified
		}
	}

	public void reverseDir() {
		velocity.x = -velocity.x;
	}


	public void onHeadHit() {
		setToDestroy = true;
		velocity = new Vector2(0, 0);
		setRegion(new TextureRegion(screen.getImages().findRegion("goomba"), 32, 0, 16, 16));
		
		screen.hud.addScore(25);
	}



	// ===================================================================================
	// ====================================  Getters  ====================================
	// ==================================== & Setters ====================================
	// ===================================================================================
	public float getSpeed() {
		return speed;
	}

	public float getxPos() {
		return xPos;
	}

	public float getyPos() {
		return yPos;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setxPos(float xPos) {
		this.xPos = xPos;
	}

	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

}
