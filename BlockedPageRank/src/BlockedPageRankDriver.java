import java.io.IOException;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Counters;
import org.apache.hadoop.mapred.Counters.Counter;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.InputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputFormat;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.RunningJob;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class BlockedPageRankDriver {

	public static void main(String[] args) {
		Integer i = 0;
		while (true) {
			JobClient client = new JobClient();
			// Configurations for Job set in this variable
			JobConf conf = new JobConf(BlockedPageRankDriver.class);

			// Name of the Job
			conf.setJobName("PageRankBasic1.1");

			// Data type of Output Key and Value
			conf.setOutputKeyClass(Text.class);
			conf.setOutputValueClass(Text.class);

			// Setting the Mapper and Reducer Class
			conf.setMapperClass(BlockedPageRankMapper.class);
			conf.setReducerClass(BlockedPageRankReducer.class);

			// Formats of the Data Type of Input and output
			conf.setInputFormat((Class<? extends InputFormat>) TextInputFormat.class);
			conf.setOutputFormat((Class<? extends OutputFormat>) TextOutputFormat.class);

			// Specify input and output DIRECTORIES (not files)
			if (i == 0) {
				FileInputFormat.setInputPaths(conf, new Path(args[0]));
			} else {
				FileInputFormat.setInputPaths(conf, new Path(args[1] + "_"+ new Integer(i - 1).toString()));
			}
			FileOutputFormat.setOutputPath(conf,
					new Path(args[1] + "_" + i.toString()));
			client.setConf(conf);
			RunningJob job = null;
			Counters c = null;
			Counter counter;
			try {
				job = JobClient.runJob(conf);
				c = job.getCounters();
				counter = c.findCounter(GlobalCounters.RESIDUAL);
				Double residual = (double) (counter.getValue()/685230.0)/1000000000;
				System.out.println("RESIDUAL: " + residual);
				if(i != 0 && residual < 0.001){
					break;
				}
				i = i+1;
			} catch (Exception e) {
				System.out.println("Error unexpected!");
				e.printStackTrace();
			}
		}
	}

}