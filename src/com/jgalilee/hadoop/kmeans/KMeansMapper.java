package com.jgalilee.hadoop.kmeans;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.jgalilee.hadoop.kmeans.util.IterationManager;

import com.jgalilee.hadoop.kmeans.writable.Point;
import com.jgalilee.hadoop.kmeans.writable.Centroid;
import com.jgalilee.hadoop.kmeans.writable.DifferentDimensionsException;

/**
 * Convert the string of each point into a point and map it to the closest
 * centroid.
 *
 * @author jgalilee
 */
public class KMeansMapper extends Mapper<LongWritable, Text, Centroid, Point> {

	private Point point = new Point();
	private IterationManager manager = null;

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		// Load the centroids into the iteration manager that assists with lookup
		// and comparisons.
		manager = new IterationManager();
		List<URI> uris = Arrays.asList(context.getCacheFiles());
		for (URI uri : uris) {
			Path p = new Path(uri);
			FileSystem fs = FileSystem.get(context.getConfiguration());
			InputStreamReader ir = new InputStreamReader(fs.open(p));
			BufferedReader br = new BufferedReader(ir);
			String line = br.readLine();
			while (line != null) {
				manager.add(line);
				line = br.readLine();
			}
		}
		return;
	}

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		try {
			point.parse(value.toString());
			context.write(manager.closest(point), point);
		} catch (DifferentDimensionsException e) {
			e.printStackTrace();
		}
		return;
	}

}
