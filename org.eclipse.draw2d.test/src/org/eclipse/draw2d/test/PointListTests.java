/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.draw2d.test;

import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

public class PointListTests
	extends BaseTestCase
{
	
public void testIntersects() {
	PointList points = new PointList();
	points.addPoint(0, 0);
	points.addPoint(0, 3);
	// simple case
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(-10, 0, 20, 3)));
	// line starts and ends on opposite edges of the rectangle
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(-2, 1, 4, 1)));
	// parallel
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(0, 0, 10, 10)));
	// vertical
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(0, 3, 10, 10)));
	// rect is close, but not on the line -- off by a pixel
	assertFalse(points.intersects(Rectangle.SINGLETON.setBounds(1, 1, 50, 50)));
	// empty rect located on the line
	assertFalse(points.intersects(Rectangle.SINGLETON.setBounds(0, 1, 0, 0)));
	
	points.addPoint(10, 10);
	// line cuts through two adjacent sides of the rectangle
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(-50, 5, 58, 100)));
	points.removePoint(2);
	
	points.addPoint(-1, -3);
	// line doesn't intersect with the rect's diagonals
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(-10, 2, 40, 50)));
	points.removeAllPoints();
	
	points.addPoint(10, 10);
	points.addPoint(8, 12);
	// line starts on rectangle's bottom-right corner
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(0, 0, 10, 10)));
	// line is tangential to the rectangle's bottom-right corner
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(0, 0, 9, 11)));
	
	points.addPoint(new PrecisionPoint(1.51, 1.51));
	assertTrue(points.intersects(Rectangle.SINGLETON.setBounds(0, 0, 1, 1)));
}

}