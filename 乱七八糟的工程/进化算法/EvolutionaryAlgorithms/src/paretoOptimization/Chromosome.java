package paretoOptimization;

import java.util.ArrayList;

/* 本例是实现多目标优化（加强帕累托进化算法）
 * 函数1为 f1= n个累加(xi^2)
 * 函数2为 f2= n个累加(xi-2)^2 
 * −10 ≤ xi ≤ 10 ，n=1
 * 最优解为x ∈ [0, 2]
 * 
 * 采用16位二进制串代表目标函数
 * 变异率为 1/L，L为染色体长度
 */

public class Chromosome {
	private String bitstring;
	private ArrayList<Double> vector;
	private double fitness;
	private ArrayList<Double> objectives;
	private ArrayList<Chromosome> dom_set;
	private double dist;
	private double raw_fitness;
	private double density;
	
	public void print(int gen,Chromosome best){
		//System.out.println("第" + gen + "代，字符串为：" + best.getBitstring());
		// vector;
		//System.out.println("第" + gen + "代，适应度为：" + best.getFitness());
		System.out.println("第" + gen + "代，目标为：" + best.getObjectives().get(0)+"  "+ best.getObjectives().get(1));
		// dom_set;
		//System.out.println("第" + gen + "代，欧几里得距离为：" + best.getDist());
		//System.out.println("第" + gen + "代，生适应度为：" + best.getRaw_fitness());
		//System.out.println("第" + gen + "代，密度为：" + best.getDensity());
	}
	
	public String getBitstring() {
		return bitstring;
	}
	public void setBitstring(String bitstring) {
		this.bitstring = bitstring;
	}
	public ArrayList<Double> getVector() {
		return vector;
	}
	public void setVector(ArrayList<Double> vector) {
		this.vector = vector;
	}
	public double getFitness() {
		return fitness;
	}
	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	public ArrayList<Double> getObjectives() {
		return objectives;
	}
	public void setObjectives(ArrayList<Double> objectives) {
		this.objectives = objectives;
	}
	public ArrayList<Chromosome> getDom_set() {
		return dom_set;
	}
	public void setDom_set(ArrayList<Chromosome> dom_set) {
		this.dom_set = dom_set;
	}
	public double getDist() {
		return dist;
	}
	public void setDist(double dist) {
		this.dist = dist;
	}
	public double getRaw_fitness() {
		return raw_fitness;
	}
	public void setRaw_fitness(double raw_fitness) {
		this.raw_fitness = raw_fitness;
	}
	public double getDensity() {
		return density;
	}
	public void setDensity(double density) {
		this.density = density;
	}
}
