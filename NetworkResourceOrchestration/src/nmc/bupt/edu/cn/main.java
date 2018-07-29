package nmc.bupt.edu.cn;

public class main {
	public static SmallNetwork smallNetwork = new SmallNetwork();
	public static MediumNetwork mediumNetwork = new MediumNetwork();
	public static LargeNetwork largeNetwork = new LargeNetwork();
	public static void main(String[] args) {
		//NROT 类构造方法只有一个Integer型参数，即为算法输入：SFC的数量
		NROT nrot = new NROT(3 );
	}
}
