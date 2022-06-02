/* 
  * Brief Description: 
  * This is the class which the GameFrame will be run. The mian method will run the GameFrame,
  * set the frame to visible, thus run the whole game.
  */ 

class SpellGame {
	public static void main(String[] args) {
		// initialize the frame and set it to visible
		GameFrame game = new GameFrame();
		game.pack();
		game.setVisible(true);
	}
}
