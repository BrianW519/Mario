package mario.game;

import mario.objects.Mario;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;


public class CollisionListener implements ContactListener {

	@Override public void beginContact(Contact contact) {
		Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
			case MainGame.GROUND_BIT | MainGame.MARIO_BIT:
				((Mario) fixA.getUserData()).brickCollide();

		}

	}



	@Override public void endContact(Contact contact) {

	}



	@Override public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
}
