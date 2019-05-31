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
import mario.sprite.Mario;
import mario.sprite.Mario.State;


public class CollisionListener implements ContactListener {


	@Override public void beginContact(Contact contact) {
		//Gdx.app.log("Begin Contact", "");
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		// Check if either in collision is marios head
		if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
			Fixture head;
			Fixture object;

			// if A is the head
			if (fixA.getUserData() == "head") {
				head = fixA;
				object = fixB;
			} else {
				head = fixB;
				object = fixA;
			}



			// if marios head hits coin or brick
			if (object.getUserData() != null
					&& interactiveObject.class.isAssignableFrom(object.getUserData().getClass())) {
				((interactiveObject) object.getUserData()).onHeadHit();
			}
		}

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
					Gdx.app.log("Pipe", "Activated");
					((Pipe) object.getUserData()).active();
				}
			}
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
