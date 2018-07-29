package TSP;

import java.util.Random;
import java.util.Vector;

/* 实数编码的染色体，基因表示为0,1,2....n-1
 *  根据需求自己修改适应度函数calculatefitness()
 *  本例适应度为一个染色体中各个基因之间的距离（TSP的城市间距离）的倒数
 *  比如您可以给路径加权，加约束条件等等
*/

public class Chromosome implements Cloneable {

	private int geneNum; // 总的基因数目（TSP问题中的城市数目）
	private int[][] distance; // 基因之间的度量（TSP问题中的城市之间的距离）
	private int chromLen; // 每条染色体携带的基因数目（TSP问题中的要旅行的城市数目，也即染色体长度，一般为总基因数）
	private int[] geneSequence; // 染色体内的基因序列
	private double fitness; // 适应度

	public Chromosome() {
		this.geneNum = 30;
		this.distance = new int[geneNum][geneNum];
		this.chromLen = geneNum;
		this.geneSequence = new int[geneNum];
	}

	public Chromosome(int num, int[][] distance, int chromlen) {
		this.geneNum = num;
		this.distance = distance;
		this.chromLen = chromlen;
		this.geneSequence = new int[chromLen];
	}

	public void randomGeneration() {
		Vector<Integer> allowedGenes = new Vector<Integer>();
		for (int i = 0; i < geneNum; i++) {
			allowedGenes.add(Integer.valueOf(i));
		}
		Random r = new Random(System.currentTimeMillis());
		for (int i = 0; i < geneNum; i++) {
			int index = r.nextInt(allowedGenes.size());
			int selectedGene = allowedGenes.get(index).intValue();
			geneSequence[i] = selectedGene;
			allowedGenes.remove(index);
		}
	}

	public void print() {
		for (int i = 0; i < geneNum; i++) {
			System.out.print(geneSequence[i] + ",");
		}
		System.out.println();
		System.out.println("Its fitness measure is  " + getFitness());
	}

	// 计算适应度函数
	private double calculatefitness() {
		double fitness = 0.0;
		int len = 0;
		for (int i = 0; i < geneNum - 1; i++) {
			len += distance[this.geneSequence[i]][this.geneSequence[i + 1]];
		}
		fitness = 1.0 / len; // 适应度为路程倒数
		return fitness;
	}

	public int[] getgeneSequence() {
		return geneSequence;
	}

	public void setgeneSequence(int[] geneSequence) {
		this.geneSequence = geneSequence;
	}

	public int[][] getDistance() {
		return distance;
	}

	public void setDistance(int[][] distance) {
		this.distance = distance;
	}

	public int getgeneNum() {
		return geneNum;
	}

	public void setgeneNum(int geneNum) {
		this.geneNum = geneNum;
	}

	public double getFitness() {
		this.fitness = calculatefitness();
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Chromosome chromosome = (Chromosome) super.clone();
		chromosome.geneNum = this.geneNum;
		chromosome.distance = this.distance.clone();
		chromosome.geneSequence = this.geneSequence.clone();
		chromosome.fitness = this.fitness;
		return chromosome;
	}

}
