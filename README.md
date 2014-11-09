# Hadoop K-Means

K-Means algorithm implementation using Hadoop. This algorithm does not perform
any calculation of the initial centroids, these must be given.

## Usage

*home input clusters number output delta max*

```
hadoop jar HadoopKMeans.jar com.jgalilee.hadoop.kmeans.driver.Driver \
  input/kmeans.state \
  input/points.txt \
  input/clusters.txt \
  2 \
  output/ \
  0.0 \
  5
```

* home - Path to a filename iteration state can be written each iteration.
* input - Path to the input points data.
* clusters - Path to the input clusters data.
* number - Number of reducers to suggest to the Hadoop job.
* output - Path to write the output for iteration n - i.e. output/n
* delta - Delta definining the maximum difference between the last and current centroids.
* max - Maximum number of iterations.

## Assumptions

Input data is assumed to be string representations in double format. Each point
is assumed to be in the same dimensional space, as with each centroid. If two
points are not in the same dimensional space an exception is thrown.

Points should be in the form.

1.0 \s 2.0 \s ...

Centroids should be in the form.

1 \t 1.0 \s 2.0 \s ...

Where \t is a tab, and \s is a space. The number of duplicate items or order is
not important. This will be resolved during the map phase.

## Dependencies

* Hadoop 2.2.0

## Licence

```
The MIT License (MIT)

Copyright (c) 2014 Jack Galilee

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
