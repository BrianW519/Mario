package mario.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import mario.objects.*;

import mario.sprite.Mario;

public class CollisionListener implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
	Gdx.app.log("Begin Contact", "");
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
	    
	    //if marios head hits coin or brick
	    if (object.getUserData() != null && interactiveObject.class.isAssignableFrom(object.getUserData().getClass())) {
		((interactiveObject) object.getUserData()).onHeadHit();
	    }
	}
	
	/*
	     Fixture fixA = contact.getFixtureA();
		Fixture fixB = contact.getFixtureB();

		int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

		switch (cDef) {
		case MainGame.MARIO_HEAD_BIT | MainGame.BRICK_BIT:
		case MainGame.MARIO_HEAD_BIT | MainGame.COIN_BIT:
		    if (fixA.getFilterData().categoryBits == MainGame.MARIO_HEAD_BIT)
			((interactiveObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
		    else
			((interactiveObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
		    break;
		}
	     */
    }

    @Override
    public void endContact(Contact contact) {
	Gdx.app.log("End Contact", "");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
	// TODO Auto-generated method stub

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
	// TODO Auto-generated method stub

    }
}
