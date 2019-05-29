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

	private float			speed	= 0.5f;										 // 10 pixels per second.
	private float			xPos;
	private float			yPos;


	public Body				body;
	public World			world;
	public Texture			texture;
	public Sprite			sprite;

	private TextureRegion	marioStanding;

	public State			currentState;

	public enum State {
		ALIVE, DEAD
	};

	public Goomba(World world, GameScreen screen) {
		// Get goomba images from atlas
		super(screen.getImages().findRegion("goomba"));
		this.world = world;

		// Set Starting Position
		xPos = 64;
		yPos = 64;
		// Create mario body and texture
		createGoomba();

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

	public void createGoomba() {
		// Create Body Definition
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		// Set body's position in the world
		bodyDef.position.set(xPos / MainGame.PPM, yPos / MainGame.PPM);

		// Create body in the world using body definition
		body = world.createBody(bodyDef);

		// Create shape for fixture
		PolygonShape polygon = new PolygonShape();
		CircleShape circle = new CircleShape();
		circle.setRadius(5 / MainGame.PPM);
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

	// ===================================================================================
	// ====================================  Methods  ====================================
	// ===================================================================================
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
