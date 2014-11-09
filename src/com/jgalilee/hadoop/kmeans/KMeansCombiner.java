package com.jgalilee.hadoop.kmeans;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.hadoop.mapreduce.Reducer;

import com.jgalilee.hadoop.kmeans.writable.Centroid;
import com.jgalilee.hadoop.kmeans.writable.Point;

/**
 * Merge points into each other so they can be compressed and sent to reducer.
 * Essentially this just creates partial sums so that the points can be later
 * more effectively summed in the reducer.
 *
 * @author jgalilee
 */
public class KMeansCombiner extends Reducer<Centroid, Point, Centroid, Point> {

	@Override
	public void reduce(Centroid key, Iterable<Point> points, Context context) throws IOException, InterruptedException {
		Point result = null;
		Iterator<Point> iterator = points.iterator();
		while (iterator.hasNext()) {
			Point point = iterator.next();
			int count = Double.valueOf(point.getNumber()).intValue();
			double[] vector = point.getVector();
			double[] temp = Arrays.copyOf(vector, vector.length);
			if (null == result) {
				result = new Point();
				result.setVector(temp);
				result.setNumber(count);
			} else {
				result.add(temp, count);
			}
		}
		if (null != result) {
			context.write(key, result);
		}
		return;
	}

}
