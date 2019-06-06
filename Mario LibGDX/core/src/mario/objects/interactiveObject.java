package mario.objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import mario.screens.GameScreen;
import mario.sprite.Mario;


public abstract class interactiveObject {

	protected World			world;
	protected TiledMap		map;
	protected Rectangle		bounds;
	protected Body			body;
	protected GameScreen	screen;
	protected MapObject		object;

	protected Fixture		fixture;

	public interactiveObject(GameScreen screen, MapObject object) {
		this.object = object;
		this.screen = screen;
		this.world = screen.getWorld();
		this.map = screen.getMap();
		this.bounds = ((RectangleMapObject) object).getRectangle();						//Get rectangle size from map
		
		
		//*****Create body and fixture*****
		BodyDef bdef = new BodyDef();
		FixtureDef fdef = new FixtureDef();
		PolygonShape shape = new PolygonShape();

		bdef.type = BodyDef.BodyType.StaticBody;
		bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / GameScreen.PPM,
				(bounds.getY() + bounds.getHeight() / 2) / GameScreen.PPM);

		body = world.createBody(bdef);

		shape.setAsBox(bounds.getWidth() / 2 / GameScreen.PPM, bounds.getHeight() / 2 / GameScreen.PPM);
		fdef.shape = shape;
		fixture = body.createFixture(fdef);

	}

	//Method for when marios head hits object
	public abstract void onHeadHit();

	//Set the filter ID
	public void setCategoryFilter(short filterID) {
		Filter filter = new Filter();
		filter.categoryBits = filterID;
		fixture.setFilterData(filter);
	}

	//Get where the object is on the map
	public TiledMapTileLayer.Cell getCell() {
		TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
		return layer.getCell((int) (body.getPosition().x * GameScreen.PPM / 16),
				(int) (body.getPosition().y * GameScreen.PPM / 16));
	}

}
