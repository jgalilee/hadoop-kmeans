package com.jgalilee.hadoop.kmeans;

import org.apache.hadoop.mapreduce.Partitioner;

import com.jgalilee.hadoop.kmeans.writable.Centroid;
import com.jgalilee.hadoop.kmeans.writable.Point;

/**
 * Using the centroids interger label representation calculate the partion.
*
 * @author jgalilee
 */
public class KMeansPartitioner extends Partitioner<Centroid, Point> {

	@Override
	public int getPartition(Centroid key, Point value, int numReducers) {
		return Integer.valueOf(key.getLabel()) % numReducers;
	}

}
