/*
 * Carrot2 Project Copyright (C) 2002-2004, Dawid Weiss Portions (C)
 * Contributors listed in carrot2.CONTRIBUTORS file. All rights reserved.
 * 
 * Refer to the full license file "carrot2.LICENSE" in the root folder of the
 * CVS checkout or at: http://www.cs.put.poznan.pl/dweiss/carrot2.LICENSE
 */
package com.stachoodev.carrot.filter.lingo.local;

import com.dawidweiss.carrot.core.local.clustering.RawDocument;
import com.dawidweiss.carrot.core.local.clustering.RawDocumentBase;
import com.stachoodev.carrot.filter.lingo.common.Snippet;

/**
 * A two-way adapter of an input {@link RawDocument} to an internal
 * class {@link Snippet}, supported by the old lingo, and back to
 * {@link RawDocument} for the use by further components.
 * 
 * @author Dawid Weiss
 * @version $Revision$
 */
public final class SnippetInterfaceAdapter extends Snippet implements RawDocument {

	private final RawDocument document;
	private final RawDocumentBase base;

	/**
	 * Creates a new wrapper around an existing <code>document</code>.
	 */
	public SnippetInterfaceAdapter(String id, RawDocument document) {
		super(id, document.getTitle(), document.getSnippet());
		this.document = document;

        Object lang = document.getProperty(RawDocument.PROPERTY_LANGUAGE);
        if (lang != null) {
            super.setLanguage((String) lang);
        }
		
		this.base = new RawDocumentBase() {
			public Object getId() {
				return SnippetInterfaceAdapter.this.document.getId();
			}
		};
        base.setProperty(PROPERTY_SNIPPET, document.getSnippet());
	}

	//
	// delegate all method calls to the adapter.
	//

	/*
	 * @see com.dawidweiss.carrot.core.local.clustering.RawDocument#getProperty(java.lang.String)
	 */
	public Object getProperty(String name) {
		return base.getProperty(name);
	}

	/*
	 * @see com.dawidweiss.carrot.core.local.clustering.RawDocument#setProperty(java.lang.String, java.lang.Object)
	 */
	public Object setProperty(String propertyName, Object value) {
		return base.setProperty(propertyName, value);
	}
	
	/*
	 * @see com.dawidweiss.carrot.core.local.clustering.RawDocument#getId()
	 */
	public Object getId() {
		return base.getId();
	}

	/*
	 * @see com.dawidweiss.carrot.core.local.clustering.RawDocument#getUrl()
	 */
	public String getUrl() {
		return base.getUrl();
	}

	/* 
	 * @see com.dawidweiss.carrot.core.local.clustering.RawDocument#getSnippet()
	 */
	public String getSnippet() {
		return base.getSnippet();
	}

	/*
	 * @see com.dawidweiss.carrot.core.local.clustering.RawDocument#getScore()
	 */
	public float getScore() {
		return base.getScore();
	}

}
