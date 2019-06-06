package mario.game;


public class Levels {

	private int			levelCount;
	private boolean[]	levelComplete;
	private boolean[]	levelUnlocked;

	public Levels(int count) {
		levelCount = count;
		levelComplete = new boolean[levelCount];			//Array for levels completed or not
		levelUnlocked = new boolean[levelCount];
		
		for (int i = 0; i < levelCount; i++) {				//Set all levels to locked at beginning
			levelComplete[i] = false;
			levelUnlocked[i] = false;
		}
		levelUnlocked[0] = true;							//Set first level to be unlocked by default
	}

	public void complete(int level) {
		levelComplete[level - 1] = true;
	}

	public void unlock(int level) {
		if (level <= levelCount)
			levelUnlocked[level - 1] = true;
	}

	public boolean isUnlocked(int level) {
		if (level <= levelCount)
			return levelUnlocked[level - 1];
		else
			return false;
	}
}
