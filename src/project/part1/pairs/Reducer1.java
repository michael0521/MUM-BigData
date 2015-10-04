package project.part1.pairs;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer1 extends Reducer<Pair, IntWritable, Text, Text> {
	Map<String, Integer> map = new HashMap<>();
	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	@Override
	public void reduce(Pair key, Iterable<IntWritable> values, Context context)
			throws IOException, InterruptedException {
		int count = 0;

		if (key.getNeighbor().equals("*")) {
			for (IntWritable v : values) {
				count += v.get();
			}
			map.put(key.getHost(), count);
		} else {
			for (IntWritable v : values) {
				count += v.get();
			}
			context.write(
					new Text(key.toString()),
					new Text(decimalFormat.format(count
							/ new Double(map.get(key.getHost())))));
		}
	}

}