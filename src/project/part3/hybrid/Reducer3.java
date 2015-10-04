package project.part3.hybrid;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reducer3 extends Reducer<Text, Pair, Text, Text> {
	Map<String, Integer> map = new HashMap<>();
	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	@Override
	public void reduce(Text key, Iterable<Pair> values, Context context)
			throws IOException, InterruptedException {
		
		int sum = 0;
		for (Pair pair: values) {
			String pKey = pair.getNeighbor();
			if(map.containsKey(pKey)){
				map.put(pKey, map.get(pKey) + 1);
			}else{
				map.put(pKey, new Integer(pair.getCount()));
			}
			sum += 1;
		}
		
		StringBuilder sb = new StringBuilder("[");
		for(Entry<String, Integer> entry: map.entrySet()){
			String frequency = decimalFormat.format(entry.getValue()/new Double(sum));
			sb.append(" (" + entry.getKey() + ", " + frequency + ") ");
			//context.write(new Text(key.toString()), new Text("(" + entry.getKey() + ", " + frequency + ")"));
		}
		sb.append("]");
		context.write(new Text(key.toString()), new Text(sb.toString()));
		map.clear();
	}
}