import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class PageRankMapper extends Mapper<LongWritable, Text, Text, Text>{
    
//	Context context;
//	public void setup(Context context){
//		this.context = context;
//	}
	@Override
	protected void map(LongWritable key, Text value,Context context) 
			throws IOException, InterruptedException {
		
		String tempString = value.toString();
		tempString = tempString.trim().replaceAll("\\s+", "\t");
		String[] fields = tempString.split("\t");
//	    System.out.println("KEY: "+fields[0]);
//	    System.out.println("VALUE: "+fields[1]);
		context.write(new Text(fields[0]), new Text(fields[1]));
	}
}