package virtualNetworkEmbedding;

import java.util.Random;
import java.util.Vector;

import Graph.Graph;

/* 使用的TSP问题的代码实现VNE问题 */

public class Chromosome implements Cloneable {
	private Graph physicGraph;
	private Graph virtualGraph;
	private int geneNum; // 物理网络节点数
	private int chromLen; // 虚拟网络节点数
	private int[] geneSequence; // 染色体内的基因序列（物理节点序列）
	private double fitness; // 适应度

	public Chromosome(int geneNum, int chromLen) {
		this.physicGraph = new Graph("P.txt", true);
		this.virtualGraph = new Graph("V.txt", false);
		this.geneNum = geneNum;
		this.chromLen = chromLen;
		this.geneSequence = new int[this.chromLen];
		this.fitness = 0.0;
	}

	public int getChromLen() {
		return chromLen;
	}

	public void setChromLen(int chromLen) {
		this.chromLen = chromLen;
	}

	public int[] getGeneSequence() {
		return geneSequence;
	}

	public void setGeneSequence(int[] geneSequence) {
		this.geneSequence = geneSequence;
	}

	public double getFitness() {
		this.fitness = calculatefitness();
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	// 生成随机解
	public void randomGeneration() {
		Vector<Integer> allowedGenes = new Vector<Integer>();
		for (int i = 0; i < geneNum; i++) {
			allowedGenes.add(Integer.valueOf(i));
		}
		for (int i = 0; i < chromLen; i++) {
			Random r = new Random(System.currentTimeMillis() + new Random().nextInt(1000));
			int index = r.nextInt(allowedGenes.size());
			geneSequence[i] = allowedGenes.get(index).intValue();
			allowedGenes.remove(index);
		}
	}

	// 计算适应度函数
	private double calculatefitness() {
		double nodeWeight = 1.0; // 节点开销权重参数
		double nodeCost = 0.0; // 节点开销
		double linkWeight = 1.0; // 链路开销权重参数
		double linkCost = 0.0; // 链路开销
		double fitness = 0.0;

		for (int i = 0; i < chromLen; i++) {
			nodeCost += virtualGraph.nodes[i].getCapacity();
		}
		for (int i = 0; i < virtualGraph.links.length; i++) {
			int left = geneSequence[virtualGraph.links[i].getLeftID()];
			int right = geneSequence[virtualGraph.links[i].getRightID()];
			int[] path = physicGraph.dijkstra(left, right, virtualGraph.links[i].getBandWidth());
			if (path == null) {
				// System.out.println("ERROR!");
				physicGraph.refreshMatrix();
				fitness = Double.MIN_VALUE;
				return fitness;
			} else {
				linkCost += virtualGraph.links[i].getBandWidth() * (path.length - 1);
			}
		}
		fitness = 1000000.0 / (nodeWeight * nodeCost + linkWeight * linkCost);
		physicGraph.refreshMatrix();
		return fitness;
	}

	public void print() {
		System.out.print("虚拟网络节点映射的物理节点序列为：");
		for (int i = 0; i < chromLen; i++) {
			System.out.print(geneSequence[i] + ", ");
		}
		System.out.println("适应度为： " + getFitness());
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		Chromosome chromosome = (Chromosome) super.clone();
		chromosome.chromLen = this.chromLen;
		chromosome.geneSequence = this.geneSequence.clone();
		chromosome.fitness = this.fitness;
		return chromosome;
	}
}
