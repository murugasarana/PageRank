import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class PageRankBasicReducer extends MapReduceBase implements 
org.apache.hadoop.mapred.Reducer<Text, Text, Text, Text> {
	
	private static final Double damping = 0.85;
	private static final Double randomFactor = (1.0-damping)/685229.0;
	
	@Override
	public void reduce(Text _key,
		Iterator<Text> values,
		OutputCollector<Text, Text> output,
		Reporter reporter) throws IOException {
		
		Double pageRank = 0.0;
		String[] selfTuple = null;
		
		while(values.hasNext()) {
			String[] nodeFields = values.next().toString().split(",");
			if(nodeFields.length < 2) {
				pageRank += Double.valueOf(nodeFields[0]);
			} else {
				selfTuple = new String[nodeFields.length];
				System.arraycopy(nodeFields, 0, selfTuple, 0, nodeFields.length);
			}
		}
		if(selfTuple == null){
			selfTuple = new String[2];
			Double p = (double)1.0/685229.0;
			selfTuple[0] = p.toString();
			Integer deg = 0;
			selfTuple[1] = deg.toString();
		}
		pageRank = pageRank * damping + randomFactor;
		if(pageRank != 0.0){
		    double residual = Math.abs(Double.valueOf(selfTuple[0])-pageRank)/pageRank;
		    selfTuple[0] = pageRank.toString();
		    Long r = (long) (residual * 1000000000);
		    reporter.getCounter(GlobalCounters.RESIDUAL).increment(r);
		    output.collect(_key,new Text(StringUtils.join(selfTuple, ",")));
		}
	}

}