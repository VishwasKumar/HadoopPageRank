package indiana.cgl.hadoop.pagerank.helper;

import indiana.cgl.hadoop.pagerank.HadoopPageRank;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

 

public class CreateGraphReduce extends Reducer<LongWritable, Text, LongWritable, Text>{
	
	public void reduce(LongWritable key, Iterable<Text> values,
			Context context) throws IOException {
	
		try {
			
			Text outputValue = values.iterator().next();
			context.write(key, outputValue);
			// check the value of reduce 
			Log log = LogFactory.getLog(HadoopPageRank.class);
			System.out.println("values.iterator().next() = " + outputValue);
			log.info("values.iterator().next() = " + outputValue);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}