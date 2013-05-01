import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class RankReducer extends Reducer<Text, Text, Text, Text> {
	@Override
	protected void reduce(Text _key,
			Iterable<Text> values,
			Context context) throws IOException, InterruptedException {
		
		System.out.println("REDUCER");
		
		String pagerank = (double)1.0/685229+",";
		boolean first = true;
		int i = 1;
		String blockU = "";
		Iterator<Text> VALUES = values.iterator();
		String key = "";
		while(VALUES.hasNext()) {
			if(!first) {
				i++;
				pagerank += ",";
			}
			String v = VALUES.next().toString();
			pagerank += v.split(",")[1];
			if(first){
				key = v.split(",")[0];
				pagerank = _key.toString()+","+pagerank;
			}
			System.out.println("VALUE: "+v);
			System.out.println("PAGERANK VALUE: "+pagerank);
			first = false;
		}
		pagerank += ","+i+","+"DUMMY"; 
		System.out.println("PAGERANK: "+pagerank);
		context.write(new Text(key), new Text(pagerank));
	}

}