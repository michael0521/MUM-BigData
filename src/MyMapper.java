import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<LongWritable, Text, Pair, IntWritable> {

	@Override
	public void map(LongWritable key, Text value, Context context)
			throws IOException, InterruptedException {
		// System.out.println(value.toString());
		String[] items = value.toString().split(" ");
		for (int i = 0; i < items.length; i++) {
			System.out.println(getN(i, items));
			for (String neighbor : getN(i, items)) {

				if (neighbor.equals("")) {
					continue;
				}
				Pair pair = new Pair(items[i], neighbor);
				context.write(pair, new IntWritable(1));
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

/**
 * class Pair implements WritableComparable<Pair>{ private Text host; private
 * Text neighbor;
 * 
 * public Pair(Text first, Text second) { set(first, second); }
 * 
 * public Pair() { set(new Text(), new Text()); }
 * 
 * public Pair(String first, String second) { set(new Text(first), new
 * Text(second)); }
 * 
 * public Text getFirst() { return host; }
 * 
 * public Text getSecond() { return neighbor; }
 * 
 * public void set(Text first, Text second) { this.host = first; this.neighbor =
 * second; }
 * 
 * @Override public void readFields(DataInput in) throws IOException {
 *           host.readFields(in); neighbor.readFields(in); }
 * @Override public void write(DataOutput out) throws IOException {
 *           host.write(out); neighbor.write(out); }
 * @Override public String toString() { return host + " " + neighbor; }
 * @Override public int compareTo(Pair tp) { int cmp = host.compareTo(tp.host);
 * 
 *           if (cmp != 0) { return cmp; }
 * 
 *           return neighbor.compareTo(tp.neighbor); }
 * @Override public int hashCode(){ return host.hashCode()*163 +
 *           neighbor.hashCode(); }
 * @Override public boolean equals(Object o) { if(o instanceof Pair) { Pair tp =
 *           (Pair) o; return host.equals(tp.host) &&
 *           neighbor.equals(tp.neighbor); } return false; } }
 */

class Pair implements WritableComparable<Pair> {

	private Text host = new Text();
	private Text neighbor = new Text();

	public Pair() {
	}

	public Pair(String host, String neighbor) {
		this.host = new Text(host);
		this.neighbor = new Text(neighbor);
	}

	public String getHost() {
		return host.toString();
	}

	public void setHost(String host) {
		this.host = new Text(host);
	}

	public String getNeighbor() {
		return neighbor.toString();
	}

	public void setNeighbor(String neighbor) {
		this.neighbor = new Text(neighbor);
	}

	@Override
	public void readFields(DataInput in) throws IOException {
		host.readFields(in);
		neighbor.readFields(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		host.write(out);
		neighbor.write(out);
	}

	@Override
	public int compareTo(Pair pair) {
		int tmp = this.getHost().compareTo(pair.getHost());
		return tmp == 0 ? this.getNeighbor().compareTo(pair.getNeighbor())
				: tmp;
	}

	@Override
	public String toString() {
		return "(" + host + ", " + neighbor + ")";
	}

}
