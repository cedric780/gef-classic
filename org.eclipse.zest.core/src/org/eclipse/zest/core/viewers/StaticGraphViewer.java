/*******************************************************************************
 * Copyright 2005, CHISEL Group, University of Victoria, Victoria, BC, Canada.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     The Chisel Group, University of Victoria
 *******************************************************************************/
package org.eclipse.mylar.zest.core.viewers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.mylar.zest.core.ZestStyles;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModel;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelEntityFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphmodel.GraphModelNode;
import org.eclipse.mylar.zest.core.internal.graphmodel.IGraphModelFactory;
import org.eclipse.mylar.zest.core.internal.graphviewer.StaticGraphViewerImpl;
import org.eclipse.mylar.zest.layouts.LayoutAlgorithm;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;

/**
 * This view is used to represent a static graph.  Static graphs can be layed out, 
 * but do not continually update their layout locations.
 * 
 * @author Ian Bull
 * @author Chris Callendar
 */
public class StaticGraphViewer extends StructuredViewer {

	StaticGraphViewerImpl viewer = null;
	private IGraphModelFactory modelFactory = null;
	private GraphModel model;
	
	/**
	 * Initializes the viewer.
	 * @param composite
	 * @param style	the style for the viewer and for the layout algorithm
	 * @see ZestStyles#LAYOUT_GRID
	 * @see ZestStyles#LAYOUT_TREE
	 * @see ZestStyles#LAYOUT_RADIAL
	 * @see ZestStyles#LAYOUT_SPRING
	 * @see ZestStyles#NO_OVERLAPPING_NODES
	 * @see ZestStyles#HIGHLIGHT_ADJACENT_NODES
	 * @see SWT#V_SCROLL
	 * @see SWT#H_SCROLL
	 */
	public StaticGraphViewer( Composite composite, int style ) {
		this.viewer = new StaticGraphViewerImpl(composite, style);
		hookControl( this.viewer.getControl() );
	}
	
	/**
	 * Gets the styles for this structuredViewer
	 * @return
	 */
	public int getStyle() {
		return this.viewer.getStyle();
	}
	
	/**
	 * Sets the style on this structuredViewer
	 * @param style
	 * @return
	 */
	public void setStyle(int style) {
		this.viewer.setStyle(style);
	}	
	
	/**
	 * Sets the layout algorithm to use for this viewer.
	 * @param algorithm the algorithm to layout the nodes
	 * @param runLayout if the layout should be run
	 */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm, boolean runLayout) {
		viewer.setLayoutAlgorithm(algorithm, runLayout);
	}
	
	
	public void setContentProvider(IContentProvider contentProvider) {
		if (contentProvider instanceof IGraphContentProvider) {
			super.setContentProvider(contentProvider);
		} else if ( contentProvider instanceof IGraphEntityContentProvider ) {
			super.setContentProvider( contentProvider );
		} else {
			throw new IllegalArgumentException("Invalid content provider, only IGraphContentProvider and IGraphEntityContentProvider are supported.");
		}
	}
	
	protected void inputChanged(Object input, Object oldInput) {
		boolean highlightAdjacentNodes = ZestStyles.checkStyle(viewer.getStyle(), ZestStyles.HIGHLIGHT_ADJACENT_NODES);
		boolean directedGraph = ZestStyles.checkStyle(viewer.getStyle(), ZestStyles.DIRECTED_GRAPH);
		
		if (modelFactory == null) {
			if (getContentProvider() instanceof IGraphContentProvider) {
				modelFactory = new GraphModelFactory(this, highlightAdjacentNodes);
			}
			else if ( getContentProvider() instanceof IGraphEntityContentProvider ) {
				modelFactory = new GraphModelEntityFactory(this, highlightAdjacentNodes);
			}
		}
		
		GraphModel newModel = modelFactory.createModelFromContentProvider(input);
		// only update if the number of nodes has changed
		if ((model == null) || (newModel.getNodes().size() != model.getNodes().size())) {
			// get current list of nodes before they are re-created from the factory & content provider
			Map oldNodesMap = (model != null ? model.getNodesMap() : Collections.EMPTY_MAP);

			model = newModel;
			model.setDirectedEdges(directedGraph); 

			// check if any of the pre-existing nodes are still present
			// in this case we want them to keep the same location & size
			for (Iterator iter = oldNodesMap.keySet().iterator(); iter.hasNext(); ) {
				Object data = iter.next();
				GraphModelNode newNode = model.getInternalNode(data);
				if (newNode != null) {
					GraphModelNode oldNode = (GraphModelNode)oldNodesMap.get(data);
					newNode.setPreferredLocation(oldNode.getXInLayout(), oldNode.getYInLayout());
				}
			}

			// set the model contents (initializes the layout algorithm)
			viewer.setContents(model, modelFactory);
		} else {
			//viewer.applyLayout();
		}
		
	}
	
	protected Widget doFindInputItem(Object element) {
		return null;
	}

	protected Widget doFindItem(Object element) {
		return null;
	}

	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {

	}

	protected List getSelectionFromWidget() {
		return new ArrayList(0);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.StructuredViewer#refresh()
	 */
	public void refresh() {
		if (viewer.hasLayoutRun()) {
			viewer.applyLayout();
		}
	}
	
	protected void internalRefresh(Object element) {
		
	}

	public void reveal(Object element) {

	}

	protected void setSelectionToWidget(List l, boolean reveal) {

	}

	public Control getControl() {
		return viewer.getControl();
	}
	
	/**
	 * Adds a new node to the graph
	 * @param o The new node to add
	 */
	public void addNode(Object o) {
		viewer.addNode(o);
	}

	/**
	 * Removes a node from the graph.
	 * @param o the node to remove
	 */
	public void removeNode(Object o) {
		viewer.removeNode(o);
	}

}