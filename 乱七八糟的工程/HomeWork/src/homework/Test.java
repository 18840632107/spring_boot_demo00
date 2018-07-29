package homework;
class  Circle
{
	double round;
	public Circle() {
		
	}
	public double area()
	{
		return Math.PI*round*round;
	}
}

class Cylinder extends Circle
{
	double  height;
	double round;
	public Cylinder(double height,double round) {
		this.height = height;
		this.round = round;
	}
	public double area() {
		return 2*Math.PI*round*round+2*Math.PI*round*height;
	}
}
class Sphere extends Circle
{
	double round;
	public Sphere(double round) {
		this.round = round;
	}
	public double area() {
		return 4*Math.PI*round*round;
	}
}
/*
public class Test {
	public static void main(String[] args) {
		Circle cy1 = new Cylinder(10,3);
		System.out.println("圆柱体的面积:"+cy1.area());
		Circle sp1 = new Sphere(1);
		System.out.println("球的面积:"+sp1.area());
	}
}*/

public class Test {
	public static void main(String[] args) {
		Circle[] objects = {new Cylinder(10,3),new Cylinder(10,2),new Sphere(1),new Sphere(10),new Sphere(5)};
		for (Circle object : objects) {
			System.out.println(object.getClass().getSimpleName()+"的面积:"+object.area());
		}
	}
}
