import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputFormat;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;


public class PageRankBasicDriver {

	public static void main(String[] args) {
		JobClient client = new JobClient();
		// Configurations for Job set in this variable
		JobConf conf = new JobConf(PageRankBasicDriver.class);

		// Name of the Job
		conf.setJobName("PageRankBasic1.0");
		
		// Data type of Output Key and Value
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);

		// Setting the Mapper and Reducer Class
		conf.setMapperClass(PageRankBasicMapper.class);
		conf.setReducerClass(PageRankBasicReducer.class);
		
		// Formats of the Data Type of Input and output
		conf.setInputFormat((Class<? extends InputFormat>) TextInputFormat.class);
		conf.setOutputFormat((Class<? extends OutputFormat>) TextOutputFormat.class);

		// Specify input and output DIRECTORIES (not files)
		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));
		
		client.setConf(conf);
		try {
			JobClient.runJob(conf);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
