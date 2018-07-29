package homework;
public class  Employee
{
	private int normalWorkTime;
	private int extraWorkTime;
	public Employee(int normalWorkTime,int extraWorkTime) {
		this.normalWorkTime = normalWorkTime;
		this.extraWorkTime = extraWorkTime;
	}
	public int calc(int normalWorkSalary,int extraWorkSalary) {
		return normalWorkTime*normalWorkSalary+extraWorkTime*extraWorkSalary;
	}
	public int calc(int normalWorkSalary) {
		return normalWorkTime*normalWorkSalary;
	}
	public static void main(String[] args) 
	{
		Employee employee = new Employee(22,6);

		System.out.println("��һ�ֹ���:"+employee.calc(80,100));
		System.out.println("�ڶ��ֹ���:"+employee.calc(100));
	}
}
