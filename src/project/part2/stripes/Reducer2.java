package project.part2.stripes;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.MapWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer2 extends Reducer<Text, MapWritable, Text, Text> {

	Map<String, Integer> map = new HashMap<>();
	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	@Override
	public void reduce(Text key, Iterable<MapWritable> values, Context context)
			throws IOException, InterruptedException {

		Map<String, Integer> mapvs = new HashMap<>();
		for (MapWritable v : values) {
			for (Entry<Writable, Writable> e : v.entrySet()) {

				String nKey = e.getKey().toString();

				if (mapvs.containsKey(nKey)) {
					mapvs.put(nKey, mapvs.get(nKey) + 1);
				} else {
					mapvs.put(nKey, 1);
				}

				String hKey = key.toString();

				if (map.containsKey(hKey)) {
					map.put(hKey, map.get(hKey) + 1);
				} else {
					map.put(hKey, 1);
				}
			}
		}
		StringBuilder sb = new StringBuilder("[");
		for (Entry<String, Integer> entry : mapvs.entrySet()) {
			String frequency = decimalFormat.format(entry.getValue()
					/ new Double(map.get(key.toString())));
			sb.append(" (" + entry.getKey() + ", " + frequency + ") ");
		}
		sb.append("]");
		context.write(new Text(key.toString()), new Text(sb.toString()));
	}
}