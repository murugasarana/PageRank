import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class PageRankReducer extends MapReduceBase implements 
		org.apache.hadoop.mapred.Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text _key,
			Iterator<Text> values,
			OutputCollector<Text, Text> output,
			Reporter reporter) throws IOException {
		
		Text key = _key;
		String pagerank = (double)1.0/685229+"\t";
		
		boolean first = true;
		int i = 1;
		while (values.hasNext()) {
			if(!first) {
				i++;
				pagerank += ", ";
			}
			
			pagerank += values.next().toString();
			first = false;
		}
		pagerank += ", "+i; 
		output.collect(key, new Text(pagerank));
	}

}
