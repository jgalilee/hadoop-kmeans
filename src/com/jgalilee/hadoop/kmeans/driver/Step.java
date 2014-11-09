package com.jgalilee.hadoop.kmeans.driver;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import com.jgalilee.hadoop.kmeans.KMeansCombiner;
import com.jgalilee.hadoop.kmeans.KMeansMapper;
import com.jgalilee.hadoop.kmeans.KMeansPartitioner;
import com.jgalilee.hadoop.kmeans.KMeansReducer;
import com.jgalilee.hadoop.kmeans.writable.Centroid;
import com.jgalilee.hadoop.kmeans.writable.Point;
import com.jgalilee.java.patterns.resurecting.Iteration;

/**
 * K-Means Hadoop algorithm step job following the Iteration interface.
 *
 * @author jgalilee
 */
public class Step extends Job implements Iteration {

	@SuppressWarnings("deprecation")
	public Step(Configuration conf) throws IOException {
		super(conf);
		setJarByClass(getClass());
		addCacheFiles();
		FileOutputFormat.setOutputPath(this, getOutputPath());
		FileInputFormat.addInputPath(this, getInputPath());
		setMapperClass(KMeansMapper.class);
		setCombinerClass(KMeansCombiner.class);
		setPartitionerClass(KMeansPartitioner.class);
		setReducerClass(KMeansReducer.class);
		setMapOutputKeyClass(Centroid.class);
		setMapOutputValueClass(Point.class);
		setOutputKeyClass(Text.class);
		setOutputValueClass(NullWritable.class);
		setNumReduceTasks(conf.getInt("number", getNumReduceTasks()));
	}

	@Override
	public Boolean run() {
		Path output = getOutputPath();
		try {
			FileSystem dfs = FileSystem.get(getConfiguration());
			if (dfs.isDirectory(output)) {
				dfs.delete(output, true);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Boolean result = false;
		try {
			result = waitForCompletion(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Boolean retry() {
		return run();
	}

	/**
	 * Return the path to the iteration step's output.
	 *
	 * @return Return the path to the iteration step's output.
	 */
	private Path getOutputPath() {
		String sep = System.getProperty("file.separator");
		System.out.println("Using output " + conf.get("output") + sep + conf.get("iteration"));
		return new Path(conf.get("output") + sep + String.valueOf(conf.getInt("iteration", 0)));
	}

	/**
	 * Return the path to the iteration step's input.
	 *
	 * @return Return the path to the iteration step's input.
	 */
	private Path getInputPath() {
		System.out.println("Using input " + conf.get("input"));
		return new Path(conf.get("input"));
	}

	/**
	 * Adds the files from the previous iteration to the distributed cache.
	 */
	private void addCacheFiles() throws IOException {
		Integer iteration = conf.getInt("iteration", 0);
		if (iteration > 1) {
			String sep = System.getProperty("file.separator");
			String output = conf.get("output") + sep + String.valueOf(conf.getInt("iteration", 0) - 1);
			Path out = new Path(output, "part-r-[0-9]*");
			System.out.println("Checking path " + out.toString());
			FileSystem fs = FileSystem.get(conf);
			FileStatus[] ls = fs.globStatus(out);
			for (FileStatus fileStatus : ls) {
				Path pfs = fileStatus.getPath();
				System.out.println("Adding " + pfs.toUri().toString());
				addCacheFile(pfs.toUri());
			}
		} else {
			Path pfs = new Path(conf.get("state"));
			System.out.println("First iteration adding " + pfs.toUri().toString());
			addCacheFile(pfs.toUri());
		}
	}

}
