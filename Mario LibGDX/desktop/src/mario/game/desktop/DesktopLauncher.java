package mario.game.desktop;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mario.game.MainGame;


public class DesktopLauncher {

	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 1200;
		config.height = 624;
		//Create New Game Window
		new LwjglApplication(new MainGame(), config);
	}


}

