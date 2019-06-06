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

		//Make sure both arent deleted bodies
		if (fixA != null & fixB != null) {
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

				//If an enemy hits an object or another enemy
				//====================================================================================================
				case MainGame.ENEMY | MainGame.OBJECT:
				case MainGame.ENEMY | MainGame.COIN:
				case MainGame.ENEMY | MainGame.ENEMY:
				case MainGame.ENEMY | MainGame.BRICK:
					//Two ifs so both goombas can reverse direction
					if (fixA.getFilterData().categoryBits == MainGame.ENEMY)
						((Goomba) fixA.getUserData()).reverseDir();
					if (fixB.getFilterData().categoryBits == MainGame.ENEMY)
						((Goomba) fixB.getUserData()).reverseDir();
					break;

				//If mario jumps on enemy
				//====================================================================================================
				case MainGame.MARIO | MainGame.ENEMY_HEAD:
					//If fixture A is the enemy
					Gdx.app.log("Goomba", "Squashed");
					if (fixA.getFilterData().categoryBits == MainGame.ENEMY_HEAD)
						((Goomba) fixA.getUserData()).onHeadHit();
					else
						((Goomba) fixB.getUserData()).onHeadHit();
					break;

				//If mario gets hit by enemy
				//====================================================================================================
				case MainGame.MARIO | MainGame.ENEMY:
					//If fixture A is the enemy
					Gdx.app.log("Mario", "Hit By Enemy | Lost Life");
					if (fixA.getFilterData().categoryBits == MainGame.MARIO)
						((Mario) fixA.getUserData()).loseLife();
					else
						((Mario) fixB.getUserData()).loseLife();
					break;


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
