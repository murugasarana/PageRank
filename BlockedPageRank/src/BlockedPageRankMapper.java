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


public class BlockedPageRankMapper extends MapReduceBase implements 
			org.apache.hadoop.mapred.Mapper<LongWritable, Text, Text, Text>{

	@Override
	public void map(LongWritable _key, Text value,
		OutputCollector<Text, Text> output, Reporter reporter) 
		throws IOException {
	
//		String tempString = value.toString().replaceAll("\\s+", " ");
		String tempString = value.toString();
		String[] KeyValue = tempString.split("\t");
//		System.out.println("MAPPER: KeyValue" + Arrays.toString(KeyValue));
		String key = KeyValue[0];
		String nodeValue = KeyValue[1];
		String[] fields = nodeValue.split(",");
		String U = fields[0];
		Double pageRank = Double.valueOf(fields[1]);
		
		String block = "";
		String V = "";
		for(int i = 2; i< fields.length-2; i++) {
			if(Integer.valueOf(fields[fields.length-2]) != 0){
//				System.out.println("MAPPER: Fields: "+i+" " + fields[i]);
				V = fields[i].split(":")[0];
				block = fields[i].split(":")[1];
				Double PR = (double)pageRank/Double.valueOf(fields[fields.length-2]);
				output.collect(new Text(block), new Text(U+","+V+","+PR));
			}	
		}
		output.collect(new Text(key), new Text(nodeValue));
	}
}