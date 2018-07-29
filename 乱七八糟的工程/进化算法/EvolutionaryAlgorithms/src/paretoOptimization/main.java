package paretoOptimization;

import java.util.ArrayList;

public class main {
	public static int problem_size = 1;// 1代表 search_space 为一个解，一维数组可以表示
	public static ArrayList<ArrayList<Integer>> search_space = new ArrayList<ArrayList<Integer>>();;
	public static int max_gens = 50;
	public static int pop_size = 80;
	public static int archive_size = 40;
	public static double p_cross = 0.90;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GA ga = new GA();
		int[] domainOfX = new int[] {-10, 10};
		ArrayList<ArrayList<Integer>> search_space = new ArrayList<ArrayList<Integer>>();
		ArrayList<Integer> content = new ArrayList<Integer>();
		for(int i = 0; i < domainOfX.length; i++) {
			content.add(domainOfX[i]);
		}
		for(int i = 0; i < problem_size; i++) {
			search_space.add(new ArrayList<Integer>(content));
		}
		ga.search(search_space, max_gens, pop_size, archive_size, p_cross);
	}
}
