package game;

import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;

public final class OnX extends JApplet{
	
	private static final long serialVersionUID = 1L;
	private final Tile[] TILES = new Tile[9]; // The final array of Tiles. Final because we will only ever need the specified amount, in this case, 9.
	private final int TILE_SPACING = 96; // This is the final integer primitive of how far apart (in pixels) the Tiles are.
	private final int WIDTH = 96, HEIGHT = 96; // Normal dimensions for our Tiles to use.
	private final JFrame GAMEFRAME = new JFrame("Naughts and Crosses"); // The game frame. The whole game will be shown on this Object. Final because we never need to make a new one.
	private final TilePainter PAINTER = new TilePainter(this); // This will get added into GAMEFRAME so that the repaint call will affect it. Final because, just like the GAMEFRAME, it will never need to be reinstantiated.
	private final ClickHandler CLICK_HANDLER = new ClickHandler(this); // Creates the ClickHandler to get attached to GAMEFRAME to handle clicks. Is declared final for the same reason as PAINTER.
	private final boolean AI;
	private boolean aiTurn = false;
	private Holder turn = Holder.X; // Current holder. Instantiates as X.
	private int whoseTurn = 0; // Whose turn is it? Used to tell when to swap who startes the game.
	private final Dimension FRAME_SIZE = new Dimension(295, 304); // Creates the dimensions to be added to GAMEFRAME. Represents the only size our game will ever become. Final because it will never change.
	private final int FONT_SIZE = 64; // The integer primitive that represents the size of all global fonts, to this game.
	private int oWins = 0; // How many times the player O has won.
	private int xWins = 0; // How many times the player X has won.
	private boolean gameOver = false; // Whether or not to stop the game loop, and start a new game.
	private boolean nextTurn = false; // Used to tell the game loop when it is time to switch players.
	public final AI GAME_AI = new AI(this);
	
	public void init() {
		OnX game = new OnX(true);
		game.newGame();
	}
	
	private final int[][] WINS = { { 1, 1, 1, 0, 0, 0, 0, 0, 0 },

	{ 0, 0, 0, 1, 1, 1, 0, 0, 0 },

	{ 0, 0, 0, 0, 0, 0, 1, 1, 1 },

	{ 1, 0, 0, 0, 1, 0, 0, 0, 1 },

	{ 1, 0, 0, 1, 0, 0, 1, 0, 0 },

	{ 0, 1, 0, 0, 1, 0, 0, 1, 0 },

	{ 0, 0, 1, 0, 0, 1, 0, 0, 1 },

	{ 0, 0, 1, 0, 1, 0, 1, 0, 0 } }; 
	/* This array represents all possible winning arrangements. The reason they 
	are 1 and 0 is because the game's win checker will use the current turn, and 
	the 1s mean that the current player has to control this tile. If the player 
	controls all 3 tiles that are represented as 1, then they win this game, 
	and gameOver is set to true.
	*/
	
	public boolean allFull() { 
		/* Returns whether or not any more moves can be made. Positive means 
		that all tiles are claimed, and false means there are still move(s) to 
		be made. */
		for (Tile t : TILES) { 
			if (!t.isClaimed()) { // If even one tile isn't claimed yet, return false, as there is atleast 1 more move to be made.
				return false;
			}
		}
		return true;
	}

	public boolean hasWon(Holder h) { // Check whether the specified holder has won the game.
		boolean hasWon; // Uses this value to check whether or not they have won.
		for (int[] i : WINS) {
			hasWon = true; // Set it to true. Unless proved otherwise, this player has won.
			for (int j = 0; j < i.length; j++) { // Iterate through the possible wins.
				if (i[j] == 1) { // If the encountered value is a 1 (Meaning the player must control this tile)...
					if (TILES[j].getHolder() != h) { // If the player does NOT control this tile...
						hasWon = false; // Proves otherwise (Sets hasWon to
										// false, meaning this win combo is not
										// possible.
						j = i.length; // Set j to the length of this win, so
										// that we can exit this loop, as we
										// have already proven the player has
										// not won this one.
					}
				}
			}
			if (hasWon) // If hasWon is still true, then they must have won.
				return true; // Return true, as the player has won.
		}
		return false; // Else, if it gets throught the entire wins array, and
						// non of them have been achieved, return false, this
						// player has not won.
	}
	
	public int toWin(Holder h, int[] win, Tile[] tiles) {
		int total = 0;
		for (int j = 0; j < win.length; j++) {
			if (win[j] == 1) {
				total++;
				if (tiles[j].getHolder() == h) {
					total--;
				} else if (tiles[j].getHolder() != Holder.GAME) {
					return -1;
				}
			}
		}
		return total;
	}

	public Tile getWin(Holder h, int[] win, Tile[] tiles) {
		for (int j = 0; j < win.length; j++) {
			if (win[j] == 1) {
				if (tiles[j].getHolder() == Holder.GAME) {
					return TILES[j];
				}
			}
		}
		return null;
	}

	public void endGame() {
		gameOver = true;
	}

	public int getFontSize() { // Used by the other classes to get the font size they should use.
		return FONT_SIZE; // Return the specified font size.
	}

	public OnX(boolean ai) { // Constructor
		this.AI = ai;
		PAINTER.setSize(FRAME_SIZE); // Set the size of PAINTER to that of FRAME_SIZE.
		buildFrame(); // Frame Builder method. Only used once, but it improves code readability.
		loadTiles(); // Load the tiles. Gets them set up with the correct coordinates and dimensions.
	}
	
	public void loadTiles() { // Loads the tiles (Gets them ready for use).
		int tile = 0; // Current tile being set. Increases each time a tile is set. Starts at 0, ends at 8.
		for (int i = 0; i < TILES.length / 3; i++) { // iterate through TILES by a third of the size of our game (3). This iteration is the columns.
			for (int j = 0; j < TILES.length / 3; j++) { // iterate throught TILES in a similar maner as above. This iteration is the rows.
				TILES[tile] = new Tile(i * this.TILE_SPACING, j * this.TILE_SPACING, this.WIDTH, this.HEIGHT, this); 
				// Instantiate a new tile at the specified position, using the 
				// default values. Uses the iteration value * whatever the 
				// default size is, so that they will be equally spread apart.
				tile++; // Increases tile by 1, so that next time, it will be a different tile being set.
			}
		}
	}
	
	private void nextTurn() { 
		// Used to switch turns. Called each time a valid move is made.
		if (hasWon(turn)) { // Check to see if the move the current player made was a winning move.
			gameOver = true; // If it was, then set gameOver to true;
			sendWin(turn); // Also, display a window informing the user(s) of what occured. Also shows scores.
			return; // Exit this method. Nothing left to do.
		}
		if (allFull()) { // If there are no moves left, and the last players move wasn't a winning one...
			gameOver = true; // The game is over
			sendDraw(); // Call the method that creates a frame informing the user(s) of the draw. Also shows scores.
			return; // Exit this method. Nothing left to do.
		}
		turn = turn.getOpposite(); // Switch turns.
		if (aiTurn && AI) {
			aiTurn = false;
		} else if (!aiTurn && AI) {
			aiTurn = true;
			GAME_AI.aiMove();
		} else {
			aiTurn = false;
		}
	}
	
	public void attemptClaim(int x, int y) { 
		// Used to claim a Tile. Checks to get which tile is within coordinates, 
		// and then if it isn't claimed, claims it.
		for (int i = 0; i < TILES.length; i++) { // Iterate through all the tiles.
			if (!TILES[i].isClaimed() && TILES[i].inArea(x, y)) { // If the tile isn't claimed, and the tiles is in the required area.
				TILES[i].claim(turn); // Claim the tile under the current player.
				nextTurn = true; // on next game loop, ensure that the players will be switched, as the current players turn has ended.
				return; // Return. Nothing left to do.
			}
		}
	}
	
	public boolean isAiTurn() {
		return aiTurn;
	}
	
	private void buildFrame() { // Sets up GAMEFRAME.
		getGameframe().addMouseListener(CLICK_HANDLER); // Add ClickHandler
		getGameframe().setSize(FRAME_SIZE); // Add the dimensions
		getGameframe().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Make it so game exits when this frame closes.
		getGameframe().setResizable(false); // Make it so that this frame is always the same size.
		getGameframe().setMaximumSize(FRAME_SIZE); // Honestly, these probably arn't needed, but just in case.
		getGameframe().setMinimumSize(FRAME_SIZE);
		getGameframe().add(PAINTER); // Add the TilePainter.
		getGameframe().pack(); // Pack all the changes.
	}
	
	private JFrame outcome = new JFrame();
	
	private void sendWin(Holder winner) { 
		// Create the win frame based on the winner defined in parameters.
		outcome.setVisible(false); // If there was already a copy of outcome in use, remove it from site.
		outcome.dispose(); // Remove any current outcome's members.
		outcome = null; // Destroy any current outcome.
		if (winner == Holder.X) // If the winner is X
			xWins++; // X wins +1
		else if (winner == Holder.O) // Else if the winner is O
			oWins++; // O wins +1
		outcome = new JFrame(winner.getText() + " has won!"); // Create new JFrame with winning title.
		JLabel winMessage = new JLabel(" " + winner.getText()
				+ " has won! Score is X: " + xWins + ", O: " + oWins); // Create a message that informs of win, and displays current score.
		outcome.add(winMessage); // Add the message to the JFrame.
		outcome.setResizable(false); // Set the size to constant.
		outcome.setAlwaysOnTop(true); // Make it always on top.
		outcome.pack(); // Pack it.
		outcome.setVisible(true); // Set it visible so it can be seen.
	}
	
	private void sendDraw() { // Send a JFrame with Draw. Practically the same as sendWin, but different event.
		outcome.setVisible(false); // If any current instance is visible, not any more.
		outcome.dispose(); // Remove members.
		outcome = null; // Destroy
		outcome = new JFrame("Draw!"); // Declare the match a draw.
		JLabel drawMessage = new JLabel(" Its a Draw! Score is X: " + xWins
				+ ", O: " + oWins); // Display Draw, and score.
		outcome.add(drawMessage); // Add the message to the JFrame.
		outcome.setResizable(false); // Set the size to constant.
		outcome.setAlwaysOnTop(true); // Make it always on top.
		outcome.pack(); // Pack it.
		outcome.setVisible(true); // Set it visible to be seen.
	}
	
	private void resetTiles() { // Resets the tiles
		for (Tile t : TILES) // Iterate through all the tiles.
			t.reset(); // Reset each tile.
	}
	
	public void newGame() { // Called when our game is executed. This is the game loop.
		while (true) { // Repeat forever. Makes sure you can keep playing using the same settings, as this way, it just repeats the game startup process.
			gameOver = false; // Game is no longer over.
			resetTiles(); // Reset the tiles to clear the data from the previous game.
			GAMEFRAME.setVisible(true); // Set GAMEFRAME visible, so you can see it.
			if (whoseTurn == 1 && this.AI) {
				whoseTurn = 0;
				turn = Holder.O;
				aiTurn = true;
			} else if (this.AI) {
				turn = Holder.X;
				aiTurn = false;
				whoseTurn = 1;
			} else {
				turn = Holder.X;
			}
			if (aiTurn)
				GAME_AI.aiMove();
			while (!gameOver) { // Loop until game over is true.
				if (nextTurn) { // If nextTurn was set to true..
					nextTurn = false; // set back to false, so it doesn't repeat next loop.
					nextTurn(); // Switch turns.
				}
				try {
					TimeUnit.MILLISECONDS.sleep(25); // Wait 25 milliseconds. Added to decrease the load on your CPU from constant updates.
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				GAMEFRAME.repaint(); // Repaint. Displays any changes made.
			}
				 // Game is now over.

			try {
				TimeUnit.SECONDS.sleep(5); // Wait 5 seconds, so user(s) can see why they lost/won/ had a draw.
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			getGameframe().setVisible(false); // Remove GAMEFRAME from sight so it's members can be reset.
		}
	}
	
	public int[][] getWins() {
		return WINS;
	}
	
	public Holder getTurn() { // Returns the current turn (player who is playing).
		return turn;
	}

	public Tile[] getTiles() { // Returns TILES so that other classes can use them.
		return TILES;
	}

	public static void main(String[] args) {
		OnX game = new OnX(false); // Instantiate new instance of TicTacToe.
		if (args.length >= 1) {
			game = new OnX(false);
			game.newGame();
		} else {
			game = new OnX(true);
			game.newGame();
		}
	}
	
	public JFrame getGameframe() {
		return GAMEFRAME;
	}

}
