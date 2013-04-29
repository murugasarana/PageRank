import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class PageRankMapper extends MapReduceBase implements 
		org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, Text>{

	@Override
	public void map(LongWritable key, Text value,
			OutputCollector<Text, Text> output, Reporter reporter) 
			throws IOException {
		
		String tempString = value.toString();
		
		output.collect(new Text(tempString.substring(0,6)), new Text(tempString.substring(7, 13)));
	}

}
