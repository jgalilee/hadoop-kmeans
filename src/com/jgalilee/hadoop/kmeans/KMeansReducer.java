package com.jgalilee.hadoop.kmeans;

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counter;
import org.apache.hadoop.mapreduce.Reducer;

import com.jgalilee.hadoop.kmeans.writable.Point;
import com.jgalilee.hadoop.kmeans.writable.Centroid;
import com.jgalilee.hadoop.kmeans.writable.DifferentDimensionsException;

/**
 * Calculates the new centroid given the current centroid and assigned points.
 *
 * @author jgalilee
 */
public class KMeansReducer extends Reducer<Centroid, Point, Text, NullWritable> {

	private Double delta = 0.00;
	private Text outKey = new Text();
	private Counter adjusted;

	@Override
	protected void setup(Context context) throws IOException, InterruptedException {
		adjusted = context.getCounter(KMeansCounters.ADJUSTED);
		Configuration conf = context.getConfiguration();
		delta = conf.getDouble("delta", 0.00);
	}

	@Override
	public void reduce(Centroid key, Iterable<Point> points, Context context) throws IOException, InterruptedException {
		try {
			Centroid newKey = Centroid.calculate(key.getText(), points);
			double similarity = key.distance(newKey);
			if (similarity > delta) {
				adjusted.increment(1L);
			}
			outKey.set(newKey.toString());
			context.write(outKey, NullWritable.get());
		} catch (DifferentDimensionsException e) {
			e.printStackTrace();
		}
	}

}
