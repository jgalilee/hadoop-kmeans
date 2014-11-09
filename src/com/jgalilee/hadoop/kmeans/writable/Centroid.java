package com.jgalilee.hadoop.kmeans.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

/**
 * Composite writable to represent a point in a dataset.
 *
 * @author jgalilee
 */
public class Centroid implements WritableComparable<Centroid> {

	private Text text = new Text();
	private Point point = new Point();

	/**
	 * Calculate the new centroid with the given label from an iterable of points.
	 *
	 * @return New Centroid with the given label dervied from the points.
	 */
	public static Centroid calculate(Text label, Iterable<Point> points) throws DifferentDimensionsException {
		Point result = null;
		Iterator<Point> iterator = points.iterator();
		while (iterator.hasNext()) {
			Point point = iterator.next();
			double[] vector = point.getVector();
			int count = Double.valueOf(point.getNumber()).intValue();
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
			result.compress();
		}
		return new Centroid(label, result);
	}

	public Centroid() { /* none */ }

	/**
	 * Construct a new centroid writable from the text label and a point writable.
	 */
	public Centroid(Text label, Point point) {
		this.text = label;
		this.point = point;
	}

	/**
	 * Parse the centroids from the string representation. Assumes that the string
	 * is in the form label\tp1\sp2\spn.
	 *
	 * @return Centroid representation of the given string.
	 */
	public Centroid parse(String value) {
		int index = value.indexOf("	");
		this.text.set(value.substring(0, index));
		this.point.parse(value.substring(index + 1, value.length()));
		return this;
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		text.readFields(in);
		point.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		text.write(out);
		point.write(out);
	}

	/**
	 * Returns the text label for the centroid.
	 *
	 * @return Returns the text label for the centroid.
	 */
	public Text getText() {
		return this.text;
	}

	/**
	 * Return the text label as a string.
	 *
	 * @return String value of the text.
	 */
	public String getLabel() {
		return this.text.toString();
	}

	/**
	 * Return the centroids internal point representation.
	 *
	 * @return Point representing the centroid.
	 */
	public Point getPoint() {
		return this.point;
	}

	/**
	 * Return the centroids internal point vector.
	 *
	 * @return Returns the intenal points vector.
	 */
	public double[] getVector() {
		return this.getPoint().getVector();
	}

	@Override
	public int compareTo(Centroid c) {
		return this.text.compareTo(c.getText());
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Centroid) {
			Centroid c = (Centroid) o;
			return this.text.equals(c.getText());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.text.hashCode();
	}

	/**
	 * Return the number of dimensions the centroids internal point is in.
	 *
	 * @return Number of dimensions the centroids point is in.
	 */
	public int getDimensions() {
		return this.point.getDimensions();
	}

	/**
	 * Return the distance from this centroid and another.
	 *
	 * @return Distance from this centroid and another.
	 */
	public double distance(Centroid centroid) throws DifferentDimensionsException {
		Point point = centroid.getPoint();
		return this.getPoint().distance(point);
	}

	/**
	 * Return the distance from this centroid and a point.
	 *
	 * @return Distance from this centroid and a point.
	 */
	public double distance(Point point) throws DifferentDimensionsException {
		return this.getPoint().distance(point);
	}

	/**
	 * Return the string representation of the centroid.
	 *
	 * @return String representation of the point as label\tp1\sp2\spn.
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getText().toString());
		sb.append("	");
		sb.append(this.getPoint());
		return sb.toString();
	}

}
