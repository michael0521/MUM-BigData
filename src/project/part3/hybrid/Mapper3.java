package project.part3.hybrid;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Mapper;

public class Mapper3 extends Mapper<LongWritable, Text, Text, Pair> {

	Map<Pair, Integer> map = new HashMap<>();

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
				Pair pair = new Pair(neighbor, 1);
				//Pair p = new Pair(items[i], "*");
				context.write(new Text(items[i]), pair);
				//context.write(p, new IntWritable(1));
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

class Pair implements WritableComparable<Pair> {

	private Text neighbor = new Text();
	private IntWritable count = new IntWritable();

	public Pair() {
	}

	public Pair(String neighbor, int count) {
		this.neighbor = new Text(neighbor);
		this.count = new IntWritable(count);
	}

	public String getNeighbor() {
		return neighbor.toString();
	}

	public void setNeighbor(String host) {
		this.neighbor = new Text(host);
	}

	public String getCount() {
		return count.toString();
	}

	public void setCount(int count) {
		this.count = new IntWritable(count);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		neighbor.readFields(in);
		count.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		neighbor.write(out);
		count.write(out);
	}

	@Override
	public int compareTo(Pair pair) {
		int tmp = this.getNeighbor().compareTo(pair.getNeighbor());
		return tmp == 0 ? this.getCount().compareTo(pair.getCount())
				: tmp;
	}

	@Override
	public String toString() {
		return "(" + neighbor + ", " + count + ")";
	}

	@Override
	public int hashCode() {
		return neighbor.hashCode() * 163 + count.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Pair) {
			Pair tp = (Pair) o;
			return neighbor.equals(tp.neighbor) && count.equals(tp.count);
		}
		return false;
	}

}
