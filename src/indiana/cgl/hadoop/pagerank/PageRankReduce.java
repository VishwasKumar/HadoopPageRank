package indiana.cgl.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
 
public class PageRankReduce extends Reducer<LongWritable, Text, LongWritable, Text>{
	public void reduce(LongWritable key, Iterable<Text> values,
			Context context) throws IOException, InterruptedException {
		double sumOfRankValues = 0.0;
		String targetUrlsList = "";
		
		int numUrls = context.getConfiguration().getInt("numUrls",1);
		
		//hints each tuple may include: rank value tuple or link relation tuple  
		for (Text value: values){
			System.out.println(key.toString() + value.toString());
			if (value.toString().contains("#")) {
				targetUrlsList = value.toString();
			}
			else {
				sumOfRankValues += Double.parseDouble(value.toString());
			}
		} // end for loop
		sumOfRankValues = (0.85*sumOfRankValues)+(0.15*(1.0)/(double)numUrls);
		context.write(key, new Text(sumOfRankValues+targetUrlsList));
	}
}
