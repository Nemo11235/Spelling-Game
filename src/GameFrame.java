/* 
  * Brief Description: 
  * This is the class that contains all the GUI compoents and the main body of the game. The class will first set the 
  * screen to the center of the screen and then maximize it. And then it will put all the GUI components in the places
  * where they belong to. 
  * 
  * The game will first ask for player's name and keep verifying until the player put in his name in correct format.
  * And then there will be a short description about how the game works, then the game will start.
  * Player has to complete the wrod on the screen refering to the picture by clicking the letters at the bottom of 
  * the screen. The player will get one point for each word he completed, and loss the game if he press a letter that
  * is not one of the missing letters. After he win/loss the game, his score will be shown on the screen as well as
  * the leaderboard of the top five players in the history. If he beat one of the top five, his name will be on the 
  * leaderboard. 
  * 
  * The class also contains all the actinos for the buttons.
  */ 

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import java.lang.*;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.applet.*;
import javax.sound.sampled.*;
import javax.sound.midi.*;

@SuppressWarnings({ "unused", "serial" })
public class GameFrame extends JFrame
{	
	// variables regarding the organization of the frame
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension d = tk.getScreenSize();
	private GridBagConstraints c;

	// Strings, arrays and arrayLists about the word and questions
	private ArrayList<String> wordList;
	private ArrayList<Character> missingLetters;
	private String[] words;
	public String word = "";
	public String question;
	
	// GUIComponents
	private JLabel wordLabel;		// the label for the question
	private JLabel imageLabel;		// label that hold the corresponding image
	private JLabel scoreLabel;		// label that hold the score
	private ImageIcon image; 		// declare the images corresponds to the words
	private ArrayList<JButton> buttonList = new ArrayList<JButton>(); // the ArrayList for the letter buttons;
	private char buttonChar; 		// the index for referring to a button
	private JButton leaderBoardBttn,stopButton,startButton,buttonA,buttonB,buttonC,buttonD,buttonE,buttonF,buttonG,buttonH,buttonI,buttonJ,buttonK,buttonL,
	buttonM,buttonN,buttonO,buttonP,buttonQ,buttonR,buttonS,buttonT,buttonU,buttonV,buttonW,buttonX,buttonY,buttonZ;
	
	// variables about file I/O
	private String inputFile;
	private String playerName;
	private Player player;
	private Scanner scan;
	
	// counters
	private int score = 0;
	public int currentIndex = 0;
	private int numOfWords = 0;
	private int letterA = 'A';
	
	// array of sounds
	URL localUrl;
	@SuppressWarnings("deprecation")
	AudioClip songHolder[] = new AudioClip[3];
	
	
	
	// constructor to the frame
	@SuppressWarnings("deprecation")
	public GameFrame()
	{
		super("Spelling Game");							// set the title of the frame
		player = new Player("", 0);						// initiate an empty player with no info
		playerName = Validator.validateUserName("Welcome to the game!\nPlease enter your name(FML):\n");
		player.setName(playerName);  					// set the player's name
		JOptionPane.showMessageDialog(null, "Look at the picture on the right, complete the word on the left by clicking the letters"
				+ " at the bottom! \nYou will get one point for each word you complete successfully, no mistakes allowed. Have fun!");

		centerWindow(this); 							// center the window
		setLayout(new GridBagLayout()); 				// set the layout
		setExtendedState(JFrame.MAXIMIZED_BOTH); 		// set it to maximum
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set default close operation
		setResizable(false); 							// set resizable to false
		wordList = new ArrayList<String>();
		
		// get the list of words from the .txt file
		File inputFile = new File("src/words.txt");
		try
		{
			scan = new Scanner(inputFile);
			while(scan.hasNextLine())
			{	
				word = scan.nextLine();
				wordList.add(word);
			}
			scan.close();
			// convert the ArrayList into an array of String
			words = new String[wordList.size()];
			words = wordList.toArray(words);
		}
		catch(FileNotFoundException e)
		{
			System.out.println(e);
		} // end of getting audio

		// create the GUI components
		createGUIComponents();
		
		// initiate the array to hold the audio objects
		String musicName[] = new String[3];
		musicName[0] = "src/sounds/SEASHORE.wav"; 				//background music
		musicName[1] = "src/sounds/correct.wav"; 				//correct music
		musicName[2] = "src/sounds/incorrect.wav"; 				//incorrect music

		// get all the audio from the file
		for(int a = 0; a < 3; a ++) {
			try {
				localUrl = new URL("file:" + new File(".").getCanonicalPath() + "/" + musicName[a]);
				songHolder[a] = Applet.newAudioClip(localUrl);	//Get the music object
			}
			catch (IOException e) {
				System.out.println(e);
			}
		}
		
		 //starts the background music (constant)
		songHolder[0].loop();
	}
	// method that add all the components into the frame
	private void createGUIComponents()
	{
		/*
		 * words: String array that contains all the words
		 * word: String of the current word
		 * question: String of the question string, which is the word with several underscore
		 * wordLabel: Jlabel for the question
		 * missingLetters: ArrayList that contains all the missing letters in the question
		 */
		setLayout(new GridBagLayout());
		c = new GridBagConstraints();								// initiate the constraints
		words = shuffle(words); 									// shuffle words
		word = words[currentIndex]; 								// get the first word in the array
		question = makeQuestion(word); 								// make it into a question
		wordLabel = new JLabel(question); 							// make it a label and add it to the frame
		wordLabel.setFont(new Font("Courier", Font.PLAIN, 120)); 	// set the font and size of the label
		wordLabel.setForeground(new Color((int)(Math.random() * 0x1000000)));
		c = setupConstraints(0,0,1,1, GridBagConstraints.CENTER);
		add(wordLabel, c);
		missingLetters = makeArrayOfMissing(word, question); // make an array of missing letters to check
		
		// add the image correspond to the word
		image = new ImageIcon("src/images/" + word + ".jpg");
		imageLabel = new JLabel(image);
		c = setupConstraints(1,0,1,1, GridBagConstraints.EAST);
		add(imageLabel, c);
		
		// initiate the letter panel which will contains 26 clickable image buttons
		JPanel letterPanel = new JPanel();
		
		// set the layout for the letter panel
		letterPanel.setLayout(new GridLayout(2,13));
		letterPanel.setPreferredSize(new Dimension(13*100, 2*100));
		
		// initiate the buttons and add them to the buttonList
		buttonList.add(buttonA = new JButton());
		buttonList.add(buttonB = new JButton());
		buttonList.add(buttonC = new JButton());
		buttonList.add(buttonD = new JButton());
		buttonList.add(buttonE = new JButton());
		buttonList.add(buttonF = new JButton());
		buttonList.add(buttonG = new JButton());
		buttonList.add(buttonH = new JButton());
		buttonList.add(buttonI = new JButton());
		buttonList.add(buttonJ = new JButton());
		buttonList.add(buttonK = new JButton());
		buttonList.add(buttonL = new JButton());
		buttonList.add(buttonM = new JButton());
		buttonList.add(buttonN = new JButton());
		buttonList.add(buttonO = new JButton());
		buttonList.add(buttonP = new JButton());
		buttonList.add(buttonQ = new JButton());
		buttonList.add(buttonR = new JButton());
		buttonList.add(buttonS = new JButton());
		buttonList.add(buttonT = new JButton());
		buttonList.add(buttonU = new JButton());
		buttonList.add(buttonV = new JButton());
		buttonList.add(buttonW = new JButton());
		buttonList.add(buttonX = new JButton());
		buttonList.add(buttonY = new JButton());
		buttonList.add(buttonZ = new JButton());
		
		// actions for music control
		class musicActions implements ActionListener
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{
				Object source = e.getSource();
				if (source == stopButton) 
					songHolder[0].stop();
				if (source == startButton)
					songHolder[0].loop();
			}
		}
		
		// the action for leaderboard button
		class leaderboardAciton implements ActionListener
		{
			public void actionPerformed(ActionEvent e)
			{
				Object source = e.getSource();
				if (source == leaderBoardBttn)
				{
					LinkedList<Player> players = Player.getPlayers("src/leaderboard.txt");
					Collections.sort(players, Collections.reverseOrder());
					String leaderBoard = "";
					int i = 0;
					for (Player p : players) {
						if (i > 4)
							break;
						leaderBoard += p.toString();
						i++;
					}
					JOptionPane.showMessageDialog( null, new JTextArea(leaderBoard),
					         "Top Five", JOptionPane.INFORMATION_MESSAGE );
				}
					
			}
		}
		
		// actions for the button events
		class buttonActions implements ActionListener
		{
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e)
			{
				Object source = e.getSource();
				buttonChar = (char)(buttonList.indexOf(source) + 97); // this will be the corresponding char of the button
				char[] questionChars = question.toCharArray();  	  // convert the question to an array of char
				char[] wordChars = word.toCharArray(); 				  // convert the correct word to an array of char
				
				// if player clicked the correct button
				if(missingLetters.contains(buttonChar))
				{
					// fill in all blanks that contains that letter
					for (int i = 0; i < word.length(); i ++)
					{
						if (wordChars[i] == buttonChar)
						{
							songHolder[1].play(); 					  //cheer sound plays if player gets correct letter
							questionChars[i] = buttonChar;

						}
					}
					// update the question string and the label
					question = new String(questionChars);
					wordLabel.setText(question);
					c = setupConstraints(0,0,1,1, GridBagConstraints.CENTER);
					add(wordLabel, c);
					
					// if player completed the word and not at the last word
					if (question.equalsIgnoreCase(word) && score < wordList.size())
					{
						score++;
						scoreLabel.setText("Score: " + score);
						// if player finished the last word
						if (score == wordList.size())
						{
							JOptionPane.showMessageDialog(null, "Congradulation! " + playerName + ", you completed all the words!" + 
						"\n Your final score is: " + score);
							player = new Player(playerName, score);
							LinkedList<Player> players = Player.getPlayers("src/leaderboard.txt");
							players.add(player);
							Collections.sort(players, Collections.reverseOrder());
							String leaderBoard = "";
							int i = 0;
							for (Player p : players) {
								if (i > 4)
									break;
								leaderBoard += p.toString();
								i++;
							}
							JOptionPane.showMessageDialog( null, new JTextArea(leaderBoard),
							         "Top Five", JOptionPane.INFORMATION_MESSAGE );
							try {
								TextWriter.saveToFile("src/leaderboard.txt", leaderBoard);
							} catch (IOException ex) {
								// TODO Auto-generated catch block
								ex.printStackTrace();
							}
							System.exit(0);
						}
						// go to the next word, make it a new question, update the labels
						word = words[++currentIndex]; 	// get the first word in the array
						question = makeQuestion(word); 	// make it into a question
						wordLabel.setText(question); 	// make it a label and add it to the frame
						wordLabel.setForeground(new Color((int)(Math.random() * 0x1000000))); // new color
						c = setupConstraints(0,0,1,1, GridBagConstraints.CENTER);
						add(wordLabel, c);
						missingLetters = makeArrayOfMissing(word, question);
						imageLabel.setIcon(new ImageIcon("src/images/" + word + ".jpg"));
						setIconImage(image.getImage());
					}
				}
				else // this part runs when the player clicks the wrong button
				{
					songHolder[2].play(); //boo sound plays if you lose
					JOptionPane.showMessageDialog(null, "Sorry " + playerName + ", You lost the game,"
							+ " the correct word is: " + word + "\n Your final score is: " + score);
					player = new Player(playerName, score);
					LinkedList<Player> players = Player.getPlayers("src/leaderboard.txt");
					players.add(player);
					Collections.sort(players, Collections.reverseOrder());
					String leaderBoard = "";
					int i = 0;
					for (Player p : players) {
						if (i > 4)
							break;
						leaderBoard += p.toString();
						i++;
					}
					JOptionPane.showMessageDialog( null, new JTextArea(leaderBoard),
					         "Top Five", JOptionPane.INFORMATION_MESSAGE );
					try {
						TextWriter.saveToFile("src/leaderboard.txt", leaderBoard);
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
					System.exit(0);
				}
			}
		}
		
		// declare the action objects
		buttonActions buttonActions = new buttonActions();
		musicActions musicActions = new musicActions();
		leaderboardAciton lbAction = new leaderboardAciton();
		
		// add the buttons to the action listener and to the panel
		for (JButton button : buttonList)
		{
			String picturePath = "src/letterPictures/" + (char)(letterA + buttonList.indexOf(button)) + ".jpg";
			button.setIcon(new ImageIcon(picturePath));
			button.addActionListener(buttonActions);
			letterPanel.add(button);
		}
		
		// add the letterPanel to the frame
		c = setupConstraints(0,1,2,1, GridBagConstraints.SOUTH);
		add(letterPanel, c);
		
		// setup and add the music control button to the frame
		stopButton = new JButton("Music Off");
		startButton = new JButton("Music On");
		leaderBoardBttn = new JButton("Leader Board");
		leaderBoardBttn.addActionListener(lbAction);
		stopButton.addActionListener(musicActions);
		startButton.addActionListener(musicActions);
		c = setupConstraints(1,3,1,1, GridBagConstraints.SOUTHEAST);
		add(leaderBoardBttn, c);
		c = setupConstraints(0,2,1,1, GridBagConstraints.SOUTHWEST);
		add(stopButton, c);
		c = setupConstraints(0,3,1,1, GridBagConstraints.SOUTHWEST);
		add(startButton,c);
		
		// set up and add the label which displays the current score to the frame
		scoreLabel = new JLabel("Score: " + score);
		c = setupConstraints(1,2,1,1, GridBagConstraints.SOUTHEAST);
		add(scoreLabel, c);
	} // end of greatGUIComponents
	
	// this method return a random integer from min to max
	public static int randInt(int min, int max)
	{
		return min + (int)(Math.random() * ((max - min) + 1));
	} // end of readInt
	
	// this method returns an ArrayList that contains all the missing letters in the question
	public ArrayList<Character> makeArrayOfMissing(String word, String question)
	{
		ArrayList<Character> missingLetters = new ArrayList<Character>();
		for (int i = 0; i < word.length(); i++)
		{
			if (question.charAt(i) == '_')
				missingLetters.add(word.charAt(i));
		}
		return missingLetters;
	} // end of makeArrayOfMissing
	
	// this method shuffle an array and return a new one
	public String[] shuffle(String[] og)
	{
		Random rgen = new Random();  // Random number generator			
		for (int i=0; i<og.length; i++) 
		{
		    int randomPosition = rgen.nextInt(og.length);
		    String temp = og[i];
		    og[i] = og[randomPosition];
		    og[randomPosition] = temp;
		}
		return og;
	} // end of shuffle
	
	// this method takes away random number of chars in the word, replace those chars with underscore
	public String makeQuestion(String word)
	{
		char[] chars = word.toCharArray();
		int numMissing = randInt(1, word.length()-1);
		int indexMissing;
		
		for (int i = 0; i < numMissing; i++)
		{
			do
			{
				indexMissing = randInt(0, word.length()-1);
			} while (chars[indexMissing] == '_');
			chars[indexMissing] = '_';
		}
		
		return new String(chars);
	} // end of makeQuestion
	
	// method that set up the grid bag constraints
	private GridBagConstraints setupConstraints(int gridx, int gridy, int gridwidth, int gridheight, int anchor)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = gridx; 
		c.gridy = gridy;
		c.insets = new Insets(5,5,5,5);
		c.gridwidth = gridwidth;
		c.gridheight = gridheight;
		c.anchor = anchor;
		c.ipadx = c.ipady = 0;
		return c;
	} // end of setupConstraints
	
	// method that sets the window to the center of the screen
	public void centerWindow(Window w)
	{
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension d = tk.getScreenSize();
		setLocation((d.width-w.getWidth())/2, (d.height-w.getHeight())/2);
	} // end of centerWindow
}


