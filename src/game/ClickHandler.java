package game;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public final class ClickHandler implements MouseListener{
	
	private final OnX game; 

	public ClickHandler(OnX game) {
		this.game = game;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// Mouse released event. Inside this is what happens when the mouse is 
		// released over our game.
		if (!game.isAiTurn()) {
			game.attemptClaim(e.getX() - 3, e.getY() - 26); // Subtracts from the values to eliminate interference from the frame border.
			game.getGameframe().repaint(); // Make sure that changes made here are shown right away.
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
