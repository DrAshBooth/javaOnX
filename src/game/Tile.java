package game;

import java.awt.Graphics;

public final class Tile {
	
	private final int x, y, width, height;
	// The x, y, width, and height of this class. Can be final, as we are going 
	// to reuse the same Tiles, and there is no need to change the position once 
	// created.
	
	private boolean claimed = false; 
	// This is used by the getter to tell whether or not this instance is 
	// claimed by a Holder.
	
	private Holder heldBy = Holder.GAME; 
	// This is the holder of this Tile. By default it is held by GAME, which 
	// means no one has claimed it yet.
	
	private final OnX game; 
	// Private final instance of our naughts and crosses game, used to interact 
	// with our game.
	
	public Tile(int x, int y, int width, int height, OnX game) {
		// Constructor. Include the dimensions, and the TicTacToe instance used 
		// for communication.
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.game = game;
	}
	
	public void claim(Holder h) {
		// Used by OnX to claim this Tile under the holder specified in the parameters.
		if (heldBy == Holder.GAME && !claimed) { // Ensure not claimed
			claimed = true;
			heldBy = h;
		}
	}
	
	public Holder getHolder() {
		return heldBy;
	}
	
	public void paint(Graphics g) {
		g.setColor(heldBy.getColor());		// Getting the color to use. Uses the color of the current holder.
		g.fillRect(x, y, width, height);	// Creating the rectangle of the background using the dimensions specified.
		g.setColor(heldBy.getTextColor());	// Getting the color that the text should be. Uses the current holder to get the color.
		g.drawString(heldBy.getText(), middleX()-(game.getFontSize()/3), middleY()+(game.getFontSize()/3));
		// Drawing the text (X or O) specified by the Holder. Subtracts one third 
		// and adds one third of the font size used by the game, so that the text will be centered.
	}
	
	private int middleX() { // Get the middle x value of this Tile
		return (x + width / 2); // Returns the middle x value.
	}

	private int middleY() { // Get the middle y value of this Tile
		return (y + height / 2); // Returns the middle y value.
	}
	
	public void reset() {
		this.heldBy = Holder.GAME;
		this.claimed = false;
	}
	
	public boolean inArea(int x, int y) {
		// Checks whether the x y parameters are within the range of this Tile.
		return (this.x <= x && this.x + this.width >= x)
				&& (this.y <= y && this.y + this.height >= y);
	}
	
	public boolean isClaimed() {
		return claimed;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
