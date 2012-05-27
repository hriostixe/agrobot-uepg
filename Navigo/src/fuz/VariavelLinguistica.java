package fuz;

public class VariavelLinguistica {
	private double start;
	private double leftTop;
	private double rightTop;
	private double finish;
	private String name;
	public double getStart() {
		return start;
	}
	
	public VariavelLinguistica(double start, double leftTop, double rightTop,
			double finish, String name) {

		this.start = start;
		this.leftTop = leftTop;
		this.rightTop = rightTop;
		this.finish = finish;
		this.name = name;
	}
	public void setStart(double start) {
		this.start = start;
	}
	public double getLeftTop() {
		return leftTop;
	}
	public void setLeftTop(double leftTop) {
		this.leftTop = leftTop;
	}
	public double getRightTop() {
		return rightTop;
	}
	public void setRightTop(double rightTop) {
		this.rightTop = rightTop;
	}
	public double getFinish() {
		return finish;
	}
	public void setFinish(double finish) {
		this.finish = finish;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
