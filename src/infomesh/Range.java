package infomesh;

public class Range {
	private double min;
	private double max;
	public Range(double min, double max) {
		super();
		this.min = min;
		this.max = max;
	}
	public double getMax() {
		return max;
	}
	public void setMax(double max) {
		this.max = max;
	}
	public double getMin() {
		return min;
	}
	public void setMin(double min) {
		this.min = min;
	}
	@Override
    public String toString() {
		return "[" +min + ","+max+"]";
    }
	public double getDiff() {
		return max-min;
	}
	
}
