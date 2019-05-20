package mario.objects;

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
import mario.game.GameScreen;
import mario.game.MainGame;


public class Mario extends Sprite {

	private float			speed	= 1.0f;			 // 10 pixels per second.
	private float			xPos;
	private float			yPos;

	public Body				body;
	public World			world;
	public Texture			texture;
	public Sprite			sprite;

	private TextureRegion	marioStanding;

	public State			currentState;
	public State			previousState;

	public enum State {
		FALLING, JUMPING, STANDING, RUNNING, DEAD
	};

	public Mario(World world, GameScreen screen) {
		// Get little mario images loaded from atlas
		super(screen.getImages().findRegion("little_mario"));
		this.world = world;

		// Set Starting Position
		xPos = 32;
		yPos = 64;
		// Create mario body and texture
		createMario();

		// define mario standing image from atlas
		marioStanding = new TextureRegion(getTexture(), 0, 10, 16, 16);
		// set how big it should be
		setBounds(0, 0, 16 / MainGame.PPM, 16 / MainGame.PPM);
		setRegion(marioStanding);

	}

	public void update(float time) {
		// Attach sprite image to body
		setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
	}

	public void createMario() {
		// Create Body Definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(xPos / MainGame.PPM, yPos / MainGame.PPM);

		// Create body in the world using body definition
		body = world.createBody(bodyDef);

		// Create shape for fixture
		PolygonShape polygon = new PolygonShape();
		CircleShape circle = new CircleShape();
		circle.setRadius(7 / MainGame.PPM);
		polygon.setAsBox(10 / MainGame.PPM, 10 / MainGame.PPM); //

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 35f;
		fixtureDef.friction = 10f;
		fixtureDef.restitution = 0.1f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		Fixture fixture = body.createFixture(fixtureDef);

		polygon.dispose();
	}

	// ===================================== Methods
	// =====================================
	public boolean moveLeft() {
		// Make sure not moving too fast
		if (body.getLinearVelocity().x >= -1)
			body.applyLinearImpulse(new Vector2(-0.2f, 0), body.getWorldCenter(), true);

		return true;
	}

	public boolean moveRight() {
		// Make sure not moving too fast
		if (body.getLinearVelocity().x <= 1)
			body.applyLinearImpulse(new Vector2(0.2f, 0), body.getWorldCenter(), true);

		return true;
	}

	public boolean jump() {
		if (body.getLinearVelocity().y == 0) {
			body.applyLinearImpulse(new Vector2(0, 2f), body.getWorldCenter(), true);
			//currentState = State.JUMPING;
		}

		return true;
	}

	public void brickCollide() {
		body.setAwake(false);
	}

	public State getState() {
		// if mario is going positive in Y-Axis he is jumping... or if he just jumped
		// and is falling remain in jump state

		// if(marioIsDead)
		// return State.DEAD;

		if ((body.getLinearVelocity().y > 0 && currentState == State.JUMPING)
				|| (body.getLinearVelocity().y < 0 && previousState == State.JUMPING))
			return State.JUMPING;
		// if negative in Y-Axis mario is falling
		else if (body.getLinearVelocity().y < 0)
			return State.FALLING;
		// if mario is positive or negative in the X axis he is running
		else if (body.getLinearVelocity().x != 0)
			return State.RUNNING;
		// if none of these return then he must be standing
		else
			return State.STANDING;
	}

	// ===================================================================================
	// ===================================== Getters
	// =====================================
	// ===================================== & Setters
	// ==================================
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
