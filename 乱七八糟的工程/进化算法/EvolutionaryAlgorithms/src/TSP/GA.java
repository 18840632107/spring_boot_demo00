package TSP;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GA {
	private Chromosome[] chromosomes;
	private Chromosome[] nextGeneration;
	private int population;
	private int geneNum;
	private double p_c_t;
	private double p_m_t;
	private int MAX_GEN;
	private int bestLength;
	private int[] GeneSeq;
	private double bestFitness;
	private double[] averageFitness;
	private int[][] distance;

	/*
	 * Constructor of GA class
	 * 
	 * @param n 种群规模
	 * 
	 * @param num 基因数目
	 * 
	 * @param g 运行代数
	 * 
	 * @param p_c 交叉率
	 * 
	 * @param p_m 变异率
	 * 
	 * @param filename 数据文件名
	 */
	public GA(int n, int num, int g, double p_c, double p_m) {
		this.population = n;
		this.geneNum = num;
		this.MAX_GEN = g;
		this.p_c_t = p_c;
		this.p_m_t = p_m;
		this.GeneSeq = new int[geneNum];
		this.averageFitness = new double[MAX_GEN];
		this.bestFitness = 0.0;
		this.chromosomes = new Chromosome[population];
		this.nextGeneration = new Chromosome[population];
		this.distance = new int[geneNum][geneNum];
	}

	public void solve() throws IOException {
		// System.out.println("---------------------Start
		// initilization---------------------");
		initPopulations();
		// System.out.println("---------------------End
		// initilization---------------------");
		// System.out.println("---------------------Start
		// evolution---------------------");
		for (int i = 0; i < MAX_GEN; i++) {
			// System.out.println("-----------Start generation " + i +
			// "----------");
			evolve(i);
			// System.out.println("-----------End generation " + i +
			// "----------");
		}
		// System.out.println("---------------------End
		// evolution---------------------");
		printOptimal();
		outputResults();
	}

	// 初始化GA
	private void initPopulations() throws IOException {
		int distanceGene = 100;
		int[] x = new int[geneNum];
		int[] y = new int[geneNum];
		distance = new int[geneNum][geneNum];
		Random random = new Random(System.currentTimeMillis());

		for (int i = 0; i < geneNum; i++) {
			x[i] = random.nextInt(distanceGene);
			y[i] = random.nextInt(distanceGene);
		}
		// 计算距离矩阵,计算方法为伪欧氏距离
		for (int i = 0; i < geneNum - 1; i++) {
			distance[i][i] = 0;
			for (int j = i + 1; j < geneNum; j++) {
				double rij = Math.sqrt((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j]) * (y[i] - y[j]));
				int tij = (int) Math.round(rij);
				distance[i][j] = tij;
				distance[j][i] = distance[i][j];
			}
		}
		distance[geneNum - 1][geneNum - 1] = 0;

		for (int i = 0; i < population; i++) {
			Chromosome chromosome = new Chromosome(geneNum, distance, geneNum);
			chromosome.randomGeneration();
			chromosomes[i] = chromosome;
			// chromosome.print();
		}
	}

	private void evolve(int g) {
		double[] selectionP = new double[population];// 选择概率
		double sum = 0.0;
		double tmp = 0.0;

		for (int i = 0; i < population; i++) {
			sum += chromosomes[i].getFitness();
			if (chromosomes[i].getFitness() > bestFitness) {
				bestFitness = chromosomes[i].getFitness();
				bestLength = (int) (1.0 / bestFitness);
				for (int j = 0; j < geneNum; j++) {
					GeneSeq[j] = chromosomes[i].getgeneSequence()[j];
				}
			}
		}
		averageFitness[g] = sum / population;
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
				int r1 = random.nextInt(geneNum);
				if ((r1 > 0) && (r1 < geneNum - 1)) {
					cutPoint1 = r1;
					int r2 = random.nextInt(geneNum - r1);
					if (r2 == 0) {
						cutPoint2 = r1 + 1;
					} else if (r2 > 0) {
						cutPoint2 = r1 + r2;
					}
				}
				if (cutPoint1 > 0 && cutPoint2 > 0) {
					int[] tour1 = new int[geneNum];
					int[] tour2 = new int[geneNum];
					if (cutPoint2 == geneNum - 1) {
						for (int j = 0; j < geneNum; j++) {
							tour1[j] = children[0].getgeneSequence()[j];
							tour2[j] = children[1].getgeneSequence()[j];
						}
					} else {
						for (int j = 0; j < geneNum; j++) {
							if (j < cutPoint1) {
								tour1[j] = children[0].getgeneSequence()[j];
								tour2[j] = children[1].getgeneSequence()[j];
							} else if (j >= cutPoint1 && j < cutPoint1 + geneNum - cutPoint2 - 1) {
								tour1[j] = children[0].getgeneSequence()[j + cutPoint2 - cutPoint1 + 1];
								tour2[j] = children[1].getgeneSequence()[j + cutPoint2 - cutPoint1 + 1];
							} else {
								tour1[j] = children[0].getgeneSequence()[j - geneNum + cutPoint2 + 1];
								tour2[j] = children[1].getgeneSequence()[j - geneNum + cutPoint2 + 1];
							}
						}
					}

					for (int j = 0; j < geneNum; j++) {
						if ((j < cutPoint1) || (j > cutPoint2)) {
							children[0].getgeneSequence()[j] = -1;
							children[1].getgeneSequence()[j] = -1;
						} else {
							int tmp1 = children[0].getgeneSequence()[j];
							children[0].getgeneSequence()[j] = children[1].getgeneSequence()[j];
							children[1].getgeneSequence()[j] = tmp1;
						}
					}
					if (cutPoint2 == geneNum - 1) {
						int position = 0;
						for (int j = 0; j < cutPoint1; j++) {
							for (int m = position; m < geneNum; m++) {
								boolean flag = true;
								for (int n = 0; n < geneNum; n++) {
									if (tour1[m] == children[0].getgeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[0].getgeneSequence()[j] = tour1[m];
									position = m + 1;
									break;
								}
							}
						}
						position = 0;
						for (int j = 0; j < cutPoint1; j++) {
							for (int m = position; m < geneNum; m++) {
								boolean flag = true;
								for (int n = 0; n < geneNum; n++) {
									if (tour2[m] == children[1].getgeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[1].getgeneSequence()[j] = tour2[m];
									position = m + 1;
									break;
								}
							}
						}
					} else {
						int position = 0;
						for (int j = cutPoint2 + 1; j < geneNum; j++) {
							for (int m = position; m < geneNum; m++) {
								boolean flag = true;
								for (int n = 0; n < geneNum; n++) {
									if (tour1[m] == children[0].getgeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[0].getgeneSequence()[j] = tour1[m];
									position = m + 1;
									break;
								}
							}
						}
						for (int j = 0; j < cutPoint1; j++) {
							for (int m = position; m < geneNum; m++) {
								boolean flag = true;
								for (int n = 0; n < geneNum; n++) {
									if (tour1[m] == children[0].getgeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[0].getgeneSequence()[j] = tour1[m];
									position = m + 1;
									break;
								}
							}
						}
						position = 0;
						for (int j = cutPoint2 + 1; j < geneNum; j++) {
							for (int m = position; m < geneNum; m++) {
								boolean flag = true;
								for (int n = 0; n < geneNum; n++) {
									if (tour2[m] == children[1].getgeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[1].getgeneSequence()[j] = tour2[m];
									position = m + 1;
									break;
								}
							}
						}
						for (int j = 0; j < cutPoint1; j++) {
							for (int m = position; m < geneNum; m++) {
								boolean flag = true;
								for (int n = 0; n < geneNum; n++) {
									if (tour2[m] == children[1].getgeneSequence()[n]) {
										flag = false;
										break;
									}
								}
								if (flag) {
									children[1].getgeneSequence()[j] = tour2[m];
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
					int r1 = random.nextInt(geneNum);
					if ((r1 > 0) && (r1 < geneNum - 1)) {
						cutPoint1 = r1;
						int r2 = random.nextInt(geneNum - r1);
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
						if (cutPoint2 == geneNum - 1) {
							for (int k = 0; k < cutPoint1; k++) {
								tour.add(Integer.valueOf(children[j].getgeneSequence()[k]));
							}
						} else {
							for (int k = 0; k < geneNum; k++) {
								if (k < cutPoint1 || k > cutPoint2) {
									tour.add(Integer.valueOf(children[j].getgeneSequence()[k]));
								}
							}
						}
						int position = random.nextInt(tour.size());
						if (position == 0) {
							for (int k = cutPoint2; k >= cutPoint1; k--) {
								tour.add(0, Integer.valueOf(children[j].getgeneSequence()[k]));
							}
						} else if (position == tour.size() - 1) {
							for (int k = cutPoint1; k <= cutPoint2; k++) {
								tour.add(Integer.valueOf(children[j].getgeneSequence()[k]));
							}
						} else {
							for (int k = cutPoint1; k <= cutPoint2; k++) {
								tour.add(position, Integer.valueOf(children[j].getgeneSequence()[k]));
							}
						}
						for (int k = 0; k < geneNum; k++) {
							children[j].getgeneSequence()[k] = tour.get(k).intValue();
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

	private void printOptimal() {
		System.out.println("The best fitness is " + bestFitness);
		System.out.println("The best tour length is " + bestLength);
		System.out.print("The best tour is ");
		for (int i = 0; i < geneNum; i++) {
			System.out.print(GeneSeq[i] + ",");
		}
		System.out.println();
	}

	// 把适应度结果写入文件
	private void outputResults() {
		String filename = "F://GA//result.txt";
		try {
			@SuppressWarnings("resource")
			FileOutputStream outputStream = new FileOutputStream(filename);
			for (int i = 0; i < averageFitness.length; i++) {
				String line = String.valueOf(averageFitness[i]) + "\r\n";
				outputStream.write(line.getBytes());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Chromosome[] getChromosomes() {
		return chromosomes;
	}

	public void setChromosomes(Chromosome[] chromosomes) {
		this.chromosomes = chromosomes;
	}

	public int getgeneNum() {
		return geneNum;
	}

	public void setgeneNum(int geneNum) {
		this.geneNum = geneNum;
	}

	public double getP_c_t() {
		return p_c_t;
	}

	public void setP_c_t(double p_c_t) {
		this.p_c_t = p_c_t;
	}

	public double getP_m_t() {
		return p_m_t;
	}

	public void setP_m_t(double p_m_t) {
		this.p_m_t = p_m_t;
	}

	public int getMAX_GEN() {
		return MAX_GEN;
	}

	public void setMAX_GEN(int mAX_GEN) {
		this.MAX_GEN = mAX_GEN;
	}

	public int getBestLength() {
		return bestLength;
	}

	public void setBestLength(int bestLength) {
		this.bestLength = bestLength;
	}

	public int[] getGeneSeq() {
		return GeneSeq;
	}

	public void setGeneSeq(int[] GeneSeq) {
		this.GeneSeq = GeneSeq;
	}

	public double[] getAverageFitness() {
		return averageFitness;
	}

	public void setAverageFitness(double[] averageFitness) {
		this.averageFitness = averageFitness;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int n) {
		this.population = n;
	}

	public int[][] getDistance() {
		return distance;
	}

	public void setDistance(int[][] distance) {
		this.distance = distance;
	}

}

