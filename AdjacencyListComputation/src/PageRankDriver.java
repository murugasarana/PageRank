import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class PageRankDriver {

	private static BufferedReader br;

	public static void main(String[] args) throws Exception {
		
		//Read blocks.txt and convert into String[]
		FileInputStream fis = new FileInputStream(args[0]);
		br = new BufferedReader(new InputStreamReader(fis));
		String line = null;
		String value = "";
		while(( line = br.readLine()) != null) {
			value += line + " ";
		}
		String[] val = value.trim().replaceAll("\\s+", "\t").split("\t");
		Configuration conf = new Configuration();
		conf.setStrings("BLOCKS", val);
		Job job = new Job(conf);
//		conf.setJarByClass(PageRank.class);
		
		// Name of the Job
		job.setJarByClass(PageRankDriver.class);
		job.setJobName("PageRank1.0");
		
		// Data type of Output Key and Value
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		// Setting the Mapper and Reducer Class
		job.setMapperClass(PageRankMapper.class);
		job.setReducerClass(PageRankReducer.class);
		
		// Specify input and output DIRECTORIES (not files)
		FileInputFormat.addInputPath(job, new Path(args[1]));
		FileOutputFormat.setOutputPath(job, new Path(args[2]));
				
		// Formats of the Data Type of Input and output
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
				
		job.waitForCompletion(true);
	}

}