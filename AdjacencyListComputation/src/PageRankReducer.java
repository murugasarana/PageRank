import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;


public class PageRankReducer extends Reducer<Text, Text, Text, Text> {

//	@Override
	protected void reduce(Text _key,
			Iterator<Text> values,
			Context context) throws IOException {
		
		System.out.println("REDUCER");
		
		String pagerank = (double)1.0/685229+",";
		boolean first = true;
		int i = 1;
		String blockU = "";
		while (values.hasNext()) {
			if(!first) {
				i++;
				pagerank += ",";
			}
			String v = values.next().toString();
			if(first){
				blockU = v.split(",")[0];
			}
			pagerank += v.split(",")[1];
			System.out.println("VALUE: "+v);
			System.out.println("PAGERANK VALUE: "+pagerank);
			first = false;
		}
		pagerank = _key.toString()+","+pagerank+","+i; 
		System.out.println("PAGERANK: "+pagerank);
		try {
			context.write(_key, new Text(pagerank));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Reducer exit on error");
			e.printStackTrace();
		}
	}

}