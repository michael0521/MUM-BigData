package project.part2.stripes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper2 extends Mapper<LongWritable, Text, Text, MapWritable> {


	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {

		String[] items = value.toString().split(" ");
		for (int i = 0; i < items.length; i++) {
			// System.out.println(getN(i, items));
			for (String neighbor : getN(i, items)) {
				if (neighbor.equals("")) {
					continue;
				}
				
				MapWritable mapw = new MapWritable();
				mapw.put(new Text(neighbor), new IntWritable(1));
				context.write(new Text(items[i]), mapw);
			}
		}
	}

	public List<String> getN(int idx, String[] items) {

		List<String> list = new ArrayList<>();
		for (int j = idx + 1; j < items.length; j++) {
			if (items[idx].equals(items[j])) {
				return list;
			} else {
				list.add(items[j]);
			}
		}
		return list;
	}
}


