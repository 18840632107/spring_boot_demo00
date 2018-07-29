package virtualNetworkEmbedding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import virtualNetworkEmbedding.Chromosome;

public class GA {
	private int physicalNodeNum = 55;
	private int virtualNodeNum = 14;

	private Chromosome[] chromosomes;
	private Chromosome[] nextGeneration;
	private int population;
	private int genMax;
	private double p_c_t;
	private double p_m_t;
	private double bestFitness;
	private double[] averageFitness;
	private int[] GeneSeq;

	public GA(int population, int genMax, double p_c, double p_m) {
		this.chromosomes = new Chromosome[population];
		this.nextGeneration = new Chromosome[population];
		this.population = population;
		this.genMax = genMax;
		this.p_c_t = p_c;
		this.p_m_t = p_m;
		this.averageFitness = new double[genMax];
		this.bestFitness = 0.0;
		this.GeneSeq = new int[virtualNodeNum];
	}

	// 种群初始化，产生随机解
	public void iniPopulation() {
		for (int i = 0; i < population; i++) {
			Chromosome chromosome = new Chromosome(physicalNodeNum, virtualNodeNum);
			chromosome.randomGeneration();
			chromosomes[i] = chromosome;
			// System.out.println(i+": ");
			// chromosome.print();
		}
	}

	// 遗传算子：选择，交叉与变异
	public void evolve(int genMax) {
		double[] selectionP = new double[population];// 选择概率
		double sum = 0.0;
		double tmp = 0.0;

		int count = 0;
		for (int i = 0; i < population; i++) {
			if (chromosomes[i].getFitness() == Double.MIN_VALUE) {
				count++;
			} else {
				sum += chromosomes[i].getFitness();
			}
			if (chromosomes[i].getFitness() > bestFitness) {
				bestFitness = chromosomes[i].getFitness();
				for (int j = 0; j < virtualNodeNum; j++) {
					GeneSeq[j] = chromosomes[i].getGeneSequence()[j];
				}
			}
		}
		averageFitness[genMax] = sum / (population - count);
		/*
		 * 输出每一代的平均适应度与最佳适应度 System.out.println("The average fitness in " + g +
		 * " generation is " + averageFitness[g] + ", and the best fitness is "
		 * + bestFitness);
		 */

		/*
		 * 使用轮盘赌选择法选择染色体 还有很多选择方法，具体见说明文档，有需要可以自己修改 （PS：哪种选择的方法不是选择啊）
		 */
		for (int i = 0; i < population; i++) {
			tmp += chromosomes[i].getFitness() / sum;
			selectionP[i] = tmp;
		}
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < population; i += 2) {
			Chromosome[] children = new Chromosome[2];
			// System.out.println("---------start
			// selection-----------");System.out.println();
			// 选出两个父代染色体
			for (int j = 0; j < 2; j++) {
				int selectedGene = 0;
				for (int k = 0; k < population - 1; k++) {
					double p = random.nextDouble();
					if (p > selectionP[k] && p <= selectionP[k + 1]) {
						selectedGene = k;
					}
					if (k == 0 && random.nextDouble() <= selectionP[k]) {
						selectedGene = 0;
					}
				}
				try {
					children[j] = (Chromosome) chromosomes[selectedGene].clone();
					// children[j].print(); System.out.println();
				} catch (CloneNotSupportedException e) {
					e.printStackTrace();
				}
			}
			/*
			 * 交叉操作(OX) 还有很多交叉方法，具体见说明文档，有需要可以自己修改 （PS：哪种交叉的方法不是交叉啊）
			 */
			// System.out.println("----------Start
			// crossover----------");System.out.println();
			if (random.nextDouble() < p_c_t) {
				// 定义两个cut点
				int cutPoint1 = -1;
				int cutPoint2 = -1;
				int r1 = random.nextInt(virtualNodeNum);
				if ((r1 > 0) && (r1 < virtualNodeNum - 1)) {
					cutPoint1 = r1;
					int r2 = random.nextInt(virtualNodeNum - r1);
					if (r2 == 0) {
						cutPoint2 = r1 + 1;
					} else if (r2 > 0) {
						cutPoint2 = r1 + r2;
					}
				}
				if (cutPoint1 > 0 && cutPoint2 > 0) {
					int[] tour1 = new int[virtualNodeNum];
					int[] tour2 = new int[virtualNodeNum];
					if (cutPoint2 == virtualNodeNum - 1) {
						for (int j = 0; j < virtualNodeNum; j++) {
							tour1[j] = children[0].getGeneSequence()[j];
							tour2[j] = children[1].getGeneSequence()[j];
						}
					} else {
						for (int j = 0; j < virtualNodeNum; j++) {
							if (j < cutPoint1) {
								tour1[j] = children[0].getGeneSequence()[j];
								tour2[j] = children[1].getGeneSequence()[j];
							} else if (j >= cutPoint1 && j < cutPoint1 + virtualNodeNum - cutPoint2 - 1) {
								tour1[j] = children[0].getGeneSequence()[j + cutPoint2 - cutPoint1 + 1];
								tour2[j] = children[1].getGeneSequence()[j + cutPoint2 - cutPoint1 + 1];
							} else {
								tour1[j] = children[0].getGeneSequence()[j - virtualNodeNum + cutPoint2 + 1];
								tour2[j] = children[1].getGeneSequence()[j - virtualNodeNum + cutPoint2 + 1];
							}
						}
					}

					for (int j = 0; j < virtualNodeNum; j++) {
						if ((j < cutPoint1) || (j > cutPoint2)) {
							children[0].getGeneSequence()[j] = -1;
							children[1].getGeneSequence()[j] = -1;
						} else {
							int tmp1 = children[0].getGeneSequence()[j];
							children[0].getGeneSequence()[j] = children[1].getGeneSequence()[j];
							children[1].getGeneSequence()[j] = tmp1;
						}
					}
					if (cutPoint2 == virtualNodeNum - 1) {
						int position = 0;
						for (int j = 0; j < cutPoint1; j++) {
							for (int m = position; m < virtualNodeNum; m++) {
								boolean flag = true;
								for (int n = 0; n < virtualNodeNum; n++) {
									if (tour1[m] == children[0].getGeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[0].getGeneSequence()[j] = tour1[m];
									position = m + 1;
									break;
								}
							}
						}
						position = 0;
						for (int j = 0; j < cutPoint1; j++) {
							for (int m = position; m < virtualNodeNum; m++) {
								boolean flag = true;
								for (int n = 0; n < virtualNodeNum; n++) {
									if (tour2[m] == children[1].getGeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[1].getGeneSequence()[j] = tour2[m];
									position = m + 1;
									break;
								}
							}
						}
					} else {
						int position = 0;
						for (int j = cutPoint2 + 1; j < virtualNodeNum; j++) {
							for (int m = position; m < virtualNodeNum; m++) {
								boolean flag = true;
								for (int n = 0; n < virtualNodeNum; n++) {
									if (tour1[m] == children[0].getGeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[0].getGeneSequence()[j] = tour1[m];
									position = m + 1;
									break;
								}
							}
						}
						for (int j = 0; j < cutPoint1; j++) {
							for (int m = position; m < virtualNodeNum; m++) {
								boolean flag = true;
								for (int n = 0; n < virtualNodeNum; n++) {
									if (tour1[m] == children[0].getGeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[0].getGeneSequence()[j] = tour1[m];
									position = m + 1;
									break;
								}
							}
						}
						position = 0;
						for (int j = cutPoint2 + 1; j < virtualNodeNum; j++) {
							for (int m = position; m < virtualNodeNum; m++) {
								boolean flag = true;
								for (int n = 0; n < virtualNodeNum; n++) {
									if (tour2[m] == children[1].getGeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[1].getGeneSequence()[j] = tour2[m];
									position = m + 1;
									break;
								}
							}
						}
						for (int j = 0; j < cutPoint1; j++) {
							for (int m = position; m < virtualNodeNum; m++) {
								boolean flag = true;
								for (int n = 0; n < virtualNodeNum; n++) {
									if (tour2[m] == children[1].getGeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[1].getGeneSequence()[j] = tour2[m];
									position = m + 1;
									break;
								}
							}
						}
					}
				}
			}
			// children[0或1].print();
			/*
			 * 变异操作(DM) 还有很多变异方法，具体见说明文档，有需要可以自己修改 （PS：哪种变异的方法不是变异啊）
			 */
			// System.out.println("---------Start
			// mutation------");System.out.println();
			if (random.nextDouble() < p_m_t) {
				for (int j = 0; j < 2; j++) {
					// 定义两个cut点
					int cutPoint1 = -1;
					int cutPoint2 = -1;
					int r1 = random.nextInt(virtualNodeNum);
					if ((r1 > 0) && (r1 < virtualNodeNum - 1)) {
						cutPoint1 = r1;
						int r2 = random.nextInt(virtualNodeNum - r1);
						if (r2 == 0) {
							cutPoint2 = r1 + 1;
						} else if (r2 > 0) {
							cutPoint2 = r1 + r2;
						}
					}
					if (cutPoint1 > 0 && cutPoint2 > 0) {
						List<Integer> tour = new ArrayList<Integer>();
						// System.out.println("Cut point is "+cutPoint+", and
						// cut point is "+cutPoint);
						if (cutPoint2 == virtualNodeNum - 1) {
							for (int k = 0; k < cutPoint1; k++) {
								tour.add(Integer.valueOf(children[j].getGeneSequence()[k]));
							}
						} else {
							for (int k = 0; k < virtualNodeNum; k++) {
								if (k < cutPoint1 || k > cutPoint2) {
									tour.add(Integer.valueOf(children[j].getGeneSequence()[k]));
								}
							}
						}
						int position = random.nextInt(tour.size());
						if (position == 0) {
							for (int k = cutPoint2; k >= cutPoint1; k--) {
								tour.add(0, Integer.valueOf(children[j].getGeneSequence()[k]));
							}
						} else if (position == tour.size() - 1) {
							for (int k = cutPoint1; k <= cutPoint2; k++) {
								tour.add(Integer.valueOf(children[j].getGeneSequence()[k]));
							}
						} else {
							for (int k = cutPoint1; k <= cutPoint2; k++) {
								tour.add(position, Integer.valueOf(children[j].getGeneSequence()[k]));
							}
						}
						for (int k = 0; k < virtualNodeNum; k++) {
							children[j].getGeneSequence()[k] = tour.get(k).intValue();
						}
					}
				}
			}
			// children[0或1].print();
			nextGeneration[i] = children[0];
			nextGeneration[i + 1] = children[1];
		}
		for (int k = 0; k < population; k++) {
			try {
				chromosomes[k] = (Chromosome) nextGeneration[k].clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	public void printOptimal() {
		System.out.println("最佳适应度为：" + bestFitness);
		System.out.print("最优映射结果的物理节点序列为： ");
		for (int i = 0; i < virtualNodeNum; i++) {
			System.out.print(GeneSeq[i] + ",");
		}
		System.out.println();
	}

	// 运行遗传算法
	public void solve() {
		iniPopulation();
		for (int i = 0; i < genMax; i++) {
			evolve(i);
		}
		printOptimal();
	}

}