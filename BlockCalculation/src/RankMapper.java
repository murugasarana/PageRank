import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class RankMapper extends Mapper<LongWritable, Text, Text, Text>{
    
//	Context context;
//	public void setup(Context context){
//		this.context = context;
//	}
	String[] blocks;
	@Override
	protected void map(LongWritable key, Text value,Context context) 
			throws IOException, InterruptedException {
		
		Configuration conf = context.getConfiguration();
		blocks = conf.getStrings("BLOCKS");
		String tempString = value.toString();
		tempString = tempString.trim().replaceAll("\\s+", "\t");
		String[] fields = tempString.split("\t");
		String blockU = getBlock(fields[0]);
	    String blockV = getBlock(fields[1]);
	    String VALUE = blockU+","+fields[1]+":"+blockV;
	    System.out.println("KEY: "+fields[0]);
	    System.out.println("VALUE: "+VALUE);
		context.write(new Text(fields[0]), new Text(VALUE));
	}
	private String getBlock(String node) {
		Integer i = 0;
		String block = null;
		while(i < blocks.length){
			if(Integer.valueOf(node) > Integer.valueOf(blocks[i])){
				i++;
			}
			else{
				block = i.toString();
				break;
			}
		}	
		return block;
	}

}