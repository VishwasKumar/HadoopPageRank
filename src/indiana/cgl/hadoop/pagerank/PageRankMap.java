package indiana.cgl.hadoop.pagerank;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
 
public class PageRankMap extends Mapper<LongWritable, Text, LongWritable, Text> {

// each map task handles one line within an adjacency matrix file
// key: file offset
// value: <sourceUrl PageRank#targetUrls>
	public void map(LongWritable key, Text value, Context context)
		throws IOException, InterruptedException {
			int numUrls = context.getConfiguration().getInt("numUrls",1);
			String line = value.toString();
			StringBuffer sb = new StringBuffer();
			// instance an object that records the information for one webpage
			RankRecord rrd = new RankRecord(line);
			// double rankValueOfSrcUrl;
			if (rrd.targetUrlsList.size()<=0){
				// there is no out degree for this webpage; 
				// scatter its rank value to all other urls
				double rankValuePerUrl = rrd.rankValue/(double)numUrls;
				for (int i=0;i<numUrls;i++){
				context.write(new LongWritable(i), new Text(String.valueOf(rankValuePerUrl)));
				}
			} else {
				// calculate the current node's contribution to the page rank of its out links
				double rankValuePerUrl = rrd.rankValue/(double)rrd.targetUrlsList.size();
				//iterate through all the out links 
				for (Integer targetUrls : rrd.targetUrlsList) {
					//append the out links from the current source node
					sb.append("#" + String.valueOf(targetUrls));
					context.write(new LongWritable(targetUrls), new Text(String.valueOf(rankValuePerUrl)));
				}
			} //for
			if (!sb.toString().isEmpty()) {
				context.write(new LongWritable(rrd.sourceUrl), new Text(sb.toString()));
			}
		} // end map

}
