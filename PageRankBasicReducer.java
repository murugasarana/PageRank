import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class PageRankBasicReducer extends MapReduceBase implements 
org.apache.hadoop.mapred.Reducer<Text, Text, Text, Text> {
	
	private static final double damping = 0.85F;
	private static final double randomFactor = (1-damping)/685229;
	
	@Override
	public void reduce(Text _key,
		Iterator<Text> values,
		OutputCollector<Text, Text> output,
		Reporter reporter) throws IOException {
		
		double pageRank = 0.0;
		String[] selfTuple =null;
		
		while(values.hasNext()) {
			String[] nodeFields = values.next().toString().split(",");
			System.out.println(Arrays.toString(nodeFields));
			if(nodeFields.length < 2) {
				pageRank += Double.valueOf(nodeFields[0]);
			} else {
				selfTuple = new String[nodeFields.length];
				System.arraycopy(nodeFields, 0, selfTuple, 0, nodeFields.length);
			}
		}
		pageRank = pageRank * damping + randomFactor;
		Double residual = (pageRank - Double.valueOf(selfTuple[0])) / Double.valueOf(selfTuple[0]) ;
		selfTuple[0] = ""+pageRank;
		output.collect(_key,new Text(Arrays.toString(selfTuple)));
	}

}
