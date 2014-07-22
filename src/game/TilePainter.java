package game;

// import graphics stuff
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public final class TilePainter extends Component{
	
	private static final long serialVersionUID = 1L;
	// Default serialized value. Required by all classes that extend Component. 
	// We do not need to worry about this, in this tutorial, as we are not going 
	// to be messing with serializable classes and methods.
	
	private final OnX game;

	public TilePainter(OnX game) {
		this.game = game;
	}
	
	public void paint(Graphics g) {
		// Paint method. Called when the repaint method is called on the JFrame 
		// this is attached to.
		
		Graphics2D g2d = (Graphics2D) g; 
		// Create a Graphics2D out of casting the Graphics provided.
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); 
		// Create Anti-aliasing so our characters are smooth.
		
		g2d.setFont(new Font("Dialogue", Font.BOLD, game.getFontSize())); 
		// Since all the Xs and Os need to be bold, and their is no other text 
		// to be displayed, simply make it bold. Uses the font size provided by 
		// the TicTacToe game.
		
		for (Tile t : game.getTiles()) { 
			g.setColor(Color.BLACK); 
			// Set the color to black, so that we can draw an outline to show 
			// the different Tiles and their boundaries.
			
			g.drawRect(t.getX() - 1, t.getY() - 1, t.getWidth() + 1,
					t.getHeight() + 1); 
			// Drawing the outline using the dimensions of the Tile being shown. 
			// Makes the dimensions either 1px smaller or larger, so that acts 
			// as an outline.
			
			t.paint(g2d); 
			// Call paint on the Tile itself, so that the tile can display itself.
		}
	}

}
