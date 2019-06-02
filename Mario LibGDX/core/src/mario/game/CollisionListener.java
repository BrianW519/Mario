package mario.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import mario.objects.*;
import mario.sprite.Goomba;
import mario.sprite.Mario;
import mario.sprite.Mario.State;


public class CollisionListener implements ContactListener {


	@Override public void beginContact(Contact contact) {
		//Gdx.app.log("Begin Contact", "");
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		//Find the two objects colliding using categories defined
		int collidingObjects = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;


		switch (collidingObjects) {
			//If marios head hits coin or brick
			//====================================================================================================
			case MainGame.MARIO_HEAD | MainGame.COIN:
			case MainGame.MARIO_HEAD | MainGame.BRICK:
				//if Fixture A is marios head
				if (fixA.getFilterData().categoryBits == MainGame.MARIO_HEAD)
					((interactiveObject) fixB.getUserData()).onHeadHit();
				else
					((interactiveObject) fixA.getUserData()).onHeadHit();
				break;

			//If mario is on top of pipe
			//====================================================================================================
			case MainGame.MARIO | MainGame.PIPE:
				//if Fixture A is the pipe
				if (fixA.getFilterData().categoryBits == MainGame.PIPE)
					((Pipe) fixA.getUserData()).active();
				else
					((Pipe) fixB.getUserData()).active();
				break;

			//If an enemy hits an object
			//====================================================================================================
			case MainGame.ENEMY | MainGame.OBJECT:
				//If fixture A is the enemy
				Gdx.app.log("Goomba", "Hit Pipe");
				if (fixA.getFilterData().categoryBits == MainGame.ENEMY)
					((Goomba) fixA.getUserData()).reverseDir();
				else
					((Goomba) fixB.getUserData()).reverseDir();
				break;

		}
	}



	@Override public void endContact(Contact contact) {
		//Gdx.app.log("End Contact", "");
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		//If either collision is Mario
		if (fixA.getUserData() != null && fixB.getUserData() != null) {
			if (Mario.class.isAssignableFrom(fixA.getUserData().getClass())
					|| Mario.class.isAssignableFrom(fixB.getUserData().getClass())) {
				Fixture body;
				Fixture object;

				// if A is the body
				if (Mario.class.isAssignableFrom(fixA.getUserData().getClass())) {
					body = fixA;
					object = fixB;
				} else {
					body = fixB;
					object = fixA;
				}


				// if marios body enters pipe
				if (object.getUserData() != null
						&& Pipe.class.isAssignableFrom(object.getUserData().getClass())) {
					Gdx.app.log("Pipe", "De-Activated");
					((Pipe) object.getUserData()).inactive();
				}
			}
		}
	}

	@Override public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
