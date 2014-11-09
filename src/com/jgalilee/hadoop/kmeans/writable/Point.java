package com.jgalilee.hadoop.kmeans.writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.ArrayPrimitiveWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Writable;

/**
 * Composite writable to represent a point in the input dataset.
 *
 * @author jgalilee
 */
public class Point implements Writable {

	private ArrayPrimitiveWritable vector = null;
	private IntWritable number = null;

	/**
	 * Instantiates a blank point comprised of no vectors. It should have the
	 * add method called before set is used.
	 */
	public Point() {
		vector = new ArrayPrimitiveWritable();
		number = new IntWritable(0);
	}

	/**
	 * Given a space seperate string of doubles convert it into a point.
	 *
	 * @return Point representation of the string.
	 */
	public Point parse(String value) {
		String[] coords = value.toString().split(" ");
		double[] temp = new double[coords.length];
		for (int i = 0, j = temp.length; i < j; i++) {
			temp[i] = Double.valueOf(coords[i]);
		}
		vector.set(temp);
		number.set(1);
		return this;
	}

	/**
	 * Returns the internal vector array for the point.
	 */
	public double[] getVector() {
		return (double[]) vector.get();
	}

	/**
	 * Sets the internal vector array to the given value.
	 */
	public void setVector(double[] temp) {
		vector.set(temp);
	}

	/**
	 * Read the input buffer and convert it back into a point.
	 */
	@Override
	public void readFields(DataInput in) throws IOException {
		vector.readFields(in);
		number.readFields(in);
	}

	/**
	 * Write the results of this point to the output buffer
	 */
	@Override
	public void write(DataOutput out) throws IOException {
		vector.write(out);
		number.write(out);
	}

	/**
	 * Returns the dimensionality of the point.
	 *
	 * @return Number of dimensions this point resides in.
	 */
	public int getDimensions() {
		return getVector().length;
	}

	/**
	 * Calculates and returns this distance between this point and another point.
	 */
	public double distance(Point point) throws DifferentDimensionsException {
		getVector();
		double sum = 0.0f;
		double[] thisVector = this.getVector();
		double[] otherVector = point.getVector();
		if (thisVector.length != otherVector.length) {
			throw new DifferentDimensionsException();
		}
		for (int i = 0, j = thisVector.length; i < j; i++) {
			double thisCoordinate = thisVector[i];
			double otherCoordinate = otherVector[i];
			sum += Math.pow(thisCoordinate - otherCoordinate, 2);
		}
		return Math.sqrt(sum);
	}

	/**
	 * Adds the value of each dimension in the given vector to the dimension in
	 * the points existing dimensions.
	 */
	public void add(double[] otherVector, int nums) {
		double[] thisVector = this.getVector();
		for (int i = 0, j = thisVector.length; i < j; i++) {
			thisVector[i] = thisVector[i] + otherVector[i];
		}
		number.set(number.get() + nums);
		setVector(thisVector);
	}

	/**
	 * Compresses the point by dividing each dimension by the total number of
	 * points that make up the point. This mutates the point object.
	 */
	public void compress() {
		double[] thisVector = this.getVector();
		double currentNumber = number.get();
		for (int i = 0, j = thisVector.length; i < j; i++) {
			thisVector[i] /= currentNumber;
		}
		number.set(1);
		setVector(thisVector);
	}

	/**
	 * Returns the string representation of the array.
	 */
	public String toString() {
		double[] thisVector = this.getVector();
		StringBuffer sb = new StringBuffer();
		for (int i = 0, j = thisVector.length; i < j; i++) {
			sb.append(thisVector[i]);
			if (i < thisVector.length - 1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	/**
	 * Returns the number of points that make up the sum of this point.
	 */
	public double getNumber() {
		return number.get();
	}

	/**
	 * Returns true if the objects are equal, false otherwise.
	 */
	public boolean equals(Object o) {
		if (o instanceof Point) {
			Point p = (Point) o;
			return Arrays.equals(getVector(), p.getVector());
		}
		return false;
	}

	/**
	 * Returns the hash code of the points internal array.
	 *
	 * @return Array hash code
	 */
	public int hashCode() {
		return Arrays.hashCode(this.getVector());
	}

	/**
	 * Sets the number of points that make up this point.
	 */
	public void setNumber(int i) {
		this.number.set(i);
	}

}
