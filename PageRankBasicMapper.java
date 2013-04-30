import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;


public class PageRankBasicMapper extends MapReduceBase implements 
			org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, Text>{

	@Override
	public void map(LongWritable key, Text value,
		OutputCollector<Text, Text> output, Reporter reporter) 
		throws IOException {
	
		String tempString = value.toString();
		
		String node = tempString.substring(0, 6);
		String[] fields = tempString.substring(7).split(",");
		double pageRank = Double.valueOf(fields[0]);
		
		for(int i = 1; i< fields.length-1; i++) {
			double PR = pageRank/Double.valueOf(fields[fields.length-1]);
			output.collect(new Text(fields[i]), new Text(node+","+PR));
		}
		
		output.collect(new Text(key.toString()), value);
	}
}