/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef.editparts;

import org.eclipse.draw2d.*;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

import org.eclipse.gef.SnapToGrid;

/**
 * @author Pratik Shah
 */
public class GridLayer 
	extends FreeformLayer 
{

protected int gridX = SnapToGrid.DEFAULT_GRID_SIZE;
protected int gridY = SnapToGrid.DEFAULT_GRID_SIZE;

protected Point origin = new Point();

public GridLayer() {
	super();
	setForegroundColor(ColorConstants.lightGray);
}


/**
 * Overridden to indicate on preferred size.  The grid layer should not affect the size of
 * the layered pane in which it is placed.
 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
 */
public Dimension getPreferredSize(int wHint, int hHint) {
	return new Dimension();
}

protected void paintFigure(Graphics graphics) {
	super.paintFigure(graphics);
	paintGrid(graphics);
}

/**
 * Paints the grid.  This method will be invoked only if the grid is enabled.  Sub-classes 
 * can override to customize the grid's look.
 * 
 * @param	g	The Graphics object to be used to do the painting 
 */
protected void paintGrid(Graphics g) {
	FigureUtilities.paintGrid(g, this, origin, gridX, gridY);
}

public void setOrigin(Point p) {
	if (p == null)
		p = new Point();
	if (!origin.equals(p)) {
		origin = p;
		repaint();
	}
}

public void setSpacing(Dimension spacing) {
	if (spacing == null)
		spacing = new Dimension(SnapToGrid.DEFAULT_GRID_SIZE,
					SnapToGrid.DEFAULT_GRID_SIZE);
	if (!spacing.equals(gridX, gridY)) {
		gridX = spacing.width != 0 ? spacing.width : gridX;
		gridY = spacing.height != 0 ? spacing.height : gridY;
		repaint();
	}
}

}