import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class PageRankReducer extends Reducer<Text, Text, Text, Text> {

	@Override
	protected void reduce(Text _key,
			Iterable<Text> values,
			Context context) throws IOException {
				
		String pagerank = (double)1.0/685229+",";
		boolean first = true;
		int i = 1;
		Iterator<Text> VALUES = values.iterator();
		while (VALUES.hasNext()) {
			if(!first) {
				i++;
				pagerank += ",";
			}
			String v = VALUES.next().toString();
			pagerank += v;
			first = false;
		}
		pagerank = pagerank+","+i; 
//		System.out.println("PAGERANK: "+pagerank);
		try {
			context.write(_key, new Text(pagerank));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Reducer exit on error");
			e.printStackTrace();
		}
	}

}