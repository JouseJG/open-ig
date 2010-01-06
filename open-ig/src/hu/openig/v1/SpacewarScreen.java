/*
 * Copyright 2008-2009, David Karnok 
 * The file is part of the Open Imperium Galactica project.
 * 
 * The code should be distributed under the LGPL license.
 * See http://www.gnu.org/licenses/lgpl.html for details.
 */

package hu.openig.v1;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * The spacewar screen.
 * @author karnokd, 2010.01.06.
 * @version $Revision 1.0$
 */
public class SpacewarScreen extends ScreenBase {
	/** A three phase button. */
	class ThreePhaseButton {
		/** The X coordinate. */
		int x;
		/** The Y coordinate. */
		int y;
		/** The three phases: normal, selected, selected and pressed. */
		BufferedImage[] phases;
		/** Selected state. */
		boolean selected;
		/** Pressed state. */
		boolean pressed;
		/** The action to perform on the press. */
		Act action;
		/**
		 * Constructor.
		 * @param phases the phases
		 */
		public ThreePhaseButton(BufferedImage[] phases) {
			this.phases = phases;
		}
		/**
		 * Constructor.
		 * @param x the x coordinate
		 * @param y the y coordinat
		 * @param phases the phases
		 */
		public ThreePhaseButton(int x, int y, BufferedImage[] phases) {
			this.x = x;
			this.y = y;
			this.phases = phases;
		}
		/** 
		 * Render the button.
		 * @param g2 the graphics object
		 */
		public void paintTo(Graphics2D g2) {
			if (pressed) {
				g2.drawImage(phases[2], x, y, null);
			} else
			if (selected) {
				g2.drawImage(phases[1], x, y, null);
			} else {
				g2.drawImage(phases[0], x, y, null);
			}
		}
		/**
		 * Test if the mouse is within this button.
		 * @param mx the mouse X coordinate
		 * @param my the mouse Y coordinate
		 * @return true if within the button
		 */
		public boolean test(int mx, int my) {
			return mx >= x && my >= y && mx < x + phases[0].getWidth() && my < y + phases[0].getHeight();
		}
		/** Invoke the associated action if present. */
		public void invoke() {
			if (action != null) {
				action.act();
			}
		}
	}
	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#finish()
	 */
	@Override
	public void finish() {
		// TODO Auto-generated method stub

	}
	/** The group for the main buttons. */
	List<ThreePhaseButton> mainCommands;
	/** The view toggle buttons. */
	List<ThreePhaseButton> viewCommands;
	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#initialize()
	 */
	@Override
	public void initialize() {
		mainCommands = new ArrayList<ThreePhaseButton>();
		mainCommands.add(new ThreePhaseButton(33, 24, commons.spacewar.stop));
		mainCommands.add(new ThreePhaseButton(33 + 72, 24, commons.spacewar.move));
		mainCommands.add(new ThreePhaseButton(33, 24 + 35, commons.spacewar.kamikaze));
		mainCommands.add(new ThreePhaseButton(33 + 72, 24 + 35, commons.spacewar.attack));
		mainCommands.add(new ThreePhaseButton(33, 24 + 35 * 2, commons.spacewar.guard));
		mainCommands.add(new ThreePhaseButton(33 + 72, 24 + 35 * 2, commons.spacewar.rocket));
		
		viewCommands = new ArrayList<ThreePhaseButton>();
		
		viewCommands.add(new ThreePhaseButton(33, 24 + 35 * 3, commons.spacewar.command));
		viewCommands.add(new ThreePhaseButton(33 + 72, 24 + 35 * 3, commons.spacewar.damage));
		viewCommands.add(new ThreePhaseButton(33, 24 + 35 * 3 + 30, commons.spacewar.fireRange));
		viewCommands.add(new ThreePhaseButton(33 + 72, 24 + 35 * 3 + 30, commons.spacewar.grid));
	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#keyTyped(int, int)
	 */
	@Override
	public void keyTyped(int key, int modifiers) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#mouseMoved(int, int, int, int)
	 */
	@Override
	public void mouseMoved(int button, int x, int y, int modifiers) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#mousePressed(int, int, int, int)
	 */
	@Override
	public void mousePressed(int button, int x, int y, int modifiers) {
		boolean needRepaint = false;
		// the command panel
		if (x < commons.spacewar.commands.getWidth() && y < commons.spacewar.commands.getHeight() + 20 + commons.spacewar.frameTopLeft.getHeight()) {
			for (ThreePhaseButton btn : mainCommands) {
				if (btn.test(x, y)) {
					btn.selected = true;
					btn.pressed = true;
					needRepaint = true;
					for (ThreePhaseButton btn2 : mainCommands) {
						if (btn != btn2) {
							btn2.pressed = false;
							btn2.selected = false;
						}
					}
					btn.invoke();
					break;
				}
			}
			for (ThreePhaseButton btn : viewCommands) {
				if (btn.test(x, y)) {
					btn.selected = !btn.selected;
					btn.pressed = true;
					needRepaint = true;
					btn.invoke();
					break;
				}
			}
		}
		if (needRepaint) {
			repaint();
		}
	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#mouseReleased(int, int, int, int)
	 */
	@Override
	public void mouseReleased(int button, int x, int y, int modifiers) {
		boolean needRepaint = false;
		for (ThreePhaseButton btn : mainCommands) {
			if (btn.pressed) {
				btn.pressed = false;
				needRepaint = true;
				break;
			}
		}
		for (ThreePhaseButton btn : viewCommands) {
			if (btn.pressed) {
				btn.pressed = false;
				needRepaint = true;
				break;
			}
		}
		if (needRepaint) {
			repaint();
		}
	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#mouseScrolled(int, int, int, int)
	 */
	@Override
	public void mouseScrolled(int direction, int x, int y, int modifiers) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#onEnter()
	 */
	@Override
	public void onEnter() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#onLeave()
	 */
	@Override
	public void onLeave() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#onResize()
	 */
	@Override
	public void doResize() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see hu.openig.v1.ScreenBase#paintTo(java.awt.Graphics2D)
	 */
	@Override
	public void paintTo(Graphics2D g2) {
		onResize();
		g2.drawImage(commons.spacewar.frameTopLeft, 0, 20, null);
		
		g2.drawImage(commons.spacewar.frameTopRight, parent.getWidth() - commons.spacewar.frameTopRight.getWidth(), 20, null);

		g2.drawImage(commons.spacewar.commands, 0, 20 + commons.spacewar.frameTopLeft.getHeight(), null);
		g2.drawImage(commons.spacewar.frameRight, parent.getWidth() - commons.spacewar.frameRight.getWidth(), 20 + commons.spacewar.frameTopRight.getHeight(), null);
		
		g2.drawImage(commons.spacewar.panelStatLeft, 0, parent.getHeight() - commons.spacewar.panelStatLeft.getHeight() - 18, null);
		
		g2.drawImage(commons.spacewar.panelStatRight, parent.getWidth() - commons.spacewar.panelStatRight.getWidth(), parent.getHeight() - commons.spacewar.panelStatRight.getHeight() - 18, null);

		Paint p = g2.getPaint();

		TexturePaint tp = new TexturePaint(commons.spacewar.frameTopFill, new Rectangle(commons.spacewar.frameTopLeft.getWidth(), 20, 1, commons.spacewar.frameTopFill.getHeight()));
		g2.setPaint(tp);
		g2.fillRect(commons.spacewar.frameTopLeft.getWidth(), 20, parent.getWidth() - commons.spacewar.frameTopLeft.getWidth() - commons.spacewar.frameTopRight.getWidth(), commons.spacewar.frameTopFill.getHeight());
		
		tp = new TexturePaint(commons.spacewar.panelStatFill, new Rectangle(commons.spacewar.panelStatLeft.getWidth(), parent.getHeight() - commons.spacewar.panelStatLeft.getHeight() - 18, 1, commons.spacewar.panelStatFill.getHeight()));
		g2.setPaint(tp);
		g2.fillRect(commons.spacewar.panelStatLeft.getWidth(), parent.getHeight() - commons.spacewar.panelStatLeft.getHeight() - 18, parent.getWidth() - commons.spacewar.frameTopRight.getWidth() - commons.spacewar.frameTopLeft.getWidth(), commons.spacewar.panelStatFill.getHeight());
		
		tp = new TexturePaint(commons.spacewar.frameRightFill, new Rectangle(parent.getWidth() - commons.spacewar.frameRight.getWidth(), 20 + commons.spacewar.frameTopRight.getHeight() + commons.spacewar.frameRight.getHeight(), commons.spacewar.frameRightFill.getWidth(), commons.spacewar.frameRightFill.getHeight()));
		g2.setPaint(tp);
		g2.fillRect(parent.getWidth() - commons.spacewar.frameRight.getWidth(), 20 + commons.spacewar.frameTopRight.getHeight() + commons.spacewar.frameRight.getHeight(), commons.spacewar.frameRightFill.getWidth(), parent.getHeight() - 38 - commons.spacewar.frameTopRight.getHeight() - commons.spacewar.frameRight.getHeight() - commons.spacewar.panelStatRight.getHeight());
		
		tp = new TexturePaint(commons.spacewar.frameLeftFill, new Rectangle(0, 20 + commons.spacewar.frameTopLeft.getHeight() + commons.spacewar.commands.getHeight(), commons.spacewar.frameLeftFill.getWidth(), commons.spacewar.frameLeftFill.getHeight()));
		g2.setPaint(tp);
		g2.fillRect(0, 20 + commons.spacewar.frameTopLeft.getHeight() + commons.spacewar.commands.getHeight(), commons.spacewar.frameLeftFill.getWidth(), 
				parent.getHeight() - 36 - commons.spacewar.frameTopLeft.getHeight() - commons.spacewar.commands.getHeight() - commons.spacewar.panelStatLeft.getHeight());
		
		g2.setPaint(p);
		
		for (ThreePhaseButton btn : mainCommands) {
			btn.paintTo(g2);
		}

		for (ThreePhaseButton btn : viewCommands) {
			btn.paintTo(g2);
		}
		
	}

}
