package com.jgalilee.hadoop.kmeans.writable;

/**
 * Simple execption to be thrown when two points writables of different
 * dimensions are used with one another.
 */
public class DifferentDimensionsException extends Exception {

	private static final long serialVersionUID = 5458470991021764887L;

	public DifferentDimensionsException() {
		super();
		return;
	}

}
