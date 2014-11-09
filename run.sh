ant clean && ant jar
rm -rf ./output/*
rm input/kmeans.state
hadoop jar HadoopKMeans.jar com.jgalilee.hadoop.kmeans.driver.Driver \
  input/kmeans.state \
  input/points.txt \
  input/clusters.txt \
  2 \
  output/ \
  0.0 \
  10
cat output/9/* | sort
