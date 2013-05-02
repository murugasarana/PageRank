import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;


public class BlockedPageRankReducer extends MapReduceBase implements 
org.apache.hadoop.mapred.Reducer<Text, Text, Text, Text> {
	
	private static final Double damping = 0.85;
	private static final Double randomFactor = (1.0-damping)/685230.0;
	
	@Override
	public void reduce(Text _key,
		Iterator<Text> values,
		OutputCollector<Text, Text> output,
		Reporter reporter) throws IOException {
		
		Double pageRank = 0.0;
		
		Map<String, String> selfTuples = new HashMap<>();
		Map<String, Double> pageRankMap = new HashMap<>();
		Set<String> inBlockNodes = new HashSet<String>();
		
		while(values.hasNext()) {
			String tuple = values.next().toString();
			String[] nodeFields = tuple.split(",");
			if(nodeFields.length < 4) {
				pageRank = Double.valueOf(nodeFields[2]);
				if(pageRankMap.containsKey(nodeFields[1])) {
					try {
						pageRank = pageRankMap.get(nodeFields[1]) + pageRank;
					} catch(Exception e) {
						System.out.println("ERROR");
					}
				}
				pageRankMap.put(nodeFields[1], pageRank);
				inBlockNodes.add(nodeFields[0]);
			} else {
				selfTuples.put(nodeFields[0],tuple);
			}
		}
		
		Iterator<String> inBlockIter = inBlockNodes.iterator();
		while(inBlockIter.hasNext()) {
			String inBlockNode = inBlockIter.next();
			String[] inBlockValues;
			Double residual;	
			Double pageRankUpdated = 1.0/685230.0;
			if(pageRankMap.containsKey(inBlockNode)){
			    pageRankUpdated = pageRankMap.get(inBlockNode) * damping + randomFactor;

			}
		    if(selfTuples.containsKey(inBlockNode)) {	
				inBlockValues = selfTuples.get(inBlockNode).split(",");
			    residual = Math.abs(Double.valueOf(inBlockValues[1])-Double.valueOf(pageRankUpdated));
			    residual = residual/Double.valueOf(pageRankUpdated);
				inBlockValues[1] = pageRankUpdated.toString();
			} else {
				inBlockValues = new String[4];
				inBlockValues[0] = inBlockNode; 
				residual = Math.abs(pageRankUpdated / pageRankUpdated);
				inBlockValues[1] = pageRankUpdated.toString();
				Integer deg = 0;
				inBlockValues[2] = deg.toString();
				inBlockValues[3] = "DUMMY";
			}
		    Long r = (long) (residual * 1000000000);
		    reporter.getCounter(GlobalCounters.RESIDUAL).increment(r);
		    output.collect(_key,new Text(StringUtils.join(inBlockValues, ",")));
		}
	}

}