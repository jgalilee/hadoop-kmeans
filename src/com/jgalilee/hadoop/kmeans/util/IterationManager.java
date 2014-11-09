package com.jgalilee.hadoop.kmeans.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jgalilee.hadoop.kmeans.writable.Centroid;
import com.jgalilee.hadoop.kmeans.writable.DifferentDimensionsException;
import com.jgalilee.hadoop.kmeans.writable.Point;

/**
 * Simple class to abstract the management of the centroids from the mapper.
 *
 * @author jgalilee
 */
public class IterationManager {

	private List<Centroid> centroids = null;

	/**
	 * Create the new iteration manager and instantiate the array list that is
	 * used to handle the centroids.
	 */
	public IterationManager() throws IOException {
		centroids = new ArrayList<Centroid>();
	}

	/**
	 * Adds the string representation of a centroid into the list of centroids.
	 *
	 * @return Centroid representing of the given string.
	 */
	public Centroid add(String record) {
		Centroid centroid = new Centroid().parse(record);
		return add(centroid);
	}

	/**
	 * Add the given centroid to the iteration manager.
	 *
	 * @return Centroid given to add.
	 */
	public Centroid add(Centroid centroid) {
		this.centroids.add(centroid);
		return centroid;
	}

	/**
	 * Returns the centroid for which the given point is closest.
	 *
	 * @author jgalilee
	 */
	public Centroid closest(Point point) throws DifferentDimensionsException {
		Centroid closest = null;
		if (centroids.size() > 0) {
			closest = centroids.get(0);
			double minDist = closest.distance(point);
			for (Centroid centroid : centroids) {
				double dist = centroid.distance(point);
				if (minDist >= dist) {
					closest = centroid;
					minDist = dist;
				}
			}
		}
		return closest;
	}

}
